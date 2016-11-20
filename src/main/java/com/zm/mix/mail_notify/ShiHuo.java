package com.zm.mix.mail_notify;

import com.google.gson.Gson;

/**
 * Created by zhangmin on 2015/8/7.
 */
public class ShiHuo {
    public static void main(String[] args){
        String url = "http://www.shihuo.cn/haitao/getOrderLogistics?number=TBA810019560083&order_number=1507286738712029";
        String result = MyHttp.HttpGet(url);
        String temp = "";
        MyMail mail = new MyMail();
        Gson gson = new Gson();
        InfoJson infoJson = null;
        while(true){
            System.out.println("checking ...");
            temp =MyHttp.HttpGet(url);
            if(!result.equals(temp) && !temp.equals("")){
                result = temp;
                infoJson = gson.fromJson(result, InfoJson.class);
                mail.send("512444693@qq.com", "EXPRESS", infoJson.toString());
            }
            try {
                Thread.sleep(60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
