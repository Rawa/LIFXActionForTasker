package com.rawa.tasker.lifxplugin.lifx.LifxConfig;

import android.util.Log;

import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.AAPICall;

/**
 * Created by rawa on 2016-04-22.
 */
public class LifxConfig {
    private final static String tag = "LifxConfig";

    private Token token;
    private AAPICall apiCall;
    private boolean debug;
    private Selector selector;

    public LifxConfig(Token token, AAPICall apiCall, boolean debug){
        this.token = token;
        this.apiCall = apiCall;
        this.debug = debug;
        this.selector = new Selector();
    }

    public boolean isDebugEnabled(){
        return debug;
    }

    public void setDebugEnabled(boolean debugMode){
        this.debug = debugMode;
    }

    public AAPICall getApiCall(){
        return apiCall;
    }

    public Selector getSelector(){
        return selector;
    }

    public void setSelector(Selector selector){
        this.selector = selector;
    }

    public void setApiCall(AAPICall apiCall){
        this.apiCall = apiCall;
    }

    public Token getToken(){
        return token;
    }

    public void setToken(Token token){
        this.token = token;
    }

    public String toString(){
        Log.d(tag, apiCall.toString());
        Log.d(tag, String.format("%s, %s", selector.toString(), apiCall.toString()));
        return String.format("%s, %s", selector.toString(), apiCall.toString());
    }
}
