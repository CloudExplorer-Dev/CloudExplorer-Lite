package com.fit2cloud.service;

import com.fit2cloud.dto.DemoObject;

public interface IDemoService {

    DemoObject getDemoObject();

    DemoObject setDemoObjectValue(String value);

}
