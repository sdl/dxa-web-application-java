package com.sdl.webapp.common.impl.localization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.sdl.webapp.common.api.localization.Localization;
import com.sdl.webapp.common.api.localization.LocalizationFactoryException;
import com.sdl.webapp.common.api.localization.SiteLocalization;
import com.sdl.webapp.common.api.mapping.semantic.config.SemanticSchema;
import com.sdl.webapp.common.impl.localization.semantics.JsonSchema;
import com.sdl.webapp.common.impl.localization.semantics.JsonVocabulary;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.sdl.webapp.common.impl.localization.semantics.SemanticsConverter.convertSemantics;

/**
 * Docs (Ish) localization.
 */
@Slf4j
public class DocsLocalization implements Localization {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.findAndRegisterModules();
    }

    /**
     * The Id for the current publication.
     */
    private String publicationId;

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return publicationId;
    }

    /**
     * {@inheritDoc}
     */
    public String getPath() {
        return "/";
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStaticContent(String url) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDefault() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStaging() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public String getVersion() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getCulture() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Locale getLocale() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<SiteLocalization> getSiteLocalizations() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getConfiguration(String key) {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    public String getResource(String key) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Map<Long, SemanticSchema> getSemanticSchemas() {
        try {
            List<JsonSchema> schemas = getJsonObject("semantic-schemas/schemas.json",
                    new TypeReference<List<JsonSchema>>() {
                    });
            List<JsonVocabulary> vocabularies = getJsonObject("semantic-schemas/vocabularies.json",
                    new TypeReference<List<JsonVocabulary>>() {
                    });

            Iterable<SemanticSchema> semanticSchemas = convertSemantics(schemas, vocabularies);
            Map<Long, SemanticSchema> schemasMap = new HashMap<>();
            for (SemanticSchema semanticSchema : semanticSchemas) {
                schemasMap.put(semanticSchema.getId(), semanticSchema);
            }
            return schemasMap;
        } catch (IOException e) {
            log.error("Unable to read semantic-schemas/schemas.json or semantic-schemas/vocabularies.json.", e);
        } catch (LocalizationFactoryException e) {
            log.error("Unable to convert semantics.", e);
        }
        return null;
    }

    private <T> T getJsonObject(String path, TypeReference<T> resultType) throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        return OBJECT_MAPPER.readValue(in, resultType);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getIncludes(String pageTypeId) {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    public String localizePath(String url) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getDataFormats() {
        return new ArrayList<>(Arrays.asList("json"));
    }

    /**
     * {@inheritDoc}
     */
    public String getCmUriScheme() {
        return "ish";
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("publicationId", publicationId)
                .toString() + ", " + super.toString();
    }

    public String getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }
}
