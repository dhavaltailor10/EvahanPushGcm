package com.evahanpushgcm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by new-3 on 7/21/2016.
 */
public class ListTrakingAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public ListTrakingAdapter(Activity a, ArrayList<HashMap<String, String>> sdata) {
        activity = a;
        this.data = sdata;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item_display_detail, null);

        TextView textViewtime = (TextView) vi.findViewById(R.id.texttrakingtime);
        TextView textViewstatus = (TextView) vi.findViewById(R.id.texttrackingstatus);

        // TextView output=(TextView)vi.findViewById(R.id.outputdata);

        textViewtime.setText(data.get(position).get("ttime"));
        textViewstatus.setText(data.get(position).get("tstatus"));

        return vi;
    }
}
