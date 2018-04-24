package com.spreadyourmusic.spreadyourmusic.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by abel
 * On 21/01/18.
 */

@Dao
public interface SongDao {

    @Query("SELECT * FROM SongVo")
    List<SongVo> getAll();

    @Query("SELECT count(*) FROM SongVo WHERE sid == :songVoId")
    Long exist(Long songVoId);

    @Insert
    Long insert(SongVo songVo);

    @Update
    void update(SongVo songVo);

    @Delete
    void delete(SongVo songVo);
}
