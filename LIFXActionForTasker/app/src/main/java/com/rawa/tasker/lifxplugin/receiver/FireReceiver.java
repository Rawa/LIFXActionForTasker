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

package com.rawa.tasker.lifxplugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

import com.rawa.tasker.lifxplugin.Constants;
import com.rawa.tasker.lifxplugin.LogHelper;
import com.rawa.tasker.lifxplugin.bundle.BundleScrubber;
import com.rawa.tasker.lifxplugin.bundle.PluginBundleManager;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.AAPICall;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.State;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.Toggle;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.LifxConfig;
import com.rawa.tasker.lifxplugin.lifx.LifxCloudController;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Brightness;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Color;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Duration;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Parameters.Power;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Token;

import static com.rawa.tasker.lifxplugin.LogHelper.LogDebug;

public final class FireReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationHelper";
    private static final boolean DEBUGGING = true;

    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        /*
         * Always be strict on input parameters! A malicious third-party app could send a malformed Intent.
         */

        if (!com.twofortyfouram.locale.api.Intent.ACTION_FIRE_SETTING.equals(intent.getAction()))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                      String.format(Locale.US, "Received unexpected Intent action %s", intent.getAction())); //$NON-NLS-1$
            }
            return;
        }


        BundleScrubber.scrub(intent);

        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.api.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(bundle);
        if (PluginBundleManager.isBundleValid(bundle))
        {
            final String tokenString = bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_API_TOKEN);
            final Token token = new Token(tokenString);
            final String selector = bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_SELECTOR);
            final String action = bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_ACTION);

            AAPICall apiCall = null;
            switch (action){
                case State.ACTION:
                    final String powerString = bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_POWER);
                    LogDebug(DEBUGGING, TAG, "powerString=" + powerString);
                    final Power power = Power.find(powerString);
                    LogDebug(DEBUGGING, TAG, "power=" + powerString);

                    Duration duration = new Duration();
                    if(bundle.containsKey(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_DURATION)){
                        final Double durationDouble = bundle.getDouble(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_DURATION);
                        LogDebug(DEBUGGING, TAG, "durationDouble=" + durationDouble);
                        duration = new Duration(durationDouble);
                    }
                    LogDebug(DEBUGGING, TAG, duration.toString());

                    final String colorString = bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_COLOR);
                    LogDebug(DEBUGGING, TAG, "colorString=" + colorString);
                    Color color = null;
                    if(colorString != null){
                        color = new Color(colorString);
                        LogDebug(DEBUGGING, TAG, color.toString());
                    }

                    Brightness brightness = null;
                    if(bundle.containsKey(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_BRIGHTNESS)){
                        final Double brightnessDouble = bundle.getDouble(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_BRIGHTNESS);
                        LogDebug(DEBUGGING, TAG, "brightnessDouble=" + brightnessDouble);
                        brightness = new Brightness(brightnessDouble);
                        LogDebug(DEBUGGING, TAG, brightness.toString());
                    }


                    apiCall = new State(duration, brightness, power, color);
                    break;
                case Toggle.ACTION:
                    final Double durationD= bundle.getDouble(PluginBundleManager.BUNDLE_EXTRA_DOUBLE_DURATION);

                    final Duration dur = new Duration(durationD);

                    apiCall = new Toggle(dur);
                    break;
                default:
                    Log.d("LIFXPlugin", "Something went terribly wrong, this should not happen");
                    throw new IllegalStateException("Something went sooo wrong");
            }


            final boolean debug = bundle.getBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOLEAN_DEBUG);

            LifxConfig lifxConfig = new LifxConfig(token, apiCall, debug);

            new LifxCloudController(context).performLIFXAction(lifxConfig);
        }
    }
}