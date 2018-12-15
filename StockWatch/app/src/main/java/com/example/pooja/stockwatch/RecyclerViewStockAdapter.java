package com.example.pooja.stockwatch;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by pooja on 01,October,2018
 */
public class RecyclerViewStockAdapter extends RecyclerView.Adapter<RecyclerViewStockViewHolder>{

    private ArrayList<Stock> stock_list;
    private MainActivity mAct;
    private static String marketweb =  "http://www.marketwatch.com/investing/stock/";
    private static final String TAG = "RecyclerViewStockAdapte";

    public RecyclerViewStockAdapter(MainActivity ma,ArrayList<Stock> stocks){
        mAct=ma;
        stock_list=stocks;
    }

    public ArrayList<Stock> sortList()
    {
        Collections.sort(stock_list, new Comparator<Stock>() {
            @Override
            public int compare(Stock stock, Stock stock1) {
                return stock.compare(stock.getSymbol(), stock1.getSymbol());
            }
        });
        return stock_list;
    }




    @Override
    public void onBindViewHolder( RecyclerViewStockViewHolder recyclerViewStockViewHolder, int pos) {


Stock st = stock_list.get(pos);
        Log.d(TAG, "onBindViewHolder: ST" +st);
        Double change_stock = this.stock_list.get(pos).getChange();
        Log.d(TAG, "onBindViewHolder: HERE STOCKLIST"+stock_list);
        Log.d(TAG, "onBindViewHolder: CHNAGE in ADAP"+change_stock);
        Log.d(TAG, "onBindViewHolder: Holder List "+stock_list);
       // DecimalFormat df= new DecimalFormat("#.##");


        if (change_stock>=0) {

           // Toast.makeText(mAct,"Stock Displayed", Toast.LENGTH_SHORT).show();
            recyclerViewStockViewHolder.company_name.setText(this.stock_list.get(pos).getCompany());
            recyclerViewStockViewHolder.company_name.setTextColor(Color.GREEN);
            recyclerViewStockViewHolder.company_symbol.setText(this.stock_list.get(pos).getSymbol());
            recyclerViewStockViewHolder.company_symbol.setTextColor(Color.GREEN);
            recyclerViewStockViewHolder.value.setText(String.valueOf(this.stock_list.get(pos).getPrice()));
            recyclerViewStockViewHolder.value.setTextColor(Color.GREEN);


            recyclerViewStockViewHolder.change_symbol.setText("▲ ");
            Log.d(TAG, "onBindViewHolder: " + "▲ ");
            recyclerViewStockViewHolder.change_symbol.setTextColor(Color.GREEN);
            Log.d(TAG, "onBindViewHolder: CHANGE positive DECIMALS" +this.stock_list.get(pos).getChange());
            recyclerViewStockViewHolder.change.setText(String.valueOf(this.stock_list.get(pos).getChange()));
            recyclerViewStockViewHolder.change.setTextColor(Color.GREEN);


            recyclerViewStockViewHolder.percent_change.setText("(" + String.format("%.2f", (stock_list.get(pos).getPercent())) + "%)");
            recyclerViewStockViewHolder.percent_change.setTextColor(Color.GREEN);
        }

        else if(change_stock<0)
        {
           // Toast.makeText(mAct,"Stock Displayed", Toast.LENGTH_SHORT).show();
            recyclerViewStockViewHolder.company_name.setText(this.stock_list.get(pos).getCompany());
            recyclerViewStockViewHolder.company_name.setTextColor(Color.RED);
            recyclerViewStockViewHolder.company_symbol.setText(this.stock_list.get(pos).getSymbol());
            recyclerViewStockViewHolder.company_symbol.setTextColor(Color.RED);
            recyclerViewStockViewHolder.value.setText(String.valueOf(this.stock_list.get(pos).getPrice()));
            recyclerViewStockViewHolder.value.setTextColor(Color.RED);
            recyclerViewStockViewHolder.change_symbol.setText("▼ ");
            Log.d(TAG, "onBindViewHolder: " + "▼ ");
            recyclerViewStockViewHolder.change_symbol.setTextColor(Color.RED);
            String format_percent = String.format("%.2f", this.stock_list.get(pos).getPercent());
            Log.d(TAG, "onBindViewHolder: CHANGE negative DECIMALS" +this.stock_list.get(pos).getChange());
            Log.d(TAG, "onBindViewHolder: AFTER val of "+String.valueOf(this.stock_list.get(pos).getChange()));

            recyclerViewStockViewHolder.change.setText(String.valueOf(this.stock_list.get(pos).getChange()));
            recyclerViewStockViewHolder.change.setTextColor(Color.RED);
//            double round_percent = this.stock_list.get(pos).getPercent();
//            round_percent = Math.round((round_percent * 100) / 100);
//            recyclerViewStockViewHolder.percent_change.setText("( " + format_percent + "% )");
            recyclerViewStockViewHolder.percent_change.setText("(" + String.format("%.2f", (stock_list.get(pos).getPercent())) + "%)");
            recyclerViewStockViewHolder.percent_change.setTextColor(Color.RED);
        }

      //  Toast.makeText(mAct,"Stock Displayed", Toast.LENGTH_SHORT).show();


    }
    @Override
    public int getItemCount() {
        return stock_list.size();
    }
    @Override
    public RecyclerViewStockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        //Create a view of the template with the filled data
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stockitemlist, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                final int position=mAct.getRecyclerView().getChildLayoutPosition(view);
                String url= marketweb + stock_list.get(position).getSymbol();
                Log.d(TAG, "onClick: marketURL"+url);
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mAct.startActivity(intent);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            //on long click deletes the particular data clicked from list and database
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: IN");
                final int position=mAct.getRecyclerView().getChildLayoutPosition(view);
                AlertDialog.Builder builder=new AlertDialog.Builder(mAct);
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAct.getDbHandler().deleteStockEntries(stock_list.get(position).getSymbol());
                        String toShowDeletedStock = stock_list.get(position).getSymbol() + " Deleted !";
                        stock_list.remove(position);
                        notifyDataSetChanged();
                        toasting(toShowDeletedStock);
                       // Toast.makeText(mAct,"Stock Deleted" , Toast.LENGTH_SHORT);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                //Confirm with the user do you want to delete the particular stock
                builder.setMessage("Delete Stock Symbol "+stock_list.get(position).getSymbol()+ "?");
                builder.setTitle("Delete Stock");
                builder.setIcon(R.drawable.ic_delete_black_24dp);
                AlertDialog dialog=builder.create();
                dialog.show();
                Log.d(TAG, "onLongClick: ");
                return true;

            }
        });        return new RecyclerViewStockViewHolder(itemView);
    }
public void toasting(String s){
        Toast.makeText(mAct, s, Toast.LENGTH_SHORT).show();
}

}
