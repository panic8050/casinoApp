package com.mapplic.slots.core;

public class Utils {

    public static String getUrlParams(String url) {
        return url.substring(url.indexOf("?") + 1);
    }

}
