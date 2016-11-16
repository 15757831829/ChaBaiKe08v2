package com.example.chenxiaojun.chabaike08v2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenxiaojun.chabaike08v2.R;
import com.example.chenxiaojun.chabaike08v2.utils.ThumbnailUtils;

/**
 * Created by my on 2016/11/14.
 */
public class MyCursorAdapter extends CursorAdapter {

    private int titleIndex;
    private int create_timeIndex;
    private int sourceIndex;
    private int wap_thumbIndex;
    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.titleIndex = c.getColumnIndex("title");
        this.create_timeIndex = c.getColumnIndex("create_time");
        this.sourceIndex = c.getColumnIndex("source");
        this.wap_thumbIndex = c.getColumnIndex("wap_thumb");


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity_shou_cang_listview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        String title = cursor.getString(titleIndex);
        String create_time = cursor.getString(create_timeIndex);
        String source = cursor.getString(sourceIndex);
        final String wap_thumb = cursor.getString(wap_thumbIndex);
        viewHolder.titleTextView.setText(title);
        viewHolder.sourceTextView.setText(source);
        viewHolder.create_timeTextView.setText(create_time);
        viewHolder.imageView.setTag(wap_thumb);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = ThumbnailUtils.getBitmap(wap_thumb);
                Log.d("1606",wap_thumb);
                viewHolder.imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (viewHolder.imageView.getTag().equals(wap_thumb)){
                            viewHolder.imageView.setImageBitmap(bitmap);
                        }
                    }
                });
            }
        }).start();
    }

    public static class ViewHolder{
        TextView titleTextView;
        TextView sourceTextView;
        TextView create_timeTextView;
        ImageView imageView;
        public ViewHolder(View convertView){
            titleTextView = (TextView) convertView.findViewById(R.id.item_activity_shou_cang_listview_title_textview);
            sourceTextView = (TextView) convertView.findViewById(R.id.item_activity_shou_cang_listview_source_textview);
            create_timeTextView = (TextView) convertView.findViewById(R.id.item_activity_shou_cang_listview_creat_time_textview);
            imageView = (ImageView) convertView.findViewById(R.id.item_activity_shou_cang_listview_wap_thumb_imageview);
        }
    }
}

