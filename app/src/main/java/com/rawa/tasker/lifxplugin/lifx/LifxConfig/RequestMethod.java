package com.rawa.tasker.lifxplugin.lifx.LifxConfig;

/**
 * Created by rawa on 2016-04-22.
 */
public enum RequestMethod {
    POST("POST"),
    GET("GET"),
    PUT("PUT");

    private final static String tag = "RequestMethod";

    String rm;

    RequestMethod(String rm){
        this.rm = rm;
    }

    @Override
    public String toString() {
        return rm;
    }
}
