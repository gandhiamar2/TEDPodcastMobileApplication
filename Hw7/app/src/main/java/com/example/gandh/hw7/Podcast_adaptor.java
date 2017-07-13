package com.example.gandh.hw7;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by gandh on 3/6/2017.
 */

public class Podcast_adaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Podcast> podcast_datalist;
    play_clciker click_intf;
    int selector;

    class Viewholder0 extends RecyclerView.ViewHolder
    {

        ImageView iv;
       public TextView tv1, tv2;
        ImageButton ib;
        View v;
        public Viewholder0(View v) {
            super(v);
            this.v = v;
            iv = (ImageView) v.findViewById(R.id.iv1);
            tv1 = (TextView) v.findViewById(R.id.tv1);
            tv2 = (TextView) v.findViewById(R.id.tv2);
            ib = (ImageButton) v.findViewById(R.id.imageButton);
        }
    }
    class Viewholder1 extends RecyclerView.ViewHolder
    {

        ImageView iv;
        public TextView tv1;
        ImageButton ib;
        View v;
        public Viewholder1(View v) {
            super(v);
            this.v = v;
            iv = (ImageView) v.findViewById(R.id.imageView4);
            tv1 = (TextView) v.findViewById(R.id.textView4);
            ib = (ImageButton) v.findViewById(R.id.imageButton2);
        }
    }

    public Podcast_adaptor(Context context, ArrayList<Podcast> podcast_datalist, play_clciker click_intf,int selector) {
        this.context = context;
        this.podcast_datalist = podcast_datalist;
        this.click_intf = click_intf;
        this.selector = selector;

    }

    interface play_clciker {

        void play_click_listener (int position) throws IOException;
        void intent_generator (int position) throws  IOException;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.linear, parent, false);
                return new Viewholder0(v);

            case 1:
                LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v1 = inflater1.inflate(R.layout.grid, parent, false);
                return new Viewholder1(v1);

        }
        return  null;
    }



    @Override
    public int getItemViewType(int position) {
        return selector;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
       switch (holder.getItemViewType()) {
           case (0):
           Viewholder0 holder0 = (Viewholder0) holder;
           Podcast pod = podcast_datalist.get(position);
           holder0.tv1.setText(pod.getTitle());
           holder0.tv2.setText("posted: " + pod.getDate());
           Picasso.with(context).load(pod.getImage()).into(holder0.iv);
               holder0.v.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       try {
                           click_intf.intent_generator(position);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               });
           holder0.ib.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try {
                       click_intf.play_click_listener(position);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           });

            break;
           case (1):
               Viewholder1 holder1 = (Viewholder1) holder;
               Podcast pod1 = podcast_datalist.get(position);
               holder1.tv1.setText(pod1.getTitle());
               Picasso.with(context).load(pod1.getImage()).into(holder1.iv);
               holder1.v.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       try {
                           click_intf.intent_generator(position);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               });
               holder1.ib.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       try {
                           click_intf.play_click_listener(position);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               });
       }

    }

    @Override
    public int getItemCount() {
        return podcast_datalist.size();
    }
}
