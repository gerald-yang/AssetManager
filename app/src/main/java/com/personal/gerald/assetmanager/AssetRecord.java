package com.personal.gerald.assetmanager;

/**
 * Created by gerald on 2015/1/5.
 */
public class AssetRecord implements Comparable{
    public String table_name;
    public int row_id;
    public String title_str;
    public int year_month_day;
    public int date_int;
    public String date_str;
    public int money_int;
    public String money_str;
    public String note;
    public int total_instalment;
    public int current_instalment;
    public int start_date_int;
    public String start_date_str;
    public int end_date_int;
    public String end_date_str;


    @Override
    public int compareTo(Object another) {

        /* compare the class by date, for collections.sort */
        AssetRecord o = (AssetRecord) another;

        if(date_int > o.date_int)
            return 1;
        else if(date_int < o.date_int)
            return -1;
        else
            return 0;
    }
}
