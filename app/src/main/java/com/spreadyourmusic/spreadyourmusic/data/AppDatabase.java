package com.spreadyourmusic.spreadyourmusic.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by abel
 * On 21/01/18.
 */

@Database(entities = {AlbumVo.class, SongVo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "sym-db").build();
        }
        return INSTANCE;
    }

    public abstract MusicDao musicDao();

}
