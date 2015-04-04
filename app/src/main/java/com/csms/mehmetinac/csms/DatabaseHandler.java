package com.csms.mehmetinac.csms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehmetinac on 28/01/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "csms";
    private static final String TABLE_SMS = "sms";
    private static final String KEY_ID = "id";
    private static final String KEY_PHONE = "phoneNumber";
    private static final String KEY_MESSAGE = "message";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_SMS + " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PHONE + " TEXT," + KEY_MESSAGE + " TEXT )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
        onCreate(db);
    }

    public void addSMS(SMS sms) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PHONE, sms.phoneNumber);
        values.put(KEY_MESSAGE, sms.message);

        db.insert(TABLE_SMS, null, values);
        db.close();
    }

    public SMS getSMS(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SMS, new String[] { KEY_ID,
                        KEY_PHONE, KEY_MESSAGE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        SMS sms = new SMS(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return sms;
    }

    public List<SMS> getAllSMS() {
        List<SMS> smsList = new ArrayList<SMS>();

        String selectQuery = "SELECT  * FROM " + TABLE_SMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SMS sms = new SMS();
                sms.id = Integer.parseInt(cursor.getString(0));
                sms.phoneNumber = cursor.getString(1);
                sms.message = cursor.getString(2);
                smsList.add(sms);
            } while (cursor.moveToNext());
        }

        return smsList;
    }

    public int getSMSCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateSMS(SMS sms) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PHONE, sms.phoneNumber);
        values.put(KEY_MESSAGE, sms.message);

        return db.update(TABLE_SMS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sms.id) });
    }

    public void deleteSMS(SMS sms) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SMS, KEY_ID + " = ?",
                new String[] { String.valueOf(sms.id) });
        db.close();
    }

}
