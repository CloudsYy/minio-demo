package com.example.miniodemo.Controller;

import java.io.File;

public class test {
    public static void main(String[] args) {
        String str = "1.jpdgs";
        if (str.contains(".jpg")&&str.contains(".gif")&&str.contains("png")&&str.contains(".txt")){
            File file = new File("C:\\User\\");
        }
        System.out.println(str.contains(".jpg"));
    }
}
