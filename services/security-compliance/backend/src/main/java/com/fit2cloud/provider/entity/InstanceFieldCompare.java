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
    EQ("等于", "if(doc[params.field].length>0){\n" +
            "                   for(int i;i<doc[params.field].length;i++){\n" +
            "                       if(doc[params.field][i].toString()!=params.value){\n" +
            "                         return false;\n" +
            "                      }\n" +
            "                     }\n" +
            "                   return true;\n" +
            "                }else{\n" +
            "                  return false;\n" +
            "                }"),

    IN("包含", "if(doc[params.field].length>0){\n" +
            "         for(int i;i<doc[params.field].length;i++){\n" +
            "           if(doc[params.field][i].toString()==params.value){\n" +
            "            return true;\n" +
            "          }\n" +
            "         }\n" +
            "        }else{\n" +
            "          return false;\n" +
            "        }"),

    LE("小于等于", "if(doc[params.field].length>0){\n" +
            "         for(int i;i<doc[params.field].length;i++){\n" +
            "           if(Integer.parseInt(doc[params.field][i].toString())<=params.value){\n" +
            "            return true;\n" +
            "          }\n" +
            "         }\n" +
            "        }else{\n" +
            "          return false;\n" +
            "        }"),

    GE("大于等于", "if(doc[params.field].length>0){\n" +
            "         for(int i;i<doc[params.field].length;i++){\n" +
            "           if(Integer.parseInt(doc[params.field][i].toString())>=params.value){\n" +
            "            return true;\n" +
            "          }\n" +
            "         }\n" +
            "        }else{\n" +
            "          return false;\n" +
            "        }"),
    AVG_GE("平均值大于等于", "if(doc[params.field].length>0){\n" +
            "         double sum=0; \n" +
            "         for(int i;i<doc[params.field].length;i++){\n" +
            "           sum+=Double.parseDouble(doc[params.field][i].toString())\n" +
            "         }\n" +
            "        return (sum/doc[params.field].length)>=params.value;\n" +
            "        }else{\n" +
            "          return false;\n" +
            "        }"),

    AVG_LE("平均值小于等于", "if(doc[params.field].length>0){\n" +
            "         double sum=0; \n" +
            "         for(int i;i<doc[params.field].length;i++){\n" +
            "           sum+=Double.parseDouble(doc[params.field][i].toString())\n" +
            "         }\n" +
            "        return (sum/doc[params.field].length)<=params.value;\n" +
            "        }else{\n" +
            "          return false;\n" +
            "        }"),

    SUM_GE("总和大于等于", "if(doc[params.field].length>0){\n" +
            "         double sum=0; \n" +
            "         for(int i;i<doc[params.field].length;i++){\n" +
            "           sum+=Double.parseDouble(doc[params.field][i].toString())\n" +
            "         }\n" +
            "        return sum>=params.value;\n" +
            "        }else{\n" +
            "          return false;\n" +
            "        }"),

    SUM_LE("总和小于等于", "if(doc[params.field].length>0){\n" +
            "         double sum=0; \n" +
            "         for(int i;i<doc[params.field].length;i++){\n" +
            "           sum+=Double.parseDouble(doc[params.field][i].toString())\n" +
            "         }\n" +
            "        return sum<=params.value;\n" +
            "        }else{\n" +
            "          return false;\n" +
            "        }");

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
