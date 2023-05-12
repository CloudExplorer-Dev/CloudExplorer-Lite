package com.fit2cloud.common.job.result;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/11  9:38}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface Result  {
    /**
     * 获取执行结果Code
     *
     * @return 执行结果Code
     */
    int getCode();

    /**
     * 执行结果提示
     *
     * @return 执行结果提示
     */
    String getMessage();

    /**
     * 返回值数据
     *
     * @return 返回值数据
     */
    Object getData();

    /**
     * 是否终止执行,下面的Step都不在执行
     *
     * @return true 终止执行下面的步骤,false,继续执行
     */
    boolean getTermination();
}
