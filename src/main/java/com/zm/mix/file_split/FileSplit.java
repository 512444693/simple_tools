package com.zm.mix.file_split;

import java.io.*;

/**
 * Created by zhangmin on 2015/4/14.
 */
public class FileSplit {
    public static void main(String[] args) throws FileNotFoundException {
        if(args.length !=2)
        {
            System.out.println("Usage: java FileSplit file_name file_number");
            return;
        }
        String fileName = args[0];
        int fileNum = Integer.parseInt(args[1]);
        new FileSplit(fileName).splitOff(fileNum);
    }

    public FileSplit(String fileName)
    {
        file = new File(fileName);
    }

    public void splitOff(int fileNum) throws FileNotFoundException {
        if(file.length() < 1024*1024*10)
        {
            System.out.println("文件小于10M，不需要分割");
            return;
        }

        //进度监控

        new ProcessMonitor().register(this)
                .setFinalValue(file.length())
                .setFunctionName("getIndex");
        //文件输入
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        //缓冲
        byte[] data = new byte[1024];
        //每个小文件大小
        long fileSize = file.length()/fileNum;
        for(int i=0; i<fileNum; i++)
        {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file.getPath()+"_"+i));
            try {
                int length = 0;
                while((length =in.read(data)) !=-1)
                {
                    index += length;
                    out.write(data,0,length);
                    if(index >= (i+1)*fileSize)
                    {
                        out.close();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public long getIndex() {
        return index;
    }
    File file = null;
    long index = 0;
}
