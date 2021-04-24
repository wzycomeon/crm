package com.bjcomeon.crm.workbench.service;

import com.bjcomeon.crm.workbench.domain.Tran;
import com.bjcomeon.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);


    List<TranHistory> getHistoryListByTranId(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();

}
