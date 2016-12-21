package com.zm.mix.process_curl_log;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */
public class Main {
    public static void main(String[] args) throws IOException {
/*        if(args.length != 1) {
            System.out.println("Usage: java Main directory");
            return;
        }
        File dir = new File(args[0]);*/
        File dir = new File("C:\\Users\\zhangmin\\Desktop\\联通");
        if(!dir.isDirectory()) {
            System.out.println("Need a directory");
            return;
        }

        File[] files = dir.listFiles();
        File tmp = null;
        List<LineInfo> lineArray = new ArrayList<>();
        for(int i = 0; i < files.length; i++) {
            tmp = files[i];
            if(tmp.getName().endsWith(".log")) {
                lineArray.add(process(tmp));
            }
        }

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

        PrintWriter writer = new PrintWriter(new FileWriter(dir.getAbsolutePath() + File.separator + dir.getName() + ".csv"));
        writer.write(outBuffer.toString());
        writer.close();
    }

    public static LineInfo process(File file) throws IOException {
        LineInfo ret = new LineInfo();
        ret.name = file.getName().substring(2,4);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp = "";
        int lineNum = 0;
        boolean start = false;
        while((tmp = reader.readLine()) != null) {
            lineNum ++;
            String[] fields = tmp.trim().split("\\s+");
            if(lineNum != 1 && fields.length == 12) {
                if(fields[9].equals("0:00:01")){
                    start = true;
                }
                if (start) {
                    ret.list.add(parseToKb(fields[11]));
                }
            }
            if(lineNum == 20) {
                ret.name += "-" + fields[1];
            }
        }
        reader.close();
        return ret;
    }

    private static String parseToKb(String field) {
        if(!field.endsWith("k")) {
            int tmp = Integer.parseInt(field);
            tmp /= 1024;
            return tmp + "";
        }
        return field.substring(0, field.length() - 1);
    }
}

class LineInfo {
    List<String> list = new ArrayList<>();
    String name = "";
}
