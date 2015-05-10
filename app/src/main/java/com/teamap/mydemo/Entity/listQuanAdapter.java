package com.teamap.mydemo.Entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamap.mydemo.R;

import java.util.List;

/**
 * Created by Gaming on 5/10/2015.
 */
public class listQuanAdapter extends ArrayAdapter<tblQuan> {

    private final Context context;
    private final List<tblQuan> data;

    public listQuanAdapter(Context context, List<tblQuan> data) {
        super(context, R.layout.listquan);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listquan, parent,
                false);
        TextView tvTenquan=(TextView) rowView.findViewById(R.id.tvTenquan);
        TextView tvDiachi=(TextView) rowView.findViewById(R.id.tvDiachi);
        TextView tvLuotthich=(TextView) rowView.findViewById(R.id.tvLuotthich);
        TextView tvLuotden=(TextView) rowView.findViewById(R.id.tvLuotden);
        ImageView ivQuan=(ImageView) rowView.findViewById(R.id.ivQuan);
        return rowView;
    }
}
