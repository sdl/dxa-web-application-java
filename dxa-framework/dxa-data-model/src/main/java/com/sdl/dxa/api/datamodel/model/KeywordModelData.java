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

@EqualsAndHashCode(callSuper = true)
@JsonTypeName
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
public class KeywordModelData extends ViewModelData implements JsonPojo {

    private String id;

    private String namespace;

    private String description;

    private String key;

    private String taxonomyId;

    private String title;

    public String getNamespace() {
        return namespace == null ? Constants.DEFAULT_NAMESPACE : namespace;
    }

    @Override
    public ModelDataWrapper getDataWrapper() {
        return new ModelDataWrapper() {
            @Override
            public ContentModelData getMetadata() {
                return KeywordModelData.this.getMetadata();
            }

            @Override
            public Object getWrappedModel() {
                return KeywordModelData.this;
            }
        };
    }
}
