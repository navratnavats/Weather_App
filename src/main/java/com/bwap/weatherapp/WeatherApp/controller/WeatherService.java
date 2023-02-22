package com.bwap.weatherapp.WeatherApp.controller;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class WeatherService {
    ArrayList<Double> coordinates = new ArrayList<Double>();
    private OkHttpClient client;
    private Response response;
    private String cityName;
    private String unit;
    private String state;
    private String country;
    private String APIkey = "ba61e79a0119a86f77cc471643451b24";

    //Getting Data from OpenWeather API
    public JSONArray getGeoCoding(){
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.openweathermap.org/geo/1.0/direct?q="+getCityName()+","+ getState()+","+getCountry()+"&limit=1&appid="+APIkey)
                .build();
        try {
            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            return new JSONArray(responseBody);
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getWeather(){
        client = new OkHttpClient();  //using OKHTTP dependency . You have to add this mannually form OKHTTP website
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?lat="+coordinates.get(0)+"&lon="+coordinates.get(1)+"&units="+getUnit()+"&appid="+APIkey)
                .build();
        System.out.println(request.url());

        try {
            response = client.newCall(request).execute();
//            System.out.println("JSON "+new JSONObject(response.body().string()));
            String responseBody = response.body().string();
            return new JSONObject(responseBody);
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    //Getting required data from Weather JSON API
    //JSON Objects and JSON Arrays


    public JSONArray returnWeatherArray() throws JSONException {
        JSONArray weatherJsonArray = getWeather().getJSONArray("weather");
        return weatherJsonArray;
    }

    public JSONObject returnMainObject() throws JSONException {
        JSONObject mainObject = getWeather().getJSONObject("main");
        return mainObject;
    }


    public JSONObject returnWindObject() throws JSONException {
        JSONObject wind = getWeather().getJSONObject("wind");
        return wind;
    }

    public void geoTag() throws JSONException {
        coordinates.clear();
        for(int i=0;i<getGeoCoding().length();i++)
        {
            JSONObject jsonObject = getGeoCoding().getJSONObject(i);
//            System.out.println("json object " +jsonObject);
            coordinates.add(jsonObject.getDouble("lat"));
            coordinates.add(jsonObject.getDouble("lon"));
            System.out.println(coordinates);
        }
    }
    public JSONObject returnSysObject() throws JSONException{
        JSONObject sys = getWeather().getJSONObject("sys");
        return sys;
        } // to  pull the values of Sys from JSON


     // Getters and Setters for CityName and Unit

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
