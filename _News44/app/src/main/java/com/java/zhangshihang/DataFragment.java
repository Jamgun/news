package com.java.zhangshihang;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFragment extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private String mPage;
    private ListView lv1;
    private ArrayList<news_item>mdata;
    private BaseAdapter mAdapter=null;
    private int currentnum=0;
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    public static DataFragment newInstance(String page) {
        Bundle args = new Bundle();
        args.putString(ARGS_PAGE, page);

        DataFragment fragment = new DataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getString(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_data_fragment, container, false);
        lv1 = rootView.findViewById(R.id.lv1);
        if(mPage.equals("123"))
        {
            Log.e("123", "onCreateView: " );
            mdata=new ArrayList<news_item>();
            myDBHelper = new MyDBOpenHelper(getActivity(), "shoucang.db", null, 1);
            db = myDBHelper.getWritableDatabase();

            Cursor cursor = db.query("news", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String title0= cursor.getString(cursor.getColumnIndex("title"));
                    String url0= cursor.getString(cursor.getColumnIndex("url"));
                    String datee=cursor.getString(cursor.getColumnIndex("date"));
                    mdata.add(new news_item(title0,url0,datee));
                } while (cursor.moveToNext());
            }
            cursor.close();
            Log.e("456", "onCreateView: " );
            mAdapter =new MyAdapter<news_item>(mdata,R.layout.list_simple_layout) {
                @Override
                public void bindView(ViewHolder holder,news_item obj)
                {
                    holder.setText(R.id.tv1,obj.gettitle());
                    holder.setText(R.id.tv2,obj.getdate());
                    holder.setTextcolor(R.id.tv1,obj.getisread());
                }
            };
            lv1.setAdapter(mAdapter);
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String TAG=mdata.get(position).getuurl();
                    String TIL=mdata.get(position).gettitle();
                    String dat=mdata.get(position).getdate();
                    try {
                        mdata.get(position).setIsread(true);
                        mAdapter.notifyDataSetChanged();
                        Intent it = new Intent(getActivity(), webv.class);
                        Bundle bd=new Bundle();
                        bd.putString("url",TAG);
                        bd.putString("title",TIL);
                        bd.putString("date",dat);
                        it.putExtras(bd);
                        startActivity(it);


                    } catch (Exception ex) {
                        // 显示异常信息
                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Log.e(TAG, "onItemClick: ");
                }
            });

            Log.e("789", "onCreateView: " );



        }else if(mPage.equals("456"))
        {
            Log.e("123", "onCreateView: " );
            mdata=new ArrayList<news_item>();
            myDBHelper = new MyDBOpenHelper(getActivity(), "kanguo.db", null, 1);
            db = myDBHelper.getWritableDatabase();

            Cursor cursor = db.query("news", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String title0= cursor.getString(cursor.getColumnIndex("title"));
                    String url0= cursor.getString(cursor.getColumnIndex("url"));
                    String datee=cursor.getString(cursor.getColumnIndex("date"));
                    mdata.add(new news_item(title0,url0,datee));
                } while (cursor.moveToNext());
            }
            cursor.close();
            Log.e("456", "onCreateView: " );
            mAdapter =new MyAdapter<news_item>(mdata,R.layout.list_simple_layout) {
                @Override
                public void bindView(ViewHolder holder,news_item obj)
                {
                    holder.setText(R.id.tv1,obj.gettitle());
                    holder.setText(R.id.tv2,obj.getdate());
                    holder.setTextcolor(R.id.tv1,obj.getisread());
                }
            };
            lv1.setAdapter(mAdapter);
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String TAG=mdata.get(position).getuurl();
                    String TIL=mdata.get(position).gettitle();
                    String dat=mdata.get(position).getdate();
                    try {
                        mdata.get(position).setIsread(true);
                        mAdapter.notifyDataSetChanged();
                        Intent it = new Intent(getActivity(), webv.class);
                        Bundle bd=new Bundle();
                        bd.putString("url",TAG);
                        bd.putString("title",TIL);
                        bd.putString("date",dat);
                        it.putExtras(bd);
                        startActivity(it);


                    } catch (Exception ex) {
                        // 显示异常信息
                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Log.e(TAG, "onItemClick: ");
                }
            });

            Log.e("789", "onCreateView: " );



        }
        else {
            webTask task1 = new webTask();
            task1.execute();
        }

        return rootView;

    }
    public  String getmapage()
    {
        return mPage;
    }




    private  class webTask extends AsyncTask<String, Integer, String> {
        List<Map<String, String>> listmap = new ArrayList<Map<String, String>>();
        public webTask()
        {
            super();
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url1 = new URL(mPage);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                InputStream input = url1.openStream();
                parser.setInput(input, "UTF-8");
                publishProgress(123);

                int eventType = parser.getEventType();
                int flag=1;
                while (eventType != XmlPullParser.END_DOCUMENT&&flag<=500) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if ("item".equals(parser.getName())) {
                                flag++;
                                parser.next();
                                parser.next();
                                String titlee=parser.nextText();

                                parser.next();

                                parser.next();

                                parser.next();
                                String urlll=parser.getText();
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
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.e(Integer.toString(listmap.size()), "onPostExecute: " );

            mdata=new ArrayList<news_item>();
            for(int i=currentnum;i<currentnum+20;i++)
            {
                mdata.add(new news_item(listmap.get(i).get("title"),listmap.get(i).get("url"),listmap.get(i).get("date")));
            }
            currentnum+=20;
//            mdata.add(new news_item(mPage,"url"));
//            mdata.add(new news_item("title","url"));
//            mdata.add(new news_item("title","url"));
            mAdapter =new MyAdapter<news_item>(mdata,R.layout.list_simple_layout) {
                @Override
                public void bindView(ViewHolder holder,news_item obj)
                {
                    holder.setText(R.id.tv1,obj.gettitle());
                    holder.setText(R.id.tv2,obj.getdate());
                    holder.setTextcolor(R.id.tv1,obj.getisread());
                }
            };
            lv1.setAdapter(mAdapter);
            lv1.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                                    Toast.makeText(getActivity(), "没有更多内容了", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity(), "正在加载", Toast.LENGTH_SHORT).show();

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
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String TAG=mdata.get(position).getuurl();
                    String TIL=mdata.get(position).gettitle();
                    String dat=mdata.get(position).getdate();
                    try {
                        mdata.get(position).setIsread(true);
                        mAdapter.notifyDataSetChanged();
                        Intent it = new Intent(getActivity(), webv.class);
                        Bundle bd=new Bundle();
                        bd.putString("url",TAG);
                        bd.putString("title",TIL);
                        bd.putString("date",dat);
                        it.putExtras(bd);
                        startActivity(it);


                    } catch (Exception ex) {
                        // 显示异常信息
                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
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




}
