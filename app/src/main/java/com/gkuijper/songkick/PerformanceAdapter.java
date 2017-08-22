package com.gkuijper.songkick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gabrielle on 21-08-17.
 */

public class PerformanceAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Performance> currentList;

    public PerformanceAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Performance> PerformanceArrayList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.currentList = PerformanceArrayList;
    }

    @Override
    public int getCount() {
        return currentList.size();
    }

    @Override
    public Performance getItem(int position) {
        return this.currentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PerformanceAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_performance_row, null);

            viewHolder = new PerformanceAdapter.ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name_row_id);
            viewHolder.billing = (TextView) convertView.findViewById(R.id.billing_id);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PerformanceAdapter.ViewHolder) convertView.getTag();
        }
        final Performance performance = currentList.get(position);
        viewHolder.name.setText(performance.getName());
        viewHolder.billing.setText(performance.getBilling());

        return convertView;
    }

    private static class ViewHolder {
        private TextView name, billing;
    }

}
