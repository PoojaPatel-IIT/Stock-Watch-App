package com.example.pooja.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    String stck_symbl;
    private static final String TAG = "MainActivity";
    private ArrayList<Stock> everyStock = new ArrayList<>();
    private RecyclerView rv;
    Boolean checknet = true;
    private RecyclerViewStockAdapter RVStockAdap;
    SymbolAndCompanyAsyncTack SNCAsyncTask;
    private SwipeRefreshLayout swipeRefresh;
    private StockDatabase stockDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.recycler_stock);
        RVStockAdap = new RecyclerViewStockAdapter(this, everyStock);
        Log.d(TAG, "onCreate: ALL STOCKS"+everyStock);
        rv.setAdapter(RVStockAdap);
        rv.setLayoutManager(new LinearLayoutManager(this));
        //asyncLoaderTask = new AsyncLoaderTask(this);
        stockDB = new StockDatabase(this);
      // stockDB.deleteDB();
        Log.d(TAG, "onCreate: I AM here");
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swiper);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            //this method is used to refrsh the code and update stocks and also check the network connectivity
            public void onRefresh() {
                if(!inspectNetwork())
                {
                    //calling  no network functionality
                    nonetNotUpdate();
                }
                else
                    // refreshing
                    revive();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void nonet(){
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Stocks Cannnot Be Added Without A Network Connection");
        builder1.setTitle("No Network Connection");
        android.app.AlertDialog dialog = builder1.create();
        dialog.show();
    }



    public void nonetNotUpdate(){
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Stocks Cannnot Be Updated Without A Network Connection");
        builder1.setTitle("No Network Connection");
        android.app.AlertDialog dialog = builder1.create();
        dialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflating menu
        getMenuInflater().inflate(R.menu.stockpage, menu);
        return true;
    }

    public RecyclerView getRecyclerView() {
        return rv;
    }


    @Override
    protected void onResume() {
        stockDB.dumpDbToLog();

        if(!inspectNetwork())
        {
           nonetNotUpdate();
        }
        else {
            ArrayList<String[]> list = stockDB.loadingStocks();

            ArrayList<Stock> stocks_lst = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                new FinanceAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list.get(i)[0]);
            }
            everyStock.clear();
            everyStock.addAll(stocks_lst);
            Log.d(TAG, "onResume: " + list);
            RVStockAdap.notifyDataSetChanged();
        }
        super.onResume();
    }

    private void revive() {
        // dumping data
        stockDB.dumpDbToLog();
        // loading data in list
        ArrayList<String[]> load_list = stockDB.loadingStocks();

        for (int i = 0; i < load_list.size(); i++) {
            new FinanceAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, load_list.get(i)[0]);
        }
        Log.d(TAG, "revive: "+load_list);
        // clearing list
        ArrayList<Stock> stocks_lst = new ArrayList<>();
        everyStock.clear();
        // adding all to list
        everyStock.addAll(stocks_lst);
// notifying change to adapter
        RVStockAdap.notifyDataSetChanged();
    }


    public StockDatabase getDbHandler() {
        return stockDB;
    }
