package com.personal.gerald.assetmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Overview extends Activity {

    private ArrayList<String> overview_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        final ListView overview_listview = (ListView) findViewById(R.id.overviewListView);

        AssetDB adb = new AssetDB(getApplicationContext());
        //adb.reset_db();

        overview_list = new ArrayList<String>();

        overview_list.add(this.getString(R.string.current_asset) +":  " + adb.get_current_asset());
        overview_list.add(this.getString(R.string.current_month_income) + ":  " + adb.get_current_month_income());
        overview_list.add(this.getString(R.string.current_month_outgoing )+ ":  " + adb.get_current_month_outgoing());
        overview_list.add(this.getString(R.string.detail_income));
        overview_list.add(this.getString(R.string.detail_outgoing));
        overview_list.add(this.getString(R.string.detail_in_and_out));
        overview_list.add(this.getString(R.string.predict_future_asset));


        final ArrayAdapter adapter_overview = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, overview_list);
        overview_listview.setAdapter(adapter_overview);

        overview_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id){

                Intent intent;
                Bundle bundle;

                /* detail income and outgoing launch the same activity (class Detail) */
                switch((int)id) {
                    case 3:
                        intent = new Intent();
                        bundle = new Bundle();
                        intent.setClass(Overview.this, Detail.class);
                        bundle.putInt("select", Detail.DETAIL_INCOME);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;

                    case 4:
                        intent = new Intent();
                        bundle = new Bundle();
                        intent.setClass(Overview.this, Detail.class);
                        bundle.putInt("select", Detail.DETAIL_OUTGOING);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;

                    case 5:
                        intent = new Intent();
                        bundle = new Bundle();
                        intent.setClass(Overview.this, Detail.class);
                        bundle.putInt("select", Detail.DETAIL_IN_AND_OUT);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;

                    case 6:
                        intent = new Intent();
                        bundle = new Bundle();
                        intent.setClass(Overview.this, Predict.class);
                        startActivity(intent);
                        break;
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
