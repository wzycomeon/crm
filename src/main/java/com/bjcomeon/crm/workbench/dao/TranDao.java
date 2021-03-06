package com.bjcomeon.crm.workbench.dao;

import com.bjcomeon.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    Tran detail(String id);

    int changeStage(Tran t);

    int total();

    List<Map<String, Object>> getChars();
}
