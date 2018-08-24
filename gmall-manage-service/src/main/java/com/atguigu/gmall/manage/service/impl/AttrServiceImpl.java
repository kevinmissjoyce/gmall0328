package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3Id) {

        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> select = baseAttrInfoMapper.select(baseAttrInfo);
        return select;
    }

    @Override
    public void saveAttr(BaseAttrInfo baseAttrInfo) {

        baseAttrInfoMapper.insertSelective(baseAttrInfo);

        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

        for (BaseAttrValue baseAttrValue : attrValueList) {
            baseAttrValue.setAttrId(baseAttrInfo.getId());

            baseAttrValueMapper.insert(baseAttrValue);
        }
    }

    @Override
    public void deleteAttr(BaseAttrInfo baseAttrInfo) {

        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValue);

        baseAttrInfoMapper.delete(baseAttrInfo);
    }

    @Override
    public List<BaseAttrInfo> getAttrListByCtg3Id(String catalog3Id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> select = baseAttrInfoMapper.select(baseAttrInfo);

        for (BaseAttrInfo attrInfo : select) {
            String attrId = attrInfo.getId();

            BaseAttrValue baseAttrValue = new BaseAttrValue();
            baseAttrValue.setAttrId(attrId);
            List<BaseAttrValue> select1 = baseAttrValueMapper.select(baseAttrValue);

            attrInfo.setAttrValueList(select1);
        }

        return select;
    }

    @Override
    public List<BaseAttrInfo> getAttrListByValueIds(Set<String> valueIds) {

        if (valueIds!=null&&valueIds.size()>0){
            String join = StringUtils.join(valueIds, ",");

            List<BaseAttrInfo> baseAttrInfos = baseAttrValueMapper.selectAttrListByValueIds(join);
            return baseAttrInfos;
        }else {
            return null;
        }

    }
}
