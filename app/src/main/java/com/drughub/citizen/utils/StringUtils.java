package com.drughub.citizen.utils;


public class StringUtils {


    public static String findAndReplace(String mainstring, String substring, String replcewith)
    {
        String replced_string = mainstring.replace(substring , replcewith);

        return replced_string;
    }

}
