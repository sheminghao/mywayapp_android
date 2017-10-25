package com.mywaytec.myway.model.db;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by shemh on 2017/7/13.
 */

@Entity
public class MyTrack {

    @Id
    private Long id;

    private String trackName;

    private String time;

    private String duration;

    private String totalDistance;

    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> tracks;

    @Generated(hash = 1776248385)
    public MyTrack(Long id, String trackName, String time, String duration,
            String totalDistance, List<String> tracks) {
        this.id = id;
        this.trackName = trackName;
        this.time = time;
        this.duration = duration;
        this.totalDistance = totalDistance;
        this.tracks = tracks;
    }

    @Generated(hash = 1945207357)
    public MyTrack() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getTracks() {
        return tracks;
    }

    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }
}
