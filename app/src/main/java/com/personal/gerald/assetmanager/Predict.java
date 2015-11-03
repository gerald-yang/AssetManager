package com.personal.gerald.assetmanager;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Predict extends Activity {

    private void set_predict_asset(String predict_asset_str) {
        TextView money_textview = (TextView)findViewById(R.id.predict_moneyTextView);

        money_textview.setText(predict_asset_str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);

        AssetDB adb = new AssetDB(getApplicationContext());
        set_predict_asset(this.getString(R.string.predict_asset) + ":  "
                + adb.get_current_asset());


        Button confirm_button = (Button)findViewById(R.id.predict_confirmButton);
        confirm_button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
            int date;
            AssetDB adb = new AssetDB(getApplicationContext());
            DatePicker datepicker = (DatePicker)findViewById(R.id.predictDatePicker);

            date = datepicker.getYear();
            date = date * 10000;
            date += (datepicker.getMonth() + 1) * 100;
            date += datepicker.getDayOfMonth();

            set_predict_asset(Predict.this.getString(R.string.predict_asset) + ":  "
                    + adb.get_asset(date));
            }
        });

        Button cancel_button = (Button)findViewById(R.id.predict_cancelButton);
        cancel_button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Predict.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
