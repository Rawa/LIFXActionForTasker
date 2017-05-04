package com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls;


import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Brightness;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.RequestMethod;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Color;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Duration;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Power;

import java.util.HashMap;

/**
 * Created by rawa on 2016-04-22.
 */
public class State extends AAPICall {
    public final static String ACTION = "state";

    private Power power;
    private Duration duration;
    private Brightness brightness;
    private Color color;

    public State(Duration duration, Brightness brightness, Power power, Color color){
        super();
        this.duration = duration;
        this.brightness = brightness;
        this.power = power;
        this.color = color;
    }

    public Duration getDuration() {
        return duration;
    }

    public Power getPower() {
        return power;
    }

    public Brightness getBrightness() {
        return brightness;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.PUT;
    }

    @Override
    public HashMap<String, String> getParamMap() {
        HashMap<String, String> params = new HashMap<>();

        addIParameter(params, power);
        addIParameter(params, brightness);
        addIParameter(params, duration);
        addIParameter(params, color);

        return params;
    }

    @Override
    public String getAction() {
        return ACTION;
    }

}
