package com.nowcoder.community;

import org.junit.Test;

import java.io.IOException;

public class WkTests {

    public static void main(String[] args){
        String cmd = "d:/work/wkhtmltopdf/bin/wkhtmltoimage --quality 75 http://www.nowcoder.com d:/work/data/wk-imgs/3.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("Ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
