package com.example.anan.AAChartCore.ChartsDemo.MainContent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.anan.AAChartCore.R;

import java.util.List;

public class GameRecordAdapter extends BaseAdapter {

    private List<String> mData;
    private Context mContext;

    public GameRecordAdapter(List<String> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.head2head_list_item,parent,false);

        TextView txt_aName = (TextView) convertView.findViewById(R.id.game_list_item);
        txt_aName.setText(mData.get(position));

        return convertView;
    }
}
