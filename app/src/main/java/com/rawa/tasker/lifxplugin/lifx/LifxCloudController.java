package com.rawa.tasker.lifxplugin.lifx;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rawa.tasker.lifxplugin.LogHelper;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.APICalls.AAPICall;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.LifxConfig;
import com.rawa.tasker.lifxplugin.lifx.LifxConfig.Token;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

import static com.rawa.tasker.lifxplugin.LogHelper.LogDebug;

/**
 * Created by rawa on 2015-06-16.
 */
public class LifxCloudController {
    private final static String TAG = "LifxCloudController";

    private Context context;
    private static final boolean DEBUGGING = true;

    public LifxCloudController(Context context) {
        this.context = context;
    }

    public void performLIFXAction(LifxConfig lifx){
        new APICaller(lifx.getApiCall(), lifx.getToken()).execute();
    }

    private class APICaller extends AsyncTask<Void, Void, String> {
        private final int TIMEOUT_MS = 30000;

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
                LogDebug(DEBUGGING, TAG, "apiCall=" + apiCall.toString());

                url = new URL("https://api.lifx.com/v1/lights/all/" + this.apiCall.getAction());
                
                LogDebug(DEBUGGING, TAG, "URL=" + url.toString());

                LogDebug(DEBUGGING, TAG, "Opening connection");
                conn = (HttpsURLConnection) url.openConnection();

                conn.setReadTimeout(TIMEOUT_MS);
                conn.setConnectTimeout(TIMEOUT_MS);

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod(this.apiCall.getRequestMethod().toString());
                conn.setRequestProperty("Authorization", "Bearer " + token.getStringValue());

                LogDebug(DEBUGGING, TAG, "Sending data");
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                BufferedWriter writer = new BufferedWriter(osw);
                String message = getDataString(this.apiCall.getParamMap());
                writer.write(message);
                writer.flush();
                writer.close();

                LogDebug(DEBUGGING, TAG, "Message=" + message);

                handleResponse(conn);

                response = conn.getResponseCode() + conn.getResponseMessage();

                LogDebug(DEBUGGING, TAG, "Response=" + response);

            } catch (Exception e) {
                NotificationHelper.notification(context, "No Internet Connection", "Failed to turn on the lights");
                e.printStackTrace();
                response = e.toString();
            } finally {
                //Close the connection when we are done
                if(conn != null){
                    conn.disconnect();
                    LogDebug(DEBUGGING, TAG, "Closing connection");
                }
            }
            return response;
        }

        private void handleResponse(HttpsURLConnection conn) throws IOException {
            switch (conn.getResponseCode()){
                case HttpURLConnection.HTTP_OK:
                    // Everything worked as expected
                    break;
                case 207:
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ByteArrayOutputStream buf = new ByteArrayOutputStream();
                    int result = bis.read();
                    while(result != -1) {
                        buf.write((byte) result);
                        result = bis.read();
                    }
                    LogDebug(DEBUGGING, TAG, "READTHIS MOFO=" + buf.toString());
                    // Inspect the response body to check status on individual operations.
                    break;
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    // Request was invalid.
                    break;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    // Bad access token.
                    break;
                case HttpURLConnection.HTTP_FORBIDDEN:
                    // Bad OAuth scope.
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Selector did not match any lights.
                    break;
                case 422:
                    // 422 Unprocessable Entity
                    // Missing or malformed parameters
                    break;
                case 426:
                    // 426 Upgrade Required
                    // HTTP was used to make the request instead of HTTPS. Repeat the request using HTTPS instead. See the Authentication section.
                    break;
                case 429:
                    // Too Many Requests
                    // The request exceeded a rate limit. See the Rate Limits section.
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    // Something went wrong on LIFX's end.
                    break;
                case HttpURLConnection.HTTP_BAD_GATEWAY:
                    // Something went wrong on LIFX's end.
                    break;
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    // Something went wrong on LIFX's end.
                    break;
                case 523:
                    // Something went wrong on LIFX's end.
                    break;
                default:
                    // Unknown response code

            }

        }

        @Override
        protected void onPostExecute(String result) {
            NotificationHelper.notification(context, "LIFXPlugin for Tasker", result);
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
