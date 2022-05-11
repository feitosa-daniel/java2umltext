package com.java2umltext.model;

import java.util.ArrayList;

public record MethodWrapper(
    Visibility visibility,
    boolean isStatic,
    boolean isAbstract,
    String returnType,
    String name,
    ArrayList<String> parameters
) implements UML { 
    public MethodWrapper(Visibility visibility, boolean isStatic, boolean isAbstract, String returnType, String name) {
        this(visibility, isStatic, isAbstract, returnType, name, new ArrayList<>());
    }
}
