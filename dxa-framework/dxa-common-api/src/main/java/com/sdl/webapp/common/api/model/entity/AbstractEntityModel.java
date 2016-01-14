package com.sdl.webapp.common.api.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdl.webapp.common.api.localization.Localization;
import com.sdl.webapp.common.api.mapping.semantic.annotations.SemanticMappingIgnore;
import com.sdl.webapp.common.api.model.EntityModel;
import com.sdl.webapp.common.api.model.MvcData;
import com.sdl.webapp.common.api.model.RichTextFragment;
import com.sdl.webapp.common.exceptions.DxaException;
import com.sdl.webapp.common.markup.html.HtmlElement;
import com.sdl.webapp.common.util.ApplicationContextHolder;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.Objects;

@SemanticMappingIgnore
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractEntityModel implements EntityModel, RichTextFragment {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("XpmMetadata")
    private Map<String, String> xpmMetadata;

    @JsonProperty("XpmPropertyMetadata")
    private Map<String, String> xpmPropertyMetadata;

    @JsonProperty("MvcData")
    private MvcData mvcData;

    @JsonProperty("HtmlClasses")
    private String htmlClasses;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Map<String, String> getXpmMetadata() {
        return xpmMetadata;
    }

    public void setXpmMetadata(Map<String, String> xpmMetadata) {
        this.xpmMetadata = xpmMetadata;
    }

    @Override
    public Map<String, String> getXpmPropertyMetadata() {
        return xpmPropertyMetadata;
    }

    public void setXpmPropertyMetadata(Map<String, String> propertyData) {
        this.xpmPropertyMetadata = propertyData;
    }

    @Override
    public MvcData getMvcData() {
        return mvcData;
    }

    public void setMvcData(MvcData mvcData) {
        this.mvcData = mvcData;
    }

    @Override
    public String getHtmlClasses() {
        return this.htmlClasses;
    }

    @Override
    public void setHtmlClasses(String htmlClasses) {
        this.htmlClasses = htmlClasses;
    }


    @Override
    @SneakyThrows(JsonProcessingException.class)
    public String getXpmMarkup(Localization localization) {
        return getXpmMetadata() == null ? "" : String.format("<!-- Start Component Presentation: %s -->",
                ApplicationContextHolder.getContext().getBean(ObjectMapper.class).writeValueAsString(getXpmMetadata()));
    }

    @Override
    public HtmlElement toHtmlElement() throws DxaException {
        throw new UnsupportedOperationException(
                String.format("Direct rendering of View Model type '%s' to HTML is not supported." +
                                " Consider using View Model property of type RichText in combination with DxaRichText() in view code to avoid direct rendering to HTML." +
                                " Alternatively, override method %s.toHtmlElement().",
                        getClass().getName(), getClass().getName())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntityModel that = (AbstractEntityModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(xpmMetadata, that.xpmMetadata) &&
                Objects.equals(xpmPropertyMetadata, that.xpmPropertyMetadata) &&
                Objects.equals(mvcData, that.mvcData) &&
                Objects.equals(htmlClasses, that.htmlClasses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, xpmMetadata, xpmPropertyMetadata, mvcData, htmlClasses);
    }
}