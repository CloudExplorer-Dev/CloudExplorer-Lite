package com.fit2cloud.provider.entity;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/7  15:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum InstanceFieldCompare {
    NotExist("不存在", "doc[params.field].length==0"),

    Exist("存在", "doc[params.field].length>0"),

    NotEq("不等于", "doc[params.field].stream().allMatch(s->(s!=null&&params.value!=null&&s.toString()!=params.value.toString()))"),
    EQ("等于", "doc[params.field].stream().anyMatch(s->(s==null&&params==null)||(s!=null&&params.value!=null&&s.toString()==params.value.toString()))||(doc[params.field].length==0&&(params==null||params.value==null))"),

    NotIn("不包含", "!doc[params.field].stream().anyMatch(s->(s==null&&params==null)||(s!=null&&params.value!=null&&s.toString()==params.value.toString()))"),
    IN("包含", "doc[params.field].stream().anyMatch(s->(s==null&&params==null)||(s!=null&&params.value!=null&&s.toString()==params.value.toString()))"),
    LE("小于等于", "doc[params.field].stream().map(s-> s instanceof String?Double.parseDouble(s):s).anyMatch(s->s<=params.value)"),
    GE("大于等于", "doc[params.field].stream().map(s-> s instanceof String?Double.parseDouble(s):s).anyMatch(s->s>=params.value)"),
    AVG_GE("平均值大于等于", "doc[params.field].stream().map(s-> s instanceof String?Double.parseDouble(s):s).collect(Collectors.summarizingDouble(Double::doubleValue)).getAverage()>=params.value"),
    AVG_LE("平均值小于等于", "doc[params.field].stream().map(s-> s instanceof String?Double.parseDouble(s):s).collect(Collectors.summarizingDouble(Double::doubleValue)).getAverage()<=params.value"),
    SUM_GE("总和大于等于", "doc[params.field].stream().map(s-> s instanceof String?Double.parseDouble(s):s).collect(Collectors.summarizingDouble(Double::doubleValue)).getSum()>=params.value"),
    SUM_LE("总和小于等于", "doc[params.field].stream().map(s-> s instanceof String?Double.parseDouble(s):s).collect(Collectors.summarizingDouble(Double::doubleValue)).getSum()<=params.value");
    /**
     * 比较器提示
     */
    private final String message;
    /**
     * 比较器脚本
     */
    private final String script;

    InstanceFieldCompare(String message, String script) {
        this.message = message;
        this.script = script;
    }

    public String getMessage() {
        return message;
    }

    public String getScript() {
        return script;
    }
}
