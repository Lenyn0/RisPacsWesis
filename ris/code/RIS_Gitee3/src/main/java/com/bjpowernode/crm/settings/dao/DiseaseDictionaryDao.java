package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DiseaseDictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DiseaseDictionaryDao {

    List<DiseaseDictionary> getListBybodyPart(String bodyPart);

    String getDiseaseDescription(String name);

    String getDiseaseNameById(String id);

    int getTotalByCondition(Map<String, Object> map);

    List<DiseaseDictionary> getDiseaseListByCondition(Map<String, Object> map);

    List<DiseaseDictionary> getDisIDByValue(String value);

    int saveDisDic(DiseaseDictionary diseaseDictionary);

    int deleteDisDicByID(String id);

    DiseaseDictionary getByID(String id);

    int updateDisDicByID(DiseaseDictionary diseaseDictionary);

    int updateDisDicValueByID(@Param("old_value") String old_value, @Param("value")String value);

    List<DiseaseDictionary> getByBodyPart(String bodyPart);

    int updateDisDicBodyPartByID(String id, String bodyPart);

    int deleteDisDicByBodyPart(String bodyPart);
}
