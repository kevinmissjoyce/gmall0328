package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.util.MyUploadUtil;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class SpuController {

    @Reference
    SpuService spuService;

    @RequestMapping("getSpuImageListBySpuId")
    @ResponseBody
    public List<SpuImage> getSpuImageListBySpuId(String spuId){

        List<SpuImage> spuImages = spuService.getSpuImageListBySpuId(spuId);

        return spuImages;
    }


    @RequestMapping("getSaleAttrListBySpuId")
    @ResponseBody
    public List<SpuSaleAttr> getSaleAttrListBySpuId(String spuId){

        List<SpuSaleAttr> spuSaleAttrs = spuService.getSaleAttrListBySpuId(spuId);

        return spuSaleAttrs;
    }


    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file")  MultipartFile file){
        // fdfs的上传工具
        String imgUrl = MyUploadUtil.uploadImage(file);
        return imgUrl;

    }

    @RequestMapping("saveSpu")
    @ResponseBody
    public String saveSpu(SpuInfo spuInfo){
        spuService.saveSpu(spuInfo);
        return "success";

    }

    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<BaseSaleAttr> baseSaleAttrList(){

        List<BaseSaleAttr> baseSaleAttrs = spuService.baseSaleAttrList();
        return baseSaleAttrs;

    }


    @RequestMapping("spuList")
    @ResponseBody
    public List<SpuInfo> spuList(String catalog3Id){

        List<SpuInfo> spuInfos = spuService.spuList(catalog3Id);
        return spuInfos;

    }


}
