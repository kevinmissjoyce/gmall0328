package com.atguigu.gmall.cart;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallCartWebApplicationTests {
	public static void main(String[] args) {
		//1 初始化
		BigDecimal b1 = new BigDecimal(0.01d);
		BigDecimal b2 = new BigDecimal(0.01f);
		BigDecimal b3 = new BigDecimal("0.01");

		System.out.println(b1);
		System.out.println(b2);
		System.out.println(b3);

		// 2 大小比较
		int i = b1.compareTo(b2);
		System.out.println(i);

		// 3 加减乘除运算
		BigDecimal b4 = new BigDecimal("7");
		BigDecimal b5 = new BigDecimal("6");

		BigDecimal add = b4.add(b5);
		System.out.println(add);

		BigDecimal subtract = b4.subtract(b5);
		System.out.println(subtract);

		BigDecimal multiply = b4.multiply(b5);

		System.out.println(multiply);

		// 约数，保留小数

		BigDecimal divide = b4.divide(b5,2,BigDecimal.ROUND_HALF_DOWN);
		System.out.println(divide);
		BigDecimal add1 = b1.add(b2);
		System.out.println(add1);
		BigDecimal bigDecimal = add1.setScale(3, BigDecimal.ROUND_HALF_DOWN);
		System.out.println(bigDecimal);
	}

	@Test
	public void contextLoads() {
	}

}
