package com.fit2cloud.factory.optimize;

import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.constants.AnalysisConstants;

import java.util.Objects;

/**
 * @author jianneng
 * @date 2023/4/7 10:29
 **/
public class OptimizeFactory {

    public static IOptimizeStrategy getOptimizeApplyStrategy(String code) {
        try {
            return SpringUtil.getBean(Objects.requireNonNull(AnalysisConstants.OptimizeSuggestEnum.byCode(code)).getClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
