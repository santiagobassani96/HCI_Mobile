package com.bassanidevelopment.santiago.hci_movil.Model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bassanidevelopment.santiago.hci_movil.R;

import java.util.List;

public class SimpleListAdapter extends ArrayAdapter<SimpleList> {
    Context context;
    public SimpleListAdapter(@NonNull Context context, int resource, @NonNull List<SimpleList> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    private class ViewHolder{
        TextView name;
        ImageView img;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        SimpleList rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.simple_list_view, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.img= (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.name.setText(rowItem.getName());

        return convertView;
    }
}
