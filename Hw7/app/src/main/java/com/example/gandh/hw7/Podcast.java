package com.example.gandh.hw7;

import java.io.Serializable;

/**
 * Created by gandh on 3/6/2017.
 */

public class Podcast implements Serializable{

    String title, description, date, image, duration, mp3,short_date;

    public String getShort_date() {
        return short_date;
    }

    public void setShort_date(String short_date) {
        this.short_date = short_date;
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", image='" + image + '\'' +
                ", duration='" + duration + '\'' +
                ", mp3='" + mp3 + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }
}
