package org.qtrp.nadir.Database;

import android.provider.BaseColumns;

/**
 * Created by do on 28/04/17.
 */

public final class FilmRollContract {
    private FilmRollContract(){}

    public static class Roll implements BaseColumns {
        public static final String TABLE_NAME = "roll";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_COLOUR = "colour";

        public static final String SQL_CREATE = "CREATE TABLE " + Roll.TABLE_NAME + "(" +
                Roll._ID + " INTEGER PRIMARY KEY, " +
                Roll.COLUMN_NAME_NAME + " TEXT, " +
                Roll.COLUMN_NAME_TIMESTAMP + " INTEGER,  " +
                Roll.COLUMN_NAME_COLOUR + " TEXT " +
                ")";

        public static final String SQL_DROP = "DROP TABLE IF EXISTS " + Roll.TABLE_NAME;

    }


}
