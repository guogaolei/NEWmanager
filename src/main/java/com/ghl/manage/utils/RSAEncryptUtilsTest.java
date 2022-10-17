package com.ghl.manage.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAEncryptUtilsTest {
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
// genKeyPair();
        //加密字符串
//        String message = "{\"username\":\"aaaa\"}";
//        System.out.println("随机生成的公钥为:" + keyMap.get(0));
//        System.out.println("随机生成的私钥为:" + keyMap.get(1));
//        String messageEn = encrypt(message,keyMap.get(0));
//        System.out.println(message + "\t加密后的字符串为:" + messageEn);
//        String messageDe = decrypt(messageEn,keyMap.get(1));
//        System.out.println("还原后的字符串为:" + messageDe);
    	String str="{\"username\":\"哈哈哈\"}";
    	String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDeuw1VKmvZUZ53X8+cQE6LNO7pyE54T3VbxG69rlgRlBioQe8Z6uOE+UDay/DefSqeLqbFNxG+CsghaEKXWl1P0f8chOrdd7jWHySCov2hD9ElC5Fo8jzCnuh+4ygOAJtDJtvM9cjesnM2Q1OmtaFvuo0XJ+Fqs3vtuT4qfm4CsQIDAQAB";
        String praviteKey="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAN67DVUqa9lRnndfz5xATos07unITnhPdVvEbr2uWBGUGKhB7xnq44T5QNrL8N59Kp4upsU3Eb4KyCFoQpdaXU/R/xyE6t13uNYfJIKi/aEP0SULkWjyPMKe6H7jKA4Am0Mm28z1yN6yczZDU6a1oW+6jRcn4Wqze+25Pip+bgKxAgMBAAECgYEAxpIFJPSZCjqWCN9GfItoevtKNOr76o8Mli/eewVARwu9n+SIgpbDGP4PMAG6UOFOaZA15oVeMAv0uLP3CnDp1mGpdoYk1Bme0DCTXIuFVsOTl5fbATNP6OqAgQ0t4tb/zLlYmEnyxeFr+GWrWlO2T0epvXDLbOkLNwkN8a6uLEECQQD6KB+Vhtle6ECHk6bjwdzt1jrEeAv3DKyVOSADa3BxuW82qUoTicqNU0ip44Kh7p3Ov+f1SK8ruI1RaP+kYl9dAkEA4+7tcanCT8xRUGU20PGfLyqxuWilP2TAECDaNqbvl70YTTOAyXyK5jdjI1UduBsM9P+DhVmP2pb7t+vnaNi/ZQJAAykWnREDFgvhJNVcumvsmqSmQW0y2YAzff8mxGRLG2S4XvxsjyqigH6Y4GybbehR3hbWm2a7r9W5E1o8QJttkQJBANnHGlUkZoTJ6dNPsW03vulY+I/2/RQx1hRS30uien9Xa9WvZNieRzQpLW6/z3JtXhbj3yzU4s5uvbkAC40wssUCQQDr4PiGpjuwhmk6ygZw6z5mlQzR32l7yGZQ4VDH2MSEyBGLkK2D7iiNAKNTGttKOcA9b36B/JTrIXKuaPUwxXBM";
    	String encrypt = encrypt(str, publicKey);
    	System.out.println(encrypt);
    	String decrypt = decrypt(encrypt, praviteKey);
    	System.out.println(decrypt);
    }

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
    }
    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }


}
