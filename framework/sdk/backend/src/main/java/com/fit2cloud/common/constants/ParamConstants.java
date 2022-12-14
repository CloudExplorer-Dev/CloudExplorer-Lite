package com.fit2cloud.common.constants;

/**
 * @author jianneng
 * @date 2022/12/11 15:15
 **/
public interface ParamConstants {
    String getValue();

    enum Log implements ParamConstants {
        KEEP_LOGIN_MONTHS("log.keep.login.months"),
        KEEP_PERF_MONTHS("log.keep.perf.months"),
        KEEP_SYSTEM_MONTHS("log.keep.system.months"),
        KEEP_API_MONTHS("log.keep.api.months");

        private String value;

        Log(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
