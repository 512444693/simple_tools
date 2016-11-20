package com.zm.mix.history2sh;

import java.io.*;

/**
 * Created by zhangmin on 2014/12/30.
 */
public class History2Sh {
    public static void main(String[] args) throws IOException {

        if(args.length != 2)
        {
            System.out.println("use eg: java History2Sh a.sh b.sh");
            return;
        }

        File file = new File(args[0]);

        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        PrintWriter out = new PrintWriter(new File(args[1]));

        String line;
        while((line = in.readLine()) != null)
        {
            line = line.replaceAll("[0-9]{3,}","");
            line = line.replaceAll("    ","");
            line = line.replaceAll("   ","");
            line = line.replaceAll("  ","");
            line += ";";
            out.println(line);
        }
        in.close();
        out.close();
    }
}
