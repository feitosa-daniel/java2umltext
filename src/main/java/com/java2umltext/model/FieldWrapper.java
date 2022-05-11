package com.java2umltext.model;

public record FieldWrapper(
    Visibility visibility,
    boolean isStatic,
    String type,
    String name
) implements UML { }
