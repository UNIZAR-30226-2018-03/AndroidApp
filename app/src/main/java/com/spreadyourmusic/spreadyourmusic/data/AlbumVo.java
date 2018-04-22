package com.spreadyourmusic.spreadyourmusic.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;


/**
 * Created by abel
 * On 22/04/18.
 */

@Entity
public class AlbumVo {

    @PrimaryKey
    private long aid;

    private String name;

    @ColumnInfo(name = "creator_name")
    private String creatorName;

    @ColumnInfo(name = "art_location_path")
    private String artLocationPath;

 //   @ColumnInfo(name = "release_date")
   // private Calendar releaseDate;

    public AlbumVo(long aid, String name, String creatorName, String artLocationPath) {//, Calendar releaseDate
        this.aid = aid;
        this.name = name;
        this.creatorName = creatorName;
        this.artLocationPath = artLocationPath;
     //   this.releaseDate = releaseDate;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

  /*  public Calendar getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }
    */
}
