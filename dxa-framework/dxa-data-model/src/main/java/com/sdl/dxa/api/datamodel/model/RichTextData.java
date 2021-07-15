package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sdl.dxa.api.datamodel.model.util.CanGetAndCast;
import com.sdl.dxa.api.datamodel.model.util.DelegatesToList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@JsonTypeName
@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RichTextData implements DelegatesToList<Object>, CanGetAndCast<Integer>, JsonPojo {

    private List<Object> fragments;

    @Override
    public List<Object> getValues() {
        return getFragments();
    }

    @Override
    public Object getElement(Integer identifier) {
        return getFragments().get(identifier);
    }
}