//    @Override
//    protected void onDestroy() {
//        stockDB.shutDown();
//        super.onDestroy();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                Toast.makeText(this, "About Stock Watch", Toast.LENGTH_SHORT).show();
                    Intent intent_about = new Intent(this, aboutstockwatch.class);
                    startActivity(intent_about);
                    return true;
            case R.id.add_stock_Entry:
                //To add the stock and display the alert for entering stock symbol
                Log.d(TAG, "onOptionsItemSelected: Add Stock");

                // checking network connectivity

                inspectNetwork();
                if (checknet == true) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    final EditText stock_input = new EditText(this);
                    stock_input.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                    stock_input.setInputType(InputType.TYPE_CLASS_TEXT);
                    stock_input.setGravity(Gravity.CENTER_HORIZONTAL);


                    builder.setView(stock_input);
                    //If positive button is clicked then execute Asnyc task
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stck_symbl = stock_input.getText().toString();
                            SNCAsyncTask = new SymbolAndCompanyAsyncTack(MainActivity.this);
                            SNCAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, stck_symbl.toString());

                        }
                    });
                 //   Toast.makeText(this, "Stock Displayed", Toast.LENGTH_SHORT).show();

                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.setMessage("Please Enter The Stock Symbol:");
                    builder.setTitle("Stock Selection");

                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                 nonet();

                }
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    //to check the network connectivity
    private boolean inspectNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = connectivityManager.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            checknet = true;
        } else {  checknet = false;
        }        return checknet;
    }

    //If there is not stock symbol then display this alert
    public void nullentryStock(String inputsymbol) {
        String input = inputsymbol;
    }

    public void emptyEntry(String inputsymbol) {
        String input = inputsymbol;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Symbol Not Found : " + inputsymbol.toUpperCase());
        builder.setMessage("Data for stock symbol");
        Toast.makeText(this, "Sorry Stock Not Found", Toast.LENGTH_SHORT).show();
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    //this is to get the data from the stock symbol asynch task
    public void symbolData(HashMap stock_symb, String stock) {
        //if no such entry then invalid dialog
        if (stock_symb.size() == 0) {
            nullentryStock(stock);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Symbol Not Found :" + stock.toUpperCase());
            builder.setMessage("Data for stock symbol");
            Toast.makeText(this, "Sorry Stock Not Found", Toast.LENGTH_SHORT).show();
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
        //If only one entry directly display
        else if (stock_symb.size() == 1) {
            retrieveFinanceDetails(stock);
            Log.d(TAG, "symbolData: I am one");
          //  toasting(stock);
        }

        else {
// to show the search results for the stock entered in the dialog box
            HashMap<String, String> stocks = stock_symb;
            Log.d(TAG, "symbolData: "+stock_symb.size());

            final CharSequence[] stockdisplay = new CharSequence[stocks.size()];

            int eachsearch = 0;
            for (Map.Entry<String, String> entry : stocks.entrySet()) {
                String line = entry.getKey() + " - " + entry.getValue();
                stockdisplay[eachsearch++] = line;
                Log.d(TAG, "symbolData: Searched " + line);
            }
            android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(this);
            builder1.setTitle("Make a Selection");
            builder1.setItems(stockdisplay, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String stockName = stockdisplay[i].toString().substring(0, (stockdisplay[i].toString().indexOf("-") - 1));
                    retrieveFinanceDetails(stockName);
                    Log.d(TAG, "onClick: Reached");
// TRY THIS
//toasting(stockName);
                }
            });            builder1.setNegativeButton("NEVERMIND", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {                }
            });
            android.app.AlertDialog dialog = builder1.create();
            dialog.show();
        
        }
    }

    public void toasting(String s){
        Toast.makeText(this, s + " Displayed !", Toast.LENGTH_SHORT).show();
    }

    //This gets the fetched data like price,trade price and percent and if not display alert
    public void sendFinanceDataToMA(HashMap stock_fin) {
        Log.d(TAG, "sendFinanceDataToMA: I AM HERE 0000000000000000000000000000000000000000000000");
        Log.d(TAG, "sendFinanceDataToMA: stock_fin" + stock_fin);
        // Log.d(TAG, "sendFinanceDataToMA: Stock Size"+stock_fin.size());
        if (stock_fin == null || stock_fin.size() == 0) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage("No Financial Stock Data found for the selected item!");
            builder.setTitle("No Data Found");
            android.app.AlertDialog dialog = builder.create();
            dialog.show();

        } else {            Stock stock1 = new Stock();
            stock1.setSymbol(stock_fin.get("sym").toString());
            stock1.setCompany(stock_fin.get("cmpy_name").toString());

            try { Double val = Double.parseDouble(stock_fin.get("currentprice").toString());
                stock1.setPrice(val);
                //val = Double.parseDouble(stock_fin.get("change_price").toString());
                stock1.setChange(Double.valueOf(stock_fin.get("change").toString()));
                Log.d(TAG, "sendFinanceDataToMA: CHANGE VAL "+stock_fin.get("change").toString());
                val = Double.parseDouble(stock_fin.get("per").toString());
                stock1.setPercent(val);
                // HERE TOAST
//                String stk = stock_fin.get("sym").toString();
//                toasting(stk);
            } catch (Exception e) {
                Log.d(TAG, "sendFinanceDataToMA: EXCEPTION HERE");
            }
            Log.d(TAG, "sendFinanceDataToMA: "+stock1.getChange());
            Log.d(TAG, "sendFinanceDataToMA: "+stock_fin.get("change"));
            everyStock.add(stock1);
            RVStockAdap.sortList();
            RVStockAdap.notifyDataSetChanged();
         //   toasting(stock_fin.get("sym").toString());
            Log.d(TAG, "sendFinanceDataToMA: ");
            stockDB.addStockEntries(stock1);
        }
    }


    public void retrieveFinanceDetails(String stock_sym) {
// creating below to know whether stock was displayed before or not
        boolean toKnow = false;
        for(int i=0; i< everyStock.size() && toKnow == false; i++)
        {
            if(everyStock.get(i).getSymbol().equals(stock_sym)){
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Duplicate Stock");
                builder.setMessage("Stock Symbol"+ " "+stock_sym + " is already displayed");
                builder.setIcon(R.drawable.baseline_warning_black_48);

                android.app.AlertDialog dialog = builder.create();
                dialog.show();
                toKnow =true; }
        }if(toKnow == false)
            new FinanceAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, stock_sym);
        // toast
      //  toasting(stck_symbl);


        for(int i=0; i< everyStock.size() && toKnow == false; i++) {
            if (everyStock.get(i).getSymbol().equals(stock_sym)) {
                // toast nothing
            }

            else {
                //toasting(stck_symbl);
            }

        }
// end

    }


    @Override
    protected void onDestroy() {
        stockDB.shutDown();
        super.onDestroy();
    }
}
