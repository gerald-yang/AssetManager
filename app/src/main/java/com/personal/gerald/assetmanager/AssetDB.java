package com.personal.gerald.assetmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class AssetDB {

    private SQLiteDatabase db;
    private AssetDbHelper adbhelper;

    public static final int SELECT_DAY = 0;
    public static final int SELECT_MONTH = 1;
    public static final int SELECT_YEAR = 2;
    public static final int SELECT_INSTALMENT = 3;

    public AssetDB(Context context) {

            adbhelper = new AssetDbHelper(context);
            db = adbhelper.getWritableDatabase();
    }

    public void reset_db() {

        adbhelper.reset_db(db);
    }

    public static String date_int_to_string(int date) {
        String date_str = Integer.toString(date / 10000);
        date_str += "/";
        if(date % 10000 < 1000)
            date_str += "0";
        date_str += Integer.toString((date % 10000) / 100);
        date_str += "/";
        if(date % 100 < 10)
            date_str +="0";
        date_str += Integer.toString(date % 100);

        return date_str;
    }

    public static int date_add(int date, int year, int month) {
        int result;
        int result_year = date / 10000;
        int result_month = (date % 10000) / 100;
        int result_day = date % 100;

        if(year > 0) {
            result_year += year;
        }

        if(month > 0) {
            for(int i = 0; i < month; i++) {
                result_month++;
                if(result_month > 12) {
                    result_year++;
                    result_month = 1;
                }
            }

            if(result_day == 31) {
                switch(result_month) {
                    case 2:
                        result_day = 28;
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        result_day = 30;
                        break;
                }
            }

            if(result_day == 30 && result_month == 2) {
                result_day = 28;
            }

            if(result_day == 29 && result_month == 2) {
                result_day = 28;
            }
        }

        result = result_year * 10000;
        result += (result_month * 100);
        result += result_day;

        return result;
    }

    public static int date_sub(int date, int year, int month) {
        int result;
        int result_year = date / 10000;
        int result_month = (date % 10000) / 100;
        int result_day = date % 100;

        if(year > 0) {
            result_year -= year;
        }

        if(month > 0) {
            for(int i = 0; i < month; i++) {
                result_month--;
                if(result_month == 0) {
                    result_year--;
                    result_month = 12;
                }
            }

            if(result_day == 31) {
                switch(result_month) {
                    case 2:
                        result_day = 28;
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        result_day = 30;
                        break;
                }
            }

            if(result_day == 30 && result_month == 2) {
                result_day = 28;
            }

            if(result_day == 29 && result_month == 2) {
                result_day = 28;
            }
        }

        result = result_year * 10000;
        result += (result_month * 100);
        result += result_day;

        return result;
    }

    public void add_one_record(int date, int money, String note, int num, String table_name) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("money", money);
        values.put("note", note);
        values.put("num", num);

        db.insert(table_name, null, values);
    }

    public void del_one_record(int row_id, String table_name) {

        db.delete(table_name, AssetDbHelper.FIELD_ID + "=" + row_id, null);
    }

    private int get_money_until_date(int until_date, String table_name, int day_month_year) {

        int total = 0, count, i;
        Cursor cursor;
        String [] columns = {AssetDbHelper.FIELD_ID, AssetDbHelper.FIELD_DATE,
                AssetDbHelper.FIELD_MONEY, AssetDbHelper.FIELD_NUM};
        int date, num = 6, total_month = 0, total_year = 0;

        // calculate all onetime income until given day
        cursor = db.query(
                table_name, columns,
                "date <= " + until_date, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        switch(day_month_year) {
            case SELECT_DAY:
                for(i = 0; i < count; i++) {
                    total += cursor.getInt(AssetDbHelper.INDEX_MONEY);
                    cursor.moveToNext();
                }
                break;

            case SELECT_MONTH:
                for(i = 0; i < count; i++) {
                    date = cursor.getInt(AssetDbHelper.INDEX_DATE);

                    total_month = 0;
                    while (until_date >= date) {
                        total_month++;
                        date = date_add(date, 0, 1);
                    }

                    total += cursor.getInt(AssetDbHelper.INDEX_MONEY) * total_month;
                    cursor.moveToNext();
                }
                break;

            case SELECT_YEAR:
                for(i = 0; i < count; i++) {
                    date = cursor.getInt(AssetDbHelper.INDEX_DATE);

                    total_year = 0;
                    while(until_date >= date) {
                        total_year++;
                        date = date_add(date, 1, 0);
                    }

                    total += cursor.getInt(AssetDbHelper.INDEX_MONEY) * total_year;
                    cursor.moveToNext();
                }
                break;

            case SELECT_INSTALMENT:
                for(i = 0; i < count; i++) {
                    date = cursor.getInt(AssetDbHelper.INDEX_DATE);
                    num = cursor.getInt(AssetDbHelper.INDEX_NUM);

                    total_month = 0;
                    while (until_date >= date && num > 0) {
                        total_month++;
                        num--;
                        date = date_add(date, 0, 1);
                    }

                    total += (cursor.getInt(AssetDbHelper.INDEX_MONEY) / cursor.getInt(AssetDbHelper.INDEX_NUM))* total_month;
                    cursor.moveToNext();
                }
                break;

            default:
                return 0;
        }

        cursor.close();

        return total;
    }

    public int get_current_month_income() {

        int current_income = 0;
        int today = 0, current_month;
        Calendar cal = Calendar.getInstance();
        Cursor cursor;
        String [] columns = {AssetDbHelper.FIELD_ID, AssetDbHelper.FIELD_DATE, AssetDbHelper.FIELD_MONEY};
        int i, count;

        today = (cal.get(Calendar.YEAR) * 10000);
        today += ((cal.get(Calendar.MONTH) + 1) * 100);
        current_month = today;
        today += cal.get(Calendar.DAY_OF_MONTH);


        // calculate all onetime income for this month
        cursor = db.query(
                AssetDbHelper.ONETIME_INCOME_TABLE_NAME, columns,
                "date > " + current_month + " and date <= " + today,
                null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i =0; i < count; i++) {
            current_income += cursor.getInt(AssetDbHelper.INDEX_MONEY);
            cursor.moveToNext();
        }

        cursor.close();



        // calculate all monthly income for this month
        cursor = db.query(
                AssetDbHelper.MONTHLY_INCOME_TABLE_NAME, columns,
                "date <= " + today, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i = 0; i < count; i++) {
            if((today % 100) >= (cursor.getInt(AssetDbHelper.INDEX_DATE) % 100)) {
                current_income += cursor.getInt(AssetDbHelper.INDEX_MONEY);
            }

            cursor.moveToNext();
        }

        cursor.close();


        // calculate all yearly income for this month
        cursor = db.query(
                AssetDbHelper.YEARLY_INCOME_TABLE_NAME, columns,
                "date <= " + today, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i = 0; i < count; i++) {
            if((today % 10000) >= (cursor.getInt(AssetDbHelper.INDEX_DATE) % 10000)) {
                current_income += cursor.getInt(AssetDbHelper.INDEX_MONEY);
            }

            cursor.moveToNext();
        }

        cursor.close();


        return current_income;
    }

    public int get_current_month_outgoing() {

        int current_outgoing = 0;
        int today = 0, current_month;
        Calendar cal = Calendar.getInstance();
        Cursor cursor;
        String [] columns = {AssetDbHelper.FIELD_ID, AssetDbHelper.FIELD_DATE, AssetDbHelper.FIELD_MONEY};
        int i, count;

        today = (cal.get(Calendar.YEAR) * 10000);
        today += ((cal.get(Calendar.MONTH) + 1) * 100);
        current_month = today;
        today += cal.get(Calendar.DAY_OF_MONTH);


        // calculate all onetime income for this month
        cursor = db.query(
                AssetDbHelper.ONETIME_OUTGOING_TABLE_NAME, columns,
                "date > " + current_month + " and date <= " + today,
                null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i =0; i < count; i++) {
            current_outgoing += cursor.getInt(AssetDbHelper.INDEX_MONEY);
            cursor.moveToNext();
        }

        cursor.close();



        // calculate all monthly income for this month
        cursor = db.query(
                AssetDbHelper.MONTHLY_OUTGOING_TABLE_NAME, columns,
                "date <= " + today, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i = 0; i < count; i++) {
            if((today % 100) >= (cursor.getInt(AssetDbHelper.INDEX_DATE) % 100)) {
                current_outgoing += cursor.getInt(AssetDbHelper.INDEX_MONEY);
            }

            cursor.moveToNext();
        }

        cursor.close();


        // calculate all yearly income for this month
        cursor = db.query(
                AssetDbHelper.YEARLY_OUTGOING_TABLE_NAME, columns,
                "date <= " + today, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i = 0; i < count; i++) {
            if((today % 10000) >= (cursor.getInt(AssetDbHelper.INDEX_DATE) % 10000)) {
                current_outgoing += cursor.getInt(AssetDbHelper.INDEX_MONEY);
            }

            cursor.moveToNext();
        }

        cursor.close();


        return current_outgoing;
    }

    public ArrayList<AssetRecord> get_all_record(int income_outgoing, String [] title) {
        int count, i;
        Cursor cursor;
        ArrayList<AssetRecord> record_list = new ArrayList<AssetRecord>();
        AssetRecord record;
        String table_name = "";

        if(income_outgoing != Detail.DETAIL_INCOME_MOD_DEL &&
                income_outgoing != Detail.DETAIL_OUTGOING_MOD_DEL) {
            return record_list;
        }

        /* read onetime income or outgoing */
        if (income_outgoing == Detail.DETAIL_INCOME_MOD_DEL) {
            table_name = AssetDbHelper.ONETIME_INCOME_TABLE_NAME;
        } else if (income_outgoing == Detail.DETAIL_OUTGOING_MOD_DEL) {
            table_name = AssetDbHelper.ONETIME_OUTGOING_TABLE_NAME;
        }

        cursor = db.query(
                table_name, null,
                null, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i = 0; i < count; i++) {
            record = new AssetRecord();

            record.table_name = table_name;

            record.row_id = cursor.getInt(AssetDbHelper.INDEX_ID);

            record.title_str = title[0];

            record.year_month_day = SELECT_DAY;

            record.date_int = cursor.getInt(AssetDbHelper.INDEX_DATE);;
            record.date_str = date_int_to_string(record.date_int);

            record.money_int = cursor.getInt(AssetDbHelper.INDEX_MONEY);
            record.money_str = Integer.toString(record.money_int);

            record.note = cursor.getString(AssetDbHelper.INDEX_NOTE);

            record.total_instalment = cursor.getInt(AssetDbHelper.INDEX_NUM);

            record_list.add(record);

            cursor.moveToNext();
        }

        cursor.close();


        // read monthly income or outgoing
        if (income_outgoing == Detail.DETAIL_INCOME_MOD_DEL) {
            table_name = AssetDbHelper.MONTHLY_INCOME_TABLE_NAME;
        } else if (income_outgoing == Detail.DETAIL_OUTGOING_MOD_DEL) {
            table_name = AssetDbHelper.MONTHLY_OUTGOING_TABLE_NAME;
        }

        cursor = db.query(
                table_name, null,
                null, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i = 0; i < count; i++) {
            record = new AssetRecord();

            record.table_name = table_name;

            record.row_id = cursor.getInt(AssetDbHelper.INDEX_ID);

            record.title_str = title[1];

            record.year_month_day = SELECT_MONTH;

            record.date_int = cursor.getInt(AssetDbHelper.INDEX_DATE);
            record.date_str = date_int_to_string(record.date_int);

            record.money_int = cursor.getInt(AssetDbHelper.INDEX_MONEY);
            record.money_str = Integer.toString(record.money_int);

            record.note = cursor.getString(AssetDbHelper.INDEX_NOTE);

            record.total_instalment = cursor.getInt(AssetDbHelper.INDEX_NUM);

            record_list.add(record);

            cursor.moveToNext();
        }

        cursor.close();


        // read yearly income or outgoing
        if (income_outgoing == Detail.DETAIL_INCOME_MOD_DEL) {
            table_name = AssetDbHelper.YEARLY_INCOME_TABLE_NAME;
        } else if (income_outgoing == Detail.DETAIL_OUTGOING_MOD_DEL) {
            table_name = AssetDbHelper.YEARLY_OUTGOING_TABLE_NAME;
        }

        cursor = db.query(
                table_name, null,
                null, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i = 0; i < count; i++) {
            record = new AssetRecord();

            record.table_name = table_name;

            record.row_id = cursor.getInt(AssetDbHelper.INDEX_ID);

            record.title_str = title[2];

            record.year_month_day = SELECT_YEAR;

            record.date_int = cursor.getInt(AssetDbHelper.INDEX_DATE);;
            record.date_str = date_int_to_string(record.date_int);

            record.money_int = cursor.getInt(AssetDbHelper.INDEX_MONEY);
            record.money_str = Integer.toString(record.money_int);

            record.note = cursor.getString(AssetDbHelper.INDEX_NOTE);

            record.total_instalment = cursor.getInt(AssetDbHelper.INDEX_NUM);

            record_list.add(record);

            cursor.moveToNext();
        }

        cursor.close();


        // read instalment table
        if (income_outgoing == Detail.DETAIL_OUTGOING_MOD_DEL) {
            table_name = AssetDbHelper.INSTALMENT_OUTGOING_TABLE_NAME;

            cursor = db.query(
                    table_name, null,
                    null, null, null, null, null, null);

            count = cursor.getCount();
            cursor.moveToFirst();

            for (i = 0; i < count; i++) {
                record = new AssetRecord();

                record.table_name = table_name;

                record.row_id = cursor.getInt(AssetDbHelper.INDEX_ID);

                record.title_str = title[3];

                record.year_month_day = SELECT_INSTALMENT;

                record.date_int = cursor.getInt(AssetDbHelper.INDEX_DATE);
                record.date_str = date_int_to_string(record.date_int);

                record.money_int = cursor.getInt(AssetDbHelper.INDEX_MONEY);
                record.money_str = Integer.toString(record.money_int);

                record.total_instalment = cursor.getInt(AssetDbHelper.INDEX_NUM);

                record.note = cursor.getString(AssetDbHelper.INDEX_NOTE);

                record_list.add(record);

                cursor.moveToNext();
            }

            cursor.close();
        }

        return record_list;
    }

    public ArrayList<AssetRecord> get_detail(int income_outgoing, String [] title) {
        int count, i, current_instalment;
        Cursor cursor;
        String[] columns = {AssetDbHelper.FIELD_ID, AssetDbHelper.FIELD_DATE,
                AssetDbHelper.FIELD_MONEY, AssetDbHelper.FIELD_NUM, AssetDbHelper.FIELD_NOTE};
        int date;
        ArrayList<AssetRecord> record_list = new ArrayList<AssetRecord>();
        AssetRecord record;
        int today = 0;
        Calendar cal = Calendar.getInstance();
        String table_name = "";
        int month;

        today += (cal.get(Calendar.YEAR) * 10000);
        today += ((cal.get(Calendar.MONTH) + 1) * 100);
        today += cal.get(Calendar.DAY_OF_MONTH);

        if(income_outgoing != Detail.DETAIL_INCOME && income_outgoing != Detail.DETAIL_OUTGOING) {
            return record_list;
        }

        /* read onetime income or outgoing */
        if (income_outgoing == Detail.DETAIL_INCOME) {
            table_name = AssetDbHelper.ONETIME_INCOME_TABLE_NAME;
        } else if (income_outgoing == Detail.DETAIL_OUTGOING) {
            table_name = AssetDbHelper.ONETIME_OUTGOING_TABLE_NAME;
        }

        cursor = db.query(
                table_name, columns,
                null, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i = 0; i < count; i++) {
            record = new AssetRecord();

            record.title_str = title[0];

            record.year_month_day = SELECT_DAY;

            date = cursor.getInt(AssetDbHelper.INDEX_DATE);
            record.date_int = date;
            record.date_str = date_int_to_string(date);

            record.money_int = cursor.getInt(AssetDbHelper.INDEX_MONEY);
            record.money_str = Integer.toString(record.money_int);

            record.note = cursor.getString(AssetDbHelper.INDEX_NOTE);

            record.total_instalment = cursor.getInt(AssetDbHelper.INDEX_NUM);

            record_list.add(record);

            cursor.moveToNext();
        }

        cursor.close();


        // read monthly income or outgoing
        if (income_outgoing == Detail.DETAIL_INCOME) {
            table_name = AssetDbHelper.MONTHLY_INCOME_TABLE_NAME;
        } else if (income_outgoing == Detail.DETAIL_OUTGOING) {
            table_name = AssetDbHelper.MONTHLY_OUTGOING_TABLE_NAME;
        }

        cursor = db.query(
                table_name, columns,
                "date <= " + today, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i = 0; i < count; i++) {
            date = cursor.getInt(AssetDbHelper.INDEX_DATE);

            month = 0;

            do {
                record = new AssetRecord();

                record.title_str = title[1];

                record.year_month_day = SELECT_MONTH;

                record.date_int = date_add(date, 0, month);
                record.date_str = date_int_to_string(record.date_int);

                record.money_int = cursor.getInt(AssetDbHelper.INDEX_MONEY);
                record.money_str = Integer.toString(record.money_int);

                record.note = cursor.getString(AssetDbHelper.INDEX_NOTE);

                record.total_instalment = cursor.getInt(AssetDbHelper.INDEX_NUM);

                record_list.add(record);

                month ++;
            } while(today >= date_add(date, 0, month));

            cursor.moveToNext();
        }

        cursor.close();


        // read yearly income or outgoing
        if (income_outgoing == Detail.DETAIL_INCOME) {
            table_name = AssetDbHelper.YEARLY_INCOME_TABLE_NAME;
        } else if (income_outgoing == Detail.DETAIL_OUTGOING) {
            table_name = AssetDbHelper.YEARLY_OUTGOING_TABLE_NAME;
        }

        cursor = db.query(
                table_name, columns,
                "date <= " + today, null, null, null, null, null);

        count = cursor.getCount();
        cursor.moveToFirst();

        for(i = 0; i < count; i++) {
            date = cursor.getInt(AssetDbHelper.INDEX_DATE);

            while (today >= date) {
                record = new AssetRecord();

                record.title_str = title[2];

                record.year_month_day = SELECT_YEAR;

                record.date_int = date;
                record.date_str = date_int_to_string(date);

                record.money_int = cursor.getInt(AssetDbHelper.INDEX_MONEY);
                record.money_str = Integer.toString(record.money_int);

                record.note = cursor.getString(AssetDbHelper.INDEX_NOTE);

                record.total_instalment = cursor.getInt(AssetDbHelper.INDEX_NUM);

                record_list.add(record);

                date = date_add(date, 1, 0);
            }

            cursor.moveToNext();
        }

        cursor.close();


        // read instalment table
        if (income_outgoing == Detail.DETAIL_OUTGOING) {
            table_name = AssetDbHelper.INSTALMENT_OUTGOING_TABLE_NAME;

            cursor = db.query(
                    table_name, columns,
                    "date <= " + today, null, null, null, null, null);

            count = cursor.getCount();
            cursor.moveToFirst();

            for (i = 0; i < count; i++) {
                int total_instalment = cursor.getInt(AssetDbHelper.INDEX_NUM);
                int num = total_instalment;

                date = cursor.getInt(AssetDbHelper.INDEX_DATE);
                int start_date = date;
                int end_date = date_add(start_date, 0, num);

                current_instalment = 1;
                do {
                    record = new AssetRecord();

                    record.title_str = title[3];

                    record.year_month_day = SELECT_INSTALMENT;

                    record.total_instalment = total_instalment;
                    record.current_instalment = current_instalment;

                    record.date_int = date_add(date, 0, current_instalment - 1);
                    record.date_str = date_int_to_string(record.date_int);

                    record.start_date_int = start_date;
                    record.start_date_str = date_int_to_string(start_date);

                    record.end_date_int = end_date;
                    record.end_date_str = date_int_to_string(end_date);

                    record.money_int =
                            cursor.getInt(AssetDbHelper.INDEX_MONEY) / cursor.getInt(AssetDbHelper.INDEX_NUM);
                    if(current_instalment == 1) {
                        record.money_int +=
                                cursor.getInt(AssetDbHelper.INDEX_MONEY) % cursor.getInt(AssetDbHelper.INDEX_NUM);
                    }
                    record.money_str = Integer.toString(record.money_int);

                    record.note = cursor.getString(AssetDbHelper.INDEX_NOTE);

                    record_list.add(record);

                    num--;
                    current_instalment++;

                } while(num > 0 && today >= date_add(date, 0, current_instalment - 1));

                cursor.moveToNext();
            }

            cursor.close();
        }

        return record_list;
    }

    public int get_current_asset() {
        int today;

        Calendar cal = Calendar.getInstance();

        today = (cal.get(Calendar.YEAR) * 10000);
        today += ((cal.get(Calendar.MONTH) + 1) * 100);
        today += cal.get(Calendar.DAY_OF_MONTH);

        return get_asset(today);
    }

    public int get_asset(int date) {

        int total = 0;

        total += get_money_until_date(date, AssetDbHelper.ONETIME_INCOME_TABLE_NAME, SELECT_DAY);
        total += get_money_until_date(date, AssetDbHelper.MONTHLY_INCOME_TABLE_NAME, SELECT_MONTH);
        total += get_money_until_date(date, AssetDbHelper.YEARLY_INCOME_TABLE_NAME, SELECT_YEAR);

        total -= get_money_until_date(date, AssetDbHelper.ONETIME_OUTGOING_TABLE_NAME, SELECT_DAY);
        total -= get_money_until_date(date, AssetDbHelper.MONTHLY_OUTGOING_TABLE_NAME, SELECT_MONTH);
        total -= get_money_until_date(date, AssetDbHelper.YEARLY_OUTGOING_TABLE_NAME, SELECT_YEAR);
        total -= get_money_until_date(date, AssetDbHelper.INSTALMENT_OUTGOING_TABLE_NAME, SELECT_INSTALMENT);

        return total;
    }
}