package com.example.gandh.hw7;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements  Podcast_async.from_podcast_async,Podcast_adaptor.play_clciker {
   RecyclerView rv;
    ProgressBar pb;
    RecyclerView.LayoutManager lmanager,gmanager;
    RecyclerView.Adapter sample;
    ImageButton pause,refresh,click;
    ArrayList<Podcast> podcast_datalist;
    MediaPlayer mediaPlayer;
    CountDownTimer cdt;
    Thread thread;
    int selector=0;
    SeekBar sb;
    Podcast_adaptor adaptor;
    int time_temp;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = (ProgressBar) findViewById(R.id.pb1);
        pause = (ImageButton) findViewById(R.id.pause);
        refresh = (ImageButton) findViewById(R.id.refresh);
        click = (ImageButton) findViewById(R.id.click);
        sb = (SeekBar) findViewById(R.id.seekBar);
        pb.setVisibility(pb.VISIBLE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        pause.setVisibility(pause.INVISIBLE);
        sb.setVisibility(sb.INVISIBLE);
        lmanager = new LinearLayoutManager(this);
        gmanager = new GridLayoutManager(this,2);
        selector=0;
        /*refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });*/
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selector==0)
                {
                    //pb.setVisibility(pb.VISIBLE);
                    selector =1;
                    adaptor = new Podcast_adaptor(MainActivity.this,podcast_datalist,MainActivity.this,selector);
                    rv.setLayoutManager(gmanager);
                    rv.setAdapter(adaptor);
                }
                else if(selector==1)
                {
                   // pb.setVisibility(pb.VISIBLE);
                    selector =0;
                    adaptor = new Podcast_adaptor(MainActivity.this,podcast_datalist,MainActivity.this,selector);
                    rv.setLayoutManager(lmanager);
                   rv.setAdapter(adaptor);
                }
            }
        });
        rv = (RecyclerView) findViewById(R.id.rv1);
        rv.setLayoutManager(gmanager);
        pb.setVisibility(pb.VISIBLE);
        if(isconnected()) {
            new Podcast_async(this).execute("https://www.npr.org/rss/podcast.php?id=510298");
        }
    }
    boolean isconnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nw = cm.getActiveNetworkInfo();
        if(nw.isConnected() && nw!=null)
        {
            return true;
        }
        else
            return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void podcast_datafixer(ArrayList<Podcast> podcast_datalis) {
        this.podcast_datalist = podcast_datalis;
        pb.setVisibility(pb.INVISIBLE);
        Collections.sort(this.podcast_datalist, new Comparator<Podcast>() {
            @Override
            public int compare(Podcast o1, Podcast o2)
            {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                Date date1 = null;
                Date date2 = null;
                try
                {
                    date1 =  format.parse(o1.getShort_date());
                    date2 = format.parse(o2.getShort_date());
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                if (date1.after(date2)) {
                   // Log.d("demo","Date1 is after Date2");
                    return -1;
                }

               else if (date1.before(date2)) {
                   // Log.d("demo","Date1 is before Date2");
                    return 1;
                }

                else   {
                  //  Log.d("demo","Date1 is equal Date2");
                    return 0;
                }


            }
        });

        pb.setVisibility(pb.INVISIBLE);
        rv.setLayoutManager(lmanager);
         adaptor = new Podcast_adaptor(this,this.podcast_datalist,this,selector);
        rv.setAdapter(adaptor);

    }

    @Override
    public void play_click_listener(final int position) throws IOException {
        pause.setVisibility(pause.INVISIBLE);
        sb.setVisibility(sb.INVISIBLE);
        sb.setProgress(0);
        if(mediaPlayer!=null) {
          // mediaPlayer.start();
            //if (mediaPlayer.isPlaying()) {
                mediaPlayer.release();
                mediaPlayer=null;
            if(cdt!=null)
                cdt.cancel();
                pause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
         //   }
        }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Toast toast =  Toast.makeText(this,"playing "+podcast_datalist.get(position).getTitle(),Toast.LENGTH_SHORT);
        toast.show();
        thread = new Thread(new Player(podcast_datalist.get(position).getMp3(),mediaPlayer));
        thread.start();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pause.setVisibility(pause.VISIBLE);
                sb.setVisibility(sb.VISIBLE);
                mediaPlayer.start();
                if(podcast_datalist.get(position).getDuration()!=null) {
                    sb.setMax(Integer.parseInt(podcast_datalist.get(position).getDuration()));
                    timer(0, Integer.parseInt(podcast_datalist.get(position).getDuration()));
                }
                else
                {
                    sb.setVisibility(sb.INVISIBLE);
                    Toast t = Toast.makeText(MainActivity.this,"Duration info not available",Toast.LENGTH_SHORT);
                    t.show();
                }
                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mediaPlayer.isPlaying())
                        {
                            mediaPlayer.pause();
                            pause.setImageDrawable(getResources().getDrawable(R.drawable.play));
                            if(cdt!=null)
                                cdt.cancel();
                        }
                        else
                        {
                            pause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                            mediaPlayer.start();
                            if(podcast_datalist.get(position).getDuration()!=null) {
                                timer(time_temp, Integer.parseInt(podcast_datalist.get(position).getDuration()));
                            }
                        }
                    }
                });
            }
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                pause.setVisibility(pause.INVISIBLE);
                sb.setVisibility(sb.INVISIBLE);
            }
        });

    }

    @Override
    public void intent_generator(int position) throws IOException {
        Intent ia = new Intent(MainActivity.this,Pod_Item_Activity.class);
        ia.putExtra("key",podcast_datalist.get(position));
        if(mediaPlayer!=null) {
           // mediaPlayer.start();
           // if(mediaPlayer.isPlaying())
            // mediaPlayer.stop();
            //mediaPlayer.reset();
            mediaPlayer.release();
            if(cdt!=null)
            cdt.cancel();
        }
        pause.setVisibility(pause.INVISIBLE);
        sb.setVisibility(sb.INVISIBLE);
        startActivity(ia);
    }

    void timer(final int time, final  int timer)
    {
        cdt = new CountDownTimer((timer-time)*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sb.setProgress((int) (timer-((millisUntilFinished/1000))));
                time_temp=sb.getProgress();
               // Log.d("time",sb.getProgress()+"");
            }

            @Override
            public void onFinish() {
                pause.setVisibility(pause.INVISIBLE);
                sb.setVisibility(sb.INVISIBLE);
            }
        }.start();
    }
}
