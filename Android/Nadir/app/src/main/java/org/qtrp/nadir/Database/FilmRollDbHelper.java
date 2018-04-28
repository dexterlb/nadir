package org.qtrp.nadir.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.qtrp.nadir.Helpers.SyncHelper;

import java.util.ArrayList;

public class FilmRollDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "filmroll.db";

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public FilmRollDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(FilmRollContract.Roll.SQL_CREATE);
        sqLiteDatabase.execSQL(FilmRollContract.Photo.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(FilmRollContract.Roll.SQL_DROP);
        sqLiteDatabase.execSQL(FilmRollContract.Photo.SQL_DROP);
        onCreate(sqLiteDatabase);
    }

    public long insertRoll(Roll roll) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FilmRollContract.Roll.COLUMN_NAME_NAME, roll.name);
        values.put(FilmRollContract.Roll.COLUMN_NAME_LAST_UPDATE, roll.lastUpdate);
        values.put(FilmRollContract.Roll.COLUMN_NAME_COLOUR, roll.colour);
        values.put(FilmRollContract.Roll.COLUMN_NAME_UNIQUE_ID, roll.uniqueId);

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
                    cursor.getLong(cursor.getColumnIndex(FilmRollContract.Roll.COLUMN_NAME_LAST_UPDATE)),
                    cursor.getString(cursor.getColumnIndex(FilmRollContract.Roll.COLUMN_NAME_UNIQUE_ID))
            );

            rolls.add(roll);
        }

        cursor.close();
        return rolls;
    }


    public long insertPhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FilmRollContract.Photo.COLUMN_NAME_ROLL_ID, photo.getRollId());
        values.put(FilmRollContract.Photo.COLUMN_NAME_LATITUDE, photo.getLatitude());
        values.put(FilmRollContract.Photo.COLUMN_NAME_LONGTITUDE, photo.getLongitude());
        values.put(FilmRollContract.Photo.COLUMN_NAME_TIMESTAMP, photo.getTimestamp());
        values.put(FilmRollContract.Photo.COLUMN_NAME_DESCRIPTION, photo.getDescription());

        values.put(FilmRollContract.Photo.COLUMN_NAME_LAST_UPDATE, photo.getLastUpdate());
        values.put(FilmRollContract.Photo.COLUMN_NAME_UNIQUE_ID, photo.getUniqueId());
        values.put(FilmRollContract.Photo.COLUMN_NAME_IS_DELETED, photo.getDeleted());

        return db.insert(FilmRollContract.Photo.TABLE_NAME, null, values);
    }

    public long updatePhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        String whereClause = "_ID = ?";
        String[] whereArgs = new String[] {
                String.valueOf(photo.getPhotoId())
        };


        values.put(FilmRollContract.Photo.COLUMN_NAME_ROLL_ID, photo.getRollId());
        values.put(FilmRollContract.Photo.COLUMN_NAME_LATITUDE, photo.getLatitude());
        values.put(FilmRollContract.Photo.COLUMN_NAME_LONGTITUDE, photo.getLongitude());
        values.put(FilmRollContract.Photo.COLUMN_NAME_TIMESTAMP, photo.getTimestamp());
        values.put(FilmRollContract.Photo.COLUMN_NAME_DESCRIPTION, photo.getDescription());

        values.put(FilmRollContract.Photo.COLUMN_NAME_LAST_UPDATE, photo.getLastUpdate());
        values.put(FilmRollContract.Photo.COLUMN_NAME_UNIQUE_ID, photo.getUniqueId());
        values.put(FilmRollContract.Photo.COLUMN_NAME_IS_DELETED, photo.getDeleted());

        return db.update(FilmRollContract.Photo.TABLE_NAME,values, whereClause, whereArgs);
    }

    public long removePhoto(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        String whereClause = "_ID = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id)
        };

        values.put(FilmRollContract.Photo.COLUMN_NAME_IS_DELETED, 1);

        return db.update(FilmRollContract.Photo.TABLE_NAME,values, whereClause, whereArgs);
    }

    public ArrayList<Photo> getPhotosByRollId(long rollId){
        ArrayList<Photo> photos = new ArrayList<Photo>();
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = "rollId = ? and isDeleted = ?";

        String[] whereArgs = new String[] {
                String.valueOf(rollId),
                String.valueOf(0)
        };

        String orderBy = "timestamp DESC";


        Cursor cursor = db.query(
                FilmRollContract.Photo.TABLE_NAME,
                null, //table columns
                whereClause, //where clause
                whereArgs, // where  values
                null,
                null,
                orderBy
        );

        while(cursor.moveToNext()) {
            Photo photo = new Photo(
                    cursor.getLong(cursor.getColumnIndex(FilmRollContract.Photo._ID)),
                    cursor.getLong(cursor.getColumnIndex(FilmRollContract.Photo.COLUMN_NAME_ROLL_ID)),
                    cursor.getDouble(cursor.getColumnIndex(FilmRollContract.Photo.COLUMN_NAME_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(FilmRollContract.Photo.COLUMN_NAME_LONGTITUDE)),
                    cursor.getLong(cursor.getColumnIndex(FilmRollContract.Photo.COLUMN_NAME_TIMESTAMP)),
                    cursor.getString(cursor.getColumnIndex(FilmRollContract.Photo.COLUMN_NAME_DESCRIPTION)),
                    cursor.getLong(cursor.getColumnIndex(FilmRollContract.Photo.COLUMN_NAME_LAST_UPDATE)),
                    cursor.getString(cursor.getColumnIndex(FilmRollContract.Photo.COLUMN_NAME_UNIQUE_ID)),
                    cursor.getInt(cursor.getColumnIndex(FilmRollContract.Photo.COLUMN_NAME_IS_DELETED))
            );

            photos.add(photo);
        }

        cursor.close();
        return photos;
    }

    // Sync stuff

    // връща максимален lastUpdate на нещо, което има isSynced
    public Long lastSync() {
       return null;
    }


    // връща всички неща, които нямат isSynced и lastUpdate >= since
    public Iterable <SyncHelper.SyncItem> forSync(Long since) {
        return null;
    }

    // ъпдейтва в базата неща и им сетва isSynced (това ще го оставим за друг път, май)
    public void syncUpdate(Iterable<SyncHelper.SyncItem> items) {
        setSynced(items);
        // ...
    }

    // сетва isSynced на нещата с тези uniqueID-та на true
    public void setSynced(Iterable<SyncHelper.SyncItem> itemUniqueIDs) {

    }
}
