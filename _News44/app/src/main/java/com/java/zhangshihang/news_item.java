package com.java.zhangshihang;

public class news_item {
    private String title;
    private String uurl;
    private boolean isread;
    private String date;


    public news_item() {
    }

    public news_item(String title, String uurl,String date) {
        this.title = title;
        this.uurl = uurl;
        this.date=date;
        this.isread=false;

    }

    public String gettitle() {
        return title;
    }
    public String getdate() {
        return date;
    }
    public String getuurl() {
        return uurl;
    }
    public boolean getisread() {
        return isread;
    }



    public void settitle(String title) {
        this.title = title;
    }
    public void setdate(String date) {
        this.date = date;
    }
    public void setuurl(String uurl) {
        this.uurl = uurl;
    }
    public void setIsread(boolean isread) {
        this.isread = isread;
    }

}
