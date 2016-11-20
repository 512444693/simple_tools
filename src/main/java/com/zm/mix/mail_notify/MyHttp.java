package com.zm.mix.mail_notify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by zhangmin on 2015/8/7.
 */
public class MyHttp {
    public static void main(String[] args){
        System.out.println(MyHttp.HttpGet("http://www.shihuo.cn/haitao/getOrderLogistics?number=TBA810019560083&order_number=1507286738712029"));
    }

    public static String HttpGet(String url){
        String result = "";
        URL realUrl = null;
        URLConnection connection = null;
        BufferedReader in = null;
        try {
            realUrl = new URL(url);
            // 打开和URL之间的连接
            connection = realUrl.openConnection();

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("cookie", "_cnzz_CV30020080=buzi_cookie%7C03ac71ce.de65.f48f.3098.2358159ab9ab%7C-1; bdshare_firstime=1438052093104; _HUPUSSOID=35224f4a-e35f-4e8b-a5b7-55b868e68e56; g=26189686%7CaHVwdV84ZGRmN2FlNDUwMDE5N2Rm; u=26189686|bWl6aA==|37c5|a2759038fde38ab5297931d59e60a1c8|fde38ab5297931d5; _dacevid3=03ac71ce.de65.f48f.3098.2358159ab9ab; CNZZDATA30020080=cnzz_eid%3D1590920989-1438049599-null%26ntime%3D1438917361; CNZZDATA30089914=cnzz_eid%3D836676710-1438048594-null%26ntime%3D1438915360; _cnzz_CV30020080=buzi_cookie%7C03ac71ce.de65.f48f.3098.2358159ab9ab%7C-1; ua=16539509");

            // 建立实际的连接
            connection.connect();
/*
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
*/
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
