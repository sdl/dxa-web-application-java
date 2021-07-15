package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.sdl.dxa.api.datamodel.Constants;
import org.joda.time.DateTime;

@JsonTypeName
public class ComponentTemplateData implements JsonPojo {
    public ComponentTemplateData() {
    }

    public ComponentTemplateData(String id, String namespace, String title, DateTime revisionDate, String outputFormat, ContentModelData metadata) {
        this.id = id;
        this.namespace = namespace;
        this.title = title;
        this.revisionDate = revisionDate;
        this.outputFormat = outputFormat;
        this.metadata = metadata;
    }

    private String id;

    private String namespace;

    private String title;

    private DateTime revisionDate;

    private String outputFormat;

    private ContentModelData metadata;

    public String getNamespace() {
        return namespace == null ? Constants.DEFAULT_NAMESPACE : namespace;
    }

    public String getId() {
        return id;
    }

    public ComponentTemplateData setId(String id) {
        this.id = id;
        return this;
    }

    public ComponentTemplateData setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ComponentTemplateData setTitle(String title) {
        this.title = title;
        return this;
    }

    public DateTime getRevisionDate() {
        return revisionDate;
    }

    public ComponentTemplateData setRevisionDate(DateTime revisionDate) {
        this.revisionDate = revisionDate;
        return this;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public ComponentTemplateData setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public ContentModelData getMetadata() {
        return metadata;
    }

    public ComponentTemplateData setMetadata(ContentModelData metadata) {
        this.metadata = metadata;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentTemplateData that = (ComponentTemplateData) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(namespace, that.namespace) &&
                Objects.equal(title, that.title) &&
                Objects.equal(revisionDate, that.revisionDate) &&
                Objects.equal(outputFormat, that.outputFormat) &&
                Objects.equal(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, namespace, title, revisionDate, outputFormat, metadata);
    }

    @Override
    public String toString() {
        return "ComponentTemplateData{" +
                "id='" + id + '\'' +
                ", namespace='" + namespace + '\'' +
                ", title='" + title + '\'' +
                ", revisionDate=" + revisionDate +
                ", outputFormat='" + outputFormat + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
