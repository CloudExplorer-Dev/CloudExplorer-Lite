package com.fit2cloud.constants;

/**
 * 特殊属性
 * @author jianneng
 * @date 2023/3/15 12:13
 **/
public class SpecialAttributesConstants {

    private SpecialAttributesConstants(){}
    public static class ResourceField {
        private ResourceField(){}
        public static final String CPU = "cpu";
        public static final String MEMORY = "memory";
        public static final String HOST = "host";
        public static final String VM = "vm";
        public static final String DATASTORE = "datastore";

        //...
    }
    public static class SpecialField {
        private SpecialField(){}
        public static final String ID = "id";
        public static final String AVERAGE = "average";
        public static final String MAX = "max";
        public static final String MIN = "min";
        public static final String AVERAGE_VALUE = "avgValue";
        public static final String MAX_VALUE = "maxValue";
        public static final String MIN_VALUE = "minValue";
        public static final String TREE = "tree";
        public static final String ORG = "org";
        public static final String ALL = "all";

        public static final String AND = "and";

        //...
    }

    public static class StatusField {

        private StatusField(){}
        public static final String VM_DELETE = "Deleted";
        public static final String FAILED = "Failed";
        public static final String VM_RUNNING = "Running";
        public static final String STOPPED = "Stopped";
        public static final String DISK_DELETE = "deleted";
        public static final String AVAILABLE = "available";
        public static final String IN_USE = "in_use";
        public static final String OTHER = "Other";

        //...
    }

}
