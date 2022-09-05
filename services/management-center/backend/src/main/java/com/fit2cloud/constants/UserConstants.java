package com.fit2cloud.constants;

/**
 * Author: LiuDi
 * Date: 2022/8/30 5:35 PM
 */
public interface UserConstants {

    public String getValue();

    public enum Source implements UserConstants {

        LOCAL("local"),  EXTRA("extra");

        private String value;

        Source(String value) {
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

    public enum Status implements UserConstants {

        ACTIVE("active"), DISABLED("disabled");

        private String value;

        Status(String value) {
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
