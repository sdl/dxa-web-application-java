package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sdl.dxa.api.datamodel.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonTypeName
public class PageTemplateData implements JsonPojo {

    private String id;

    private String namespace;

    private String title;

    private String fileExtension;

    private DateTime revisionDate;

    private ContentModelData metadata;

    public String getNamespace() {
        return namespace == null ? Constants.DEFAULT_NAMESPACE : namespace;
    }
}
