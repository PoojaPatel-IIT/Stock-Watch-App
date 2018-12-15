package com.example.pooja.stockwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class aboutstockwatch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutstockwatch);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if((item.getItemId()) == R.id.info) {
            finish();}
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aboutpage,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
