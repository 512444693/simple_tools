package com.zm.mix.chart; /**
 * Created by Administrator on 2016/8/4.
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//JFreeChart Line Chart（折线图）
public class MyChart {

    // 保存为文件
    public static void saveAsFile(JFreeChart chart, String outputPath,
                                  int weight, int height) {
        FileOutputStream out = null;
        try {
            File outFile = new File(outputPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(outputPath);
            // 保存为PNG
            // ChartUtilities.writeChartAsPNG(out, chart, 600, 400);
            // 保存为JPEG
            ChartUtilities.writeChartAsJPEG(out, chart, 600, 400);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    // 根据CategoryDataset创建JFreeChart对象
    public static JFreeChart createChart(CategoryDataset categoryDataset) {
        // 创建JFreeChart对象：ChartFactory.createLineChart
        JFreeChart jfreechart = ChartFactory.createLineChart("Load Chart", // 标题
                "time", // categoryAxisLabel （category轴，横轴，X轴标签）
                "num", // valueAxisLabel（value轴，纵轴，Y轴的标签）
                categoryDataset, // dataset
                PlotOrientation.VERTICAL, true, // legend
                false, // tooltips
                false); // URLs
        // 使用CategoryPlot设置各种参数。以下设置可以省略。
        CategoryPlot plot = jfreechart.getCategoryPlot();
        // 背景色 透明度
        //plot.setBackgroundAlpha(0.5f);
        // 前景色 透明度
        //plot.setForegroundAlpha(0.5f);
        // 其他设置 参考 CategoryPlot类
        /*LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
        renderer.setBaseShapesVisible(true); // series 点（即数据点）可见
        renderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见
        renderer.setUseSeriesOffset(true); // 设置偏移量
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);*/

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        BasicStroke realLine = new BasicStroke(2.8f); // 设置实线
        // 设置虚线
        float dashes[] = { 5.0f };
        BasicStroke brokenLine = new BasicStroke(3.2f, // 线条粗细
                BasicStroke.CAP_ROUND, // 端点风格
                BasicStroke.JOIN_ROUND, // 折点风格
                8f, dashes, 0.6f);
        for (int i = 0; i < 7; i++) {
            if (i % 2 == 0)
                renderer.setSeriesStroke(i, realLine); // 利用实线绘制
            else
                renderer.setSeriesStroke(i, brokenLine); // 利用虚线绘制
        }

        plot.setNoDataMessage("无对应的数据，请重新查询。");
        //plot.setNoDataMessageFont(titleFont);//字体的大小
        plot.setNoDataMessagePaint(Color.RED);//字体颜色

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        return jfreechart;
    }

    /**
     * 创建CategoryDataset对象
     *
     */
    public static CategoryDataset createDataset() throws IOException {
        //String[] rowKeys = {"cmd101"};
        //String[] colKeys = {"0:00", "1:00", "2:00", "7:00", "8:00", "9:00","10:00", "11:00", "12:00", "13:00", "16:00", "20:00", "21:00","23:00"};
        //double[][] data = {{4, 3, 1, 1, 1, 1, 2, 2, 2, 1, 8, 2, 1, 1},};

        // 或者使用类似以下代码
        DefaultCategoryDataset categoryDataset = new
        DefaultCategoryDataset();
        /*categoryDataset.addValue(10, "cmd101", "1991");categoryDataset.addValue(10, "cmd101", "1992");
        categoryDataset.addValue(10, "cmd102", "1991");categoryDataset.addValue(10, "cmd102", "1992");*/

        //return DatasetUtilities.createCategoryDataset(rowKeys, colKeys, data);

        char[] tmp = new char[4096];
        CharArrayWriter  charArrayWriter = new CharArrayWriter();
        File file = new File("C:\\Users\\Administrator\\Desktop\\2016_07.cmd.log");
        BufferedReader br = new BufferedReader(new FileReader(file));
        int len = 0;
        while ((len = br.read(tmp)) != -1) {
            charArrayWriter.write(tmp, 0, len);
        }
        String content = charArrayWriter.toString();

        Pattern pattern = Pattern.compile("2016-07-31 (\\d{2}:\\d{2}):\\d{2}: <<cmd>>:last 300 " +
                "\\S+cmd101:([0-9]+)\\S+cmd103:([0-9]+)\\S+cmd105:([0-9]+)\\S+" +
                "cmd107:([0-9]+)\\S+cmd113:([0-9]+)\\S+cmd258:([0-9]+)\\]\\s+" +
                ".+current size:([0-9]+)\\S+");

        Matcher matcher = pattern.matcher(content);
        String[] rowKeys = {"", "",  "101", "103", "105", "107", "113", "all", "socket"};
        int index = 0;
        while (matcher.find()) {
            /*System.out.println(matcher.group(1) + "\t" + matcher.group(2) + "\t" +
                    matcher.group(3) + "\t" + matcher.group(4) + "\t" + matcher.group(5) +
                    "\t" + matcher.group(6) + "\t" + matcher.group(7) + "\t" +matcher.group(8));*/
            if(index++ %4 == 0) {
                categoryDataset.addValue(Integer.parseInt(matcher.group(2)), rowKeys[2], matcher.group(1));
                categoryDataset.addValue(Integer.parseInt(matcher.group(3)), rowKeys[3], matcher.group(1));
                categoryDataset.addValue(Integer.parseInt(matcher.group(4)), rowKeys[4], matcher.group(1));
                categoryDataset.addValue(Integer.parseInt(matcher.group(5)), rowKeys[5], matcher.group(1));
                categoryDataset.addValue(Integer.parseInt(matcher.group(6)), rowKeys[6], matcher.group(1));
                categoryDataset.addValue(Integer.parseInt(matcher.group(7)), rowKeys[7], matcher.group(1));
                categoryDataset.addValue(Integer.parseInt(matcher.group(8)), rowKeys[8], matcher.group(1));
            }
        }
        br.close();
        return categoryDataset;
    }

    /**
     * 创建JFreeChart Line Chart（折线图）
     */
    public static void main(String[] args) throws IOException {
        // 步骤1：创建CategoryDataset对象（准备数据）
        CategoryDataset dataset = createDataset();
        // 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
        JFreeChart freeChart = createChart(dataset);
        // 步骤3：将JFreeChart对象输出到文件，Servlet输出流等
        //saveAsFile(freeChart, "E:\\line.jpg", 600, 400);

        ChartFrame chartFrame = new ChartFrame("ConSrv负载图", freeChart, true);
        chartFrame.pack();
        chartFrame.setVisible(true);

    }
}