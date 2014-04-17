package com.chatter.box;


import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JQuery {

    public void work(View v) {
        kill n = new kill();
        n.doInBackground();
    }
    private class kill extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... param) {
            try {
                //flasker-99999.appspot.com
                posts("flasker-99999.appspot.com");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void posts(String t) throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(t);


        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("id", "test"));
        urlParameters.add(new BasicNameValuePair("name", "test"));
        post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
        post.setHeader("User-Agent", "Shootis");
        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + t);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());


    }

}
