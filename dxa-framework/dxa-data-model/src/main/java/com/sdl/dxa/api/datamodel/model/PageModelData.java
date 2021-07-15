package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sdl.dxa.api.datamodel.Constants;
import com.sdl.dxa.api.datamodel.model.util.ModelDataWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@JsonTypeName
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
public class PageModelData extends ViewModelData implements JsonPojo {

    private String id;

    private String namespace;

    private String structureGroupId;

    private Map<String, String> meta;

    private PageTemplateData pageTemplate;

    private String title;

    private List<RegionModelData> regions;

    private String urlPath;

    public String getNamespace() {
        return namespace == null ? Constants.DEFAULT_NAMESPACE : namespace;
    }

    @Override
    public ModelDataWrapper getDataWrapper() {
        return new ModelDataWrapper() {
            @Override
            public ContentModelData getMetadata() {
                return PageModelData.this.getMetadata();
            }

            @Override
            public Object getWrappedModel() {
                return PageModelData.this;
            }
        };
    }
}
