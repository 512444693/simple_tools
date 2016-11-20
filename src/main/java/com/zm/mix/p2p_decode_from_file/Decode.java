package com.zm.mix.p2p_decode_from_file;

import com.zm.Field.CompareResult;
import com.zm.message.Message;
import com.zm.utils.BytesUtils;

import java.io.*;

/**
 * Created by Administrator on 2016/11/20.
 */
public class Decode {
    public static void decode(String filePath, String p2pStrPath) throws IOException {

        //读取二进制文件
        File file = new File(filePath);
        if (!file.isFile()) {
            System.out.println(filePath + " is not file");
            return;
        }
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream bytesBuffer = new ByteArrayOutputStream();
        int len = 0;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        while((len = in.read(buffer)) != -1) {
            bytesBuffer.write(buffer, 0, len);
        }
        byte[] factData = bytesBuffer.toByteArray();

        //读取p2p字符串文件
        file = new File(p2pStrPath);
        if (!file.isFile()) {
            System.out.println(filePath + " is not file");
            return;
        }
        bytesBuffer.reset();
        in = new BufferedInputStream(new FileInputStream(file));
        while((len = in.read(buffer)) != -1) {
            bytesBuffer.write(buffer, 0, len);
        }
        String p2pStr = new String(bytesBuffer.toByteArray());

        Message expect = new Message(p2pStr);
        expect.encode();
        Message fact = new Message(p2pStr, factData);
        fact.decode();
        System.out.println("解码后数据如下：\r\n" + fact);
        if(fact.dataCntLeftToDecode() > 0) {
            System.out.println("还剩" + fact.dataCntLeftToDecode() + "字节数据没有解码");
        }

        CompareResult compareResult = expect.compare(fact);
        if(!compareResult.equal){
            String expectStr = "\r\n================预期================\r\n" + expect;
            String factStr = "\r\n================实际================\r\n" + fact;

            System.out.println(compareResult.msg + expectStr + factStr);
        }


        bytesBuffer.close();
        in.close();
    }

    public static void main(String []args) throws IOException {
        /*BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("D:\\p2p.bao"));
        out.write(BytesUtils.hex2Bytes("0000000b000000021400"));
        out.flush();
        out.close();*/

        //decode("D:\\p2p.bao", "D:\\p2p.txt");
        /*if(args.length != 2) {
            System.out.println("use eg: 二进制文件路径 p2p字符串路径");
            return;
        }
        decode(args[0], args[1]);*/
    }
}
