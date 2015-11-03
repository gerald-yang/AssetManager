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

public class Addone extends Activity {

    private int select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addone);

        /* Set the title according to the value from Newincome or Newoutgoing */
        Bundle bundle = this.getIntent().getExtras();
        select = bundle.getInt("select");
        switch(select) {
            case 0:
                setTitle(this.getString(R.string.add_onetime_income));
                break;
            case 1:
                setTitle(this.getString(R.string.add_monthly_income));
                break;
            case 2:
                setTitle(this.getString(R.string.add_yearly_income));
                break;
            case 3:
                setTitle(this.getString(R.string.add_onetime_outgoing));
                break;
            case 4:
                setTitle(this.getString(R.string.add_monthly_outgoing));
                break;
            case 5:
                setTitle(this.getString(R.string.add_yearly_outgoing));
                break;
        }

        Button confirm_button = (Button)findViewById(R.id.addone_confirmButton);
        confirm_button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                int date, money;
                DatePicker datepicker = (DatePicker)findViewById(R.id.addoneDatePicker);
                EditText money_edittext = (EditText)findViewById(R.id.addone_moneyEditText);
                EditText note_edittext = (EditText)findViewById(R.id.addone_noteEditText);

                /* get date and store as an integer to date, ex: date = 20140329 */
                date = datepicker.getYear();
                date = date * 10000;
                date += (datepicker.getMonth() + 1) * 100;
                date += datepicker.getDayOfMonth();

                /* get input text for money */
                Editable money_editable = money_edittext.getText();
                if(money_editable.toString().isEmpty()) {
                    Toast.makeText(Addone.this, Addone.this.getString(R.string.please_enter_money),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                money = Integer.parseInt(money_editable.toString());

                /* get input text for note */
                Editable note_editable = note_edittext.getText();

                AssetDB adb = new AssetDB(getApplicationContext());

                /* Insert a record to database and display success message */
                switch(select) {
                    case 0:
                        adb.add_one_record(date, money, note_editable.toString(), 0,
                                AssetDbHelper.ONETIME_INCOME_TABLE_NAME);
                        Toast.makeText(Addone.this, Addone.this.getString(R.string.onetime_income_success),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        adb.add_one_record(date, money, note_editable.toString(), 0,
                                AssetDbHelper.MONTHLY_INCOME_TABLE_NAME);
                        Toast.makeText(Addone.this, Addone.this.getString(R.string.monthly_income_success),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        adb.add_one_record(date, money, note_editable.toString(), 0,
                                AssetDbHelper.YEARLY_INCOME_TABLE_NAME);
                        Toast.makeText(Addone.this, Addone.this.getString(R.string.yearly_income_success),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        adb.add_one_record(date, money, note_editable.toString(), 0,
                                AssetDbHelper.ONETIME_OUTGOING_TABLE_NAME);
                        Toast.makeText(Addone.this, Addone.this.getString(R.string.onetime_outgoing_success),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        adb.add_one_record(date, money, note_editable.toString(), 0,
                                AssetDbHelper.MONTHLY_OUTGOING_TABLE_NAME);
                        Toast.makeText(Addone.this, Addone.this.getString(R.string.monthly_outgoing_success),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        adb.add_one_record(date, money, note_editable.toString(), 0,
                                AssetDbHelper.YEARLY_OUTGOING_TABLE_NAME);
                        Toast.makeText(Addone.this, Addone.this.getString(R.string.yearly_outgoing_success),
                                Toast.LENGTH_SHORT).show();
                        break;
                }

                money_edittext.setText("");
                note_edittext.setText("");

            }
        });

        Button cancel_button = (Button)findViewById(R.id.addone_cancelButton);
        cancel_button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Addone.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
