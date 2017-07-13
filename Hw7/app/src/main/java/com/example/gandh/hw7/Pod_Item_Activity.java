package com.example.gandh.hw7;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gandh on 3/9/2017.
 */

public class Pod_Item_Activity extends AppCompatActivity {
    Podcast podcast;
    TextView tvv1, tvv2, tvv3, tvv4;
    ImageView ivv1;
    ImageButton ibb1;
    SeekBar sb2;
    Thread thread;
    CountDownTimer cdt;
    MediaPlayer mediaPlayer;
    int time_temp;
    int count =0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pod_episode);
         tvv1 = (TextView) findViewById(R.id.tvv1);
         tvv2 = (TextView) findViewById(R.id.tvv2);
         tvv3 = (TextView) findViewById(R.id.tvv3);
         tvv4 = (TextView) findViewById(R.id.tvv4);
        ivv1 = (ImageView) findViewById(R.id.imageView2);
        ibb1 = (ImageButton) findViewById(R.id.imageButton3);
        sb2 = (SeekBar) findViewById(R.id.seekBar2);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        podcast = (Podcast) getIntent().getExtras().getSerializable("key");
        String input = podcast.getShort_date();
        SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = parser.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = formatter.format(date);
        tvv1.setText(podcast.getTitle());
        tvv2.setText(podcast.getDescription());
        tvv3.setText("  "+formattedDate);
        tvv4.setText(" "+podcast.getDuration());
        Picasso.with(this).load(podcast.getImage()).into(ivv1);
        thread = new Thread(new Player(podcast.getMp3(), mediaPlayer));
        thread.start();
        ibb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0) {
                    Toast t1 = Toast.makeText(Pod_Item_Activity.this, "player is preparing", Toast.LENGTH_SHORT);
                    t1.show();
                    count = 1;
                }
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
             public void onPrepared(MediaPlayer mp) {
                  ibb1.setOnClickListener(new View.OnClickListener() {
                            @Override
                      public void onClick(View v) {

                        if(count==0||count==1) {
                            sb2.setProgress(0);
                            mediaPlayer.start();
                            if(podcast.getDuration()!=null) {
                                sb2.setMax(Integer.parseInt(podcast.getDuration()));
                                //Log.d("max", Integer.parseInt(podcast.getDuration()) + "");
                                timer(0, Integer.parseInt(podcast.getDuration()));
                            }
                            else
                            {

                                Toast t = Toast.makeText(Pod_Item_Activity.this,"Duration info not available",Toast.LENGTH_SHORT);
                                t.show();
                            }
                            Toast toast = Toast.makeText(Pod_Item_Activity.this,"playing "+podcast.getTitle(),Toast.LENGTH_SHORT);
                            toast.show();
                            ibb1.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                            if(podcast.getDuration()!=null) {
                                sb2.setMax(Integer.parseInt(podcast.getDuration()));
                                timer(0, Integer.parseInt(podcast.getDuration()));
                            }
                        }
                        count=2;
                        ibb1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mediaPlayer!=null) {

                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.pause();
                                        ibb1.setImageDrawable(getResources().getDrawable(R.drawable.play));
                                        if (cdt != null)
                                            cdt.cancel();
                                    } else {
                                        ibb1.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                                        mediaPlayer.start();
                                        if (podcast.getDuration() != null) {
                                            timer(time_temp, Integer.parseInt(podcast.getDuration()));
                                        }
                                    }
                                }
                            }
                        });

                      }
                  });
             }
        });




        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               if(mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.reset();
                mediaPlayer.release();
                   if(cdt!=null)
                cdt.cancel();}
                ibb1.setImageDrawable(getResources().getDrawable(R.drawable.play));
            }
        });


    }
    void timer(final int time, final  int timer)
    {
        cdt = new CountDownTimer((timer-time)*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sb2.setProgress((int) (timer-((millisUntilFinished/1000))));
                time_temp=sb2.getProgress();
               // Log.d("time",sb2.getProgress()+"");
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            if(mediaPlayer!=null) {
                mediaPlayer.release();
                mediaPlayer = null;
                if(cdt!=null)
                    cdt.cancel();
            }
            finish();
            return true;
        }
        return false;
    }
}
