package com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters;

/**
 * Created by rawa on 2016-04-22.
 */
public enum Power implements IParameter {
    ON("on"),
    OFF("off");

    public final static String PARAM_KEY = "power";

    private String power;
    Power(String power){
        this.power = power;
    }

    public static Power find(String power){

        for(Power p : Power.values()){
           if(p.getStringValue().equals(power)){
               return p;
           }
        }

        return null;
    }

    @Override
    public String getKey() {
        return PARAM_KEY;
    }

    @Override
    public String getStringValue(){
        return power;
    }
}
