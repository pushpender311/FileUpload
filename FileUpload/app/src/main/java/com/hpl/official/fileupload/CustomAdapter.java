package com.hpl.official.fileupload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pushpender Bhandari on 2/5/2016.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    ArrayList<String> Title;
    ArrayList<String> Size;
    ArrayList<String> Type;
    ArrayList<String> Date;

    public CustomAdapter(Context context, ArrayList<String> arrayList, ArrayList<String> arrayList1, ArrayList<String> arrayList2, ArrayList<String> arrayList3) {
        super(context, R.layout.list_row, arrayList);
        Title = arrayList;
        Size = arrayList1;
        Type = arrayList2;
        Date = arrayList3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        ViewHolder viewHolder;
        if (convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) row.findViewById(R.id.title);
            viewHolder.size = (TextView) row.findViewById(R.id.size);
            viewHolder.type = (TextView) row.findViewById(R.id.type);
            viewHolder.date = (TextView) row.findViewById(R.id.date);
            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }
        String title = Title.get(position);
        String size = Size.get(position);
        String type = Type.get(position);
        String date = Date.get(position);
        String titles = title.replace("uploads\\", "").replace("uploads/", "");

        viewHolder.title.setText(titles.toUpperCase());
        viewHolder.size.setText(size.toUpperCase());
        viewHolder.type.setText(type.toUpperCase() + " File");
        viewHolder.date.setText(date);
        return row;
    }

    public class ViewHolder {
        TextView title;
        TextView size;
        TextView type;
        TextView date;
    }
}
