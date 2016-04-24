package com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls;

import com.rawa.tasker.lifxplugin.lifx.LifxConfig.RequestMethod;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Duration;

import java.util.HashMap;

/**
 * Created by rawa on 2016-04-22.
 */
public class Toggle extends AAPICall {
    public final static String ACTION = "toggle";

    private Duration duration;

    public Toggle(Duration duration){
        super();
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    public HashMap<String, String> getParamMap() {
        HashMap<String, String> params = new HashMap<>();

        addIParameter(params, duration);

        return params;
    }

    @Override
    public String getAction() {
        return ACTION;
    }
}
