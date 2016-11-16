package com.example.chenxiaojun.chabaike08v2.ui;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chenxiaojun.chabaike08v2.R;
import com.example.chenxiaojun.chabaike08v2.adapter.MyCursorAdapter;
import com.example.chenxiaojun.chabaike08v2.database.DatabasedeHelper;

public class JiLuActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //数据库
    private DatabasedeHelper helper;
    private static SQLiteDatabase db;
    private static Cursor cursor;
    private android.app.LoaderManager loaderManager;
    // Loader分配的id
    private int LOADER_ID = 1;

    private ListView listView;
    private MyCursorAdapter adapter;

    private ImageView backImageView;
    private ImageView homeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ji_lu);

        listView = (ListView) findViewById(R.id.activity_jilu_listview);
        //初始化数据库
        helper = new DatabasedeHelper(this);
        db = helper.getReadableDatabase();

        cursor = db.query(DatabasedeHelper.TABLE_NAME_JI, null, null, null, null, null, null);
        //设置适配器
        adapter = new MyCursorAdapter(this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        //绑定适配器
        listView.setAdapter(adapter);
        //listview点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Intent intent = new Intent(JiLuActivity.this, ContentActivity.class);
                intent.putExtra("id", cursor.getInt(cursor.getColumnIndex("id")));
                startActivity(intent);
            }
        });
        //注册上下文菜单
        registerForContextMenu(listView);
        //获得loadmanager对象
        loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
        backImageView = (ImageView) findViewById(R.id.activity_jilu_back_imageview);
        homeImageView = (ImageView) findViewById(R.id.activity_jilu_home_imageview);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JiLuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
        cursor.moveToPosition(position);
        //获得游标所在位置的数据的id
        String id = cursor.getString(cursor.getColumnIndex("_id"));
        switch (itemId) {
            case R.id.delete_item:
                deleteData(id);
                break;
            case R.id.update_item:
                showUpdateDialog(id);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(String id) {
        Toast.makeText(JiLuActivity.this, "网络数据请不要修改", Toast.LENGTH_SHORT).show();
    }

    // 删除数据
    private void deleteData(String id) {
        int count = db.delete(DatabasedeHelper.TABLE_NAME_JI, "_id = ?", new String[]{id});
        if (count > 0) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }

        // 重新加载数据
        loaderManager.restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        MyLoader loader = new MyLoader(this);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    //异步任务加载器
    private static class MyLoader extends AsyncTaskLoader<Cursor> {

        public MyLoader(Context context) {
            super(context);
        }

        //开始加载数据时执行该方法
        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            //强制开启加载数据
            forceLoad();
        }

        //开启子线程，做加载数据操作
        @Override
        public Cursor loadInBackground() {
            cursor = db.query(DatabasedeHelper.TABLE_NAME_JI, null, null, null, null, null, null);
            return cursor;
        }
    }
}


