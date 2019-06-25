package com.jing.pojo;

import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * @author RanMoAnRan
 * @ClassName: test
 * @projectName gossip-parent
 * @description: TODO
 * @date 2019/6/22 19:31
 */
public class test {
    public static void main(String[] args) {
        String string = UUID.randomUUID().toString().replaceAll("-","");
        System.out.println(string);

        String date = "2019-11-11 12:22:12";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            System.out.println(simpleDateFormat.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
