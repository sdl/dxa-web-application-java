package com.sdl.dxa.api.datamodel.json;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;

import static com.sdl.dxa.api.datamodel.Constants.DOLLAR_TYPE;

/**
 * Mix-in for Jackson that makes everything polymorphic.
 * <p>To be added to the configuration of {@link ObjectMapper} to make all Java objects polymorphic,
 * so we can handle them with custom {@link ModelDataTypeIdResolver} and {@link ModelDataTypeResolver}.</p>
 */
@SuppressWarnings("unused")
@JsonTypeResolver(ModelDataTypeResolver.class)
@JsonTypeIdResolver(ModelDataTypeIdResolver.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM,
        include = JsonTypeInfo.As.PROPERTY,
        property = DOLLAR_TYPE,
        visible = true)
public interface PolymorphicObjectMixin {

}
