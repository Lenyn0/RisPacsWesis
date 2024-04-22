package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.dao.DiseaseDictionaryDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.DiseaseDictionary;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author 北京动力节点
 */
public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
    private DiseaseDictionaryDao diseaseDictionaryDao = SqlSessionUtil.getSqlSession().getMapper(DiseaseDictionaryDao.class);

    public int updateDisDicBodyPartByID(String id, String bodyPart){
        return diseaseDictionaryDao.updateDisDicBodyPartByID(id,bodyPart);
    }

    public List<DiseaseDictionary> getDisDicByBodyPart(String bodyPart){
        return diseaseDictionaryDao.getByBodyPart(bodyPart);
    }

    public int updateDicValueByID(String id, String value){
        return dicValueDao.updateDicValueByID(id, value);
    }

    public DicValue getDicValueByID(String id){
        return dicValueDao.getByID(id);
    }

    public DicValue getValueByID(String id) {
        return dicValueDao.getByID(id);
    }

    public List<DiseaseDictionary> getDisIDByValue(String value) {
        return diseaseDictionaryDao.getDisIDByValue(value);
    }
    public int updateDisDicValueByID(String id, String value){
        return diseaseDictionaryDao.updateDisDicValueByID(id,value);
    }

    public int saveDicValue(DicValue dicValue){
        return dicValueDao.saveDicValue(dicValue);
    }

    public PaginationVO<DicValue> pageList_Value(Map<String, Object> map) {
        //取得total,可能会有条件
        int total = dicValueDao.getTotalByCondition(map);
        System.out.println("total:"+total);

        //取得dataList，可能会有条件
        List<DicValue> dataList = dicValueDao.getValueListByCondition(map);
        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<DicValue> vo = new PaginationVO<DicValue>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        System.out.println("datalist"+dataList);
        //将vo返回
        return vo;
    }

    public int getMaxOrderNo(String bodyPart){
        int max = 0;
        List<String> data = dicValueDao.getMaxOrderNo(bodyPart);
        for(int i=0; i<data.size();i++){
            int order = Integer.parseInt(data.get(i));
            if(order-max>0){
                max = order;
            }
        }
        return max;
    }

    public int updateDisDicByID(DiseaseDictionary diseaseDictionary){
        return diseaseDictionaryDao.updateDisDicByID(diseaseDictionary);
    }

    public int deleteDisDicByID(String id){
        return diseaseDictionaryDao.deleteDisDicByID(id);
    }

    public DiseaseDictionary getDisDicByID(String id) {
        return diseaseDictionaryDao.getByID(id);
    }

    public int saveDisDic(DiseaseDictionary diseaseDictionary){
        return diseaseDictionaryDao.saveDisDic(diseaseDictionary);
    }

    public PaginationVO<DiseaseDictionary> pageList_Disease(Map<String, Object> map) {
        //取得total,可能会有条件
        int total = diseaseDictionaryDao.getTotalByCondition(map);
        System.out.println("total:"+total);

        //取得dataList，可能会有条件
        List<DiseaseDictionary> dataList = diseaseDictionaryDao.getDiseaseListByCondition(map);
        //dataList.set("等待填写",);
        System.out.println("dataList结果:");
        for(int i=0;i< dataList.size();i++)
            System.out.println(dataList.get(i));
        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<DiseaseDictionary> vo = new PaginationVO<DiseaseDictionary>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        System.out.println("vo值：");
        System.out.println(vo.toString());
        //将vo返回
        return vo;
    }

    public int deleteDicValueByID(String id){
        return dicValueDao.deleteDicValueByID(id);
    }

    public int deleteDisDicByBodyPart(String bodyPart){
        return diseaseDictionaryDao.deleteDisDicByBodyPart(bodyPart);
    }

    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();
        //将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();
        //将字典类型列表遍历
        for(DicType dt : dtList){
            //取得每一种类型的字典类型编码
            String code = dt.getCode();
            //根据每一个字典类型来取得字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);
            map.put(code+"List", dvList);
        }
        return map;
    }
    public Map<String, List<DiseaseDictionary>> getDiseaseAll() {
        Map<String, List<DiseaseDictionary>> map = new HashMap<String, List<DiseaseDictionary>>();
        //取出所有身体部位的value
        String code = "bodyParts";
        List<String> bodyPartList = dicValueDao.getbodyPartList(code);
        //遍历疾病字典,根据每个身体部位取得疾病列表
        for(String bodyPart : bodyPartList){
            List<DiseaseDictionary> ddList = diseaseDictionaryDao.getListBybodyPart(bodyPart);
            map.put(bodyPart+"List",ddList);
        }
        return map;
    }
}




















