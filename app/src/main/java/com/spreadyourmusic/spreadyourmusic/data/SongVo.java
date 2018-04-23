package com.spreadyourmusic.spreadyourmusic.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.spreadyourmusic.spreadyourmusic.models.Song;

import java.util.Calendar;


/**
 * Created by abel
 * On 22/04/18.
 */

@Entity
public class SongVo {

    @PrimaryKey
    private long sid;

    private String name;

    @ColumnInfo(name = "song_location_uri")
    private String songLocationUri;

    private Long duration;

    private String albumName;

    @ColumnInfo(name = "creator_name")
    private String creatorName;

    @ColumnInfo(name = "art_location_path")
    private String artLocationPath;

    @ColumnInfo(name = "release_date")
    private Calendar releaseDate;

    public SongVo(long sid, String name, String songLocationUri, Long duration, String albumName, String creatorName, String artLocationPath, Calendar releaseDate) {
        this.sid = sid;
        this.name = name;
        this.songLocationUri = songLocationUri;
        this.duration = duration;
        this.albumName = albumName;
        this.creatorName = creatorName;
        this.artLocationPath = artLocationPath;
        this.releaseDate = releaseDate;
    }

    @Ignore
    public SongVo(Song song){
        this.sid = song.getId();
        this.name = song.getName();
        this.songLocationUri = song.getLocationUri();
        this.duration = song.getDuration();
        this.albumName = song.getAlbum().getName();
        this.creatorName = song.getAlbum().getCreator().getName();
        this.artLocationPath = song.getAlbum().getArtLocationUri();
        this.releaseDate = song.getAlbum().getReleaseDate();
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
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

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getArtLocationPath() {
        return artLocationPath;
    }

    public void setArtLocationPath(String artLocationPath) {
        this.artLocationPath = artLocationPath;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }
}
