package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sdl.dxa.api.datamodel.model.util.ModelDataWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@JsonTypeName
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class PageModelData extends ViewModelData {

    private String id;

    private Map<String, String> meta;

    private String title;

    private List<RegionModelData> regions;

    private String urlPath;

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
