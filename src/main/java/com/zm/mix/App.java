package com.zm.mix;

import com.zm.mix.p2p_decode_from_file.Decode;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
        if(args.length != 2) {
            System.out.println("use eg: 二进制文件路径 p2p字符串路径");
            return;
        }
        Decode.decode(args[0], args[1]);
    }
}
