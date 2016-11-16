package com.example.chenxiaojun.chabaike08v2.bean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by my on 2016/11/14.
 */
public class TTEntity {
    private List<Data> data;

    public TTEntity() {
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private int id;
        private String title;
        private String source;
        private String description;
        private String wap_thumb;
        private String create_time;
        private String nickname;
        private String wap_content;

        public Data(int id, String title, String source, String description, String wap_thumb, String create_time, String nickname, String wap_content) {
            this.id = id;
            this.title = title;
            this.source = source;
            this.description = description;
            this.wap_thumb = wap_thumb;
            this.create_time = create_time;
            this.nickname = nickname;
            this.wap_content = wap_content;
        }

        public Data() {
        }

        public Data(JSONObject jsonObject) {
            this.wap_thumb = jsonObject.optString("wap_thumb");
            this.source = jsonObject.optString("source");
            this.description = jsonObject.optString("description");
            this.id = jsonObject.optInt("id");
            this.title = jsonObject.optString("title");
            this.create_time = jsonObject.optString("create_time");
            this.nickname = jsonObject.optString("nickname");
            this.wap_content = jsonObject.optString("wap_content");
        }

        public String getWap_content() {
            return wap_content;
        }

        public void setWap_content(String wap_content) {
            this.wap_content = wap_content;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getWap_thumb() {
            return wap_thumb;
        }

        public void setWap_thumb(String wap_thumb) {
            this.wap_thumb = wap_thumb;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

}

