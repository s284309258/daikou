package com.merchant.util.rsa;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import javax.crypto.Cipher;
import org.bouncycastle.util.encoders.Base64;

/**
*
* @author: XieminQuan
* @time  : 2007-11-20 下午04:10:22
*
* DNAPAY
*/

public class RSAEnc {
	
	private static final String ENCODING = "UTF-8";
	
    //公钥加密
	public static String encrypt(String data,String pub_key) {
		
		try {
	        KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(pub_key));
	        RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, pbk);
			
			byte[] encDate = cipher.doFinal(data.getBytes(ENCODING));
			
			return Base64.toBase64String(encDate);
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	//私钥解密
    public static String decrypt(String sign_msg, String pfx_path, String pfx_pass) {

        try {		
            RSAPrivateKey pbk = getPrivateKey(pfx_path, pfx_pass);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.DECRYPT_MODE, pbk);

            byte[] btSrc = cipher.doFinal(Base64.decode(sign_msg));
            
            return new String(btSrc,ENCODING);

        } catch (Exception e) {
			e.printStackTrace();
            return "";
        }
    }

    public static RSAPrivateKey getPrivateKey(String keyPath, String passwd) throws Exception {

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(keyPath);
            
            char[] nPassword = null;
            if ((passwd == null) || passwd.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = passwd.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
            
            Enumeration<?> enumq = ks.aliases();
            String keyAlias = null;
            if (enumq.hasMoreElements()) 
            {
                keyAlias = (String) enumq.nextElement();
            }

            PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);

            return (RSAPrivateKey) prikey;
        } catch (Exception e) {
			e.printStackTrace();
            return null;
        }
    }

    
    public static RSAPublicKey getPublicKey(String keyPath, String passwd) throws Exception {

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
        	
            FileInputStream fis = new FileInputStream(keyPath);
            
            char[] nPassword = null;
            if ((passwd == null) || passwd.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = passwd.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();

            Enumeration<?> enumq = ks.aliases();
            String keyAlias = null;
            if (enumq.hasMoreElements()) 
            {
                keyAlias = (String) enumq.nextElement();
            }

            Certificate cert = ks.getCertificate(keyAlias);
            
            PublicKey pubkey = cert.getPublicKey();

            return (RSAPublicKey) pubkey;
        } catch (Exception e) {
			e.printStackTrace();
            return null;
        }
    }
    
}
