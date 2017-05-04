package com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters;

import android.content.res.Resources;

/**
 * Created by rawa on 2016-04-22.
 */
public class Brightness implements IParameter {
    public final static String PARAM_KEY = "brightness";

    // Default duration = 1;
    private double brightness = 1;
    public final static double MIN = 0.0;
    public final static double MAX = 1.0;

    public Brightness(){
    }

    public Brightness(double brightness){
        setBrightness(brightness);
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness){
        if (brightness < MIN && brightness > MAX)
            throw new IllegalArgumentException(String.format("Argument not with in limits (%f-%f)", MIN, MAX));
        this.brightness = brightness;
    }

    public String toString(){
        return String.format("%s=%s", PARAM_KEY, String.valueOf(getStringValue()));
    }

    @Override
    public String getKey() {
        return PARAM_KEY;
    }

    public String getStringValue() {
        return String.valueOf(getValue());
    }

    public double getValue() {
        return brightness;
    }
}
