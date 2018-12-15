package com.example.pooja.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pooja on 04,October,2018
 */
public class StockDatabase extends SQLiteOpenHelper
{
    private static final String TAG = "StockDatabase";
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // DB Name
        private static final String DATABASE_NAME = "StockWatchDB";

    // DB Table Name
    private static final String TABLE_NAME = "StockTable";

    ///DB Columns
    private static final String SYMBOL = "StockSymbol";
    private static final String COMPANY = "CompanyName";


    // DB Table Create Code
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SYMBOL + " TEXT not null unique," +
                    COMPANY + " TEXT not null)";

    private SQLiteDatabase database;
    private MainActivity mainActivity;


    public StockDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //mainActivity = context;
        database = getWritableDatabase(); // Inherited from SQLiteOpenHelper
        Log.d(TAG, "StockDatabase: ");
    }

    public void deleteDB()
    {
        database.delete(TABLE_NAME,null ,null );
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate is only called if the DB does not exist

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<String[]> loadingStocks() {

        // loadingstocks() - return ArrayList of loaded books
        Log.d(TAG, "loadingStocks: ");
        ArrayList<String[]> Stocks = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_NAME,  // The table to query
                new String[]{SYMBOL,COMPANY}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
              //  SYMBOL+" ASC",
                null); // The sort order

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0); // 1st returned column
                String company = cursor.getString(1); // 2nd returned column
               Stocks.add(new String[] {symbol, company});
               // Stock b = new Stock(symbol,company);
               // Stocks.add(b);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG, "loadingStocks: DONE");

        return Stocks;
    }

    public void addStockEntries(Stock Stock) {
        ContentValues values = new ContentValues();

        values.put(SYMBOL, Stock.getSymbol());
        values.put(COMPANY, Stock.getCompany());


        long table = database.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addStockEntries: "+table);
    }

    public void updateBook(Stock Stock) {
        ContentValues values = new ContentValues();

        values.put(SYMBOL, Stock.getSymbol());
        values.put(COMPANY, Stock.getCompany());


        long key = database.update(TABLE_NAME, values, SYMBOL + " = ?", new String[]{Stock.getSymbol()});

        Log.d(TAG, "updateBook: " + key);
    }

    public void deleteStockEntries(String name) {
        Log.d(TAG, "deleteStockEntries: "+name);

        int cnt = database.delete(TABLE_NAME, SYMBOL + " = ?", new String[]{name});

        Log.d(TAG, "deleteStockEntries: "+cnt);
       // Toast.makeText(mainActivity,"Stock Deleted" , Toast.LENGTH_SHORT);
    }

//    public void findBook(HashMap<String, String> params) {
//        Log.d(TAG, "findBook: ");
//
//        StringBuilder details = new StringBuilder();
//        for (String key : params.keySet()) {
//            details.append(key + " = '" + params.get(key) + "' AND ");
//        }
//        String clause = details.substring(0, details.lastIndexOf("AND"));
//
//
//        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " + clause, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            if (cursor.getCount() > 0) {
//
//                String symbol= cursor.getString(0);
//                String company = cursor.getColumnName(1);
//                Stock b = new Stock(symbol,company);
//                mainActivity.showFindResults(b);
//            }
//            else {
//                mainActivity.showFindResults(null);
//            }
//
//            cursor.close();
//
//        }
//    }
//

    public void dumpDbToLog() {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d(TAG, "dumpDbToLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);

                Log.d(TAG, "dumpDbToLog: " +
                        String.format("%s %-18s", SYMBOL + ":", symbol) +
                        String.format("%s %-18s", COMPANY + ":", company));

                cursor.moveToNext();
            }
            cursor.close();
        }

        Log.d(TAG, "dumpDbToLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    public void shutDown() {
        database.close();
    }
}



