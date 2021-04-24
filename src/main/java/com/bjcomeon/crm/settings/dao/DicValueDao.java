package com.bjcomeon.crm.settings.dao;

import com.bjcomeon.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);

}
