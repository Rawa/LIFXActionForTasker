package com.rawa.tasker.lifxplugin.lifx.LifxConfig;

/**
 * Created by rawa on 2016-04-22.
 */
public class Selector {
    private final static String tag = "Selector";

    private String selector;

    public Selector(){
        selector = "all";
    }

    public Selector (String selector){
        assert(selector != null);

        this.selector = selector;
    }

    public String getStringValue() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    @Override
    public String toString() {
        return "Selector:" + selector;
    }
}
