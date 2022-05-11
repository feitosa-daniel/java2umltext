package com.java2umltext.model;

import java.util.ArrayList;
import java.util.HashMap;

public record ClassWrapper (
    Document document,
    String pkg,
    String type,
    String name,
    ArrayList<FieldWrapper> fields,
    ArrayList<MethodWrapper> methods,
    HashMap<String,String> imports
) implements UML {
    public ClassWrapper(Document document,String pkg,String type,String name){
        this(document,pkg,type,name, new ArrayList<>(), new ArrayList<>(), new HashMap<>());
    }
    public String pkgPrefix() {
        return ClassWrapper.pkgPrefix(pkg);
    }
    public static String pkgPrefix(String pkg) {
        return pkg == null || pkg.isBlank() ? "" : pkg + ".";
    }
}
