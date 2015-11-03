package com.personal.gerald.assetmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Newincome extends Activity {

    private String[] newincome_list = { "", "", "" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_income);

        final ListView newincome_listview = (ListView) findViewById(R.id.newincomeListView);

        newincome_list[0] = this.getString(R.string.onetime_income);
        newincome_list[1] = this.getString(R.string.monthly_income);
        newincome_list[2] = this.getString(R.string.yearly_income);

        final ArrayAdapter adapter_overview = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, newincome_list);
        newincome_listview.setAdapter(adapter_overview);

        newincome_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id){
                Intent intent = new Intent();
                intent.setClass(Newincome.this, Addone.class);

                /* Newincome and Newoutgoing launch the same activity (class Addone)
                 *  but depend on id to display the right theme
                 *  */
                Bundle bundle = new Bundle();
                bundle.putInt("select", (int)id);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
