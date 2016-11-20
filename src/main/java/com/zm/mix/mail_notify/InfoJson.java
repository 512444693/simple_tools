package com.zm.mix.mail_notify;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by zhangmin on 2015/8/7.
 */
public class InfoJson {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMeg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString(){
        String ret = "";
        for(int i =0; i<data.size(); i++){
            ret += data.get(i).getTime() + "\t" + data.get(i).getEvent() + "\r\n";
        }
        return ret;
    }

    private int status;
    private List<Data> data;
    private String msg;
    public static void main(String[] args){
        String result = "{\"status\":0,\"data\":[{\"event\":\"\\u8bc6\\u8d27\\u5df2\\u4e0b\\u5355\",\"time\":\"2015-07-28 19:58:22\"},{\"event\":\"\\u5546\\u5bb6\\u5df2\\u53d1\\u8d27\",\"time\":\"2015-07-29 09:50:08\"},{\"event\":\"\\u8f6c\\u8fd0\\u516c\\u53f8\\u5df2\\u5165\\u5e93\\uff0c\\u5f85\\u88c5\\u8fd0\",\"time\":\"2015-07-31 04:51:50\"},{\"event\":\"\\u5df2\\u88c5\\u8fd0\\uff0c\\u53d1\\u5f80\\u7f8e\\u56fd\\u673a\\u573a\",\"time\":\"2015-08-01 01:58:13\"},{\"event\":\"\\u5df2\\u8d77\\u98de\\uff0c\\u53d1\\u5f80\\u4e2d\\u56fd\",\"time\":\"2015-08-04 10:25:00\"},{\"event\":\"\\u5df2\\u964d\\u843d\\uff0c\\u6b63\\u5728\\u6e05\\u5173\",\"time\":\"2015-08-04 18:04:00\"}],\"msg\":\"success\"}";
        Gson gson = new Gson();

        InfoJson infoJson = gson.fromJson(result, InfoJson.class);
        System.out.println(infoJson);
    }
}

class Data{
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    private String event;
    private String time;
}
