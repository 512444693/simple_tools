package com.zm.mix.bits2str;

/**
 * Created by zhangmin on 2015/12/16.
 */
public class StrUtils {
    public static String bitsToStr(long bits){
        double tmp = 0.0d;
        if(bits < KBytes){
            tmp = bits / 8.0;
            return String.format("%.2f\tB", tmp);
        }
        if(bits < MBytes){
            tmp = bits / (8.0 * 1024.0);
            return String.format("%.2f\tKB", tmp);
        }
        tmp = bits / (8.0 * 1024.0 * 1024.0);
        return String.format("%.2f\tMB", tmp);
    }

    public static void main(String[] args){
        System.out.println(bitsToStr(15));
        System.out.println(bitsToStr(234));
        System.out.println(bitsToStr(19341));
        System.out.println(bitsToStr(94987234));
    }

    private static final long oneByte = 8;
    private static final long KBytes = 8 * 1024;
    private static final long MBytes = 8 * 1024 * 1024;

}
