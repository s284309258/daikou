package com.renegade.config;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/5/8.
 */
public class RandomNumber {


    private static final String POSSIBLE_CHARS="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String POSSIBLE_CHARS2="0123456789";
    private static final String POSSIBLE_CHARS3="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(POSSIBLE_CHARS2.charAt(random.nextInt(POSSIBLE_CHARS2.length())));
        }
        return sb.toString();
    }
    public static String generateRandomString2(int length) {
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(POSSIBLE_CHARS.charAt(random.nextInt(POSSIBLE_CHARS.length())));
        }
        return sb.toString();
    }

    


    public static String generateRandomString3(int length,String s) {
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        length = length-6-s.length();
        String date = new SimpleDateFormat("yyMMdd").format(new Date());
        sb.append(s);
        sb.append(date);
        for (int i = 0; i < length; i++) {
            sb.append(POSSIBLE_CHARS2.charAt(random.nextInt(POSSIBLE_CHARS2.length())));
        }
        return sb.toString();
    }


    public static Integer generateRandomString3(int length) {
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        //String date = new SimpleDateFormat("yyMMdd").format(new Date());
        //sb.append(date);
        for (int i = 0; i < length; i++) {
            sb.append(POSSIBLE_CHARS2.charAt(random.nextInt(POSSIBLE_CHARS2.length())));
        }
        return Integer.valueOf(sb.toString());
    }
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            System.out.println(RandomNumber.generateRandomString3(8));
            //System.out.println(RandomNumber.generateRandomString3(15,"g"));
            //String date = new SimpleDateFormat("yyMMddHHmm").format(new Date());
            //System.out.println(date);
        }

    }



}
