package com.personal.gerald.assetmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    private String[] main_option = { "", "", "", "", "", "" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView main = (ListView) findViewById(R.id.mainListView);

        main_option[0] = this.getString(R.string.overview);
        main_option[1] = this.getString(R.string.new_income);
        main_option[2] = this.getString(R.string.new_outgoing);
        main_option[3] = this.getString(R.string.delete_income);
        main_option[4] = this.getString(R.string.delete_outgoing);
        main_option[5] = this.getString(R.string.settings);

        final ArrayAdapter adapter_main = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, main_option);
        main.setAdapter(adapter_main);

        main.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id){
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                switch((int) id) {
                    case 0:
                        intent.setClass(MainActivity.this, Overview.class);
                        break;

                    case 1:
                        intent.setClass(MainActivity.this, Newincome.class);
                        break;

                    case 2:
                        intent.setClass(MainActivity.this, Newoutgoing.class);
                        break;

                    case 3:
                        intent.setClass(MainActivity.this, Detail.class);
                        bundle.putInt("select", Detail.DETAIL_INCOME_MOD_DEL);
                        intent.putExtras(bundle);
                        break;

                    case 4:
                        intent.setClass(MainActivity.this, Detail.class);
                        bundle.putInt("select", Detail.DETAIL_OUTGOING_MOD_DEL);
                        intent.putExtras(bundle);
                        break;

                    case 5:
                        intent.setClass(MainActivity.this, Settings.class);
                        break;

                    default:
                        return;
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
