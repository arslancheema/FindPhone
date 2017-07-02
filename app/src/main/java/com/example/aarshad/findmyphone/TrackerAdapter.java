package com.example.aarshad.findmyphone;

/**
 * Created by aarshad on 7/2/17.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TrackerAdapter extends ArrayAdapter<AdapterItems> {

    public TrackerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TrackerAdapter(Context context, int resource, List<AdapterItems> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.single_row_contact, null);
        }
        AdapterItems item = getItem(position);

        if (item != null) {

            TextView userName = (TextView) v.findViewById(R.id.tvUserName);
            TextView phoneNumber = (TextView) v.findViewById(R.id.tvPhoneNumber);

            if (userName != null) {
                userName.setText(item.userName);
            }
            if (phoneNumber != null) {
                phoneNumber.setText(item.phoneNumber);
            }
        }
        return v;
    }

}