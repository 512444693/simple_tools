package com.zm.mix.mail_notify;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;

;

/**
 * Created by zhangmin on 2015/7/14.
 */
public class HttpTest {
    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
        String data = "POST /bulletscreenmsg?md=1234&gcid=1234567890123456789012345678901234567890 HTTP/1.1"+"\r\n";
        data += "Host: 192.168.202.81:1380"+"\r\n";
        data += "Content-Length: 12"+"\r\n";
        data += "Content-Type: application/x-www-form-urlencoded"+"\r\n";
        data += "Accept: */*"+"\r\n\r\n";
        data += "中华联邦";
        send("192.168.202.81", 1380, data.getBytes("utf-8"));
    }
    public static byte[] send(String host, int port, byte[] data) throws IllegalStateException, InterruptedException {
        Socket s = null;
        BufferedInputStream in = null;
        BufferedOutputStream out =null;
        byte[] dataRec = new byte[0];
        try {
            s = new Socket();
            s.connect(new InetSocketAddress(host,port),1000);
        } catch (IOException e) {
            throw new IllegalStateException("连接服务器错误，服务器没启动或ip、端口错误");
        }
        try {
            s.setSoTimeout(60000);
            in = new BufferedInputStream(s.getInputStream());
            out = new BufferedOutputStream(s.getOutputStream());

            /*
            out.write(data, 0, data.length-15);
            System.out.print(new String(data, 0, data.length-15));
            for(int i=15; i>=3; i-=3)
            {
                out.write(data, data.length-i, 3);
                System.out.print(new String(data, data.length-i, 3));
                Thread.sleep(5000);
            }*/

            out.write(data);
            out.flush();
            //s.shutdownOutput();
            System.out.print(new String(data));
            System.out.println("------------------------------------------------");
            byte[] dataTemp = new byte[1024];
            int len = 0;
            if((len = in.read(dataTemp)) != -1 )
            {
                System.out.print(new String(dataTemp, 0, len));
            }
            System.out.println("------------------------------------------------");
        } catch (IOException e) {
            try {
                if(s != null)
                    s.close();
                if(in != null)
                    in.close();
                if(out != null)
                    out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                System.out.print("关闭资源异常");
            }
            throw new IllegalStateException("60秒内服务器没有回包，断开连接");
        }
        finally {
            try {
                if(s != null)
                    s.close();
                if(in != null)
                    in.close();
                if(out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("关闭资源异常");
            }
        }
        return dataRec;
    }
}
