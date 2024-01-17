package com.example.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowDownLoader {
    public String download(String url_) {
        try {
            URL url = new URL(url_);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String responseData = stringBuilder.toString();

                reader.close();
                inputStream.close();
                connection.disconnect();

                Log.v("download",responseData);
                return responseData;
            } else {
                Log.e("download", "Failed to download response code: " + responseCode);
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public ArrayList<LocationMessage> parseJsonObjects(String json) {
        ArrayList<LocationMessage> locations = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonData = jsonObject.getJSONArray("shops");
            for (int i = 0; i < jsonData.length(); i++) {
                jsonObject = jsonData.getJSONObject(i);
                String name = jsonObject.getString("name");
                double latitude = jsonObject.getDouble("latitude");
                double longitude = jsonObject.getDouble("longitude");
                String memo = jsonObject.getString("memo");
                LocationMessage shop = new LocationMessage(name, latitude, longitude, memo);
                locations.add(shop);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locations;
    }
}
