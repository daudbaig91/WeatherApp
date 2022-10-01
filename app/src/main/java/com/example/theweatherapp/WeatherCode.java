package com.example.theweatherapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

public class WeatherCode {
    String str = "[{\"code\":1000,\"day\":\"Sunny\",\"night\":\"Clear\",\"icon\":\"01\"},{\"code\":1003,\"day\":\"Partlycloudy\",\"night\":\"Partlycloudy\",\"icon\":\"02\"},{\"code\":1006,\"day\":\"Cloudy\",\"night\":\"Cloudy\",\"icon\":\"03\"},{\"code\":1009,\"day\":\"Overcast\",\"night\":\"Overcast\",\"icon\":\"04\"},{\"code\":1030,\"day\":\"Mist\",\"night\":\"Mist\",\"icon\":\"50\"},{\"code\":1063,\"day\":\"Patchyrainpossible\",\"night\":\"Patchyrainpossible\",\"icon\":\"10\"},{\"code\":1066,\"day\":\"Patchysnowpossible\",\"night\":\"Patchysnowpossible\",\"icon\":\"13\"},{\"code\":1069,\"day\":\"Patchysleetpossible\",\"night\":\"Patchysleetpossible\",\"icon\":\"10\"},{\"code\":1072,\"day\":\"Patchyfreezingdrizzlepossible\",\"night\":\"Patchyfreezingdrizzlepossible\",\"icon\":\"13\"},{\"code\":1087,\"day\":\"Thunderyoutbreakspossible\",\"night\":\"Thunderyoutbreakspossible\",\"icon\":\"11\"},{\"code\":1114,\"day\":\"Blowingsnow\",\"night\":\"Blowingsnow\",\"icon\":\"13\"},{\"code\":1117,\"day\":\"Blizzard\",\"night\":\"Blizzard\",\"icon\":\"13\"},{\"code\":1135,\"day\":\"Fog\",\"night\":\"Fog\",\"icon\":\"50\"},{\"code\":1147,\"day\":\"Freezingfog\",\"night\":\"Freezingfog\",\"icon\":\"50\"},{\"code\":1150,\"day\":\"Patchylightdrizzle\",\"night\":\"Patchylightdrizzle\",\"icon\":\"10\"},{\"code\":1153,\"day\":\"Lightdrizzle\",\"night\":\"Lightdrizzle\",\"icon\":\"10\"},{\"code\":1168,\"day\":\"Freezingdrizzle\",\"night\":\"Freezingdrizzle\",\"icon\":\"13\"},{\"code\":1171,\"day\":\"Heavyfreezingdrizzle\",\"night\":\"Heavyfreezingdrizzle\",\"icon\":\"13\"},{\"code\":1180,\"day\":\"Patchylightrain\",\"night\":\"Patchylightrain\",\"icon\":\"10\"},{\"code\":1183,\"day\":\"Lightrain\",\"night\":\"Lightrain\",\"icon\":\"10\"},{\"code\":1186,\"day\":\"Moderaterainattimes\",\"night\":\"Moderaterainattimes\",\"icon\":\"10\"},{\"code\":1189,\"day\":\"Moderaterain\",\"night\":\"Moderaterain\",\"icon\":\"09\"},{\"code\":1192,\"day\":\"Heavyrainattimes\",\"night\":\"Heavyrainattimes\",\"icon\":\"09\"},{\"code\":1195,\"day\":\"Heavyrain\",\"night\":\"Heavyrain\",\"icon\":\"09\"},{\"code\":1198,\"day\":\"Lightfreezingrain\",\"night\":\"Lightfreezingrain\",\"icon\":\"09\"},{\"code\":1201,\"day\":\"Moderateorheavyfreezingrain\",\"night\":\"Moderateorheavyfreezingrain\",\"icon\":\"09\"},{\"code\":1204,\"day\":\"Lightsleet\",\"night\":\"Lightsleet\",\"icon\":\"13\"},{\"code\":1207,\"day\":\"Moderateorheavysleet\",\"night\":\"Moderateorheavysleet\",\"icon\":\"13\"},{\"code\":1210,\"day\":\"Patchylightsnow\",\"night\":\"Patchylightsnow\",\"icon\":\"13\"},{\"code\":1213,\"day\":\"Lightsnow\",\"night\":\"Lightsnow\",\"icon\":\"13\"},{\"code\":1216,\"day\":\"Patchymoderatesnow\",\"night\":\"Patchymoderatesnow\",\"icon\":\"13\"},{\"code\":1219,\"day\":\"Moderatesnow\",\"night\":\"Moderatesnow\",\"icon\":\"13\"},{\"code\":1222,\"day\":\"Patchyheavysnow\",\"night\":\"Patchyheavysnow\",\"icon\":\"13\"},{\"code\":1225,\"day\":\"Heavysnow\",\"night\":\"Heavysnow\",\"icon\":\"13\"},{\"code\":1237,\"day\":\"Icepellets\",\"night\":\"Icepellets\",\"icon\":\"13\"},{\"code\":1240,\"day\":\"Lightrainshower\",\"night\":\"Lightrainshower\",\"icon\":\"09\"},{\"code\":1243,\"day\":\"Moderateorheavyrainshower\",\"night\":\"Moderateorheavyrainshower\",\"icon\":\"10\"},{\"code\":1246,\"day\":\"Torrentialrainshower\",\"night\":\"Torrentialrainshower\",\"icon\":\"10\"},{\"code\":1249,\"day\":\"Lightsleetshowers\",\"night\":\"Lightsleetshowers\",\"icon\":\"10\"},{\"code\":1252,\"day\":\"Moderateorheavysleetshowers\",\"night\":\"Moderateorheavysleetshowers\",\"icon\":\"13\"},{\"code\":1255,\"day\":\"Lightsnowshowers\",\"night\":\"Lightsnowshowers\",\"icon\":\"13\"},{\"code\":1258,\"day\":\"Moderateorheavysnowshowers\",\"night\":\"Moderateorheavysnowshowers\",\"icon\":\"13\"},{\"code\":1261,\"day\":\"Lightshowersoficepellets\",\"night\":\"Lightshowersoficepellets\",\"icon\":\"13\"},{\"code\":1264,\"day\":\"Moderateorheavyshowersoficepellets\",\"night\":\"Moderateorheavyshowersoficepellets\",\"icon\":\"13\"},{\"code\":1273,\"day\":\"Patchylightrainwiththunder\",\"night\":\"Patchylightrainwiththunder\",\"icon\":\"11\"},{\"code\":1276,\"day\":\"Moderateorheavyrainwiththunder\",\"night\":\"Moderateorheavyrainwiththunder\",\"icon\":\"11\"},{\"code\":1279,\"day\":\"Patchylightsnowwiththunder\",\"night\":\"Patchylightsnowwiththunder\",\"icon\":\"13\"},{\"code\":1282,\"day\":\"Moderateorheavysnowwiththunder\",\"night\":\"Moderateorheavysnowwiththunder\",\"icon\":\"13\"}]";



    HashMap<String,String> list = new HashMap<>();

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("error",ex.toString());
            return null;
        }
        return json;
    }


    public void create(Context context) throws JSONException {
        list.clear();
        JSONObject obj = new JSONObject(loadJSONFromAsset(context));
        JSONArray arr =  obj.getJSONArray("Array");

        for (int i = 0 ; i < arr.length(); i++){
            list.put(((JSONObject)arr.get(i)).getString("code"),
                    ((JSONObject)arr.get(i)).getString("icon"));
        }

    }


    public String getCode(String string){
        return list.get(string);
    }
}
