package com.example.gints.ImageUtils;

public class TextUtils {

    public String  removeSapaces(String text){

        String removedSpaces = text.replaceAll("\\s","");

    return removedSpaces;
    }

    public  String capitalString(String text){

        String removedSpaces = text.toUpperCase();
        return  removedSpaces;
    }
}
