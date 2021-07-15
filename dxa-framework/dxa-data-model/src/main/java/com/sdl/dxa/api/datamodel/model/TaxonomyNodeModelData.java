package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@JsonTypeName
public class TaxonomyNodeModelData extends SitemapItemModelData implements JsonPojo {

    @JsonProperty("Key")
    private String key;

    @JsonProperty("HasChildNodes")
    private boolean withChildren;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("IsAbstract")
    private boolean taxonomyAbstract;

    @JsonProperty("ClassifiedItemsCount")
    private int classifiedItemsCount;
}
