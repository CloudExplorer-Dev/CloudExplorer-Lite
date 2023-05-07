package com.fit2cloud.constants;

/**
 * @author jianneng
 * @date 2023/1/29 09:30
 **/
public class AnalysisConstants {

    public enum OptimizeSuggestEnum {
        /**
         * 优化建议建议降配
         */
        DERATING("derating", "建议降配", 1, 0, "byResourceUsedRateStrategy"),
        UPGRADE("upgrade", "建议升配", 2, 0, "byResourceUsedRateStrategy"),
        PAYMENT("payment", "建议变更付费方式", 3, 0, "byPaymentStrategy"),
        RECOVERY("recovery", "建议回收", 4, 0, "byRecoveryStrategy"),
        ;
        private final int index;
        private final String optimizeSuggestCode;
        private final String name;
        private final int value;
        private final String className;

        OptimizeSuggestEnum(String optimizeSuggestCode, String name, Integer index, Integer value, String className) {
            this.index = index;
            this.optimizeSuggestCode = optimizeSuggestCode;
            this.name = name;
            this.value = value;
            this.className = className;
        }

        public String getName() {
            return name;
        }

        public String getOptimizeSuggestCode() {
            return optimizeSuggestCode;
        }

        public int getIndex() {
            return index;
        }

        public int getValue() {
            return value;
        }

        public String getClassName() {
            return className;
        }

        public static String getName(String optimizeSuggestCode) {
            for (OptimizeSuggestEnum constant : OptimizeSuggestEnum.values()) {
                if (constant.getOptimizeSuggestCode().equals(optimizeSuggestCode)) {
                    return constant.name;
                }
            }
            return optimizeSuggestCode;
        }

        public static OptimizeSuggestEnum byCode(String optimizeSuggestCode) {
            for (OptimizeSuggestEnum constant : OptimizeSuggestEnum.values()) {
                if (constant.getOptimizeSuggestCode().equals(optimizeSuggestCode)) {
                    return constant;
                }
            }
            return null;
        }
    }


    /**
     * 比较符
     */
    public static class CompareSymbolsEnum {
        public static final String G_T = ">";
        public static final String L_T = "<";
        public static final String G_E = ">=";
        public static final String L_E = "<=";

    }

    /**
     * 优化策略条件
     */
    public enum OptimizeConditionEnum {
        /**
         * 优化建议建议降配
         */
        AVG("AVG", "平均"),
        MAX("MAX", "最大"),
        MIN("MIN", "最小"),
        OR_COMPARISON_CONDITION("OR", "或"),
        AND_COMPARISON_CONDITION("AND", "并且"),
        ;
        private final String value;
        private final String label;

        OptimizeConditionEnum(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value;
        }

        public static String getLabel(String code) {
            for (OptimizeConditionEnum constant : OptimizeConditionEnum.values()) {
                if (constant.getValue().equals(code)) {
                    return constant.getLabel();
                }
            }
            return code;
        }

        public static OptimizeConditionEnum valueOfCode(String code) {
            for (OptimizeConditionEnum constant : OptimizeConditionEnum.values()) {
                if (constant.getValue().equals(code)) {
                    return constant;
                }
            }
            return null;
        }
    }

}
