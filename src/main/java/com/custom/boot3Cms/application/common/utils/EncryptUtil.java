package com.custom.boot3Cms.application.common.utils;


import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 암호화 / 복호화 Util
 */
public class EncryptUtil {

    public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    public static String key;

    @Value("${Globals.sekorea.key}")
    private void setkey(String key){
        this.key = key;
    }

    /**
     * SHA256 암호화 함수
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String fn_encryptSHA256(String plainText){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = new byte[0];
        try {
            hash = digest.digest(plainText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return String.valueOf(hexString);
    }

}
