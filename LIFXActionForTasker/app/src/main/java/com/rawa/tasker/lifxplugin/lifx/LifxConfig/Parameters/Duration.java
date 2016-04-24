package com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters;

/**
 * Created by rawa on 2016-04-22.
 */
public class Duration implements IParameter {
    public final static String PARAM_KEY = "duration";

    // Default duration = 1.0;
    private double duration = 1;
    public final static double MIN = 0.0;
    public final static double MAX = 3155760000.0;

    public Duration(){
    }

    public Duration(double duration){
        setDuration(duration);
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration){
        if (duration < MIN && duration > MAX)
            throw new IllegalArgumentException(String.format("Argument not with in limits (%f-%f)", MIN, MAX));
        this.duration = duration;
    }

    public String toString(){
        return String.valueOf(getValue());
    }

    @Override
    public String getKey() {
        return PARAM_KEY;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(getValue());
    }

    public Double getValue() {
        return duration;
    }
}
