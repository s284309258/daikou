package service.encrypt;

import service.util.Base64;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

/**
*
* @author: XieminQuan
* @time  : 2007-11-20
*
* DNAPAY
*/

public class RSA_SHA256withRSA {
	
	private static final String ENCODING = "UTF-8";

    public static String sign(String data, String pfx_path, String key_pass) {

        try {

            RSAPrivateKey pbk = getPrivateKey(pfx_path, key_pass);

            java.security.Signature signet = java.security.Signature.getInstance("SHA256withRSA");
            signet.initSign(pbk);
            signet.update(data.getBytes(ENCODING));
            byte[] signed = signet.sign();

            return Base64.encode(signed);

        } catch (Exception e) {
			e.printStackTrace();
            return "";
        }
    }

	public static String encrypt(String data,String pub_key) {
		
		try {

	        KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(pub_key));
	        RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, pbk);
			
			byte[] encDate = cipher.doFinal(data.getBytes(ENCODING));
			
			return Base64.encode(encDate);
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}

		public static byte[] encrypt64(String data,String pub_key) {
			
			try {

		        KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
		        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(pub_key));
		        RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
				
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
				cipher.init(Cipher.ENCRYPT_MODE, pbk);
				
				byte[] encDate = cipher.doFinal(data.getBytes(ENCODING));
				
				return encDate;
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}

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

    public static boolean verify(String data, String pub_key, String value) {
    	
        try {
        	byte[] bts_data = Base64.decode(data);
        	byte[] bts_key = Base64.decode(pub_key);

            KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bts_key);
            RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);
            
            Signature signetcheck = java.security.Signature.getInstance("SHA256withRSA");
            signetcheck.initVerify(pbk);
            signetcheck.update(value.getBytes(ENCODING));
            
            return signetcheck.verify(bts_data);
            
        } catch (Exception e) {
			e.printStackTrace();
            return false;
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
            
            Enumeration enumq = ks.aliases();
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

            Enumeration enumq = ks.aliases();
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
    
    public static void main(String[] args) throws Exception {
    	String res= "wl9aM5d2Xa5lpavmkL1rPoPb6HOgOfhZGLJDCGPM0b2vGlOJ/o4WBwTgVzPTkiL+ieoECn7MnIv90OkMY42dHfv5M0bkuk3jMncH7wSr6dpUMYnumlO4bfM2tNP8JT5yKzCOn2I7/GFneBRExUNapc1+pM1nIsNFwGpncb19SUpheCcKWJgpE2kuaR+GznJzp7oDzcjJU75P544eUQcZ8lTGyRBloXtMyZDOgePnfNmVg8qyeCXMNQ0gPtgAX6alXcAUe6CIUMwt++avroyM/XRrYTTLIphUdK6ItAQNUnI3bTrpfUTz6QSZj5RQ+d/XmLJUKi4tdPvtbGvFOXxMQEtRClUg4gl2Yp+pIieNQcM0d0hkOz/bCUy/nLva60cScysD7oGIsYltIOil8UvcwcWYjqKCq0Kivk8NfvGfdfut+vEMtFwfrWd4FETFQ1ql7qsdydQIgTV3e9vE9Ksgp1AnreWl0WFNyNO6DYxVAzM/nz+au1+QD+nZl92c+eM4RfBehhsiOGZ8QgBQNWKqfSq0hfujIJoVT/5OavJfoFj1tFPM/XDohUokrbSId1NziEqilog+lRnZ+St89Gb7Jw5IypgatUf+8hnF/H/rP/rrNizqESOvIfbuR9QHwJUmTkAX422ICBxdf1ga2aj+/0pb/QlleBwCB4j4EAVWLg8WHtTWHSCI0wLfdMFSuji1RVeAd4tHqij/ZNgbc6p0GL53ujHrZp+SP7pBksAIXGThrZXfIB7uAhVVrGzVSFfj+Z3hAJVeFyH6cQTXsbWsAZ+G2/MWXbB/+YX4alWAJNspUpT040euoNp+YQAdtgfM+UJGIKi3HjNtgjJ9OZlWbQACqhWr+FeHSKCri2ehGH+d7YtXtuM7YWBWNqSRv89VQDsmYdrN/wIUy9MVWUq8rvhz10EPQZZmTUUlG1geP3kQYncz21LFMzpmjkzuVBKOQwPNx3eq8iDFI+ax1g//yC1e1HnXfut0veFWUQjQvUu6CAteZQAea/Aex0iVKrWozBynZjGQphaJo9jKc7Atfg==|Yr2aonI8sN74h4lQA7eiFJXUAGXorTnHrF1mjapNmu14Wf61MGPsTCQwjAFaGhYtGerQwq0jXM7QU+8RrnZx2+TXGfZwPXtOFgcDtzIm0StT0oISNwBckkrej822hk0flR6vgGelzkszOUHpczxRHmquBxxMvTfsiqj2/Wd+tKM=";

    	/*String dna_pub_key =      "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqWSfUW3fSyoOYzOG8joy3xldpBanLVg8gEDcvm9KxVjqvA/qJI7y0Rmkc1I7l9vAfWtNzphMC+wlulpaAsa/4PbfVj+WhoNQyhG+m4sP27BA8xuevNT9/W7/2ZVk4324NSowwWkaqo1yuZe1wQMcVhROz2h+g7j/uZD0fiCokWwIDAQAB";
		String merchant_pub_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqWSfUW3fSyoOYzOG8joy3xldpBanLVg8gEDcvm9KxVjqvA/qJI7y0Rmkc1I7l9vAfWtNzphMC+wlulpaAsa/4PbfVj+WhoNQyhG+m4sP27BA8xuevNT9/W7/2ZVk4324NSowwWkaqo1yuZe1wQMcVhROz2h+g7j/uZD0fiCokWwIDAQAB";
						*/		   
    	String dna_pub_key =      "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzWw3Qni0EBEwlb7VoEMIFCGZ4Cc+4+a9aDEVPI29WnyYVduBZ1Qo2cNUWvhVayVpWfZIvKMJfkrSr8/eFUkIakulqQNdMUIUkST7lrlivalhEvxxClYvMr1OAmuQKemKrHrdDUlG+h7oiMP9bJJpcNh+8NCjSeQEimkDJ7ESOvtuGUemgr8L2JKowPKtgvHJOmOLTMv55XUFZJn35BSQq6Bzn0osIwdU7peZrdfSTxa5p11NHRFVR84J//85Uc4Uk8Mb54JDw+kEwZSK8WOJcwRjDlbcRx0H463D4b64FYDm44F/Hx7IDJf1TqS1N7kV7TqnwZs24V4aaDOg2OmUJQIDAQAB";
		String merchant_pub_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzWw3Qni0EBEwlb7VoEMIFCGZ4Cc+4+a9aDEVPI29WnyYVduBZ1Qo2cNUWvhVayVpWfZIvKMJfkrSr8/eFUkIakulqQNdMUIUkST7lrlivalhEvxxClYvMr1OAmuQKemKrHrdDUlG+h7oiMP9bJJpcNh+8NCjSeQEimkDJ7ESOvtuGUemgr8L2JKowPKtgvHJOmOLTMv55XUFZJn35BSQq6Bzn0osIwdU7peZrdfSTxa5p11NHRFVR84J//85Uc4Uk8Mb54JDw+kEwZSK8WOJcwRjDlbcRx0H463D4b64FYDm44F/Hx7IDJf1TqS1N7kV7TqnwZs24V4aaDOg2OmUJQIDAQAB";
    	 		   
//    	String pfx_path = "C:\\yilian.pfx" ;
    	String pfx_path = "C:\\server.pfx" ;
    	String pfx_pass = "11111111";
    	
    	String xml = "<MSGBEAN><MSG_TYPE>200002</MSG_TYPE><BATCH_NO>99EE936559D864</BATCH_NO><USER_NAME>13760136514</USER_NAME><TRANS_STATE></TRANS_STATE><MSG_SIGN>";
    	String sign = RSA_SHA256withRSA.sign(xml, pfx_path, pfx_pass);
    	System.out.println("sign==="+sign);
    	System.out.println(verify(sign, dna_pub_key,xml ));
		/*if(true)
			return;*/

		String key = "0F91F44FB331EB39779F8961";
		String key_f = RSA.encrypt(Base64.encode(key.getBytes()), dna_pub_key);
		String key_enc = key_f;
//		String key_enc = "QhX+0f8JPqicd2HjJbM3p/A5aQw/xeAI/5G0bCirhy8zKCTNlUAAdF/mWx+lgvxOoBTJdGB30VZGZ+PGUN2elU1B8BVPgie9qEe/5WTfN4Ze9UEPtHdqFoaIWxE6vUcX7oiMUjn+veYJB2tRFtBrx9HKNvoo/coap4wMxq6KG0o=";
		String key_g = RSA.decrypt(key_enc, pfx_path, pfx_pass);
		System.out.println("key="+key_g );	

		if(true)
			return;
	}
}
