package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AttrController {

    @Reference
    AttrService attrService;

    @RequestMapping("getAttrListByCtg3Id")
    @ResponseBody
    public List<BaseAttrInfo> getAttrListByCtg3Id(String catalog3Id){
        List<BaseAttrInfo> baseAttrInfos = attrService.getAttrListByCtg3Id(catalog3Id);

        return baseAttrInfos;

    }


    @RequestMapping("saveAttr")
    @ResponseBody
    public String saveAttr(BaseAttrInfo baseAttrInfo){
        attrService.saveAttr(baseAttrInfo);

        return "success";

    }

    @RequestMapping("getAttrList")
    @ResponseBody
    public List<BaseAttrInfo> getAttrList(String catalog3Id){

        List<BaseAttrInfo> baseAttrInfos = attrService.getAttrList(catalog3Id);

        return baseAttrInfos;

    }
}
