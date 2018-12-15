package com.example.pooja.stockwatch;

import android.widget.TextView;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by pooja on 01,October,2018
 */
public class Stock implements Comparator<String> {

    public double change_symbol;
    public double value;
    public String company_symbol;
    public String company_name;
    public double percent_change;
    public double change;

    @Override
    public int compare(String s, String t1) {
        return s.compareTo(t1);
    }
//
//    public Stock(String s, String cn, Double val, Double change1, Double percent) {
//        company_symbol = s;
//        this.company_name = cn;
//        value = val;
//        change = change1;
//        percent_change = percent;
//    }

    @Override
    public String toString() {
        return "new_note{" +
                "symbol='" + company_symbol + '\'' +
                ", company='" + company_name + '\'' +
                ", price=" + value +
                ", change=" + change +
                ", percent=" + percent_change +
                '}';
    }

    public String getSymbol() {
        return company_symbol;
    }
    public String getCompany() {
        return company_name;
    }
    public void setCompany(String company) {
        this.company_name = company;
    }
    public double getPrice() {
        return value;
    }
    public void setPrice(Double price) {
        this.value = price;
    }
    public double getChange() {
        return change;
    }
    public void setSymbol(String symbol) {
        this.company_symbol = symbol;
    }
    public void setChange(Double change) {
        this.change = change;
    }
    public void setPercent(Double percent) {
        this.percent_change = percent;
    }
    public double getPercent() {
        return percent_change;
    }



}
