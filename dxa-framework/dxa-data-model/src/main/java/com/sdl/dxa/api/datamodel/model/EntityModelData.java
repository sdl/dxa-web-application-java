package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sdl.dxa.api.datamodel.Constants;
import com.sdl.dxa.api.datamodel.model.util.CanWrapContentAndMetadata;
import com.sdl.dxa.api.datamodel.model.util.ModelDataWrapper;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@JsonTypeName
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class EntityModelData extends ViewModelData implements CanWrapContentAndMetadata, JsonPojo {

    private String id;

    private String contextId;

    private String namespace;

    private ComponentTemplateData componentTemplate;

    private String linkUrl;

    private ContentModelData content;

    private BinaryContentData binaryContent;

    private ExternalContentData externalContent;

    @Builder
    public EntityModelData(String schemaId, String htmlClasses, Map<String, Object> xpmMetadata, ContentModelData metadata, Map<String, Object> extensionData, MvcModelData mvcData, String id, String contextId, String namespace, String linkUrl, ContentModelData content, BinaryContentData binaryContent, ExternalContentData externalContent) {//NOSONAR
        super(schemaId, htmlClasses, xpmMetadata, metadata, extensionData, mvcData);
        this.id = id;
        this.linkUrl = linkUrl;
        this.content = content;
        this.namespace = namespace;
        this.binaryContent = binaryContent;
        this.externalContent = externalContent;
        this.contextId = contextId;
    }

    @Builder
    public EntityModelData(String id, String contextId, String namespace, String linkUrl, ContentModelData content, BinaryContentData binaryContent, ExternalContentData externalContent) {
        this.id = id;
        this.linkUrl = linkUrl;
        this.content = content;
        this.namespace = namespace;
        this.binaryContent = binaryContent;
        this.externalContent = externalContent;
        this.contextId = contextId;
    }

    public String getNamespace() {
        return namespace == null ? Constants.DEFAULT_NAMESPACE : namespace;
    }

    @Override
    public ModelDataWrapper getDataWrapper() {
        return new ModelDataWrapper() {
            @Override
            public ContentModelData getContent() {
                return EntityModelData.this.getContent();
            }

            @Override
            public ContentModelData getMetadata() {
                return EntityModelData.this.getMetadata();
            }

            @Override
            public Object getWrappedModel() {
                return EntityModelData.this;
            }
        };
    }

    @Override
    public ViewModelData copyFrom(ViewModelData other) {
        super.copyFrom(other);

        EntityModelData emd = (EntityModelData) other;
        this.id = emd.id;
        this.contextId = emd.contextId;
        this.linkUrl = emd.linkUrl;
        this.content = emd.content;
        this.binaryContent = emd.binaryContent;
        this.externalContent = emd.externalContent;
        return this;
    }

    public String getContextId() {
        return StringUtils.isNotEmpty(contextId) ? contextId : "0";
    }

    @JsonIgnore
    public Boolean isDynamic() {
        return this.getId().matches("\\d+-\\d+");
    }
}
