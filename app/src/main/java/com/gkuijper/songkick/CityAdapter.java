package com.gkuijper.songkick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gabrielle on 06-08-17.
 */

public class CityAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<City> currentList;

    public CityAdapter(Context context, LayoutInflater layoutInflater, ArrayList<City> CityArrayList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.currentList = CityArrayList;
    }
    @Override
    public int getCount() {
        return currentList.size();
    }

    @Override
    public City getItem(int position) {return this.currentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CityAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_city_row, null);

            viewHolder = new CityAdapter.ViewHolder();
            viewHolder.land = (TextView) convertView.findViewById(R.id.Land_row_id);
            viewHolder.stad = (TextView) convertView.findViewById(R.id.Stad_row_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CityAdapter.ViewHolder) convertView.getTag();
        }
        final City city = currentList.get(position);

        viewHolder.land.setText(city.getCountry());
        viewHolder.stad.setText(city.getName());


        return convertView;
    }

    private static class ViewHolder {
        private TextView land, stad;
    }
}
