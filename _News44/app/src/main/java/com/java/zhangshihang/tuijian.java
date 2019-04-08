package com.java.zhangshihang;

import android.content.Context;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tuijian extends AppCompatActivity {
    private ImageView img_pgbar00;
    private AnimationDrawable ad;
    private RelativeLayout re0;
    private List<String> iiurls;
    private List<Integer> nnums;
    private ListView lv3;
    private Context mContext;
    private ArrayList<news_item> mdata;
    private BaseAdapter mAdapter=null;
    private int currentnum=0;
    List<Map<String, String>> listmap = new ArrayList<Map<String, String>>();
    private  int sunweight=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuijian);
        lv3=(ListView)findViewById(R.id.lv3) ;
        mContext = tuijian.this;
        Intent it2=getIntent();
        re0=(RelativeLayout)findViewById(R.id.re0);
        img_pgbar00 = (ImageView) findViewById(R.id.img_pgbar00);
        ad = (AnimationDrawable) img_pgbar00.getDrawable();
        img_pgbar00.postDelayed(new Runnable() {
            @Override
            public void run() {
                ad.start();
            }
        }, 100);
        iiurls=(List<String>)it2.getSerializableExtra("urls");
        nnums=(List<Integer>)it2.getSerializableExtra("num");
        Log.e(Integer.toString(iiurls.size()), "onCreate: " );
        Log.e(Integer.toString(nnums.size()), "onCreate: " );
        sunweight=0;
        if(iiurls.size()==nnums.size()&&nnums.size()!=0)
        {
            for(int i=0;i<nnums.size();i++)sunweight+=nnums.get(i);
            Log.e(Integer.toString(sunweight), "onCreate: " );
            webTask task3=new webTask();
            task3.execute();


        }
        else
        {
            Toast.makeText(mContext, "对不起，您的数据不足", Toast.LENGTH_SHORT).show();

        }


    }



    private  class webTask extends AsyncTask<String, Integer, String> {
        public webTask()
        {
            super();
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... strings) {
            for(int t=0;t<iiurls.size();t++) {
                try {
                    URL url1 = new URL(iiurls.get(t));
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    InputStream input = url1.openStream();
                    parser.setInput(input, "UTF-8");
                    publishProgress(123);

                    int eventType = parser.getEventType();
                    int flag = 1;
                    int nuuu=(int)(100*nnums.get(t)/sunweight);
                    while (eventType != XmlPullParser.END_DOCUMENT) {
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
                                    if(flag>=100-nuuu-5)
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
            Collections.shuffle(listmap);

            mdata=new ArrayList<news_item>();



            for(int i=currentnum;i<currentnum+20;i++)
            {

                    mdata.add(new news_item(listmap.get(i).get("title"),listmap.get(i).get("url"),listmap.get(i).get("date")));


            }
            currentnum+=20;
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
                Toast.makeText(mContext, "相关资料不足", Toast.LENGTH_SHORT).show();

            }
            lv3.setAdapter(mAdapter);
            re0.removeViewInLayout(img_pgbar00);
            lv3.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //滚动结束
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            //滚动停止时，判断如果滚动到底部
                            if (view.getLastVisiblePosition()==view.getCount()-1) {
                                Log.e(Integer.toString(currentnum), "onScrollStateChanged: ");
                                for(int i=currentnum;i<currentnum+10&&i<listmap.size();i++)
                                {
                                    mdata.add(new news_item(listmap.get(i).get("title"),listmap.get(i).get("url"),listmap.get(i).get("date")));
                                }
                                currentnum+=10;
                                if(currentnum>=listmap.size())
                                    Toast.makeText(mContext, "没有更多内容了", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(mContext, "正在加载", Toast.LENGTH_SHORT).show();

                                mAdapter.notifyDataSetChanged();


                            }else if (view.getFirstVisiblePosition()==0) {
                            }

                            break;
                        //开始滚动
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

                            break;
                        //正在滚动
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

                            break;

                        default:
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
            lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String TAG=mdata.get(position).getuurl();
                    String TIL=mdata.get(position).gettitle();
                    String dat=mdata.get(position).getdate();
                    try {
                        mdata.get(position).setIsread(true);
                        mAdapter.notifyDataSetChanged();
                        Intent it = new Intent(tuijian.this, webv.class);
                        Bundle bd=new Bundle();
                        bd.putString("url",TAG);
                        bd.putString("title",TIL);
                        bd.putString("date",dat);
                        it.putExtras(bd);
                        startActivity(it);


                    } catch (Exception ex) {
                        // 显示异常信息
                        Toast.makeText(tuijian.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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


















    public void onClicktuichu(View view)
    {
        finish();
    }
    public void onBackPressed() {
        finish();
    }
}
