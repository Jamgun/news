package com.java.zhangshihang;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class searchm extends AppCompatActivity {
    private DelEditText searchtext2;
    private ListView lv2;
    private RelativeLayout re;
    private BaseAdapter mAdapter=null;
    private ArrayList<news_item> mdata;
    private ImageView img_pgbar0;
    private AnimationDrawable ad;
    List<Map<String, String>> listmap = new ArrayList<Map<String, String>>();
    String str0=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchm);
        searchtext2=(DelEditText)findViewById(R.id.searchtext2);
        re=(RelativeLayout)findViewById(R.id.re);
        lv2=(ListView)findViewById(R.id.lv2);
        Intent it = getIntent();
        Bundle bd =it.getExtras();
        if(bd.getCharSequence("search").toString()==null)
        {
        }
        else
        {
            str0=bd.getCharSequence("search").toString();
            searchtext2.setText(str0);
            img_pgbar0 = (ImageView) findViewById(R.id.img_pgbar0);
            ad = (AnimationDrawable) img_pgbar0.getDrawable();
            img_pgbar0.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ad.start();
                }
            }, 100);
            webTask task2=new webTask();
            task2.execute();
        }
    }

    private  class webTask extends AsyncTask<String, Integer, String> {
        String[] urls=new String[]{
                "http://www.people.com.cn/rss/game.xml",
                "http://www.people.com.cn/rss/haixia.xml",
                "http://www.people.com.cn/rss/scitech.xml",
                "http://www.people.com.cn/rss/sports.xml",
                "http://www.people.com.cn/rss/ent.xml",
                "http://www.people.com.cn/rss/society.xml",
                "http://www.people.com.cn/rss/it.xml",
                "http://www.people.com.cn/rss/culture.xml",
                "http://www.people.com.cn/rss/health.xml",
                "http://www.people.com.cn/rss/world.xml",
                "http://www.people.com.cn/rss/history.xml",
                "http://www.people.com.cn/rss/shipin.xml",
                "http://www.people.com.cn/rss/env.xml"
        };
        public webTask()
        {
            super();
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... strings) {
            for(int t=0;t<urls.length;t++) {
                try {
                    URL url1 = new URL(urls[t]);
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    InputStream input = url1.openStream();
                    parser.setInput(input, "UTF-8");
                    publishProgress(123);

                    int eventType = parser.getEventType();
                    int flag = 1;
                    while (eventType != XmlPullParser.END_DOCUMENT && flag <=110) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                if ("item".equals(parser.getName())) {
                                    flag++;
                                    parser.next();
                                    parser.next();
                                    String titlee = parser.nextText();
                                    // Log.e(titlee, "doInBackground: " );
                                    parser.next();
                                    // Log.e(parser.getName(), "doInBackground: " );
                                    parser.next();
                                    //Log.e(parser.getName(), "doInBackground: " );
                                    parser.next();
                                    String urlll = parser.getText();
                                    parser.next();
                                    parser.next();
                                    parser.next();
                                    String data=parser.nextText();
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("title", titlee);
                                    map.put("url", urlll);
                                    map.put("date",data);
                                    listmap.add(map);
                                    //Log.e(map.get("title"), "doInBackground: " );
                                    // Log.e(map.get("url"), "doInBackground: " );
                                }
                                break;
                            case XmlPullParser.END_TAG:

                                break;
                        }
                        eventType = parser.next();
                    }
                } catch (MalformedURLException e) {

                    e.printStackTrace();
                } catch (XmlPullParserException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            Log.e(Integer.toString(listmap.size()), "doInBackground: " );
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.e(Integer.toString(listmap.size()), "onPostExecute: " );
            mdata=new ArrayList<news_item>();

            for(int i=0;i<listmap.size();i++)
            {
                if(listmap.get(i).get("title").indexOf(str0)!=-1)
                {
                    mdata.add(new news_item(listmap.get(i).get("title"),listmap.get(i).get("url"),listmap.get(i).get("date")));
                }

            }
//            currentnum+=20;
////            mdata.add(new news_item(mPage,"url"));
////            mdata.add(new news_item("title","url"));
////            mdata.add(new news_item("title","url"));
              mAdapter =new MyAdapter<news_item>(mdata,R.layout.list_simple_layout) {
                @Override
                public void bindView(ViewHolder holder,news_item obj)
                {
                    holder.setText(R.id.tv1,obj.gettitle());
                    holder.setText(R.id.tv2,obj.getdate());
                    holder.setTextcolor(R.id.tv1,obj.getisread());
                }
            };
            if(mdata.size()==0)
            {
                Toast.makeText(searchm.this, "没有相关信息", Toast.LENGTH_SHORT).show();

            }
            lv2.setAdapter(mAdapter);
            re.removeViewInLayout(img_pgbar0);
            lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String TAG=mdata.get(position).getuurl();
                    String TIL=mdata.get(position).gettitle();
                    String dat=mdata.get(position).getdate();
                   try {
                        mdata.get(position).setIsread(true);
                        mAdapter.notifyDataSetChanged();
                        Intent it = new Intent(searchm.this, webv.class);
                        Bundle bd=new Bundle();
                        bd.putString("url",TAG);
                       bd.putString("title",TIL);
                       bd.putString("date",dat);
                        it.putExtras(bd);
                        startActivity(it);


                    } catch (Exception ex) {
                        // 显示异常信息
                        Toast.makeText(searchm.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Log.e(TAG, "onItemClick: ");
                }
            });


        }



        @Override
        protected void onProgressUpdate(Integer... values) { }
        @Override
        protected void onCancelled() { }
    }
    public void onClicksearchs(View view)
    {

        str0=searchtext2.getText().toString();
        if(str0.equals("")){
            Toast.makeText(searchm.this, "请输入你要搜索的内容", Toast.LENGTH_SHORT).show();
        }else {

            mdata.clear();
            for (int i = 0; i < listmap.size(); i++) {
                if (listmap.get(i).get("title").indexOf(str0) != -1) {
                    mdata.add(new news_item(listmap.get(i).get("title"), listmap.get(i).get("url"), listmap.get(i).get("date")));
                }

            }
            if (mdata.size() == 0) {
                Toast.makeText(searchm.this, "没有相关信息", Toast.LENGTH_SHORT).show();

            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
