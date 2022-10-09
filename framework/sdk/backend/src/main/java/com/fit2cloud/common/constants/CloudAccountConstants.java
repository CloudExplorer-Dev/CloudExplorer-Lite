package com.fit2cloud.common.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;

public class CloudAccountConstants {

    public enum Status {
        INIT(-1),
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
