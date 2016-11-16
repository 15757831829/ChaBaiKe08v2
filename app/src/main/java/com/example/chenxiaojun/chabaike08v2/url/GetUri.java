package com.example.chenxiaojun.chabaike08v2.url;

/**
 * Created by my on 2016/11/14.
 */
public class GetUri {

    public static String getToutiaoUri(int id){
        String touTiaoUrl = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getHeadlines&page="+id+"&row=15";
        return touTiaoUrl;
    }

    public static String getBaikeUri(int id){
        String baiKeUrl ="http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&page="+id+"&row=15&type=16";
        return baiKeUrl;
    }

    public static String getZixunUri(int id){
        String zixunUri ="http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&page="+id+"&row=15&type=52";
        return zixunUri;
    }

    public static String getJingyinUri(int id){
        String jingYinUrl = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&page="+id+"&row=15&type=53";
        return jingYinUrl;
    }

    public static String getShujuUri(int id){
        String shuJuUrl = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&page="+id+"&row=15&type=54";
        return shuJuUrl;
    }
    public static final String HEADER_URI = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getSlideshow";

    public static final String SEARCH_URI = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.searcListByTitle&page=1&rows=10&search=%s";

    public static String getSearchUri(String search){
        String searchUri = String.format(SEARCH_URI,search);
        return searchUri;
    }

    private static final String CONTENT_URI = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getNewsContent&id=%d";
    public static String getContentUri(int id){
        String contentUri = String.format(CONTENT_URI,id);
        return contentUri;
    }
}


