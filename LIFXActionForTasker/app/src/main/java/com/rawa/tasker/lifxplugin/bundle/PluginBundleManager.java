/*
 * Copyright 2013 two forty four a.m. LLC <http://www.twofortyfouram.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.rawa.tasker.lifxplugin.bundle;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.rawa.tasker.lifxplugin.BuildConfig;
import com.rawa.tasker.lifxplugin.Constants;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.AAPICall;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.State;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.Toggle;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.LifxConfig;

/**
 * Class for managing the {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} for this plug-in.
 */
public final class PluginBundleManager
{
    /**
     * Type: {@code String}.
     * <p>
     * String message to display in a Toast message.
     */
    public static final String BUNDLE_EXTRA_STRING_API_TOKEN = "com.rawa.tasker.lifxplugin.extra.API_TOKEN"; //$NON-NLS-1$
    public static final String BUNDLE_EXTRA_STRING_ACTION= "com.rawa.tasker.lifxplugin.extra.ACTION";
    public static final String BUNDLE_EXTRA_STRING_SELECTOR = "com.rawa.tasker.lifxplugin.extra.SELECTOR"; //$NON-NLS-1$

    public static final String BUNDLE_EXTRA_STRING_POWER = "com.rawa.tasker.lifxplugin.extra.POWER"; //$NON-NLS-1$
    public static final String BUNDLE_EXTRA_DOUBLE_DURATION = "com.rawa.tasker.lifxplugin.extra.DURATION"; //$NON-NLS-1$
    public static final String BUNDLE_EXTRA_DOUBLE_BRIGHTNESS = "com.rawa.tasker.lifxplugin.extra.BRIGHTNESS"; //$NON-NLS-1$
    public static final String BUNDLE_EXTRA_STRING_COLOR = "com.rawa.tasker.lifxplugin.extra.COLOR"; //$NON-NLS-1$

    public static final String BUNDLE_EXTRA_BOOLEAN_DEBUG = "com.rawa.tasker.lifxplugin.extra.DEBUG";
    /**
     * Type: {@code int}.
     * <p>
     * versionCode of the plug-in that saved the Bundle.
     */
    /*
     * This extra is not strictly required, however it makes backward and forward compatibility significantly
     * easier. For example, suppose a bug is found in how some version of the plug-in stored its Bundle. By
     * having the version, the plug-in can better detect when such bugs occur.
     */
    public static final String BUNDLE_EXTRA_INT_VERSION_CODE = "com.rawa.tasker.lifxplugin.extra.INT_VERSION_CODE"; //$NON-NLS-1$

    /**
     * Method to verify the content of the bundle are correct.
     * <p>
     * This method will not mutate {@code bundle}.
     *
     * @param bundle bundle to verify. May be null, which will always return false.
     * @return true if the Bundle is valid, false if the bundle is invalid.
     */
    public static boolean isBundleValid(final Bundle bundle)
    {
        // Bundle should not be null;
        if (null == bundle)
            return false;

        // TODO Version number? Must have?
        if (!bundle.containsKey(BUNDLE_EXTRA_INT_VERSION_CODE)) {
            if (Constants.IS_LOGGABLE)
                Log.e(Constants.LOG_TAG, String.format("bundle must contain extra %s", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            return false;
        } else if (bundle.getInt(BUNDLE_EXTRA_INT_VERSION_CODE, 0) != bundle.getInt(BUNDLE_EXTRA_INT_VERSION_CODE, 1)) {
            if (Constants.IS_LOGGABLE)
                Log.e(Constants.LOG_TAG, String.format("bundle extra %s appears to be the wrong type.  It must be an int", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            return false;
        }

        // Must contain a token
        if (!bundle.containsKey(BUNDLE_EXTRA_STRING_API_TOKEN)) {
            if (Constants.IS_LOGGABLE)
                Log.e(Constants.LOG_TAG, String.format("Bundle must contain extra %s", BUNDLE_EXTRA_STRING_API_TOKEN)); //$NON-NLS-1$
            return false;
        } else if (TextUtils.isEmpty(bundle.getString(BUNDLE_EXTRA_STRING_API_TOKEN))){
            // TODO Validate the API token more thoroughly?
            if (Constants.IS_LOGGABLE)
                Log.e(Constants.LOG_TAG, String.format("bundle extra %s appears to be null or empty.  It must be a non-empty string", BUNDLE_EXTRA_STRING_API_TOKEN)); //$NON-NLS-1$
            return false;
        }

        // Should have an action set.
        if (!bundle.containsKey(BUNDLE_EXTRA_STRING_ACTION)){
            if (Constants.IS_LOGGABLE)
                Log.e(Constants.LOG_TAG, String.format("bundle must contain extra %s", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            return false;
        }

        switch (bundle.getString(BUNDLE_EXTRA_STRING_ACTION)){
            case State.ACTION:
                return validateState(bundle);
            case Toggle.ACTION:
                return validateToggle(bundle);
            default:
                if (Constants.IS_LOGGABLE)
                    Log.e(Constants.LOG_TAG, String.format("Bundle does not contain a valid action %s", BUNDLE_EXTRA_STRING_ACTION)); //$NON-NLS-1$
                return false;
        }
    }

    private static boolean validateToggle(Bundle bundle) {
        return true;
    }

    private static boolean validateState(Bundle bundle) {
        return true;
    }

    /**
     * @param context Application context.
     * @param lifxConfig Configuration for the lifxAction.
     * */
    public static Bundle generateBundle(final Context context, LifxConfig lifxConfig) {
        final Bundle result = new Bundle();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, BuildConfig.VERSION_CODE);

        AAPICall apiCall = lifxConfig.getApiCall();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(context));
        result.putString(BUNDLE_EXTRA_STRING_API_TOKEN, lifxConfig.getToken().getStringValue());
        result.putBoolean(BUNDLE_EXTRA_BOOLEAN_DEBUG, lifxConfig.isDebugEnabled());
        result.putString(BUNDLE_EXTRA_STRING_SELECTOR, lifxConfig.getSelector().toString());
        result.putString(BUNDLE_EXTRA_STRING_ACTION, apiCall.getAction());
        switch (apiCall.getAction()){
            case State.ACTION:
                State stateCall = (State) apiCall;
                result.putString(BUNDLE_EXTRA_STRING_POWER, stateCall.getPower().getStringValue());
                if(stateCall.getDuration() != null){
                    result.putDouble(BUNDLE_EXTRA_DOUBLE_DURATION, stateCall.getDuration().getValue());
                }
                if(stateCall.getBrightness() != null) {
                    result.putDouble(BUNDLE_EXTRA_DOUBLE_BRIGHTNESS, stateCall.getBrightness().getValue());
                }
                if(stateCall.getColor() != null) {
                    result.putString(BUNDLE_EXTRA_STRING_COLOR, stateCall.getColor().getStringValue());
                }

                break;
            case Toggle.ACTION:
                Toggle toggleCall = (Toggle) apiCall;
                if(toggleCall.getDuration() != null) {
                    result.putDouble(BUNDLE_EXTRA_DOUBLE_DURATION, toggleCall.getDuration().getValue());
                }
        }

        return result;
    }

    /**
     * Private constructor prevents instantiation
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private PluginBundleManager()
    {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}