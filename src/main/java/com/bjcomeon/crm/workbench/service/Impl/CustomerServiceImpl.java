package com.bjcomeon.crm.workbench.service.Impl;

import com.bjcomeon.crm.utils.SqlSessionUtil;
import com.bjcomeon.crm.workbench.dao.CustomerDao;
import com.bjcomeon.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public List<String> getCustomerName(String name) {

        List<String> sList = customerDao.getCustomerName(name);
        return sList;
    }
}
