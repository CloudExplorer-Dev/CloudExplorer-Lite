package com.fit2cloud.quartz;

import org.quartz.spi.InstanceIdGenerator;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class QuartzInstanceIdGenerator implements InstanceIdGenerator {

    public QuartzInstanceIdGenerator() {
    }

    public String generateInstanceId() {
        try {
            return InetAddress.getLocalHost().getHostName() + System.currentTimeMillis();
        } catch (UnknownHostException var2) {
            return "unknown-" + System.currentTimeMillis();
        }
    }
}
