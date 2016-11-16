package com.example.chenxiaojun.chabaike08v2.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenxiaojun.chabaike08v2.R;
import com.example.chenxiaojun.chabaike08v2.adapter.MyFragmentPagerAdapter;
import com.example.chenxiaojun.chabaike08v2.fragment.BaseFragment;
import com.example.chenxiaojun.chabaike08v2.fragment.Fragment_TT;
import com.example.chenxiaojun.chabaike08v2.url.GetUri;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 权限请求码
    private final int REQUEST_CODE = 1;

    private List<BaseFragment> fragments;

    private ViewPager viewPager;

    private LinearLayout tab;
    private LinearLayout tab1;
    private String uri;

    private SlidingMenu slidingMenu;

    //网址数组
    private String[] uris = new String[]{GetUri.getToutiaoUri(0),GetUri.getBaikeUri(0),GetUri.getZixunUri(0),GetUri.getJingyinUri(0),GetUri.getShujuUri(0)};

    //搜索的地址
    private String searchUri;
    //搜索关键字
    private String getSearch;
    //搜索栏
    private EditText etSearch;
    private ImageView ivDeleteText;

    private ImageView imageViewMore;
    private Button button;
    private ImageView backImageView;
    private ImageView homeImageView;
    private TextView banQuanTextView;
    private TextView yiJianTextView;
    private TextView shouCangTextView;
    private TextView jiLuTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.activity_main_viewpagerId);
        tab1 = (LinearLayout) findViewById(R.id.activity_main_top_linearLayoutId);
        tab = (LinearLayout) findViewById(R.id.activity_main_top1_linearLayoutId);
        imageViewMore = (ImageView) findViewById(R.id.activity_main_more_imageview);

        imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.showSecondaryMenu();
            }
        });

        //设置侧滑菜单
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffset(200);
        slidingMenu.setFadeDegree(0.7f);
        slidingMenu.setShadowWidth(10);
        slidingMenu.setShadowDrawable(new ColorDrawable(Color.rgb(200, 200, 200)));
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //设置侧滑菜单搜索
        slidingMenu.setMenu(R.layout.activity_slidingmenu);
        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        etSearch = (EditText) findViewById(R.id.etSearch);
        button = (Button) findViewById(R.id.btnSearch);
        backImageView = (ImageView) findViewById(R.id.activity_main_back_imageview);
        homeImageView = (ImageView) findViewById(R.id.activity_main_home_imageview);
        banQuanTextView = (TextView) findViewById(R.id.tvBanQuan);
        yiJianTextView = (TextView) findViewById(R.id.tvYiJian);
        shouCangTextView = (TextView) findViewById(R.id.tvShouCang);
        jiLuTextView = (TextView) findViewById(R.id.tvJilu);
        jiLuTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,JiLuActivity.class);
                startActivity(intent);
            }
        });
        shouCangTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ShouCangActivity.class);
                startActivity(intent);
            }
        });
        banQuanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BanQuanActivity.class);
                startActivity(intent);
            }
        });
        yiJianTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,YiJianActivity.class);
                startActivity(intent);
            }
        });


        //点击后退关闭抽屉
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.showContent();
            }
        });
        //点击主页面回到主页面
        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.showContent();
            }
        });
        ivDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        //搜索点击事件
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("getSearch",getSearch);
                intent.putExtra("searchUri",searchUri);
                startActivity(intent);
            }
        });

        //监听输入关键字的时候显示删除按钮
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //文本框内容改变时调用
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //得到关键字搜索uri
                getSearch = etSearch.getText().toString();
                searchUri = GetUri.getSearchUri(getSearch);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0){
                    ivDeleteText.setVisibility(View.GONE);
                }else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });





        //初始化viewpage中的fragment
        fragments = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            fragments.add(Fragment_TT.newInstance(uris[i]));
        }
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(pagerAdapter);
        //设置当前viewpager
        viewPager.setCurrentItem(0);
        //不设置的话会报异常
        viewPager.setOffscreenPageLimit(5);

        //初始化Tab标签
        initTab();
        initTabTitle();
    }

    private void initTabTitle() {
        for (int i = 0; i < tab1.getChildCount()-1; i++) {
            final int finalI = i;
            tab1.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(finalI);
                }
            });
        }
    }
    private void initTab() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("offset",""+positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //选择默认的选项
        selectTab(0);
    }

    private void selectTab(int position) {
        for (int i = 0; i < tab.getChildCount(); i++) {
            View view = tab.getChildAt(i);
            if(position == i)
            {
                view.setBackgroundColor(Color.GREEN);
            }
            else
            {
                view.setBackgroundColor(Color.WHITE);
            }
        }
    }

    private int backStep = 0;

    //重写back建
    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.showContent();
        } else {
            if (backStep == 0) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2 * 1000);
                            backStep = 0;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (backStep == 1) {
                super.onBackPressed();
            }
            backStep++;
        }
    }


    // Android6.0系统申请读写外部存储的运行时权限（该方法在需要操作外部存储时调用，或在相关初始化操作中调用）
    private void requestPermissions() {
        // 如果未获得外部存储读写权限，则申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    // 请求运行时权限的回调方法
    // requestCode：用于识别申请权限的请求码
    // permissions：请求的权限
    // grantResults：对应权限的请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 允许权限
                    Toast.makeText(MainActivity.this, "允许权限", Toast.LENGTH_SHORT).show();
                } else {
                    // 拒绝权限
                    Toast.makeText(MainActivity.this, "拒绝权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
