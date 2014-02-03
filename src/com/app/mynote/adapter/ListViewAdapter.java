package com.app.mynote.adapter;

import com.app.mynote.MainActivity;
import com.app.mynote.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ListViewAdapter extends BaseAdapter 
{
    Context context;
    String[] rank;
    int flag;
    LayoutInflater inflater;
 
    public ListViewAdapter(Context context, String[] rank, int flag) 
    {
        this.context = context;
        this.rank = rank;
        this.flag = flag;
    }
 
    @Override
    public int getCount() 
    {
        return rank.length;
    }
 
    @Override
    public Object getItem(int position) 
    {
        return null;
    }
 
    @Override
    public long getItemId(int position) 
    {
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        // Declare Variables
        TextView txtDate;
        ImageView imgOpen;
 
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View itemView = inflater.inflate(R.layout.listview_item, parent, false);        
 
        // Locate the TextViews in listview_item.xml
        txtDate = (TextView) itemView.findViewById(R.id.date);
        // Locate the ImageView in listview_item.xml
        imgOpen = (ImageView) itemView.findViewById(R.id.image);
 
        // Capture position and set to the TextViews
        txtDate.setText(rank[position]);
 
        // Capture position and set to the ImageView
        imgOpen.setImageResource(flag);
        
        if (position == MainActivity.counter) 
        {
        	itemView.setBackgroundColor(Color.rgb(32, 136, 178));
        } 
        else 
        {
        	itemView.setBackgroundColor(Color.rgb(216, 110, 63));
        }
 
        return itemView;
    }
}