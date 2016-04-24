package com.rawa.tasker.lifxplugin.lifx.LifxConfig;

/**
 * Created by rawa on 2016-04-22.
 */
public class Token {
    private final static String tag = "Token";

    private String token;

    public Token (String token){
        assert(token != null);

        this.token = token;
    }

    public String getStringValue(){
        return token;
    }
}
