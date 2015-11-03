package com.personal.gerald.assetmanager;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class Instalment extends Activity {

    private int select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instalment);

        Button confirm_button = (Button)findViewById(R.id.addinstalment_confirmButton);
        confirm_button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                int date, money, num;
                DatePicker datepicker = (DatePicker)findViewById(R.id.addinstalmentDatePicker);
                EditText num_edittext = (EditText)findViewById(R.id.addinstalment_numEditText);
                EditText money_edittext = (EditText)findViewById(R.id.addinstalment_moneyEditText);
                EditText note_edittext = (EditText)findViewById(R.id.addinstalment_noteEditText);

                /* get date and store as an integer to date, ex: date = 20140329 */
                date = datepicker.getYear();
                date = date * 10000;
                date += (datepicker.getMonth() + 1) * 100;
                date += datepicker.getDayOfMonth();

                /* get input text for number of instalment */
                Editable num_editable = num_edittext.getText();
                if(num_editable.toString().isEmpty()) {
                    Toast.makeText(Instalment.this, Instalment.this.getString(R.string.please_enter_num),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                num = Integer.parseInt(num_editable.toString());

                /* get input text for money */
                Editable money_editable = money_edittext.getText();
                if(money_editable.toString().isEmpty()) {
                    Toast.makeText(Instalment.this, Instalment.this.getString(R.string.please_enter_money),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                money = Integer.parseInt(money_editable.toString());

                /* get input text for note */
                Editable note_editable = note_edittext.getText();

                /* Insert a record to database and display success message */
                AssetDB adb = new AssetDB(getApplicationContext());


                adb.add_one_record(date, money, note_editable.toString(), num,
                        AssetDbHelper.INSTALMENT_OUTGOING_TABLE_NAME);
                Toast.makeText(Instalment.this, Instalment.this.getString(R.string.instalment_outgoing_success),
                        Toast.LENGTH_SHORT).show();

                num_edittext.setText("");
                money_edittext.setText("");
                note_edittext.setText("");
            }
        });

        Button cancel_button = (Button)findViewById(R.id.addinstalment_cancelButton);
        cancel_button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Instalment.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
