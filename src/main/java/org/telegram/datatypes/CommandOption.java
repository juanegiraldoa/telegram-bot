package org.telegram.datatypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum CommandOption {
    NONE(null, null, null),
    SHOW_ALL("show_all", "Show All", true),
    NEW("new", "New", true),
    DELETE("delete", "Delete", false)

    ;

    private final String callBack;
    private final String description;
    private final Boolean onNone;
}
