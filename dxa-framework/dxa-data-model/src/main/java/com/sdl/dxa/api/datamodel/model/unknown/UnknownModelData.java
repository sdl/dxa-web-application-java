package com.sdl.dxa.api.datamodel.model.unknown;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import com.sdl.dxa.api.datamodel.model.JsonPojo;

/**
 * Handler of any unknown entity-level class. Basically represents any data of a class that is not known with a JSON string as it is.
 */
@JsonDeserialize(using = UnknownModelDataDeserializer.class)
@JsonSerialize(using = UnknownModelDataSerializer.class)
public class UnknownModelData implements JsonPojo {
    public UnknownModelData(String content) {
        this.content = content;
    }

    private String content;

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnknownModelData that = (UnknownModelData) o;
        return Objects.equal(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(content);
    }

    @Override
    public String toString() {
        return "UnknownModelData{" +
                "content='" + content + '\'' +
                '}';
    }
}
