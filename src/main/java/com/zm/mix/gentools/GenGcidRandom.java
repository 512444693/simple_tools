package com.zm.mix.gentools;

import java.util.Date;
import java.util.Random;

/**
 * Created by zhangmin on 2015/7/15.
 *
 */
public class GenGcidRandom {
    public static void main(String[] args)
    {
        for(int i=0; i<500; i++)
            System.out.println(genGcid()+","+genMd());
    }

    public static String genGcid()
    {
        String ret = "";
        for(int i=0; i<40; i++)
        {
            ret += mask.charAt(random.nextInt(mask.length()));
        }
        return ret;
    }

    public static int genMd()
    {
        int ret = 0;
        ret = (random.nextInt(60) +60) * 60000;
        return ret;
    }

    static String mask = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    static Random random = new Random(new Date().getTime());
}
