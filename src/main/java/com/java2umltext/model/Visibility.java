package com.java2umltext.model;

public enum Visibility{
    PUBLIC    ('+'),
    PRIVATE   ('-'),
    PROTECTED ('#'),
    DEFAULT   ('~');

    private final char symbol;
    Visibility(char symbol) {
        this.symbol = symbol;
    }
    public char symbol() { return symbol; }
    
    public static Visibility fromString(String name) {
        name = name.trim().toUpperCase();
        if (name.equals(Visibility.PRIVATE.name())){
            return Visibility.PRIVATE;
        }
        else if (name.equals(Visibility.PROTECTED.name())){
            return Visibility.PROTECTED;
        }
        else if (name.equals(Visibility.PUBLIC.name())){
            return Visibility.PUBLIC;
        }
        else if (name.equals(Visibility.DEFAULT.name())){
            return Visibility.DEFAULT;
        }
        return null;
    }
}
