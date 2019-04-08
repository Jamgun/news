package com.java.zhangshihang;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class select_main extends AppCompatActivity {
    private List<String> alldatas = new ArrayList<>();
    private List<String> selecteddatas;
    private GridView gridicon1;
    private GridView gridicon2;
    private ArrayList<ibutton> madata1;
    private BaseAdapter myadapter1=null;
    private ArrayList<ibutton> madata2;
    private BaseAdapter myadapter2=null;
    private Context mContext;
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private Button huyan;


    private ArrayList<news_item>mdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_main);
        mContext = select_main.this;


//        alldatas.add("关注");alldatas.add("推荐");alldatas.add("热点");
//        alldatas.add("北京");alldatas.add("新时代");
        alldatas.add("环保");alldatas.add("食品");
//
        alldatas.add("文史");
        alldatas.add("台湾");alldatas.add("文化");alldatas.add("健康");
        alldatas.add("游戏");alldatas.add("IT");alldatas.add("国际");
        alldatas.add("娱乐");alldatas.add("社会");
        alldatas.add("科技");alldatas.add("体育");
        huyan=(Button) findViewById(R.id.huyan);
        myDBHelper = new MyDBOpenHelper(mContext, "shengliu.db", null, 1);
        db = myDBHelper.getReadableDatabase();
        int kk=0;
        Cursor cursor0 =  db.rawQuery("SELECT * FROM news WHERE title = ?",
                new String[]{"noimage"});
        //存在数据才返回true
        if(cursor0.moveToFirst())
        {
            kk=1;
        }
        if(kk==1)
        {
            huyan.setBackgroundResource(R.mipmap.del);
        }

        cursor0.close();


        Intent it1=getIntent();
        selecteddatas=(List<String>)it1.getSerializableExtra("selected");
        gridicon1=(GridView)findViewById(R.id.gridicon1);
        gridicon2=(GridView)findViewById(R.id.gridicon2);

        madata1=new ArrayList<ibutton>();
        for(int i=0;i<selecteddatas.size();i++)
            madata1.add(new ibutton(selecteddatas.get(i)));
        myadapter1=new MyAdapter<ibutton>(madata1,R.layout.onebutton) {
            @Override
            public void bindView(ViewHolder holder, ibutton obj) {

                holder.setText(R.id.butn, "- "+obj.getitext());
            }
        };
        gridicon1.setAdapter(myadapter1);


        gridicon1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.e("111", "onItemClick: ");

                Toast.makeText(mContext, "你删除了" + madata1.get(position).getitext() , Toast.LENGTH_SHORT).show();
                madata2.add(madata1.get(position));
                madata1.remove(position);

                myadapter1.notifyDataSetChanged();
                myadapter2.notifyDataSetChanged();
            }
        });




        madata2=new ArrayList<ibutton>();
        for(int i=0;i<alldatas.size();i++)
        {
            if(!selecteddatas.contains(alldatas.get(i)))
                madata2.add(new ibutton(alldatas.get(i)));
        }
        myadapter2=new MyAdapter<ibutton>(madata2,R.layout.onebutton) {
            @Override
            public void bindView(ViewHolder holder, ibutton obj) {

                holder.setText(R.id.butn, "+ "+obj.getitext());
            }
        };
        gridicon2.setAdapter(myadapter2);

        gridicon2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Log.e("222", "onItemClick: ");

                Toast.makeText(mContext, "你添加了" + madata2.get(position).getitext() , Toast.LENGTH_SHORT).show();
                madata1.add(madata2.get(position));
                madata2.remove(position);

                myadapter1.notifyDataSetChanged();
                myadapter2.notifyDataSetChanged();
            }
        });













    }
    public void onClickreturnmain(View view)
    {
        try
        {
            List<String> returnlist=new ArrayList<>();

            for(int i=0;i<madata1.size();i++)
            {
                returnlist.add(madata1.get(i).getitext());
                Log.e(madata1.get(i).getitext(), "onClickreturnmain: " );
            }
            Intent it = new Intent(select_main.this,MainActivity.class);

            it.putExtra("selected",(Serializable)returnlist);

            startActivity(it);
            finish();



        }
        catch (Exception ex)
        {
            // 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        try
        {
            List<String> returnlist=new ArrayList<>();

            for(int i=0;i<madata1.size();i++)
            {
                returnlist.add(madata1.get(i).getitext());
                Log.e(madata1.get(i).getitext(), "onClickreturnmain: " );
            }
            Intent it = new Intent(select_main.this,MainActivity.class);

            it.putExtra("selected",(Serializable)returnlist);

            startActivity(it);
            finish();



        }
        catch (Exception ex)
        {
            // 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void onclickhuyan(View view)

    {

        Cursor cursor =  db.rawQuery("SELECT * FROM news WHERE title = ?",
                new String[]{"noimage"});
        //存在数据才返回true
        if(cursor.moveToFirst())
        {
            huyan.setBackgroundResource(R.mipmap.image);
            db.execSQL("DELETE FROM news WHERE title = ?",
                    new String[]{"noimage"});
        }
        else
        {

            huyan.setBackgroundResource(R.mipmap.del);
            db.execSQL("INSERT INTO news(title) values(?)",
                new String[]{"noimage"});
        }
        cursor.close();



    }




}
