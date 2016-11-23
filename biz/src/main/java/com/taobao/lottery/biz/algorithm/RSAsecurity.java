package com.taobao.lottery.biz.algorithm;

import net.iharder.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by LTX on 2016/8/14.
 */


public class RSAsecurity {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static void genKeyPair() {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        SecureRandom secureRandom = new SecureRandom();
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, secureRandom);
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 得到公钥字符串
        String publicKeyString = Base64.encodeBytes(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = Base64.encodeBytes(privateKey.getEncoded());

        System.out.println("privateKeyString = " + privateKeyString);

        System.out.println("publicKeyString = " + publicKeyString);


    }

    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        } catch (IOException e) {
            throw new Exception("io出错");
        }
    }



    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
            throws Exception {
        if (privateKeyStr != null) {

            try {
                byte[] buffer = Base64.decode(privateKeyStr);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            } catch (NoSuchAlgorithmException e) {
                throw new Exception("无此算法");
            } catch (InvalidKeySpecException e) {
                throw new Exception("私钥非法");
            } catch (NullPointerException e) {
                throw new Exception("私钥数据为空");
            }
        }

        return null;
    }

    public static byte[] encryptByPubKey(RSAPublicKey publicKey, byte[] plainTextData)
            throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    public static byte[] decryptByPriKey(RSAPrivateKey privateKey, byte[] cipherData)
            throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 字节数据转十六进制字符串
     *
     * @param data
     *            输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

    public static String decryptCipher(String cipherStr){
        String priKeyStr  = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALkrO+tNJqy5ALafgkBxwGuFcOEux3HbT7twrKYMGasb6x1ZbJ4Y+Zd1l8icDyF2VD+mbITkEphhsKAqCbzseEBLaW9Rex8KlZe2CkutZgMNikdQdLKiUbrIZFmtGR4CE1piUyD2k1EwR9GGmUORz1Fbb0B003MBbGoOsAH5IyT3AgMBAAECgYEAuBy30Ed+++BbSDojnYY9J+ufiBcmMhw5FAt9bwC7CPqSdTU0HaKBkUpYKBybos7wB/WnEx9fKeqAX1BNLG8HRFs7bu9L82XzsrVjc3oGCrgeGPj2GHKnv62t2Ow63iOiDve/BB3CzUBi+WI60QtVBnpWGtA5I1Y1cveaW4CdQlECQQDxKtvfzjdxuKnM1nHQ0npe6uobpn+WqZNN5NWbln5HAqAzGjBI3tAiWXTiDhWLNNQpsGzXazAjSPhx6U2BPyp/AkEAxI6wAI4zKhBlPPrBzw5Q1hgO+MGjVYOR1wrUc42veoJ4T2/k4r7eJF/WNXcii7pi3ANURoq+jQBwbwGxT1EZiQJBAKNofO8alWrSywrmStxJoWzrE5GEsXLul8bxxOLVh96pos69cfYOM0zy/EWncWWPeCp/RmB4WneuG9ljUYa/vBUCQEBcBE86QJCOa8fKx7kXk+p2WDillujCOvexyRzJEONJY0GHfuer3asFYmuBReOAtSwbPvYWKgybTzVZ1YgGIxkCQQDhdHdw3J+tlACDwAdTPnHcXdDXimDYoiG4sQItFj+sna8Sf9UMIYSvfwxRc5tDfEgnX3WbJqT3QqEnxkTSNeXT";
        byte[] encryptData = null;
        try {
            encryptData = Base64.decode(cipherStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RSAPrivateKey privateKey = null;
        byte[] result = null;
        try{
            privateKey = RSAsecurity.loadPrivateKeyByStr(priKeyStr);
            result = RSAsecurity.decryptByPriKey(privateKey,encryptData);
        }catch (Exception e){
            e.printStackTrace();
        }


        String res = null;
        if(result != null) {
            try {
                res = new String(result,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("res = " + res);
        }
        return res;
    }

    public static String encriptString(String plainStr){
        String pubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5KzvrTSasuQC2n4JAccBrhXDhLsdx20+7cKymDBmrG+sdWWyeGPmXdZfInA8hdlQ/pmyE5BKYYbCgKgm87HhAS2lvUXsfCpWXtgpLrWYDDYpHUHSyolG6yGRZrRkeAhNaYlMg9pNRMEfRhplDkc9RW29AdNNzAWxqDrAB+SMk9wIDAQAB";

        RSAPublicKey publicKey = null;
        byte[] cipherData = null;
        try {
            publicKey = RSAsecurity.loadPublicKeyByStr(pubKeyStr);
            cipherData = RSAsecurity.encryptByPubKey(publicKey,plainStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String cipher = Base64.encodeBytes(cipherData);
        System.out.println("加密后cipher = " + cipher);
        return cipher;
    }



}