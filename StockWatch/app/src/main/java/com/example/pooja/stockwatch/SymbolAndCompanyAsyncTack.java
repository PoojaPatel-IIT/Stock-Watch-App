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
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by pooja on 04,October,2018
 */
public class SymbolAndCompanyAsyncTack extends AsyncTask<String,Integer ,String> {
    String stockSymbol;
    private static final String TAG = "SymbolAndCompanyAsyncTa";
    private MainActivity mainActivity;
    private String symbolandNameLink="https://api.iextrading.com/1.0/ref-data/symbols";

    public SymbolAndCompanyAsyncTack(MainActivity ma)


    {
        mainActivity=ma;
    }

    @Override
    protected String doInBackground(String... strings) {

// fetching the  url that is to be used for getting stock symbol and company names
        Uri.Builder buildURL = Uri.parse(symbolandNameLink).buildUpon();
        // no query parameter
        //buildURL.appendQueryParameter("query",strings[0]);


        String finalSymbolAndStockLink=buildURL.build().toString();
        Log.d(TAG, "doInBackground: Stock symbol and Name URL "+finalSymbolAndStockLink);
        StringBuilder sbForStock=new StringBuilder();
        stockSymbol=strings[0];




        try{
            Log.d(TAG, "doInBackground: -------------------------");
            URL stocklink=new URL(finalSymbolAndStockLink);
            Log.d(TAG, "doInBackground: "+  finalSymbolAndStockLink);
            HttpURLConnection connection=(HttpURLConnection)stocklink.openConnection();
            // connection
            connection.setRequestMethod("GET");
            String each;
            InputStream inputsteam=connection.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputsteam));

// reading lines
            while((each = reader.readLine())!= null)
            {
                sbForStock.append(each).append("\n");
            }

        }
        catch (Exception e)
        {
            Log.d(TAG, "doInBackground: Exception thrown here");
            e.printStackTrace();
        }

        // returning string
        return sbForStock.toString();
    }

    public HashMap<String, String> parseJSON(String s) throws org.json.JSONException {
        JSONArray stocks;
        HashMap<String,String> stock_map = new HashMap<>();
        stocks = new JSONArray(s);
        for(int i=0;i<stocks.length();++i)
        {            JSONObject stock = (JSONObject) stocks.get(i);
            Log.d(TAG, "parseJSON: "+stock.getString("symbol"));
            if(stock.getString("symbol").startsWith(stockSymbol)) { stock_map.put(stock.getString("symbol"), stock.getString("name"));
            }
        }
        return stock_map;
    }

    @Override
    protected void onPostExecute(String s) {

        Log.d(TAG, "onPostExecute: "+s);
        try{
            HashMap<String,String> stock_map = parseJSON(s);
            mainActivity.symbolData(stock_map,stockSymbol);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}

