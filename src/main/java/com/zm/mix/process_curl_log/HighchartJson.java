package com.zm.mix.process_curl_log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmin on 2016/12/23.
 */
public class HighchartJson {
    //title, subtitle, yAxis, data Array
    private final String allTpl = "{\"title\":{\"text\":\"%s\"},\"subtitle\":{\"text\":\"%s\"},\"exporting\":{},\"yAxis\":[{\"title\":{\"text\":\"%s\"}}],\"xAxis\":[{}],\"series\":[%s],\"plotOptions\":{\"series\":{\"animation\":true}}}";

    private String title;
    private String subTitle;
    private String yAxis;

    private DataItems dataItems = new DataItems();
    private Datas datas = new Datas();

    public HighchartJson(String title, String subTitle, String yAxis) {
        this.title = title;
        this.subTitle = subTitle;
        this.yAxis = yAxis;
    }

    public void addDataItem(String time, String value) {
        dataItems.addDataItem(time, value);
    }

    public void saveAsData(String name, int index) {
        datas.addData(dataItems.toJson(), name, index);
        dataItems.clear();
    }

    public String toJson() {
        return String.format(allTpl, title, subTitle, yAxis, datas.toJson());
    }

    public static void main(String[] args) {
        DataItems dataItems = new DataItems();
        dataItems.addDataItem("123", "null");
        System.out.println(dataItems.toJson());
    }
}

class DataItems {
    //time, value
    private final String dataItemTpl = "[\"%s\",%S]";

    private List<String> array = new ArrayList<>();

    public void addDataItem(String time, String value) {
        array.add(String.format(dataItemTpl, time, value));
    }

    public String toJson() {
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < array.size(); i++) {
            out.append(array.get(i)).append(",");
        }

        if(array.size() < 1) {
            return "";
        }
        return out.toString().substring(0, out.length() - 1);
    }

    public void clear() {
        array.clear();
    }
}

class Datas {
    //dataItem array, name, index, index
    private final String dataTpl = "{\"data\":[%s],\"name\":\"%s\",\"turboThreshold\":0,\"_colorIndex\":%d,\"_symbolIndex\":%d}";

    private List<String> array = new ArrayList<>();

    public void addData(String dataItems, String name, int index) {
        array.add(String.format(dataTpl, dataItems, name, index, index));
    }

    public String toJson() {
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < array.size(); i++) {
            out.append(array.get(i)).append(",");
        }
        if(array.size() < 1) {
            return "";
        }
        return out.toString().substring(0, out.length() - 1);
    }
}