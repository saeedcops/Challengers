package com.cops.challengers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cops.challengers.R;

import java.util.ArrayList;
import java.util.List;

public class LangSpinnerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> lang = new ArrayList();

    public LangSpinnerAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        lang.add("English");
        lang.add("عربي");
    }

    @Override
    public int getCount() {
        return lang.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.item_spinner_lang, null);

        TextView names = (TextView) convertView.findViewById(R.id.item_spinner_tv);
        names.setText(lang.get(position));

        return convertView;
    }
}
