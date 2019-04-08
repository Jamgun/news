package com.java.zhangshihang;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {
    private TabLayout maintabLayout;
    private ViewPager mainviewPager;
    private long lastBack = 0;
    private DelEditText searchtext;
    private List<String> iurls=new ArrayList<>();
    private List<Integer> nums=new ArrayList<>();
    private int lastindex=-1;
    private  Date lastDate = null;



    private List<String> datas = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private MyPagerAdapter adapter;
    private MyPagerAdapter adapter2;
    private TextView zsh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchtext = (DelEditText) findViewById(R.id.searchtext);
        zsh=(TextView)findViewById(R.id.zsh);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        if (isConnectIsNomarl()) {
            Intent it = getIntent();
            if (it.getSerializableExtra("selected") == null) {
                isConnectIsNomarl();
                Log.e(it.getDataString(), "onCreate: ");

                maintabLayout = (TabLayout) findViewById(R.id.maintablayout);
                mainviewPager = (ViewPager) findViewById(R.id.mainviewpager);
                //datas.add("关注");
                //datas.add("推荐");
                //datas.add("热点");
                //datas.add("台湾");

                datas.add("游戏");
                //datas.add("北京");
                //datas.add("新时代");
                datas.add("娱乐");
                //datas.add("军事");




                for (int i =0; i < datas.size(); i++) {
                    maintabLayout.addTab(maintabLayout.newTab().setText(datas.get(i)));
                    String str;
                    str = findurl(datas.get(i));
                    fragments.add(DataFragment.newInstance(str));
                    iurls.add(str);
                    nums.add(20);
                }

                maintabLayout.addOnTabSelectedListener(this);
                adapter = new MyPagerAdapter(getSupportFragmentManager(), datas, fragments);
                mainviewPager.setAdapter(adapter);
                mainviewPager.setOffscreenPageLimit(12);
                maintabLayout.setupWithViewPager(mainviewPager);




            } else {
                isConnectIsNomarl();
                maintabLayout = (TabLayout) findViewById(R.id.maintablayout);
                mainviewPager = (ViewPager) findViewById(R.id.mainviewpager);
                Log.e(it.getDataString(), "onCreate: ");
                datas.clear();
                fragments.clear();
                if (fragments.isEmpty())
                    Log.e("11111111111111", "onActivityResult: ");


                List<String> selecteddatas = (List<String>) it.getSerializableExtra("selected");
                for (int i = 0; i < selecteddatas.size(); i++) {
                    Log.e(selecteddatas.get(i), "onActivityResult: ");
                    datas.add(selecteddatas.get(i));
                }


                for (int i = 0; i < datas.size(); i++) {
                    maintabLayout.addTab(maintabLayout.newTab().setText(datas.get(i)));
                    String str;
                    str = findurl(datas.get(i));
                    fragments.add(DataFragment.newInstance(str));
                    iurls.add(str);
                    nums.add(20);
                }

                maintabLayout.addOnTabSelectedListener(this);
                adapter2 = new MyPagerAdapter(getSupportFragmentManager(), datas, fragments);


                mainviewPager.setAdapter(adapter2);
                mainviewPager.setOffscreenPageLimit(12);
                maintabLayout.setupWithViewPager(mainviewPager);


            }
            mainviewPager.addOnPageChangeListener(this);
            mainviewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View page, float position) {
                    int width = page.getWidth();
                    int height = page.getHeight();
                    //這裏只對右邊的view做了操作
                    if (position > 0 && position <= 1) {//right scorlling
                        //position是1.0->0,但是沒有等於0

                        //設置該view的X軸不動
                        page.setTranslationX(-width * position);
                        //設置縮放中心點在該view的正中心
                        page.setPivotX(width / 2);
                        page.setPivotY(height / 2);
                        //設置縮放比例（0.0，1.0]
                        page.setScaleX(1 - position);
                        page.setScaleY(1 - position);

                    } else if (position >= -1 && position < 0) {//left scrolling

                    } else {//center

                    }
                }


            });
        }else{
            Toast.makeText(MainActivity.this, "请连接网络，并重新进入", Toast.LENGTH_SHORT).show();
        }




    }


    private void initViews() {
        //循环注入标签

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mainviewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageSelected(int index) {

        if(lastindex==-1)
        {
            lastindex=index;
            lastDate=new Date(System.currentTimeMillis());
        }
        else
        {
            Date curdata=new Date(System.currentTimeMillis());
            long diff = curdata.getTime() - lastDate.getTime();
            int ite=nums.get(lastindex).intValue();
            int weightt=((int)diff)/100;
            nums.set(lastindex,ite+weightt);
            Log.e(Integer.toString(lastindex), "page ");
            Log.e(Integer.toString(nums.get(lastindex)), "weight " );
            Log.e(iurls.get(lastindex), "url" );

            lastindex=index;
            lastDate=curdata;
        }


    }
    @Override
    public void onPageScrollStateChanged(int index) {
        //crueent=index;
        //Log.e(Integer.toString(index), "onPageScrollStateChanged: ");
        //Log.e(Integer.toString(nums.get(index)), "onPageSelected: " );

    }

    @Override
    public void onPageScrolled(int index, float v, int i1) {
        //int ite=nums.get(index).intValue();
        //nums.set(index,ite+1);

    }
    public void onClickselect(View view) {
        if(isConnectIsNomarl()) {
            try {


                Intent it = new Intent(MainActivity.this, select_main.class);

                it.putExtra("selected", (Serializable) datas);

                startActivity(it);
                finish();


            } catch (Exception ex) {
                // 显示异常信息
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

        }
    }

    public  String findurl(String a)
    {
        String str=null;
        if(a.equals("台湾"))
        {
            str="http://www.people.com.cn/rss/haixia.xml";
        } else if (a.equals("科技")) {
            str="http://www.people.com.cn/rss/scitech.xml";
        }else if (a.equals("游戏")) {
            str=" http://www.people.com.cn/rss/game.xml";
        }else if (a.equals("娱乐")) {
            str="http://www.people.com.cn/rss/ent.xml";
        }else if (a.equals("体育")) {
            str="http://www.people.com.cn/rss/sports.xml";
        }
        else if (a.equals("社会")) {
            str="http://www.people.com.cn/rss/society.xml";
        }
        else if (a.equals("IT")) {
            str="http://www.people.com.cn/rss/it.xml";
        }
        else if (a.equals("文化")) {
            str="http://www.people.com.cn/rss/culture.xml";
        }
        else if (a.equals("健康")) {
            str="http://www.people.com.cn/rss/health.xml";
        }
        else if (a.equals("推荐")) {
            str="http://www.people.com.cn/rss/ent.xml";
        }
        else if (a.equals("国际")) {
            str="http://www.people.com.cn/rss/world.xml";
        }else if (a.equals("文史")) {
            str="http://www.people.com.cn/rss/history.xml";
        }else if (a.equals("娱乐")) {
            str="http://www.people.com.cn/rss/ent.xml";
        }else if (a.equals("食品")) {
            str="http://www.people.com.cn/rss/shipin.xml";
        }
        else if (a.equals("环保")) {
            str="http://www.people.com.cn/rss/env.xml";
        }
        else if (a.equals("娱乐")) {
            str="http://www.people.com.cn/rss/ent.xml";
        }else if (a.equals("娱乐")) {
            str="http://www.people.com.cn/rss/ent.xml";
        }





        return str;
    }
    public void onBackPressed() {
        if (lastBack == 0 || System.currentTimeMillis() - lastBack > 2000) {
            Toast.makeText(MainActivity.this, "再按一次返回退出程序", Toast.LENGTH_SHORT).show();
            lastBack = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
    }
    public void onClicksearch(View view)
    {
        Log.e(searchtext.getText().toString(), "onClicksearch: " );
        if(searchtext.getText().toString().equals(""))

        {
            //zsh.setBackgroundColor(R.color.black);
            Toast.makeText(MainActivity.this, "请输入要搜索的内容", Toast.LENGTH_SHORT).show();
        }else
            {
        try
        {
            //startActivity(new Intent("com.AndroidTest.SecondActivity"));//隐式intent
            Intent intent = new Intent(this, searchm.class);//显示intent
            Bundle bd = new Bundle();
            bd.putCharSequence("search",searchtext.getText().toString());
            Log.e(searchtext.getText().toString(), "onClicksearch: " );
            intent.putExtras(bd);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            // 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
    }
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String intentName = info.getTypeName();
            Log.i("通了没！", "当前网络名称：" + intentName);
            return true;
        } else {
            Log.i("通了没！", "没有可用网络");
            return false;
        }
    }
    public void onClicktuijian(View view)
    {
        if(isConnectIsNomarl()) {
            try {

                Intent it = new Intent(MainActivity.this, tuijian.class);

                it.putExtra("num", (Serializable) nums);
                it.putExtra("urls", (Serializable) iurls);

                startActivity(it);



            } catch (Exception ex) {
                // 显示异常信息
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

        }
    }

    public void onClickshoucang(View view)
    {
        try
        {
            //startActivity(new Intent("com.AndroidTest.SecondActivity"));//隐式intent
            Intent intent = new Intent(this, shoucangg.class);//显示intent
            startActivity(intent);
        }
        catch (Exception ex)
        {
            // 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}



