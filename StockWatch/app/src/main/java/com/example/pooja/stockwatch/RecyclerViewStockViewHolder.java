package com.example.pooja.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by pooja on 01,October,2018
 */
public class RecyclerViewStockViewHolder extends RecyclerView.ViewHolder
{
    public TextView change;
    public TextView change_symbol;
    public TextView value;
    public TextView company_symbol;
    public TextView company_name;
    public TextView percent_change;

    public RecyclerViewStockViewHolder(View itemView)
    {
        // getting all the fields from the layout
        super(itemView);
        company_symbol = itemView.findViewById(R.id.c_sbl);
        value = itemView.findViewById(R.id.value);
        company_name = itemView.findViewById(R.id.c_name);
        change_symbol = itemView.findViewById(R.id.change_symbol);
        percent_change = itemView.findViewById(R.id.per_change);
        change = itemView.findViewById(R.id.change);
    }

}
