package com.java.zhangshihang;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

public class webv extends AppCompatActivity {
    private ImageView img_pgbar;
    private AnimationDrawable ad;

    private String ttile;
    private String uurl;
    private String ddate;
    private WebView web1;
    private Button shoucang;
    private Context mContext;
    private  SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private  SQLiteDatabase db3;
    private MyDBOpenHelper myDBHelper3;
    private  SQLiteDatabase db2;
    private MyDBOpenHelper myDBHelper2;
    private static final String APP_CACHE_DIRNAME = "/webcache";
    private  boolean ishoucang=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webv);
        shoucang=(Button)findViewById(R.id.shoucang) ;
        img_pgbar = (ImageView) findViewById(R.id.img_pgbar);
        ad = (AnimationDrawable) img_pgbar.getDrawable();
        img_pgbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                ad.start();
            }
        }, 100);
        mContext = webv.this;
        myDBHelper = new MyDBOpenHelper(mContext, "shoucang.db", null, 1);
        myDBHelper2 = new MyDBOpenHelper(mContext, "kanguo.db", null, 1);

        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        Intent it = getIntent();
        Bundle bd = it.getExtras();
        uurl=bd.getCharSequence("url").toString();
        ttile=bd.getCharSequence("title").toString();
        ddate=bd.getCharSequence("date").toString();
        Log.e(uurl, "onCreate: " );
        web1=(WebView)findViewById(R.id.web1);


        db = myDBHelper.getReadableDatabase();
        db2 = myDBHelper2.getReadableDatabase();
        db2.execSQL("INSERT INTO news(title,url,date) values(?,?,?)",
                new String[]{gettttile(),getuuurl(),getdate()});


        Cursor cursor =  db.rawQuery("SELECT * FROM news WHERE title = ?",
                new String[]{gettttile()});
        //存在数据才返回true
        if(cursor.moveToFirst())
        {
            shoucang.setBackgroundResource(R.mipmap.favorite_black);
            ishoucang=true;
        }
        cursor.close();



        web1.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
                 //调用loadUrl方法为WebView加入链接
        WebSettings settings = web1.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启database storage API功能
        settings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        Log.i("cachePath", cacheDirPath);
        // 设置数据库缓存路径
        settings.setAppCachePath(cacheDirPath);
        settings.setAppCacheEnabled(true);
        Log.i("databasepath", settings.getDatabasePath());
        settings.setSupportZoom(true);
        // //扩大比例的缩放
        settings.setUseWideViewPort(true);
        myDBHelper3 = new MyDBOpenHelper(mContext, "shengliu.db", null, 1);
        db3 = myDBHelper3.getReadableDatabase();
        //settings.setLoadsImagesAutomatically(false);
        Cursor cursor0 =  db3.rawQuery("SELECT * FROM news WHERE title = ?",
                new String[]{"noimage"});
        int kk=0;
        //存在数据才返回true
        if(cursor0.moveToFirst())
        {
            kk=1;
            Log.e("123", "onCreate: " );

        }
        cursor0.close();
        if(kk==1)
        {
            settings.setLoadsImagesAutomatically(false);
        }
//        final Activity activity = this;
//        web1.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                // TODO Auto-generated method stub
//                return super.onJsAlert(view, url, message, result);
//            }
//
//        });

        web1.loadUrl(uurl);




    }
    public void onclickshare(View view)
    {
        String senstr=ttile+"\n"+uurl;
        Intent StringIntent = new Intent(Intent.ACTION_SEND);
        StringIntent.setType("text/plain");
        StringIntent.putExtra(Intent.EXTRA_TEXT, senstr);
        this.startActivity(Intent.createChooser(StringIntent, "分享")); // 创建选择器
    }
    public void onclickshoucang(View view)
    {
        if(ishoucang){
            shoucang.setBackgroundResource(R.mipmap.favorite);
            //SQLiteDatabase db = myDBHelper.getWritableDatabase();
            db = myDBHelper.getWritableDatabase();
            db.execSQL("DELETE FROM news WHERE title = ?",
                    new String[]{gettttile()});
            ishoucang=false;
        }
        else {
            shoucang.setBackgroundResource(R.mipmap.favorite_black);
            ishoucang=true;
            db = myDBHelper.getWritableDatabase();
            db.execSQL("INSERT INTO news(title,url,date) values(?,?,?)",
                    new String[]{gettttile(),getuuurl(),getdate()});
        }
    }


    @Override
    public void onBackPressed() {
        if(web1.canGoBack()){
            web1.goBack();
        }else{
            super.onBackPressed();
        }
    }
    public  String gettttile()
    {
        return ttile;
    }
    public  String getuuurl()
    {
        return uurl;
    }
    public  String getdate()
    {
        return ddate;
    }

}
