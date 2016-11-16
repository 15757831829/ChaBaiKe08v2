package com.example.chenxiaojun.chabaike08v2.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chenxiaojun.chabaike08v2.R;
import com.example.chenxiaojun.chabaike08v2.adapter.TTAdapter;
import com.example.chenxiaojun.chabaike08v2.adapter.TTHeaderPagerAdapter;
import com.example.chenxiaojun.chabaike08v2.bean.TTEntity;
import com.example.chenxiaojun.chabaike08v2.bean.TTHeaderEntity;
import com.example.chenxiaojun.chabaike08v2.database.DatabasedeHelper;
import com.example.chenxiaojun.chabaike08v2.ui.ContentActivity;
import com.example.chenxiaojun.chabaike08v2.url.GetUri;
import com.example.chenxiaojun.chabaike08v2.utils.MyHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2016/11/14.
 */
public class Fragment_TT extends BaseFragment {

    //数据库
    private DatabasedeHelper helper;
    private static SQLiteDatabase db;
    private static Cursor cursor;
    private android.app.LoaderManager loaderManager;
    // Loader分配的id
    private int LOADER_ID = 1;

    private SwipeRefreshLayout swipeRefresh;

    private String uri;
    private ListView listView;
    private TTAdapter ttAdapter;

    //头条数据实体类集合
    private List<TTEntity.Data> entityDatas;
    //头条头布局数据实体类
    private List<TTHeaderEntity.Data> headerEntityDatas;

    private List<TTEntity.Data> currDatas;

    private View headerView;
    private ViewPager viewPager;
    private LinearLayout linearLayout;

    private View bottomView;
    private Button moreButton;
    private int curr = 0;
    private List<Bitmap> bitmapList;
    //点数组
    private int[] pointIds = new int[]{R.id.fragment_tt_header_point1_imageviewId, R.id.fragment_tt_header_point2_imageviewId, R.id.fragment_tt_header_point3_imageviewId};
    private TTHeaderPagerAdapter ttHeaderPagerAdapter;
    List<ImageView> imgList;
    List<ImageView> pointList;

    public Fragment_TT() {
    }

