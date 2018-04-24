package com.spreadyourmusic.spreadyourmusic.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by abel
 * On 23/01/18.
 */

public class DataTypeConverters {

    @TypeConverter
    public static Calendar calendarFromLong(Long valor) {
        if (valor == null) return null;
        else {
            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(valor);
            return cal;
        }
    }

    @TypeConverter
    public static Long calendarToLong(Calendar valor) {
        if (valor == null) return null;
        else return valor.getTimeInMillis();
    }
}
