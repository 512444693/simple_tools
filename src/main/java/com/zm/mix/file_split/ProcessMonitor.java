package com.zm.mix.file_split;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zhangmin on 2015/4/14.
 * Usage:
 * new ProcessMonitor().register(this).setFinalValue(100).setFunctionName("getValue");
 */
public class ProcessMonitor extends Thread {

    @Override
    public void run() {
        long temp = 0;
        try {
            while((temp =getValue()) <= finalValue)
            {
                if(temp == now)
                    continue;
                now =temp;
                printProcess(finalValue, temp);
                if(temp == finalValue)
                {
                    System.out.println();
                    break;
                }
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void printProcess(long max, long now)
    {
        double percent = (now+0.0)/max;
        int finished =(int) (percent * 50);
        System.out.print("\r[");
        for(int i=0; i<finished; i++)
            System.out.print("#");
        for(int i=finished; i<50; i++)
            System.out.print(" ");
        System.out.print("]"+(int) (percent*100)+"%");
    }

    public ProcessMonitor register(Object obj)
    {
        this.obj = obj;
        return this;
    }

    public ProcessMonitor setFinalValue(long finalValue)
    {
        this.finalValue = finalValue;
        return this;
    }

    public ProcessMonitor setFunctionName(String functionName){
        this.functionName = functionName;

        Class aClass = obj.getClass();
        try {
            getValueMethod = aClass.getMethod(functionName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.start();
        return this;
    }

    private long getValue() throws InvocationTargetException, IllegalAccessException
    {
        return (Long) getValueMethod.invoke(obj);
    }
    private Object obj = null;
    private long finalValue = 0;
    private String functionName = "";
    private Method getValueMethod = null;

    private static long now = 0;
}


