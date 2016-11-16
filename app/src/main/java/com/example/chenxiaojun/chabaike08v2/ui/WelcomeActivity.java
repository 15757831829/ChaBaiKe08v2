package com.example.chenxiaojun.chabaike08v2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.chenxiaojun.chabaike08v2.R;
import com.example.chenxiaojun.chabaike08v2.adapter.WelcomePageAdapter;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    private Button btnImageView;
    //图片集合
    private List<ImageView> imgList;
    private int[] imgIds = new int[]{R.mipmap.slide1,R.mipmap.slide2,R.mipmap.slide3};

    //点集合
    private List<ImageView> pointList;
    private int[]pointIds = new int[]{R.id.point01_iv,R.id.point02_iv,R.id.point03_iv};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        viewPager = (ViewPager) findViewById(R.id.welcome_view_pager);
        btnImageView = (Button) findViewById(R.id.welcome_btn_imageview);

        //创建视图
        imgList = new ArrayList<>();
        pointList = new ArrayList<>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imgIds[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgList.add(imageView);
            //初始化点
            ImageView pointImageView = (ImageView) findViewById(pointIds[i]);
            pointList.add(pointImageView);

        }
        // 默认第一个点为选中状态
        pointList.get(0).setSelected(true);

        WelcomePageAdapter adapter = new WelcomePageAdapter(imgList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        btnImageView.setVisibility(position == pointList.size()-1? View.VISIBLE:View.GONE);
        btnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        for (int i = 0; i < pointList.size() ; i++) {
            pointList.get(i).setSelected(i == position);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.removeOnPageChangeListener(this);
    }
}