    public static Fragment_TT newInstance(String uri) {

        Bundle args = new Bundle();
        args.putString("uri", uri);
        Fragment_TT fragment = new Fragment_TT();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        uri = bundle.getString("uri");

        imgList = new ArrayList<>();
        pointList = new ArrayList<>();
        bitmapList = new ArrayList<>();
        entityDatas = new ArrayList<>();
        headerEntityDatas = new ArrayList<>();
        currDatas = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_tt, container, false);
        //查找控件
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_fragment_tt_swiperefreshId);
        listView = (ListView) rootView.findViewById(R.id.fragment_tt_listview);
        //实例化适配器
        ttAdapter = new TTAdapter(getContext());
        //判断是否为第一个fragment
        if (uri.equals(GetUri.getToutiaoUri(0))) {
            //添加头布局
            headerView = inflater.inflate(R.layout.fragment_tt_litview_header, listView, false);
            //初始化头布局
            initHeaderView();
            //添加头布局
            listView.addHeaderView(headerView);
        }

        bottomView = inflater.inflate(R.layout.fragment_tt_litview_foot, listView, false);
        initBottomView();
        listView.addFooterView(bottomView);

        //设置适配器
        listView.setAdapter(ttAdapter);


        //设置刷新样式
        swipeRefresh.setColorSchemeColors(new int[]{Color.GREEN, Color.GRAY, Color.DKGRAY});
        //设置刷新监听
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ttAdapter.clear();
                entityDatas.clear();
                currDatas.clear();
                curr = 0;

                loadData(uri);
            }
        });

        //加载数据
        loadData(uri);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                if (uri.equals(GetUri.getToutiaoUri(0))) {
                    position -= 1;
                }
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("id", entityDatas.get(position).getId());
                intent.putExtra("wap_thumb", entityDatas.get(position).getWap_thumb());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getContext())
                        .setTitle("确认")
                        .setMessage("确定吗？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (uri.equals(GetUri.getToutiaoUri(0))) {
                                    currDatas.remove(position - 1);
                                } else {
                                    currDatas.remove(position);
                                }
                                listView.invalidate();
                                ttAdapter.clear();
                                ttAdapter.addAll(currDatas);

                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
                return true;
            }
        });

        return rootView;
    }


    private void initBottomView() {
        moreButton = (Button) bottomView.findViewById(R.id.loadmore);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uri.equals(GetUri.getToutiaoUri(0))) {
                    loadData(GetUri.getToutiaoUri(++curr));
                    Toast.makeText(getContext(), "" + curr, Toast.LENGTH_SHORT).show();
                } else if (uri.equals(GetUri.getBaikeUri(0))) {
                    loadData(GetUri.getBaikeUri(++curr));
                    Toast.makeText(getContext(), "" + curr, Toast.LENGTH_SHORT).show();
                } else if (uri.equals(GetUri.getZixunUri(0))) {
                    loadData(GetUri.getZixunUri(++curr));
                    Toast.makeText(getContext(), "" + curr, Toast.LENGTH_SHORT).show();
                } else if (uri.equals(GetUri.getJingyinUri(0))) {
                    loadData(GetUri.getJingyinUri(++curr));
                    Toast.makeText(getContext(), "" + curr, Toast.LENGTH_SHORT).show();
                } else if (uri.equals(GetUri.getShujuUri(0))) {
                    loadData(GetUri.getShujuUri(++curr));
                    Toast.makeText(getContext(), "" + curr, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //初始化头布局
    private void initHeaderView() {
        //查找头布局控件
        viewPager = (ViewPager) headerView.findViewById(R.id.fragment_tt_header_viewpagerId);
        linearLayout = (LinearLayout) headerView.findViewById(R.id.fragment_tt_header_linearlayoutId);
        //下载头布局图片
        loadeHeaderImage();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectionIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //设置滑动改变点
    private void selectionIndicator(int position) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) linearLayout.getChildAt(i);
            if (position == i) {
                imageView.setImageResource(R.mipmap.dot);
            } else {
                imageView.setImageResource(R.mipmap.dot_1);
            }
        }
    }

    //下载头布局中的图片
    private void loadeHeaderImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ttHeaderJsonString = MyHttpUtils.getStringFromUrl(GetUri.HEADER_URI);
                try {
                    JSONObject jsonObject = new JSONObject(ttHeaderJsonString);
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.optJSONObject(i);
                        TTHeaderEntity.Data headerEntityData = new TTHeaderEntity().new Data(jsonObject);
                        headerEntityDatas.add(headerEntityData);
                        String imageUri = headerEntityDatas.get(i).getImage();

                        Bitmap bitmap = MyHttpUtils.getBitmapFromUrl(imageUri);
                        bitmapList.add(bitmap);

                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < bitmapList.size(); i++) {
                                //初始化头布局图片
                                ImageView imageView = new ImageView(getContext());
                                imageView.setImageBitmap(bitmapList.get(i));
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                imgList.add(imageView);
                            }

                            //设置默认选中的点
                            ImageView pointImageView = (ImageView) linearLayout.getChildAt(0);
                            pointImageView.setImageResource(R.mipmap.dot);
                            //实例化viewpager适配器
                            ttHeaderPagerAdapter = new TTHeaderPagerAdapter(imgList);
                            viewPager.setAdapter(ttHeaderPagerAdapter);
                            viewPager.setCurrentItem(0);
                            viewPager.setOffscreenPageLimit(3);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //加载fragment数据
    private void loadData(final String uri) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) getContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = mConnectivityManager.getActiveNetworkInfo();
                if (ni != null && ni.isConnectedOrConnecting()) {
                    String ttJsonString = MyHttpUtils.getStringFromUrl(uri);
                    try {
                        JSONObject jsonObject = new JSONObject(ttJsonString);
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.optJSONObject(i);
                            TTEntity.Data entityData = new TTEntity().new Data(jsonObject);
                            entityDatas.add(entityData);
                            addInDatabaseHuanChu(entityData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    helper = new DatabasedeHelper(getContext());
                    db = helper.getReadableDatabase();
                    cursor = db.query(DatabasedeHelper.TABLE_NAME_HUAN, null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        String ids = cursor.getString(cursor.getColumnIndex("id"));
                        int id = Integer.parseInt(ids);
                        String title = cursor.getString(cursor.getColumnIndex("id"));
                        String source = cursor.getString(cursor.getColumnIndex("source"));
                        String description = cursor.getString(cursor.getColumnIndex("description"));
                        String wap_thumb = cursor.getString(cursor.getColumnIndex("wap_thumb"));
                        String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
                        String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                        String wap_content = cursor.getString(cursor.getColumnIndex("wap_content"));
                        TTEntity.Data sqlData = new TTEntity().new Data(id, title, source, description, wap_thumb, create_time, nickname, wap_content);
                        entityDatas.add(sqlData);
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ttAdapter.addAll(entityDatas);
                        currDatas.addAll(entityDatas);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void addInDatabaseHuanChu(TTEntity.Data data) {
        ContentValues values = new ContentValues();
        values = new ContentValues();
        values.put("title", data.getTitle());
        values.put("create_time",data.getCreate_time());
        values.put("source",data.getSource());
        values.put("wap_thumb",data.getWap_thumb());
        values.put("_id",data.getId());

    }


}