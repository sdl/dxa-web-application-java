package com.sdl.webapp.tridion.linking;

import com.google.common.base.Strings;
import com.sdl.dxa.common.util.PathUtils;
import com.sdl.webapp.common.api.content.LinkResolver;
import com.sdl.webapp.common.util.TcmUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
public abstract class AbstractLinkResolver implements LinkResolver {

    /**
     * @deprecated since 2.0, use {@link PathUtils#getDefaultPageName()}
     */
    @Deprecated
    public static final String DEFAULT_PAGE_NAME = PathUtils.getDefaultPageName();

    /**
     * @deprecated since 2.0, use {@link PathUtils#getDefaultPageExtension()}
     */
    @Deprecated
    public static final String DEFAULT_PAGE_EXTENSION = PathUtils.getDefaultPageExtension();

    @Override
    public String resolveLink(@Nullable String url, @Nullable String localizationId, boolean resolveToBinary) {
        final int publicationId = !Strings.isNullOrEmpty(localizationId) ? Integer.parseInt(localizationId) : 0;

        String resolvedUrl = _resolveLink(url, publicationId, resolveToBinary);

        return PathUtils.stripDefaultExtension(PathUtils.stripIndexPath(resolvedUrl));
    }

    @Contract("null, _, _ -> null; !null, _, _ -> !null")
    private String _resolveLink(String uri, int publicationId, boolean isBinary) {
        if (uri == null || !TcmUtils.isTcmUri(uri)) {
            return uri;
        }

        Function<ResolvingData, Optional<String>> resolver;
        switch (TcmUtils.getItemType(uri)) {
            case TcmUtils.COMPONENT_ITEM_TYPE:
                resolver = isBinary ? _binaryResolver() : _componentResolver();
                break;
            case TcmUtils.PAGE_ITEM_TYPE:
                resolver = _pageResolver();
                break;
            default:
                log.warn("Could not resolve link: {}", uri);
                return "";
        }

        ResolvingData resolvingData = ResolvingData.of(
                publicationId == 0 ? TcmUtils.getPublicationId(uri) : publicationId,
                TcmUtils.getItemId(uri), uri);

        return resolver.apply(resolvingData).orElse("");
    }

    protected abstract Function<ResolvingData, Optional<String>> _binaryResolver();

    protected abstract Function<ResolvingData, Optional<String>> _componentResolver();

    protected abstract Function<ResolvingData, Optional<String>> _pageResolver();

    @AllArgsConstructor(staticName = "of")
    @Getter
    protected static class ResolvingData {

        private int publicationId;

        private int itemId;

        private String uri;
    }
}
