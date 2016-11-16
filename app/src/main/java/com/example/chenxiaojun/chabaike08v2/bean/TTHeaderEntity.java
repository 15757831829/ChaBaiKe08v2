package com.example.chenxiaojun.chabaike08v2.bean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by my on 2016/11/14.
 */
public class TTHeaderEntity {
    private List<Data> data;

    public TTHeaderEntity() {
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private String id;
        private String title;
        private String image;

        public Data() {
        }

        public Data(JSONObject jsonObject) {
            this.id = jsonObject.optString("id");
            this.title = jsonObject.optString("title");
            this.image =  jsonObject.optString("image");
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

}
