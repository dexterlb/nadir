package org.qtrp.nadir.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FilmRollDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "filmroll.db";

    public FilmRollDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(FilmRollContract.Roll.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(FilmRollContract.Roll.SQL_DROP);
        onCreate(sqLiteDatabase);
    }

    public long insertRoll(Roll roll) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FilmRollContract.Roll.COLUMN_NAME_NAME, roll.name);
        values.put(FilmRollContract.Roll.COLUMN_NAME_TIMESTAMP, roll.timestamp);
        values.put(FilmRollContract.Roll.COLUMN_NAME_COLOUR, roll.colour);

        return db.insert(FilmRollContract.Roll.TABLE_NAME, null, values);
    }

    public void removeRoll(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(FilmRollContract.Roll.TABLE_NAME, FilmRollContract.Roll._ID + " = " + id, null);
    }

    public ArrayList<Roll> getRolls(){
        ArrayList<Roll> rolls = new ArrayList<Roll>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
          FilmRollContract.Roll.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            Roll roll = new Roll(
                    cursor.getLong(cursor.getColumnIndex(FilmRollContract.Roll._ID)),
                    cursor.getString(cursor.getColumnIndex(FilmRollContract.Roll.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(FilmRollContract.Roll.COLUMN_NAME_COLOUR)),
                    cursor.getLong(cursor.getColumnIndex(FilmRollContract.Roll.COLUMN_NAME_TIMESTAMP))
            );

            rolls.add(roll);
        }

        cursor.close();
        return rolls;
    }

}
