package com.example.chenxiaojun.chabaike08v2.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by my on 2016/11/14.
 */
public class ThumbnailUtils {
    //无压缩
    public static Bitmap getBitmap(String imgUrl){
        InputStream is;
        Bitmap bitmap = null;
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.connect();
            if (coon.getResponseCode() == 200) {
                is = coon.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            }
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    // 对图像数据源采样
    public static Bitmap getThumbnailBitmap(byte[] data, int sampleSize) {
        // 第一次采样：目的是只获取图片的宽度和高度，并不希望获得图片像素点的全部数据
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 第一次采样，只采图片的边界
        options.inJustDecodeBounds = true;
        // 开始执行第一次采样
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // 设置压缩比例
        options.inSampleSize = sampleSize;
        // 图片进行第二次采样，既要采集图片的边界信息，又需要采集图片像素点的数据
        options.inJustDecodeBounds = false;
        Bitmap thumbnailBitmap = BitmapFactory
                .decodeByteArray(data, 0, data.length, options);
        return thumbnailBitmap;
    }

    // 对Bitmap对象采样，压缩倍数只有为2的n次方倍数时有效
    public static Bitmap getThumbnailBitmap(Bitmap bitmap, int sampleSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        BitmapFactory.Options options = new BitmapFactory.Options();
        // 2.解码边缘
        options.inJustDecodeBounds = true;
        // 3进行图片解码
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = sampleSize;
        // 4.锁住边缘
        options.inJustDecodeBounds = false;
        // 5.通过参数获得新的位图
        Bitmap thumbnailBitmap = BitmapFactory
                .decodeByteArray(data, 0, data.length, options);
        return thumbnailBitmap;
    }

    // 对本地图片进行二次采样
    public static Bitmap getThumbnailBitmap(String pathName, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        Bitmap thumbnailBitmap = BitmapFactory.decodeFile(pathName, options);
        return thumbnailBitmap;
    }

}