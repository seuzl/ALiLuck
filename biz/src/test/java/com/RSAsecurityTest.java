package com;

import com.taobao.lottery.biz.algorithm.RSAsecurity;
import net.iharder.Base64;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by LTX on 2016/8/14.
 */
public class RSAsecurityTest {

    /*@Test
    public void genKeyPair(){
        RSAsecurity.genKeyPair();
    }*/


    @Test
    public void pubEnpriDe(){
        String pubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5KzvrTSasuQC2n4JAccBrhXDhLsdx20+7cKymDBmrG+sdWWyeGPmXdZfInA8hdlQ/pmyE5BKYYbCgKgm87HhAS2lvUXsfCpWXtgpLrWYDDYpHUHSyolG6yGRZrRkeAhNaYlMg9pNRMEfRhplDkc9RW29AdNNzAWxqDrAB+SMk9wIDAQAB";
        String priKeyStr  = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALkrO+tNJqy5ALafgkBxwGuFcOEux3HbT7twrKYMGasb6x1ZbJ4Y+Zd1l8icDyF2VD+mbITkEphhsKAqCbzseEBLaW9Rex8KlZe2CkutZgMNikdQdLKiUbrIZFmtGR4CE1piUyD2k1EwR9GGmUORz1Fbb0B003MBbGoOsAH5IyT3AgMBAAECgYEAuBy30Ed+++BbSDojnYY9J+ufiBcmMhw5FAt9bwC7CPqSdTU0HaKBkUpYKBybos7wB/WnEx9fKeqAX1BNLG8HRFs7bu9L82XzsrVjc3oGCrgeGPj2GHKnv62t2Ow63iOiDve/BB3CzUBi+WI60QtVBnpWGtA5I1Y1cveaW4CdQlECQQDxKtvfzjdxuKnM1nHQ0npe6uobpn+WqZNN5NWbln5HAqAzGjBI3tAiWXTiDhWLNNQpsGzXazAjSPhx6U2BPyp/AkEAxI6wAI4zKhBlPPrBzw5Q1hgO+MGjVYOR1wrUc42veoJ4T2/k4r7eJF/WNXcii7pi3ANURoq+jQBwbwGxT1EZiQJBAKNofO8alWrSywrmStxJoWzrE5GEsXLul8bxxOLVh96pos69cfYOM0zy/EWncWWPeCp/RmB4WneuG9ljUYa/vBUCQEBcBE86QJCOa8fKx7kXk+p2WDillujCOvexyRzJEONJY0GHfuer3asFYmuBReOAtSwbPvYWKgybTzVZ1YgGIxkCQQDhdHdw3J+tlACDwAdTPnHcXdDXimDYoiG4sQItFj+sna8Sf9UMIYSvfwxRc5tDfEgnX3WbJqT3QqEnxkTSNeXT";
        String plainStr = "I am xuanlin";

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


        byte[] encryptData = null;
        try {
            encryptData = Base64.decode(cipher);
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
        try {
            res = new String(result,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("res = " + res);
    }

    @Test
    public void enDe(){
        String str = "xuanlin";
        String cipherStr = RSAsecurity.encriptString(str);
        String decript = RSAsecurity.decryptCipher(cipherStr);
        System.out.println("decript = " + decript);
    }
}
