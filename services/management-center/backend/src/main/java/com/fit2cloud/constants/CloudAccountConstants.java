package com.fit2cloud.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;

public class CloudAccountConstants {

    public enum Status {

        SUCCESS(0),
        FAILED(1),
        SYNCING(2);

        @EnumValue
        private final int code;

        Status(int code) {
            this.code = code;
        }
    }

}
