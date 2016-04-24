package com.rawa.tasker.lifxplugin.lifx;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.AAPICall;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.LifxConfig;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Token;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by rawa on 2015-06-16.
 */
public class LifxCloudController {
    private final static String tag = "LifxCloudController";

    private Context context;
    private boolean debug = false;

    public LifxCloudController(Context context) {
        this.context = context;
    }

    public void performLIFXAction(LifxConfig lifx){
        debug = lifx.isDebugEnabled();
        new APICaller(lifx.getApiCall(), lifx.getToken()).execute();
    }

    private class APICaller extends AsyncTask<Void, Void, String> {
        private final int TIMEOUT_MS = 5000;

        private final AAPICall apiCall;
        private final Token token;

        public APICaller(AAPICall apiCall, Token token){
            this.apiCall = apiCall;
            this.token = token;
        }


        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(Void... values) {
            URL url;
            String response = "Init";
            HttpsURLConnection conn = null;
            try {
                Log.d(tag, "Creating url");
                url = new URL("https://api.lifx.com/v1/lights/all/" + this.apiCall.getAction());

                Log.d(tag, "Opening connection");
                conn = (HttpsURLConnection) url.openConnection();
                Log.d(tag, "Settings timeouts");
                conn.setReadTimeout(TIMEOUT_MS);
                conn.setConnectTimeout(TIMEOUT_MS);

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod(this.apiCall.getRequestMethod().toString());
                conn.setRequestProperty("Authorization", "Bearer " + token.getStringValue());

                Log.d(tag, "Sending data");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(getDataString(this.apiCall.getParamMap()));

                Log.d(tag, "Creating response");
                response = "Response code: " + conn.getResponseCode() + "\n" + " Message: " + conn.getResponseMessage();
                Log.d(tag, response);

                Log.d(tag, "Closing stream");
                writer.flush();
                writer.close();
                os.close();

            } catch (Exception e) {
                NotificationHelper.notification(context, "No Internet Connection", "Failed to turn on the lights");
                e.printStackTrace();
            } finally {
                //Close the connection when we are done
                if(conn != null)
                    conn.disconnect();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if(debug){
                NotificationHelper.notification(context, "LIFXPlugin for Tasker", result);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

    }

    private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
