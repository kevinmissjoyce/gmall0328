package com.atguigu.gmall.order.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequire;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.bean.OrderDetail;
import com.atguigu.gmall.bean.OrderInfo;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.enums.PaymentWay;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @Reference
    CartService cartService;

    @Reference
    SkuService skuService;

    @Reference
    UserService userService;

    @Reference
    OrderService orderService;


    @LoginRequire(ifNeedSuccess = true)
    @RequestMapping("submitOrder")
    private String submitOrder(String tradeCode,HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        String userId = (String) request.getAttribute("userId");
        // 比较交易码
        boolean btradeCode = orderService.checkTradeCode(tradeCode,userId);

        // 订单对象
        OrderInfo orderInfo = new OrderInfo();
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();

        if(btradeCode){
            //执行提交订单的业务

            // 获取购物车中被选中的商品数据
            List<CartInfo> cartInfos = cartService.getCartCacheByChecked(userId);

            // 生成订单信息
            // 验价、眼库存
            for (CartInfo cartInfo : cartInfos) {
                OrderDetail orderDetail = new OrderDetail();
                BigDecimal skuPrice = cartInfo.getSkuPrice();
                String skuId = cartInfo.getSkuId();
                // 验价
                boolean bprice = skuService.checkPrice(skuPrice,skuId);
                // 验库存
                if (bprice){
                    orderDetail.setSkuName(cartInfo.getSkuName());
                    orderDetail.setSkuId(cartInfo.getSkuId());
                    orderDetail.setOrderPrice(cartInfo.getCartPrice());
                    orderDetail.setImgUrl(cartInfo.getImgUrl());
                    orderDetail.setSkuNum(cartInfo.getSkuNum());
                    orderDetails.add(orderDetail);
                }else {
                    // sku校验失败
                    map.put("errMsg","订单中的商品价格(库存)发生了变化，请重新确认订单");
                    return "tradeFail";
                }
            }
            orderInfo.setOrderDetailList(orderDetails);
            // 封装订单信息
            orderInfo.setProcessStatus("订单未支付");
            // 日期
            // 日期的计算
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE,1);
            orderInfo.setExpireTime(c.getTime());
            orderInfo.setOrderStatus("未支付");
            String consignee = "测试收件人";
            orderInfo.setConsignee(consignee);
            // 外部订单号
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String format = sdf.format(new Date());
            String outTradeNo = "ATGUIGU"+format+System.currentTimeMillis();
            orderInfo.setOutTradeNo(outTradeNo);
            orderInfo.setPaymentWay(PaymentWay.ONLINE);
            orderInfo.setUserId(userId);
            orderInfo.setTotalAmount(getTotalPrice(cartInfos));
            orderInfo.setOrderComment("硅谷订单");
            String address = "测试收件地址";
            orderInfo.setDeliveryAddress(address);
            orderInfo.setCreateTime(new Date());
            String tel = "123123123123123";
            orderInfo.setConsigneeTel(tel);

            String orderId = orderService.saveOrder(orderInfo);

            orderService.saveOrder(orderInfo);

            // 删除购物车中的提交的商品信息,同步缓存
            cartService.deleteCartById(cartInfos);


            // 对接支付系统接口
            return "redirect:http://payment.gmall.com:8087/index?orderId="+orderId;

        }else{
            map.put("errMsg","获取订单信息失败");
            return "tradeFail";
        }

    }

    /***
     * 订单系统中，必须登陆才能访问的方法
     * @param request
     * @param response
     * @param cartInfo
     * @param map
     * @return
     */
    @LoginRequire(ifNeedSuccess = true)
    @RequestMapping("toTrade")
    private String toTrade(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        // 需要被单点登录的拦截器
        String userId = (String) request.getAttribute("userId");

        // 将被选中的购物车对象转化为订单对象，展示出来
        List<CartInfo> cartInfos = cartService.getCartCacheByChecked(userId);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartInfo cartInfo : cartInfos) {
            OrderDetail orderDetail = new OrderDetail();

            // 将购物车对象转化为订单对象
            orderDetail.setImgUrl(cartInfo.getImgUrl());
            orderDetail.setOrderPrice(cartInfo.getCartPrice());
            orderDetail.setSkuId(cartInfo.getSkuId());
            orderDetail.setSkuName(cartInfo.getSkuName());

            orderDetails.add(orderDetail);
        }
//
        // 查询用户收获地址列表，让用户选择
        List<UserAddress> userAddressList = userService.getUserAddressList(userId);
//
        // 生成交易码
        String tradeCode = orderService.genTradeCode(userId);
        map.put("tradeCode", tradeCode);
        map.put("userAddressList", userAddressList);
        map.put("orderDetailList", orderDetails);
        map.put("totalAmount", getTotalPrice(cartInfos));
        return "trade";
    }


    private BigDecimal getTotalPrice(List<CartInfo> cartInfos) {
        BigDecimal b = new BigDecimal("0");
        for (CartInfo cartInfo : cartInfos) {

            if(cartInfo.getIsChecked().equals("1")){
                b = b.add(cartInfo.getCartPrice());
            }

        }
        return b;
    }

}
