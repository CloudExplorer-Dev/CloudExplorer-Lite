package com.fit2cloud.service.impl;

import com.fit2cloud.dto.DemoObject;
import com.fit2cloud.service.IDemoService;
import org.springframework.stereotype.Service;

import java.util.random.RandomGenerator;


@Service
public class DemoServiceImpl implements IDemoService {

    private static DemoObject object = new DemoObject().setKey("demo").setValue("value");

    @Override
    public DemoObject getDemoObject() {
        return object;
    }

    @Override
    public DemoObject setDemoObjectValue(String value) {
        object.setValue(value);
        return object;
    }


}
