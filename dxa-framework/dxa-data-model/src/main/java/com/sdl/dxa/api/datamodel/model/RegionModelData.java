package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sdl.dxa.api.datamodel.model.util.ModelDataWrapper;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ToString(callSuper = true)
@NoArgsConstructor
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeName
public class RegionModelData extends ViewModelData implements JsonPojo {

    private String name;

    private String includePageId;

    private List<EntityModelData> entities;

    private List<RegionModelData> regions;

    @Builder
    public RegionModelData(String schemaId, String htmlClasses, Map<String, Object> xpmMetadata, ContentModelData metadata, Map<String, Object> extensionData, MvcModelData mvcData, String name, String includePageId, List<EntityModelData> entities, List<RegionModelData> regions) { // NOSONAR
        super(schemaId, htmlClasses, xpmMetadata, metadata, extensionData, mvcData);
        this.name = name;
        this.includePageId = includePageId;
        this.entities = entities;
        this.regions = regions;
    }

    @Builder
    public RegionModelData(String name, String includePageId, List<EntityModelData> entities, List<RegionModelData> regions) {
        this.name = name;
        this.includePageId = includePageId;
        this.entities = entities;
        this.regions = regions;
    }

    /**
     * Adds a region to the end of the region collection.
     *
     * @param regionModelData region to add
     */
    public void addRegion(RegionModelData regionModelData) {
        if (regions == null) {
            regions = new ArrayList<>();
        }
        regions.add(regionModelData);
    }

    @Override
    public ModelDataWrapper getDataWrapper() {
        return new ModelDataWrapper() {
            @Override
            public ContentModelData getMetadata() {
                return RegionModelData.this.getMetadata();
            }

            @Override
            public Object getWrappedModel() {
                return RegionModelData.this;
            }
        };
    }
}
