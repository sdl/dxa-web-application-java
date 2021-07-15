package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;

@JsonTypeName
public class ExternalContentData implements JsonPojo {
    public ExternalContentData() {
    }

    public ExternalContentData(String displayTypeId, String id, String templateFragment, ContentModelData metadata) {
        this.displayTypeId = displayTypeId;
        this.id = id;
        this.templateFragment = templateFragment;
        this.metadata = metadata;
    }

    private String displayTypeId;

    private String id;

    private String templateFragment;

    private ContentModelData metadata;

    public String getDisplayTypeId() {
        return displayTypeId;
    }

    public ExternalContentData setDisplayTypeId(String displayTypeId) {
        this.displayTypeId = displayTypeId;
        return this;
    }

    public String getId() {
        return id;
    }

    public ExternalContentData setId(String id) {
        this.id = id;
        return this;
    }

    public String getTemplateFragment() {
        return templateFragment;
    }

    public ExternalContentData setTemplateFragment(String templateFragment) {
        this.templateFragment = templateFragment;
        return this;
    }

    public ContentModelData getMetadata() {
        return metadata;
    }

    public ExternalContentData setMetadata(ContentModelData metadata) {
        this.metadata = metadata;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalContentData that = (ExternalContentData) o;
        return Objects.equal(displayTypeId, that.displayTypeId) &&
                Objects.equal(id, that.id) &&
                Objects.equal(templateFragment, that.templateFragment) &&
                Objects.equal(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(displayTypeId, id, templateFragment, metadata);
    }

    @Override
    public String toString() {
        return "ExternalContentData{" +
                "displayTypeId='" + displayTypeId + '\'' +
                ", id='" + id + '\'' +
                ", templateFragment='" + templateFragment + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
