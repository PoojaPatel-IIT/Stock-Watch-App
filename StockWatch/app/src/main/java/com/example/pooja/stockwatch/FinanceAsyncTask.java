package com.example.pooja.stockwatch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by pooja on 04,October,2018
 */
public class FinanceAsyncTask extends AsyncTask<String,Void ,String> {
    
    private MainActivity ma;
    private static final String TAG = "FinanceAsyncTask";
    private final String finstockwebsite = "https://api.iextrading.com/1.0/stock/";

    public FinanceAsyncTask(MainActivity mainActivity) {
        this.ma = mainActivity;
    }

    @Override
    protected String doInBackground(String... strings) {

        Uri.Builder buildUri = Uri.parse(finstockwebsite).buildUpon();
        buildUri.appendPath(strings[0]);
        buildUri.appendPath("quote");
        // appending parameter
        buildUri.appendQueryParameter("displayPercent","true");


        String finurl = buildUri.build().toString();
        Log.d(TAG, "doInBackground: the url = " + finurl);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(finurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //To get request method and request code use following
            conn.setRequestMethod("GET");

            conn.getResponseCode();conn.getRequestMethod();
            InputStream inputStream = conn.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = buffer.readLine()) != null) {
                sb.append(line).append('\n');
            }
            Log.d(TAG, "doInBackground: The string got = " + sb.toString());
        } catch (Exception e) {
            //if no such financial data then exception like for the first time
            Log.d(TAG, "doInBackground: Exception in financial data");
            e.printStackTrace();
            return null;
        }

        String jsonFinData = sb.toString();
        Log.d(TAG, "doInBackground: jsonFinDAta" +jsonFinData);
        return jsonFinData;

    }

    private HashMap stockfinance(String str) {
        Log.d(TAG, "stockfinance: ");
        if (str == null) {
            Log.d(TAG, "stockfinance: I AM HERE AND WANT TO RETURN NULL");
            return null;
        }

        HashMap<String,String> finStockHash = new HashMap<String,String>();
        try {
            JSONObject getJson = new JSONObject(str);
            String price_change = getJson.getString("change");
            Log.d(TAG, "stockfinance: HERE IS CHNAGE" +getJson.getString("change"));

            // getting data from JSON and putting in Hash Map
            finStockHash.put("sym", getJson.getString("symbol"));
            finStockHash.put("cmpy_name", getJson.getString("companyName"));
            finStockHash.put("currentprice", getJson.getString("latestPrice"));
            finStockHash.put("change", price_change);
            finStockHash.put("per", getJson.getString("changePercent"));
            Log.d(TAG, "stockfinance: check it out ----------------------------------------");
            Log.d(TAG, "stockfinance: "+finStockHash.get("sym"));
            Log.d(TAG, "stockfinance: "+finStockHash.get("cmpy_name"));
            Log.d(TAG, "stockfinance: "+finStockHash.get("currentprice"));
            Log.d(TAG, "stockfinance: "+finStockHash.get("change"));
            Log.d(TAG, "stockfinance: "+finStockHash.get("per"));
            Log.d(TAG, "stockfinance: check it out ----------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "stockfinance: EXCEPTION HERE" );
            return null;
        }
        Log.d(TAG, "stockfinance: Financial "+finStockHash);
        return finStockHash;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.d(TAG, "onPostExecute: ssssssssss " +s);
        HashMap<String,String> stock_display = stockfinance(s);
        Log.d(TAG, "onPostExecute: HOLA "+stock_display);
        ma.sendFinanceDataToMA(stock_display);

    }



}

