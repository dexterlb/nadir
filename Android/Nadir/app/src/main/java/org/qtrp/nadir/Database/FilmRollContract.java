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
        public static final String COLUMN_NAME_LAST_UPDATE = "lastUpdate";
        public static final String COLUMN_NAME_COLOUR = "colour";

        public static final String SQL_CREATE = "CREATE TABLE " + Roll.TABLE_NAME + "(" +
                Roll._ID + " INTEGER PRIMARY KEY, " +
                Roll.COLUMN_NAME_NAME + " TEXT, " +
                Roll.COLUMN_NAME_LAST_UPDATE + " INTEGER,  " +
                Roll.COLUMN_NAME_COLOUR + " TEXT " +
                ")";

        public static final String SQL_DROP = "DROP TABLE IF EXISTS " + Roll.TABLE_NAME;

    }

    public static class Photo implements BaseColumns {
        public static final String TABLE_NAME = "photo";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGTITUDE = "longitude";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_ROLL_ID = "rollId";
        public static final String COLUMN_NAME_LAST_UPDATE = "lastUpdate";

        public static final String SQL_CREATE = "CREATE TABLE " + Photo.TABLE_NAME + "(" +
                Photo._ID + " INTEGER PRIMARY KEY, " +
                Photo.COLUMN_NAME_LATITUDE + " DOUBLE, " +
                Photo.COLUMN_NAME_LONGTITUDE + " DOUBLE, " +
                Photo.COLUMN_NAME_TIMESTAMP + " INTEGER,  " +
                Photo.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                Photo.COLUMN_NAME_ROLL_ID + " INTEGER, " +
                Photo.COLUMN_NAME_LAST_UPDATE + " INTEGER, " +
                " FOREIGN KEY (" + Photo.COLUMN_NAME_ROLL_ID + ") REFERENCES " + Photo.TABLE_NAME + "(" + Photo._ID + ") ON DELETE CASCADE );";

        public static final String SQL_DROP = "DROP TABLE IF EXISTS " + Roll.TABLE_NAME;
    }


}
