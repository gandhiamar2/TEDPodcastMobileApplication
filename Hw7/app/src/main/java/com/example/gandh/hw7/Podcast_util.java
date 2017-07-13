package com.example.gandh.hw7;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gandh on 3/6/2017.
 */

public class Podcast_util {
    ArrayList<Podcast> podcast_datalist = new ArrayList<>();
    Podcast pod;

    ArrayList<Podcast> podcast_parser(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser pull = XmlPullParserFactory.newInstance().newPullParser();
        pull.setInput(in,"UTF-8");
        int count =0;
        int event = pull.getEventType();
        while (event!=XmlPullParser.END_DOCUMENT)
        {
            switch (event) {
                case XmlPullParser.START_TAG:

                    if(pull.getName().equals("title")) {
                        pod = new Podcast();
                        pod.setTitle(pull.nextText());
                        count =2;
                        if(pod.getTitle().equals("TED Radio Hour"))
                        {
                            count =0;

                        }
                    }
                    else if (pull.getName().equals("description"))
                        pod.setDescription(pull.nextText());
                    else if (pull.getName().equals("pubDate")) {
                        pod.setDate(pull.nextText().substring(0,16));
                        String input = pod.getDate().substring(5,16);
                        SimpleDateFormat parser = new SimpleDateFormat("dd MMM yyyy");
                        Date date = null;
                        try {
                            date = parser.parse(input);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = formatter.format(date);
                       // Log.d("demo",formattedDate);
                        pod.setShort_date(formattedDate);
                    }
                    else if (pull.getName().equals("itunes:duration"))
                        pod.setDuration(pull.nextText());
                    else if (pull.getName().equals("itunes:image"))
                        pod.setImage(pull.getAttributeValue(null,"href"));
                    else if (pull.getName().equals("enclosure"))
                        pod.setMp3(pull.getAttributeValue(null,"url"));

                    break;

                case XmlPullParser.END_TAG:
                    if(pull.getName().equals("item") && count ==2)
                    {
                        podcast_datalist.add(pod);
                      // Log.d("demo",pod.getTitle()+pod.getDescription());

                        pod = null;
                       // Log.d("demo",podcast_datalist.size()+"");

                    }

                    break;
            }
            event = pull.next();
        }
        return podcast_datalist;
    }
}
