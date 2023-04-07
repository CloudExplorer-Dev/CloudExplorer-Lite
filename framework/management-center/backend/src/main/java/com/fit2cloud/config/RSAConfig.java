package com.fit2cloud.config;

import com.fit2cloud.common.utils.RSAUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Order(-1)
public class RSAConfig {

    @PostConstruct
    public void getKeyPairs() {
        RSAUtil.getKeyPair(true);
    }

}
