package com.java.zhangshihang;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class shoucangg extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout shoucangtabLayout;
    private ViewPager shoucangviewPager;
    private MyPagerAdapter adapter;
    private SQLiteDatabase db2;
    private MyDBOpenHelper myDBHelper2;
    private List<String> datas = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datas.add("收藏");
        datas.add("历史");
        myDBHelper2 = new MyDBOpenHelper(shoucangg.this, "kanguo.db", null, 1);
        db2 = myDBHelper2.getReadableDatabase();
        setContentView(R.layout.activity_shoucangg);
        shoucangtabLayout = (TabLayout) findViewById(R.id.shoucangtablayout);
        shoucangviewPager = (ViewPager) findViewById(R.id.shoucangviewpager);
        shoucangtabLayout.addTab(shoucangtabLayout.newTab().setText("收藏"));
        //Log.e("123", "onCreate: ");
        fragments.add(DataFragment.newInstance("123"));
        fragments.add(DataFragment.newInstance("456"));
        Log.e("123", "onCreate: ");
        shoucangtabLayout.addOnTabSelectedListener(this);
        Log.e("123", "onCreate: ");

        adapter = new MyPagerAdapter(getSupportFragmentManager(), datas, fragments);
        shoucangviewPager.setAdapter(adapter);
        shoucangviewPager.setOffscreenPageLimit(12);
        shoucangtabLayout.setupWithViewPager(shoucangviewPager);





    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
       shoucangviewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    public void onClickfinhui(View view)
    {
        finish();
    }
    public void onBackPressed() {
        finish();
    }
    public void onClickfresh(View view)
    {





        try
        {
            //startActivity(new Intent("com.AndroidTest.SecondActivity"));//隐式intent
            Intent intent = new Intent(this, shoucangg.class);//显示intent
            startActivity(intent);
            finish();

        }
        catch (Exception ex)
        {
            // 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void onClickdelt(View view)
    {
        Cursor cursor = db2.query("news", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                String name = cursor.getString(cursor.getColumnIndex("title"));
                db2.execSQL("DELETE FROM news WHERE title = ?",
                        new String[]{name});
            } while (cursor.moveToNext());
        }
        cursor.close();
        try
        {
            //startActivity(new Intent("com.AndroidTest.SecondActivity"));//隐式intent
            Intent intent = new Intent(this, shoucangg.class);//显示intent
            startActivity(intent);
            finish();
        }
        catch (Exception ex)
        {
            // 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
