package com.by.ris_springboot.settings.dao;


import com.by.ris_springboot.settings.domain.DicValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);

    List<String> getbodyPartList(String code);

    List<String> getMaxOrderNo(String bodyPart);

    int saveDicValue(DicValue dicValue);

    int getTotalByCondition(Map<String, Object> map);

    List<DicValue> getValueListByCondition(Map<String, Object> map);

    DicValue getByID(String id);

    int updateDicValueByID(@Param("id") String id, @Param("value") String value);

    int deleteDicValueByID(String id);
}
