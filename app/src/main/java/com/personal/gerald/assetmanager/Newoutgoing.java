package com.personal.gerald.assetmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Newoutgoing extends Activity {

    private String[] newoutgoing_list = { "", "", "", "" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_outgoing);

        final ListView newoutgoing_listview = (ListView) findViewById(R.id.newoutgoingListView);

        newoutgoing_list[0] = this.getString(R.string.onetime_outgoing);
        newoutgoing_list[1] = this.getString(R.string.monthly_outgoing);
        newoutgoing_list[2] = this.getString(R.string.yearly_outgoing);
        newoutgoing_list[3] = this.getString(R.string.instalment);

        final ArrayAdapter adapter_overview = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, newoutgoing_list);
        newoutgoing_listview.setAdapter(adapter_overview);

        newoutgoing_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id){
                Intent intent = new Intent();

                if(id <= 2) {
                    /* Newincome and Newoutgoing launch the same activity (class Addone)
                    *  but depend on id to display the right theme
                    *  */
                    intent.setClass(Newoutgoing.this, Addone.class);

                    Bundle bundle = new Bundle();
                    bundle.putInt("select", (int)id + 3);
                    intent.putExtras(bundle);
                } else {
                    /* launch the instalment activity (class Instalment)*/
                    intent.setClass(Newoutgoing.this, Instalment.class);
                }

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
