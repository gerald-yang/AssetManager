package com.personal.gerald.assetmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final ListView settings_listview = (ListView) findViewById(R.id.settingsListView);
        String[] settings_list = { this.getString(R.string.adjust) };

        final ArrayAdapter adapter_overview = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, settings_list);
        settings_listview.setAdapter(adapter_overview);

        settings_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id){

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
