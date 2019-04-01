package com.example.gints.pololuv2;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.*;
import java.io.*;


public class CallService extends AsyncTask <String, String, String> {

    String address;
    String statusCode;
    String statusMsg;

    protected String doInBackground(String param, String urls) {
        address = urls;

        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(param);
            os.flush();
            os.close();

            statusMsg = conn.getResponseMessage();
            statusCode = String.valueOf(conn.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            conn.disconnect();

            return "Request  satatuss code : " + statusCode + " Request message : " + statusMsg + " json parameters " + param;

        } catch (MalformedURLException error) {
            //Handles an incorrectly entered URL
            return error.getMessage();
        } catch (SocketTimeoutException error) {
            //Handles URL access timeout.
            return error.getMessage();
        } catch (IOException error) {
            //Handles input and output error
            return error.getMessage();

        }
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}








