package com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters;

/**
 * Created by rawa on 2016-04-22.
 */
public class Color implements IParameter{
    public static final String PARAM_KEY = "color";
    private String color;

    public Color(String color){
        this.color = color;
    }

    public String toString(){
        return String.format("%s=%s", PARAM_KEY, String.valueOf(getStringValue()));
    }

    @Override
    public String getKey() {
        return PARAM_KEY;
    }

    @Override
    public String getStringValue() {
        return color;
    }
}
