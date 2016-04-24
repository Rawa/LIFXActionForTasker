package com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls;

import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.IParameter;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.RequestMethod;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Created by rawa on 2016-03-02.
 */
public abstract class AAPICall {

    public abstract String getAction();
    public abstract RequestMethod getRequestMethod();

    public abstract HashMap<String, String> getParamMap();


    protected final void addIParameter(HashMap<String, String> hashMap, IParameter iParam){
        if(iParam != null)
            hashMap.put(iParam.getKey(), iParam.getStringValue());
    }

    public String toString(){
        boolean first = true;
        StringBuilder stringBuilder = new StringBuilder();

        for(Entry<String, String> e : getParamMap().entrySet()){
            if(!first){
                stringBuilder.append(", ");
            }
            if(e != null){
                stringBuilder.append(e.getKey());
                stringBuilder.append(":");
                stringBuilder.append(e.getValue());
            }

            first = false;
        }

        return stringBuilder.toString();
    }
}
