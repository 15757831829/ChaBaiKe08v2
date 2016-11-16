package com.example.chenxiaojun.chabaike08v2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenxiaojun.chabaike08v2.R;
import com.example.chenxiaojun.chabaike08v2.bean.TTEntity;
import com.example.chenxiaojun.chabaike08v2.utils.ThumbnailUtils;

/**
 * Created by my on 2016/11/14.
 */
public class TTAdapter extends MBaseAdapter<TTEntity.Data>{
    Handler handler = new Handler();

    public TTAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.item_fragment_tt, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title_tv.setText(getItem(position).getTitle());
        viewHolder.source_tv.setText(getItem(position).getSource());
        viewHolder.description_tv.setText(getItem(position).getDescription());
        viewHolder.nickname_tv.setText(getItem(position).getNickname());
        viewHolder.create_time_tv.setText(getItem(position).getCreate_time());
        final String ttUri = getItem(position).getWap_thumb();
        viewHolder.wap_thumb_iv.setTag(ttUri);
        viewHolder.wap_thumb_iv.setImageResource(R.mipmap.defaultcovers);

        if (getLruCache().get(ttUri) == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = ThumbnailUtils.getBitmap(ttUri);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (ttUri.equals(viewHolder.wap_thumb_iv.getTag())) {
                                viewHolder.wap_thumb_iv.setImageBitmap(bitmap);
                            }

                        }
                    });
                    if (bitmap != null) {
                        getLruCache().put(ttUri, bitmap);
                    }
                }
            }).start();
        } else {
            viewHolder.wap_thumb_iv.setImageBitmap(getLruCache().get(ttUri));
        }

        return convertView;
    }

    public class ViewHolder {
        private TextView title_tv, source_tv, nickname_tv, create_time_tv, description_tv;
        private ImageView wap_thumb_iv;

        public ViewHolder(View convertView) {
            wap_thumb_iv = (ImageView) convertView.findViewById(R.id.item_fragment_tt_wap_thumb_imageview);
            title_tv = (TextView) convertView.findViewById(R.id.item_fragment_tt_title_textview);
            source_tv = (TextView) convertView.findViewById(R.id.item_fragment_tt_source_textview);
            description_tv = (TextView) convertView.findViewById(R.id.item_fragment_tt_description_textview);
            nickname_tv = (TextView) convertView.findViewById(R.id.item_fragment_tt_nickname_textview);
            create_time_tv = (TextView) convertView.findViewById(R.id.item_fragment_tt_creat_time_textview);
        }
    }
}
