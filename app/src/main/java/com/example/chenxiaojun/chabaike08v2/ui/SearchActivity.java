package com.example.chenxiaojun.chabaike08v2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chenxiaojun.chabaike08v2.R;
import com.example.chenxiaojun.chabaike08v2.adapter.TTAdapter;
import com.example.chenxiaojun.chabaike08v2.bean.TTEntity;
import com.example.chenxiaojun.chabaike08v2.utils.MyHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2016/11/14.
 */
public class SearchActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView backImageView;
    private ImageView homeImageView;
    private ListView listView;
    private TTAdapter ttAdapter;


    private List<TTEntity.Data> entityDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        textView = (TextView) findViewById(R.id.searchActivity_textview);
        backImageView = (ImageView) findViewById(R.id.searchActivity_back_imageview);
        homeImageView = (ImageView) findViewById(R.id.searchActivity_home_imageview);
        listView = (ListView) findViewById(R.id.searchActivity_listview);

        final Intent intent = getIntent();
        String searchUri = intent.getStringExtra("searchUri");
        String getSearch = intent.getStringExtra("getSearch");
        textView.setText(getSearch);

        //后退
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //返回主页面
        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //设置适配器
        ttAdapter = new TTAdapter(this);
        //绑定适配器
        listView.setAdapter(ttAdapter);
        //加载数据
        loadData(searchUri);
    }

    //加载数据
    private void loadData(final String uri) {
        entityDatas = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ttJsonString = MyHttpUtils.getStringFromUrl(uri);
                try {
                    JSONObject jsonObject = new JSONObject(ttJsonString);
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.optJSONObject(i);
                        TTEntity.Data entityData = new TTEntity().new Data(jsonObject);
                        entityDatas.add(entityData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ttAdapter.addAll(entityDatas);
                    }
                });
            }
        }).start();
    }
}

