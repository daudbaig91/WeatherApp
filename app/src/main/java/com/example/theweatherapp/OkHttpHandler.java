package com.example.theweatherapp;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.View;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class OkHttpHandler extends AsyncTask {

    OkHttpClient client = new OkHttpClient();
    String stringcheck = "testtest";
    public void doInBackground2(String url,View view, Context context,int position) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        //
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            new JsonParser(str,view,context,position);
        }catch (Exception e){

            e.printStackTrace();
        }
    }
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}


