package com.sdl.dxa.tridion.pcaclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdl.web.pca.client.ApiClient;
import com.sdl.web.pca.client.DefaultApiClient;
import com.sdl.web.pca.client.DefaultGraphQLClient;
import com.sdl.web.pca.client.GraphQLClient;
import com.sdl.web.pca.client.auth.Authentication;
import com.sdl.web.pca.client.contentmodel.enums.DataModelType;
import com.sdl.web.pca.client.contentmodel.generated.ClaimValue;
import com.sdl.web.pca.client.contentmodel.generated.ClaimValueType;
import com.tridion.ambientdata.AmbientDataContext;
import com.tridion.ambientdata.claimstore.ClaimStore;
import com.tridion.ambientdata.web.WebClaims;
import com.tridion.configuration.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sdl.dxa.tridion.common.ConfigurationConstants.CONNECTION_TIMEOUT;
import static com.tridion.ambientdata.AmbientDataConfig.getAmbientDataConfig;

@Slf4j
@Service("DefaultApiClientProvider")
@Profile("!cil.providers.active")
public class DefaultApiClientProvider implements ApiClientProvider {

    private static final String X_PREVIEW_SESSION_TOKEN = "x-preview-session-token";
    private static final String PREVIEW_SESSION_TOKEN = "preview-session-token";
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.findAndRegisterModules();
    }

    private ApiClientConfigurationLoader configurationLoader;
    private Authentication auth;
    private Map<String, ClaimValue> globalClaims = new ConcurrentHashMap<>();

    @Autowired
    public DefaultApiClientProvider(ApiClientConfigurationLoader configurationLoader,
                                    Authentication auth) {
        this.configurationLoader = configurationLoader;
        this.auth = auth;
    }

    @Override
    public void addGlobalClaim(ClaimValue claim) {
        if (claim == null) return;
        globalClaims.put(claim.getUri(), claim);
    }

    @Override
    public void removeGlobalClaim(ClaimValue claim) {
        if (claim == null) return;
        globalClaims.remove(claim.getUri());

    }

    @Override
    public ApiClient getClient() {
        ClaimStore claimStore = AmbientDataContext.getCurrentClaimStore();
        if (claimStore == null) {
            log.debug("No claimstore found (is the ADF module configured in the Web.Config?) so unable to populate claims for PCA.");
        }

        String previewToken = getClaimValue(WebClaims.REQUEST_HEADERS, X_PREVIEW_SESSION_TOKEN,
                claim -> Optional.of(((List<String>) claim).get(0)))
                .orElseGet(() -> getClaimValue(WebClaims.REQUEST_COOKIES, PREVIEW_SESSION_TOKEN,
                        claim -> Optional.of(claim.toString()))
                .orElse(null));

        // Add context data to client
        Map<String, String> defaultHeaders = new HashMap<>();
        if (previewToken != null) {
            defaultHeaders.put(HttpHeaders.COOKIE, String.format("%s=%s", PREVIEW_SESSION_TOKEN, previewToken));
        }
        GraphQLClient graphQLClient = new DefaultGraphQLClient(configurationLoader.getServiceUrl(), defaultHeaders, auth);
        Integer requestTimeout = Integer.valueOf(configurationLoader.getConfiguration().getOrDefault(CONNECTION_TIMEOUT, 0).toString());
        ApiClient client = new DefaultApiClient(graphQLClient, requestTimeout);
        client.setDefaultModelType(DataModelType.R2);

        for (ClaimValue claim : globalClaims.values()) {
            log.debug("Forwarding on global claim {} with value {}", claim.getUri(), claim.getValue());
            client.getGlobalContextData().addClaimValue(claim);
        }

        if (!configurationLoader.claimForwarding()) {
            log.debug("The claimstore is not available so no claim forwarding from claimstore will be performed. Make sure the ADF module is configured in the Web.Config to enable this option.");
            return client;
        }

        if (claimStore == null) {
            log.debug("The claimstore is not available so no claim forwarding from claimstore will be performed. Make sure the ADF module is configured in the Web.Config to enable this option.");
            return client;
        }

        // Forward all claims
        List<String> forwardedClaimValues = null;
        try {
            forwardedClaimValues = getAmbientDataConfig().getForwardedClaims().values().iterator().next();
        } catch (NullPointerException | ConfigurationException e) {
            log.warn("Unable to retrieve ambient data configuration and get forwarded claims");
            return client;
        }

        if (forwardedClaimValues == null || forwardedClaimValues.size() == 0) {
            return client;
        }

        Map<URI, Object> forwardedClaims = forwardedClaimValues.stream().map(url -> {
            try {
                return new URI(url);
            } catch (URISyntaxException e) {
                log.warn("Unable to parse uri: " + url);
                return null;
            }
        }).distinct().filter(uri -> uri != null
                && claimStore.contains(uri)
                && claimStore.get(uri) != null
                && !uri.toString().equals("taf:session:preview:preview_session"))
                .collect(Collectors.toMap(uri -> uri, uri -> claimStore.get(uri)));

        if (forwardedClaims.size() == 0) {
            log.debug("No claims from claimstore to forward.");
            return client;
        }

        for (Map.Entry<URI, Object> claim : forwardedClaims.entrySet()) {
            log.debug("Forwarding claim {} from claimstore to PCA client.", claim.getKey().toString());
            ClaimValue value = new ClaimValue();
            value.setType(ClaimValueType.STRING);
            value.setUri(claim.getKey().toString());
            try {
                String jsonValue = OBJECT_MAPPER.writeValueAsString(claim.getValue());
                value.setValue(jsonValue);
            } catch (JsonProcessingException e) {
                throw new ApiClientConfigurationException("Unable to serialize claim " + claim.getKey().toString(), e);
            }
            client.getGlobalContextData().addClaimValue(value);
        }

        return client;
    }

    private Optional<String> getClaimValue(URI uri, String key, Function<Object, Optional<String>> deriveValue) {
        ClaimStore claimStore = AmbientDataContext.getCurrentClaimStore();
        if (claimStore == null) return Optional.empty();
        Map claims = claimStore.get(uri, Map.class);
        if (claims != null && claims.containsKey(key)) {
            return deriveValue.apply(claims.get(key));
        }
        return Optional.empty();
    }

}
