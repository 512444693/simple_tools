package com.zm.mix.gentools; /**
 * Created by zhangmin on 2015/4/8.
 */

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class GenToken {
    public static void main(String[] args) throws Exception
    {
        if(args.length != 4)
        {
            System.out.println("Usage: java Main ak sk id deadline");
            return;
        }
        String ak = args[0];
        String sk = args[1];
        String id = args[2];
        long deadline = System.currentTimeMillis()/1000+Integer.parseInt(args[3]);

        String url = "id="+id+"&deadline="+deadline;
        String encodedUrl = new String(Base64.encodeBase64(url.getBytes()));
        System.out.println("encodeUrl:  "+encodedUrl);
        byte[] sign = MyHmacSHA1(encodedUrl.getBytes(), sk.getBytes());
        String encodedSign = new String(Base64.encodeBase64(sign));
        System.out.println("encodedSign:  "+encodedSign);
        String token = ak+":"+encodedSign+":"+encodedUrl;
        System.out.println("token:  "+token);

    }
    public static byte[] MyHmacSHA1(byte[] data, byte[] key) throws Exception
    {
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA1");

        Mac mac = Mac.getInstance(secretKey.getAlgorithm());

        mac.init(secretKey);

        return mac.doFinal(data);
    }
}
