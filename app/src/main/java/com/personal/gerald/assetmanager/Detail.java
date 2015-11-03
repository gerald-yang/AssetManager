package com.personal.gerald.assetmanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class Detail extends Activity {

    /* the whole records include date, money and note are stored in record_list */
    private ArrayList<AssetRecord> record_list;

    private int select;
    private int select_id;
    private Dialog dialog;

    public static final int DETAIL_INCOME = 0;
    public static final int DETAIL_OUTGOING = 1;
    public static final int DETAIL_IN_AND_OUT = 2;
    public static final int DETAIL_INCOME_MOD_DEL = 3;
    public static final int DETAIL_OUTGOING_MOD_DEL = 4;

    private void update_list() {
        ListView detail_listview = (ListView) findViewById(R.id.detailListView);

        /* display_list only shows those information want to be displayed in the listview*/
        ArrayList<String> display_list;

        AssetDB adb = new AssetDB(getApplicationContext());

        Bundle bundle = this.getIntent().getExtras();
        select = bundle.getInt("select");

        String [] title_income = {this.getString(R.string.onetime_income),
                this.getString(R.string.monthly_income),
                this.getString(R.string.yearly_income)};

        String [] title_outgoing = {this.getString(R.string.onetime_outgoing),
                this.getString(R.string.monthly_outgoing),
                this.getString(R.string.yearly_outgoing),
                this.getString(R.string.instalment)};


        /* Grab all records and sort them by date */
        switch (select) {
            case DETAIL_INCOME:
                record_list = adb.get_detail(select, title_income);
                break;

            case DETAIL_OUTGOING:
                record_list = adb.get_detail(select, title_outgoing);
                break;

            case DETAIL_IN_AND_OUT:
                ArrayList<AssetRecord> temp_list;
                temp_list = adb.get_detail(DETAIL_INCOME, title_income);
                record_list = adb.get_detail(DETAIL_OUTGOING, title_outgoing);
                record_list.addAll(temp_list);
                break;

            case DETAIL_INCOME_MOD_DEL:
                record_list = adb.get_all_record(select, title_income);
                break;

            case DETAIL_OUTGOING_MOD_DEL:
                record_list = adb.get_all_record(select, title_outgoing);
                break;
        }

        Collections.sort(record_list);
        Collections.reverse(record_list);

        display_list = new ArrayList<String>();

        /* record is used to be an item displayed on the listview */
        String record;

        for(int i = 0; i < record_list.size(); i++) {
            record = new String();

            record += record_list.get(i).title_str;
            record += ":  ";

            record += record_list.get(i).date_str;
            record += "   ";

            record += this.getString(R.string.money);
            record += ":  ";
            record += record_list.get(i).money_str;

            if(record_list.get(i).year_month_day == AssetDB.SELECT_INSTALMENT) {
                record += "  (";
                if(select != DETAIL_OUTGOING_MOD_DEL) {
                    record += Integer.toString(record_list.get(i).current_instalment);
                    record += "/";
                }
                record += Integer.toString(record_list.get(i).total_instalment);
                record += this.getString(R.string.instalment_name);
                record += ")";
            }

            display_list.add(record);
        }

        final ArrayAdapter adapter_overview = new ArrayAdapter(this,
                R.layout.listview_item, R.id.item, display_list);


       // int firstPosition = detail_listview.getFirstVisiblePosition() - detail_listview.getHeaderViewsCount(); // This is the same as child #0
        //int wantedChild = 3 - firstPosition;
        //if (wantedChild < 0 || wantedChild >= detail_listview.getChildCount()) {
            //Log.w(TAG, "Unable to get view for desired position, because it's not being displayed on screen.");
        //    return;
        //}
// Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
        //View wantedView = detail_listview.getChildAt(wantedChild);
        //TextView textView=(TextView) wantedView.findViewById(android.R.id.text1);
        //textView.setTextColor(Color.BLUE);

        detail_listview.setAdapter(adapter_overview);


        View wantedView = detail_listview.getChildAt(0);
        //TextView textView=(TextView) wantedView.findViewById(R.id.item);
        //Toast.makeText(this, textView.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ListView detail_listview = (ListView) findViewById(R.id.detailListView);

        update_list();

        detail_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id){

                select_id = (int)id;

                switch(select) {
                    case DETAIL_INCOME:
                    case DETAIL_OUTGOING:
                    case DETAIL_IN_AND_OUT:
                        /* display detail information on a dialog for the selected item */
                        dialog = new Dialog(Detail.this);
                        dialog.setContentView(R.layout.activity_detail_dialog);
                        dialog.setTitle(record_list.get((int)id).title_str);


                        String detail_str = "";

                        if(record_list.get((int)id).total_instalment == 0) {
                            detail_str = Detail.this.getString(R.string.date);
                            detail_str += ":  ";
                            detail_str += record_list.get((int) id).date_str;
                        } else {
                            detail_str = Detail.this.getString(R.string.instalment_total);
                            detail_str += ":  ";
                            detail_str += record_list.get((int)id).start_date_str;
                            detail_str += " - ";
                            detail_str += record_list.get((int)id).end_date_str;

                            detail_str += "\n";

                            detail_str += Detail.this.getString(R.string.instalment_current);
                            detail_str += ": ";
                            detail_str += record_list.get((int) id).date_str;
                            detail_str += "  (";
                            detail_str += record_list.get((int)id).current_instalment;
                            detail_str += "/";
                            detail_str += record_list.get((int)id).total_instalment;
                            detail_str += ")";
                        }

                        detail_str += "\n\n";

                        detail_str += Detail.this.getString(R.string.note);
                        detail_str += ":  ";
                        detail_str += record_list.get((int)id).note;

                        TextView note_textview = (TextView)dialog.findViewById(R.id.dialog_noteTextView);
                        note_textview.setText(detail_str);

                        Button button = (Button)dialog.findViewById(R.id.dialogButton);

                        button.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        break;

                    case DETAIL_INCOME_MOD_DEL:
                    case DETAIL_OUTGOING_MOD_DEL:

                        /* display all items */
                        dialog = new Dialog(Detail.this);
                        dialog.setContentView(R.layout.activity_mod_del_dialog);
                        dialog.setTitle(record_list.get((int)id).title_str);


                        String info_str = "";

                        if(record_list.get((int)id).total_instalment == 0) {
                            info_str = Detail.this.getString(R.string.date);
                            info_str += ":  ";
                            info_str += record_list.get((int) id).date_str;
                        } else {
                            info_str = Detail.this.getString(R.string.instalment);
                            info_str += ":  ";
                            info_str += record_list.get((int) id).date_str;
                            info_str += "  (";
                            info_str += record_list.get((int)id).total_instalment;
                            info_str += Detail.this.getString(R.string.instalment_name);
                            info_str += ")";
                        }

                        info_str += "\n\n";

                        info_str += Detail.this.getString(R.string.note);
                        info_str += ":  ";
                        info_str += record_list.get((int)id).note;

                        TextView dialog_del_textview = (TextView)dialog.findViewById(R.id.dialog_delTextView);
                        dialog_del_textview.setText(info_str);


                        Button delete_button = (Button)dialog.findViewById(R.id.dialog_del_confirmButton);
                        Button cancel_button = (Button)dialog.findViewById(R.id.dialog_del_cancelButton);

                        delete_button.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                AssetDB adb = new AssetDB(getApplicationContext());
                                adb.del_one_record(record_list.get(select_id).row_id,
                                        record_list.get(select_id).table_name);
                                Toast.makeText(Detail.this,
                                        Detail.this.getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                update_list();
                            }
                        });

                        cancel_button.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
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
