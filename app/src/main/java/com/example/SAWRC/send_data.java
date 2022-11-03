package com.example.SAWRC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class send_data
{
    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson)
    {
        HashMap<String,String> direc = new HashMap<>();
        String drn = "";
        String dinc ="";


        try {

            drn = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            dinc = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");

            direc.put("duration" , drn);
            direc.put("distance", dinc);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return direc;
    }


    private HashMap<String, String> navigateNear(JSONObject object)
    {
        HashMap<String, String> mapH = new HashMap<>();
        String nam = "-NA-";
        String v = "-NA-";
        String lat = "";
        String lon = "";
        String ref = "";



        try {
            if(!object.isNull("name"))
            {

                nam = object.getString("name");

            }
            if( !object.isNull("vicinity"))
            {
                v = object.getString("vicinity");

            }
            lat = object.getJSONObject("geometry").getJSONObject("location").getString("lat");
            lon = object.getJSONObject("geometry").getJSONObject("location").getString("lng");

            ref = object.getString("reference");

            mapH.put("place_name" , nam);
            mapH.put("vicinity" , v);
            mapH.put("lat" , lat);
            mapH.put("lng" , lon);
            mapH.put("reference" , ref);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return mapH;
    }



    private List<HashMap<String,String>> nav(JSONArray jArray)
    {
        int sub = jArray.length();
        List<HashMap<String,String>> pList = new ArrayList<>();
        HashMap<String,String> pcMap ;

        for(int i = 0;i<sub;i++)
        {
            try {
                pcMap = navigateNear((JSONObject) jArray.get(i));
                pList.add(pcMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return pList;

    }

    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {


            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nav(jsonArray);
    }

    public String[] direct(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return wayArround(jsonArray);
    }

    public HashMap<String, String> director(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getDuration(jsonArray);
    }


    public String[] wayArround(JSONArray json )
    {
        int count = json.length();
        String[] polylines = new String[count];

        for(int i = 0;i<count;i++)
        {
            try
            {
                polylines[i] = way(json.getJSONObject(i));
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return polylines;
    }

    public String way(JSONObject googlePathJson)
    {
        String polygon = "";
        try
        {
            polygon = googlePathJson.getJSONObject("polyline").getString("points");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return polygon;
    }



}
