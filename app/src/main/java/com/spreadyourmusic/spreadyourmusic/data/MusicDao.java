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
public interface MusicDao {
    // TODO
    /*@Query("SELECT * FROM ClientVo ORDER BY first_name ASC")
    LiveData<List<ClientVo>> getAll();

    @Query("SELECT * FROM ClientVo ORDER BY first_name ASC")
    List<ClientVo> getAllList();

    @Query("SELECT * FROM ClientVo  WHERE lower(first_name) || ' ' || lower(last_name) LIKE lower('%' || :cadena || '%') ORDER BY first_name ASC")
    List<ClientVo> getAll(String cadena);

    @Query("SELECT * FROM ClientVo WHERE uid == :userId")
    ClientVo loadById(int userId);

    @Insert
    List<Long> insertAll(ClientVo... clientVos);

    @Update
    void update(ClientVo clientVo);

    @Delete
    void delete(ClientVo clientVo);

    @Delete
    void deleteAll(List<ClientVo> clientsVo);*/
}
