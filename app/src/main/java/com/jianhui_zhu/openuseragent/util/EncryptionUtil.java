package com.jianhui_zhu.openuseragent.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jianhuizhu on 2016-03-06.
 */
public final class EncryptionUtil {
    public static String hash(String plaintext){
        try {
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(plaintext.getBytes());
            byte[] byteData=md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
