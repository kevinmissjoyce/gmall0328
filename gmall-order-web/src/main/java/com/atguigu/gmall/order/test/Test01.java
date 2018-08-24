package com.atguigu.gmall.order.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test01 {

    public static void main(String[] args) {
        // 日期的计算
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR,-1);
        System.out.println(c.getTime());

        // 日期的格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        System.out.println(format);
    }
}
