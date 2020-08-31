package com.renegade.util;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 *
 *
 * @author wukq
 */
public class ValidateCode {

    private int width = 160;

    private int height = 40;

    private int codeCount = 5;

    private int lineCount = 150;

    private String code = null;

    private BufferedImage buffImg = null;


    private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
    								'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S',
    								'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    								'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
    								'k', 'm', 'n', 'p', 'q', 'r', 's',
    								't', 'u', 'v', 'w', 'x', 'y', 'z',
    								'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    

    //private char[] codeSequence = { '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    /**
     *
     */
    public ValidateCode() {
        this.createCode();
    }

    /**
     * @param width
     * @param height
     */
    public ValidateCode(int width, int height) {
        this.width = width;
        this.height = height;
        this.createCode();
    }

    /**
     * @param width
     * @param height
     * @param codeCount
     * @param lineCount
     */
    public ValidateCode(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        this.createCode();
    }

    public void createCode() {
        int x = 0, fontHeight = 0, codeY = 0;
        int red = 0, green = 0, blue = 0;

        x = width / (codeCount + 2);
        fontHeight = height - 2;
        codeY = height - 4;

        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();

        Random random = new Random();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
//        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
//        Font font = new Font("Times New Roman", Font.ROMAN_BASELINE, fontHeight);
        Font font = new Font("cmr10", Font.PLAIN, fontHeight);
        g.setFont(font);

        for (int i = 0; i < lineCount; i++) {

            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width / 8);
            int ye = ys + random.nextInt(height / 8);


            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }


        StringBuffer randomCode = new StringBuffer();

        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);

            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);

            randomCode.append(strRand);
        }

        code = randomCode.toString();
    }

    public void write(String path) throws IOException {
        OutputStream sos = new FileOutputStream(path);
        this.write(sos);
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public String getCode() {
        return code;
    }


    public static void main(String[] args) {
        ValidateCode vCode = new ValidateCode(120,43,5,100);
        try {
            String path="D:/"+new Date().getTime()+".png";
            System.out.println(vCode.getCode()+" >"+path);
            vCode.write(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
