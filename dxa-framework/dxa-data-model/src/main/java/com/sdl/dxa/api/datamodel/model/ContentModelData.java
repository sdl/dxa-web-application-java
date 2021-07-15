package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sdl.dxa.api.datamodel.json.ModelDataTypeIdResolver;
import com.sdl.dxa.api.datamodel.model.unknown.UnknownModelData;
import com.sdl.dxa.api.datamodel.model.util.CanGetAndCast;
import com.sdl.dxa.api.datamodel.model.util.CanWrapContentAndMetadata;
import com.sdl.dxa.api.datamodel.model.util.ModelDataWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.sdl.dxa.api.datamodel.Constants.DOLLAR_TYPE;

/**
 * {@link Map} implemenation to handle DXA polymorphic JSON logic. Is created to be able to address to {@code ContentModelData[]}.
 */
@JsonTypeName
public class ContentModelData extends HashMap<String, Object>
        implements Map<String, Object>, CanGetAndCast<String>, CanWrapContentAndMetadata, JsonPojo {

    //region Constructors matching super and copy-constructor
    public ContentModelData(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ContentModelData(int initialCapacity) {
        super(initialCapacity);
    }

    public ContentModelData() {
        super();
    }

    public ContentModelData(Map<? extends String, ?> m) {
        super(m);
    }

    public ContentModelData(ContentModelData other) {
    }
    //endregion

    @Override
    public Object put(String key, Object value) {
        return DOLLAR_TYPE.equals(key) && isRemoveDollarType(value) ? null : super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        if (m.containsKey(DOLLAR_TYPE) && isRemoveDollarType(m.get(DOLLAR_TYPE))) {
            m.remove(DOLLAR_TYPE);
        }
        super.putAll(m);
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        return DOLLAR_TYPE.equals(key) && isRemoveDollarType(value) ? null : super.putIfAbsent(key, value);
    }

    @Override
    public Object getElement(String identifier) {
        return get(identifier);
    }

    @Override
    public ModelDataWrapper getDataWrapper() {
        return new ModelDataWrapper() {
            @Override
            public ContentModelData getContent() {
                return ContentModelData.this;
            }

            @Override
            public ContentModelData getMetadata() {
                return ContentModelData.this;
            }

            @Override
            public Object getWrappedModel() {
                return ContentModelData.this;
            }
        };
    }

    /**
     * $type is normally removed and restored later by {@link ModelDataTypeIdResolver}. We need $type visible for deserializers
     * to handle {@link UnknownModelData} cases, and since it is visible, Jackson restores it in a map meaning that it will be
     * added to CMD. We don't want to pollute CMD map with it though, so remove it again from this CMD.
     * <p>In case we know for sure that the type information will be missing when serializing back or any other reason why it is needed to allow
     * adding $type keys to this map, custom implementations can save it by overriding this method.</p>
     *
     * @param typeId type ID
     * @return true if $type should be removed, false otherwise; default is always true
     */
    protected boolean isRemoveDollarType(@Nullable Object typeId) {
        // default implementation does nothing with typeId
        return true;
    }
}
