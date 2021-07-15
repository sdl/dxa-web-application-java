package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@JsonTypeName
@ToString
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MvcModelData implements JsonPojo {

    private String actionName;

    private String areaName;

    private String controllerAreaName;

    private String controllerName;

    private String viewName;

    private Map<String, String> parameters;

    @Builder
    public MvcModelData(String actionName, String areaName, String controllerAreaName, String controllerName, String viewName, Map<String, String> parameters) {
        this.actionName = actionName;
        this.areaName = areaName;
        this.controllerAreaName = controllerAreaName;
        this.controllerName = controllerName;
        this.viewName = viewName;
        this.parameters = parameters;
    }
}
