package com.example.gandh.hw7;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gandh on 3/6/2017.
 */

public class Podcast_async extends AsyncTask<String,Void,ArrayList<Podcast>> {
    from_podcast_async interf;
    BufferedReader bfr;
    String s;
    public Podcast_async(from_podcast_async interf) {
        this.interf = interf;
    }
    boolean ex = true;
    @Override
    protected ArrayList<Podcast> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            //Log.d("demo",params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            InputStream in = con.getInputStream();
            return new Podcast_util().podcast_parser(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Podcast> podcasts) {
        interf.podcast_datafixer(podcasts);

    }

    interface from_podcast_async{
        void podcast_datafixer(ArrayList<Podcast> podcast_datalist);
    }
}
