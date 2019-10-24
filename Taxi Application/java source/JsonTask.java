package com.example.uitest;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonTask extends AsyncTask<String,String,String> {
    @Override
    protected String doInBackground(String... strings) {
        try {
            JSONObject jsonObject = new JSONObject();
            if(strings[1].equals("login")){
                jsonObject.accumulate("type","login");
                jsonObject.accumulate("id",strings[2]);
                jsonObject.accumulate("password",strings[3]);
            }
            else if(strings[1].equals("logout")){
                jsonObject.accumulate("type","logout");
                jsonObject.accumulate("id",strings[2]);
            }
            else if(strings[1].equals("sign_up")){
                jsonObject.accumulate("type","sign_up");
                jsonObject.accumulate("name",strings[2]);
                jsonObject.accumulate("phone_number",strings[3]);
                jsonObject.accumulate("id",strings[4]);
                jsonObject.accumulate("password",strings[5]);
                jsonObject.accumulate("mem_code",strings[6]);
            }
            else if(strings[1].equals("location_set")){
                jsonObject.accumulate("type","location_set");
                jsonObject.accumulate("id",strings[2]);
                jsonObject.accumulate("mem_code",strings[3]);
                jsonObject.accumulate("latitude",strings[4]);
                jsonObject.accumulate("longitude",strings[5]);
            }
            else if(strings[1].equals("get_information")){
                jsonObject.accumulate("type","get_information");
                jsonObject.accumulate("id",strings[2]);
                jsonObject.accumulate("mem_code",strings[3]);
            }
            else if(strings[1].equals("get_nearestTaxi")){
                jsonObject.accumulate("type","get_nearestTaxi");
                jsonObject.accumulate("id",strings[2]);
                jsonObject.accumulate("latitude",strings[3]);
                jsonObject.accumulate("longitude",strings[4]);
            }
            else if(strings[1].equals("set_destination")){
                jsonObject.accumulate("type","set_destination");
                jsonObject.accumulate("id",strings[2]);
                jsonObject.accumulate("latitude",strings[3]);
                jsonObject.accumulate("longitude",strings[4]);
            }
            else if(strings[1].equals("ok")){
                jsonObject.accumulate("type","ok");
                jsonObject.accumulate("driver_id",strings[2]);
                jsonObject.accumulate("customer_id",strings[3]);
            }
            else if(strings[1].equals("cancel")){
                jsonObject.accumulate("type","cancel");
                jsonObject.accumulate("driver_id",strings[2]);
                jsonObject.accumulate("customer_id",strings[3]);
            }
            else if(strings[1].equals("reset_customer")){
                jsonObject.accumulate("type","reset_customer");
                jsonObject.accumulate("id",strings[2]);
            }
            else if(strings[1].equals("reset_driver")){
                jsonObject.accumulate("type","reset_driver");
                jsonObject.accumulate("id",strings[2]);
            }
            else if(strings[1].equals("isDrive_true")){
                jsonObject.accumulate("type","isDrive_true");
                jsonObject.accumulate("id",strings[2]);
            }
            else if(strings[1].equals("isDrive_false")){
                jsonObject.accumulate("type","isDrive_false");
                jsonObject.accumulate("id",strings[2]);
            }
            HttpURLConnection con = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Cache-Control","no-cache");
                con.setRequestProperty("Content-Type","application/json");
                con.setRequestProperty("Accept","text/html");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.connect();

                OutputStream outStream = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();

                InputStream stream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader!=null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}
