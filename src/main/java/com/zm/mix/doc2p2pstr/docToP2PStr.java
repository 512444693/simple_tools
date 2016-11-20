package com.zm.mix.doc2p2pstr;

import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmin on 2016/1/14.
 * 将word表格中的数据转换成p2p格式数据
 *
 * 还会输出三种不同缩进格式的json，以第一个数据为数组，其它数据为数组成员
 */
public class docToP2PStr {
    public static void main(String[] args) throws IOException {
        String docStr = "";
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(new File("in.txt")));
            char[] buffer = new char[4096];
            int len = fileIn.read(buffer);
            docStr = new String(buffer, 0 ,len);
            docStr = docStr.trim();
            if(docStr.trim().equals(""))
                throw new IllegalStateException("输入字符串为空");
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedWriter fileOut = null;
        fileOut = new BufferedWriter(new FileWriter(new File("out.txt")));

        StringBuilder out = new StringBuilder();
        ArrayJson array = new ArrayJson("", null);

        boolean netWin  = countOrderNet(docStr);
        String docLines[] = docStr.split("\r\n");
        if(!netWin)
            out.append("[config]\r\norder = host\r\n\r\n");
        for(int i = 0; i < docLines.length; i++){
            String docs[] = docLines[i].split("\t");
            StringBuilder tmp = new StringBuilder();
            if((docs[docs.length - 1].indexOf("本") != -1 || docs[docs.length - 1].indexOf("主") != -1)&& netWin)
                tmp.append("!");

            if(docs[docs.length - 1].indexOf("网") != -1 && !netWin)//网络序
                tmp.append("!");

            switch (docs[1].toLowerCase()){
                case "int8" :
                    tmp.append("1@");
                    break;
                case "int16" :
                    tmp.append("2@");
                    break;
                case "int32" :
                    if(docs[0].toLowerCase().indexOf("ip") != -1){
                        tmp.append("i@");
                        break;
                    }
                    tmp.append("4@");
                    break;
                case "int64" :
                    tmp.append("8@");
                    break;
                case "array" :
                    tmp.append("a@");
                    break;
                case "string" :
                    if(docs[0].toLowerCase().indexOf("id") != -1){
                        tmp.append("h@");
                        break;
                    }
                    tmp.append("s@");
                    break;
                default:
                    if(docs[1].toLowerCase().indexOf("array") !=-1 || docs[1].toLowerCase().indexOf("数组") != -1){
                        tmp.append("a@");
                        break;
                    }
                    tmp.append(docs[1] + "@");
            }
            tmp.append(docs[0] + " = *");
            out.append(tmp + "\r\n");
            if(i == 0){
                array.setValue(tmp.toString());
                array.setArray(new ArrayList<ArrayJson[]>());
                ArrayJson[] group = new ArrayJson[docLines.length - 1];
                array.getArray().add(group);
            }
            else {
                ArrayJson arrayJson = new ArrayJson(tmp.toString(), null);
                array.getArray().get(0)[i - 1] = arrayJson;
            }
        }
        //添加自己格式的json串
        out.append("=======================================\r\n");
        StringBuilder jsonStr = new StringBuilder("{\"value\": \"" + array.getValue());
        jsonStr.append("\",\"array\": [\r\n\t[");
        int i = 0;
        for(i = 0; i < array.getArray().get(0).length - 1; i++){
            jsonStr.append("{\"value\": \"" + array.getArray().get(0)[i].getValue() + "\"},");
        }
        jsonStr.append("{\"value\": \"" + array.getArray().get(0)[i].getValue() + "\"}");
        jsonStr.append("]\r\n]}");
        out.append(jsonStr);

        out.append("\r\n=======================================");
        //添加gson格式的json串
        out.append("\r\n" + new GsonBuilder().disableHtmlEscaping().create().toJson(array) + "\r\n");

        out.append("=======================================\r\n");
        //添加gson缩进格式的json串
        out.append(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(array) + "\r\n\r\n");

        System.out.print(out);
        fileOut.write(out.toString());
        fileOut.close();
    }

    //网络序占多数或一样多返回true，主机序占多数返回false
    public static boolean countOrderNet(String docStr){
        int net = 0;
        int host = 0;
        String docLines[] = docStr.split("\r\n");
        for(int i = 0; i < docLines.length; i++){
            String docs[] = docLines[i].split("\t");
            if(docs[docs.length -1].indexOf("本") != -1 || docs[docs.length -1].indexOf("主") != -1)//本地序，主机序
                host++;
            else net++;
        }
        return net>=host?true:false;
    }
}

class ArrayJson {
    public List<ArrayJson[]> getArray() {
        return array;
    }

    public void setArray(List<ArrayJson[]> array) {
        this.array = array;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private List<ArrayJson[]> array;
    private String value;

    public ArrayJson(String value, List<ArrayJson[]> array) {
        this.array = array;
        this.value = value;
    }
}

