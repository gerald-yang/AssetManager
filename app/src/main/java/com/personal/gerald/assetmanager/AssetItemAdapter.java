package com.personal.gerald.assetmanager;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by gerald on 2015/1/19.
 */
public class AssetItemAdapter extends ArrayAdapter<String> {

    public AssetItemAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
    }

}
