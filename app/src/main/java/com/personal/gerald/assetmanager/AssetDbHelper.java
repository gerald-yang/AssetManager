package com.personal.gerald.assetmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class AssetDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "assetmanager.db";

    public static final int INDEX_ID = 0;
    public static final int INDEX_DATE = 1;
    public static final int INDEX_MONEY = 2;
    public static final int INDEX_NUM = 3;
    public static final int INDEX_NOTE = 4;

    public static final String FIELD_ID = "_id";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_MONEY = "money";
    public static final String FIELD_NUM = "num";
    public static final String FIELD_NOTE = "note";


    // For onetime income table
    public static String ONETIME_INCOME_TABLE_NAME = "onetime_income";

    private static final String SQL_CREATE_ONETIME_INCOME_ENTRIES =
            "CREATE TABLE " + ONETIME_INCOME_TABLE_NAME +  " (" +
                    FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIELD_DATE + " INTEGER, " +
                    FIELD_MONEY + " INTEGER, " +
                    FIELD_NUM + " INTEGER, " +
                    FIELD_NOTE + " TEXT)";

    private static final String SQL_DELETE_ONETIME_INCOME_ENTRIES =
            "DROP TABLE IF EXISTS " + ONETIME_INCOME_TABLE_NAME;

    // For monthly income table
    public static String MONTHLY_INCOME_TABLE_NAME = "monthly_income";

    private static final String SQL_CREATE_MONTHLY_INCOME_ENTRIES =
            "CREATE TABLE " + MONTHLY_INCOME_TABLE_NAME + " (" +
                    FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIELD_DATE + " INTEGER, " +
                    FIELD_MONEY + " INTEGER, " +
                    FIELD_NUM + " INTEGER, " +
                    FIELD_NOTE + " TEXT)";

    private static final String SQL_DELETE_MONTHLY_INCOME_ENTRIES =
            "DROP TABLE IF EXISTS " + MONTHLY_INCOME_TABLE_NAME;

    // For yearly income table
    public static String YEARLY_INCOME_TABLE_NAME = "yearly_income";

    private static final String SQL_CREATE_YEARLY_INCOME_ENTRIES =
            "CREATE TABLE " + YEARLY_INCOME_TABLE_NAME + " (" +
                    FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIELD_DATE + " INTEGER, " +
                    FIELD_MONEY + " INTEGER, " +
                    FIELD_NUM + " INTEGER, " +
                    FIELD_NOTE + " TEXT)";

    private static final String SQL_DELETE_YEARLY_INCOME_ENTRIES =
            "DROP TABLE IF EXISTS " + YEARLY_INCOME_TABLE_NAME;


    // For onetime outgoing table
    public static String ONETIME_OUTGOING_TABLE_NAME = "onetime_outgoing";

    private static final String SQL_CREATE_ONETIME_OUTGOING_ENTRIES =
            "CREATE TABLE " + ONETIME_OUTGOING_TABLE_NAME + " (" +
                    FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIELD_DATE + " INTEGER, " +
                    FIELD_MONEY + " INTEGER, " +
                    FIELD_NUM + " INTEGER, " +
                    FIELD_NOTE + " TEXT)";

    private static final String SQL_DELETE_ONETIME_OUTGOING_ENTRIES =
            "DROP TABLE IF EXISTS " + ONETIME_OUTGOING_TABLE_NAME;

    // For monthly outgoing table
    public static String MONTHLY_OUTGOING_TABLE_NAME = "monthly_outgoing";

    private static final String SQL_CREATE_MONTHLY_OUTGOING_ENTRIES =
            "CREATE TABLE " + MONTHLY_OUTGOING_TABLE_NAME + " (" +
                    FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +FIELD_DATE + " INTEGER, " +
                    FIELD_MONEY + " INTEGER, " +
                    FIELD_NUM + " INTEGER, " +
                    FIELD_NOTE + " TEXT)";

    private static final String SQL_DELETE_MONTHLY_OUTGOING_ENTRIES =
            "DROP TABLE IF EXISTS " + MONTHLY_OUTGOING_TABLE_NAME;

    // For yearly outgoing table
    public static String YEARLY_OUTGOING_TABLE_NAME = "yearly_outgoing";

    private static final String SQL_CREATE_YEARLY_OUTGOING_ENTRIES =
            "CREATE TABLE " + YEARLY_OUTGOING_TABLE_NAME + " (" +
                    FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIELD_DATE + " INTEGER, " +
                    FIELD_MONEY + " INTEGER, " +
                    FIELD_NUM + " INTEGER, " +
                    FIELD_NOTE + " TEXT)";

    private static final String SQL_DELETE_YEARLY_OUTGOING_ENTRIES =
            "DROP TABLE IF EXISTS " + YEARLY_OUTGOING_TABLE_NAME;

    // For instalment outgoing table
    public static String INSTALMENT_OUTGOING_TABLE_NAME = "instalment_outgoing";

    private static final String SQL_CREATE_INSTALMENT_OUTGOING_ENTRIES =
            "CREATE TABLE " + INSTALMENT_OUTGOING_TABLE_NAME + " (" +
                    FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIELD_DATE + " INTEGER, " +
                    FIELD_MONEY + " INTEGER, " +
                    FIELD_NUM + " INTEGER, " +
                    FIELD_NOTE + " TEXT)";

    private static final String SQL_DELETE_INSTALMENT_OUTGOING_ENTRIES =
            "DROP TABLE IF EXISTS " + INSTALMENT_OUTGOING_TABLE_NAME;


    public AssetDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ONETIME_INCOME_ENTRIES);
        db.execSQL(SQL_CREATE_MONTHLY_INCOME_ENTRIES);
        db.execSQL(SQL_CREATE_YEARLY_INCOME_ENTRIES);
        db.execSQL(SQL_CREATE_ONETIME_OUTGOING_ENTRIES);
        db.execSQL(SQL_CREATE_MONTHLY_OUTGOING_ENTRIES);
        db.execSQL(SQL_CREATE_YEARLY_OUTGOING_ENTRIES);
        db.execSQL(SQL_CREATE_INSTALMENT_OUTGOING_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ONETIME_INCOME_ENTRIES);
        db.execSQL(SQL_DELETE_MONTHLY_INCOME_ENTRIES);
        db.execSQL(SQL_DELETE_YEARLY_INCOME_ENTRIES);
        db.execSQL(SQL_DELETE_ONETIME_OUTGOING_ENTRIES);
        db.execSQL(SQL_DELETE_MONTHLY_OUTGOING_ENTRIES);
        db.execSQL(SQL_DELETE_YEARLY_OUTGOING_ENTRIES);
        db.execSQL(SQL_DELETE_INSTALMENT_OUTGOING_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onUpgrade(db, oldVersion, newVersion);
    }

    public void reset_db(SQLiteDatabase db) {

        db.execSQL(SQL_DELETE_ONETIME_INCOME_ENTRIES);
        db.execSQL(SQL_DELETE_MONTHLY_INCOME_ENTRIES);
        db.execSQL(SQL_DELETE_YEARLY_INCOME_ENTRIES);
        db.execSQL(SQL_DELETE_ONETIME_OUTGOING_ENTRIES);
        db.execSQL(SQL_DELETE_MONTHLY_OUTGOING_ENTRIES);
        db.execSQL(SQL_DELETE_YEARLY_OUTGOING_ENTRIES);
        db.execSQL(SQL_DELETE_INSTALMENT_OUTGOING_ENTRIES);
        onCreate(db);
    }

}