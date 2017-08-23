package com.baixinping.frameuse.service;

import com.baixinping.frameuse.entity.Customer;
import com.baixinping.framework.common.helper.DataBaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public void insert(Customer customer){
        DataBaseHelper.insert(customer);
    }

    public void delete(Serializable id){
        DataBaseHelper.delete(id);
    }

    public void update(Customer customer){
        DataBaseHelper.update(customer);
    }

    public Customer getById(Serializable id){
        return DataBaseHelper.selectOne(id, Customer.class);
    }
    public List<Customer> list(){
        return  DataBaseHelper.select(Customer.class);
    }
}
