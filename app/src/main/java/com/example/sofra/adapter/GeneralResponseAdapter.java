package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.sofra.R;
import com.example.sofra.data.model.publicData.RegionData;

import java.util.List;

public class GeneralResponseAdapter extends BaseAdapter {

    Context context;
    private List<RegionData> generalResponseDataList;
    private LayoutInflater inflater;

    public GeneralResponseAdapter(Context context, List<RegionData> generalResponseDataList, String hint) {
        this.context = context;
        this.generalResponseDataList = generalResponseDataList;
        inflater = (LayoutInflater.from(context));
        generalResponseDataList.add(new RegionData(0, hint));
    }

    @Override
    public int getCount() {
        return generalResponseDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.item_spinner_auth, null);
        TextView names = view.findViewById(R.id.text_view);
        names.setText(generalResponseDataList.get(position).getName());
        names.setTextColor(ContextCompat.getColor(context, R.color.black));
        return view;
    }
}
