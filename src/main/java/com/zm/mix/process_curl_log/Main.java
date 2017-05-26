package com.zm.mix.process_curl_log;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2016/12/20.
 */
public class Main {
    public static final String rootDir = "C:\\Users\\zhangmin\\Desktop\\baishanyun_download_test";
    public static final File tplFile = new File(rootDir + File.separator + "bus_cdn_test.tpl");
    public static String tplStr = "";
    public static final File htmlFileStr = new File(rootDir + File.separator + "bus_cdn_test.html");

    public static final String div = "<div id=\"%s\" style=\"width: 700px; height: 300px; float: left;\"></div>";
    public static final String js = "$.getJSON(\"%s.json\", function(data){\n" +
            "        $('#%s').highcharts(data);\n" +
            "    });";
    public static String divStr = "";
    public static String jsStr = "";

    public static void init() throws IOException {
        StringBuilder strs = new StringBuilder();
        char[] buffer = new char[1024];
        BufferedReader reader = new BufferedReader(new FileReader(tplFile));
        int len = 0;
        while((len = reader.read(buffer)) != -1) {
            strs.append(buffer, 0, len);
        }
        reader.close();
        tplStr = strs.toString();
    }

    public static void writeHtml(String div, String js) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(htmlFileStr));
        String tmp = String.format(tplStr, div, js);
        writer.write(tmp);
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        init();
        File dir = new File(rootDir);
        if(!dir.isDirectory()) {
            System.out.println("Need a directory");
            return;
        }
        dirProcess(dir);
        writeHtml(divStr, jsStr);
    }

    public static void dirProcess(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files.length >= 1 && files[0].isFile()) {
            logProcess(dir);
            return;
        }
        for(int i = 0; i < files.length; i++) {
            if(files[i].isDirectory()) {
                dirProcess(files[i]);
            }
        }
    }

    public static void logProcess(File dir) throws IOException {
        System.out.println("Process " + dir.getAbsolutePath());
        File[] files = dir.listFiles();
        File tmp = null;
        List<LineInfo> lineArray = new ArrayList<>();
        for(int i = 0; i < files.length; i++) {
            tmp = files[i];
            if(tmp.getName().endsWith(".log")) {
                lineArray.add(process(tmp));
            }
        }

        writeCsv(lineArray, dir.getName());
        writeJson(lineArray, dir.getName());

        String name = dir.getName();
        divStr += String.format(div,name) + "\r\n";
        jsStr += String.format(js, name, name) + "\r\n";
    }

    public static void writeCsv(List<LineInfo> lineArray, String dirName) throws IOException {
        StringBuffer outBuffer = new StringBuffer();
        outBuffer.append("time");
        int maxLine = 0;
        for (int i = 0; i < lineArray.size(); i++) {
            LineInfo tmpLI = lineArray.get(i);
            outBuffer.append("," + tmpLI.name);
            if(tmpLI.list.size() > maxLine) {
                maxLine = tmpLI.list.size();
            }
        }
        outBuffer.append("\r\n");

        for(int j = 0; j < maxLine; j++) {
            Date date = new Date(j*1000);
            outBuffer.append(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
            for (int i = 0; i < lineArray.size(); i++) {
                LineInfo tmpLI = lineArray.get(i);
                if(j >= tmpLI.list.size()) {
                    outBuffer.append(",");
                }else {
                    outBuffer.append("," + tmpLI.list.get(j));
                }
            }
            outBuffer.append("\r\n");
        }

        PrintWriter writer = new PrintWriter(new FileWriter(rootDir + File.separator + dirName + ".csv"));
        writer.write(outBuffer.toString());
        writer.close();
    }

    public static void writeJson(List<LineInfo> lineArray, String dirName) throws IOException {
        HighchartJson json = new HighchartJson(Helper.getWhere(dirName), "", "速度，KB");

        //算出max line num
        int maxLine = 0;
        for (int i = 0; i < lineArray.size(); i++) {
            LineInfo tmpLI = lineArray.get(i);
            if(tmpLI.list.size() > maxLine) {
                maxLine = tmpLI.list.size();
            }
        }


        for (int i = 0; i < lineArray.size(); i++) {
            LineInfo tmpLI = lineArray.get(i);
            for(int j = 0; j < maxLine; j++) {
                Date date = new Date(j * 1000);
                String time = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                if(j < tmpLI.list.size()) {
                    json.addDataItem(time, tmpLI.list.get(j));
                }
            }
            json.saveAsData(tmpLI.name, i);
        }

        PrintWriter writer = new PrintWriter(new FileWriter(rootDir + File.separator + dirName + ".json"));
        writer.write(json.toJson());
        writer.close();
    }

    public static LineInfo process(File file) throws IOException {
        LineInfo ret = new LineInfo();
        if (file.getName().contains("log.baishan.log")) {
            ret.name = "白山云-";
        }
        if (file.getName().contains("log.wangsu.log")) {
            ret.name = "网宿-";
        }
        if (file.getName().contains("log.tengxun.log")) {
            ret.name = "腾讯-";
        }
        ret.name += file.getName().substring(2,4);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp = "";
        int lineNum = 0;
        boolean start = false;
        while((tmp = reader.readLine()) != null) {
            lineNum ++;
            String[] fields = tmp.trim().split("\\s+");
            if(lineNum != 1 && fields.length == 12) {
                if(fields[9].contains("0:00")){
                    start = true;
                }
                if (start) {
                    ret.list.add(parseToKb(fields[11]));
                }
            }
            if(lineNum == 6) {
                ret.name += "-" + fields[1];
            }
        }
        reader.close();
        return ret;
    }

    private static String parseToKb(String field) {
        if(!field.endsWith("k")) {
            int tmp = 0;
            if(field.endsWith("M")) {
                tmp = (int) Float.parseFloat(field.substring(0, field.length() - 1));
                tmp *= 1024;
            } else {
                tmp /= 1024;
            }
            return tmp + "";
        }
        return field.substring(0, field.length() - 1);
    }
}

class LineInfo {
    List<String> list = new ArrayList<>();
    String name = "";
}

class Helper {
    public static String getWhere(String key) {
        Map<String, String> map = new HashMap<>();
        map.put("anhuiguangdian_61", "安徽广电");
        map.put("fujiantietong_145", "福建铁通");
        map.put("fujianyidong_200", "福建移动");
        map.put("guizhouguangdian_75", "贵州广电");
        map.put("guizhouyidong_165", "贵州移动");
        map.put("jilintietong_20", "吉林铁通");
        map.put("nanyangjiaoyu_215", "南阳教育");
        map.put("ningxiayidong_195", "宁夏移动");
        map.put("yunnatietong_88", "云南铁通");
        return map.get(key);
    }
}
