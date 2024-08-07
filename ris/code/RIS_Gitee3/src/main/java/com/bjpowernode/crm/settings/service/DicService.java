package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.DiseaseDictionary;
import com.bjpowernode.crm.vo.PaginationVO;

import java.util.List;
import java.util.Map;

/**
 * Author 北京动力节点
 */
public interface DicService {
    Map<String, List<DicValue>> getAll();

    Map<String, List<DiseaseDictionary>> getDiseaseAll();

    PaginationVO<DiseaseDictionary> pageList_Disease(Map<String, Object> map);

    int saveDisDic(DiseaseDictionary diseaseDictionary);


    int deleteDisDicByID(String id);

    DiseaseDictionary getDisDicByID(String id);

    int updateDisDicByID(DiseaseDictionary diseaseDictionary);

    int getMaxOrderNo(String typeCode);

    int saveDicValue(DicValue dicValue);

    PaginationVO<DicValue> pageList_Value(Map<String, Object> map);

    int updateDicValueByID(String id, String value);

    List<DiseaseDictionary> getDisDicByBodyPart(String bodyPart);

    int updateDisDicBodyPartByID(String id, String bodyPart);

    int updateDisDicValueByID(String id, String value);

    DicValue getDicValueByID(String id);

    DicValue getValueByID(String id);

    List<DiseaseDictionary> getDisIDByValue(String value);

    int deleteDicValueByID(String id);

    int deleteDisDicByBodyPart(String bodyPart);
}
