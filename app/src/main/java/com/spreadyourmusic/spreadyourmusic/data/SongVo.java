package com.spreadyourmusic.spreadyourmusic.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;


/**
 * Created by abel
 * On 22/04/18.
 */

@Entity(foreignKeys = @ForeignKey(entity = AlbumVo.class, parentColumns = {"aid"},
        childColumns = {"albumid"},
        onDelete = ForeignKey.CASCADE)
)
public class SongVo {

    @PrimaryKey
    private int sid;

    private String name;

    @ColumnInfo(name = "song_location_uri")
    private String songLocationUri;

    private Long duration;

    private int albumid;

    public SongVo(int sid, String name, String songLocationUri, Long duration, int albumid) {
        this.sid = sid;
        this.name = name;
        this.songLocationUri = songLocationUri;
        this.duration = duration;
        this.albumid = albumid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSongLocationUri() {
        return songLocationUri;
    }

    public void setSongLocationUri(String songLocationUri) {
        this.songLocationUri = songLocationUri;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getAlbumid() {
        return albumid;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }
}
