package com.fit2cloud.quartz;

import com.fit2cloud.base.service.IBaseSystemParameterService;
import com.fit2cloud.common.constants.ParamConstants;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.es.entity.OperatedLog;
import com.fit2cloud.es.entity.PerfMetricMonitorData;
import com.fit2cloud.es.entity.SystemLog;
import com.fit2cloud.service.ILogService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author jianneng
 * @date 2022/12/11 17:25
 **/
@Component
@EnableScheduling
public class LogCleanJob {
    @Resource
    private IBaseSystemParameterService baseSystemParameterService;
    @Resource
    private ILogService logService;

    @Scheduled(cron = "0 30 1 * * ?")
    public void cleanApiLog() {
        String operatedLogValue = baseSystemParameterService.getValue(ParamConstants.Log.KEEP_API_MONTHS.getValue());
        String loginLogValue = baseSystemParameterService.getValue(ParamConstants.Log.KEEP_LOGIN_MONTHS.getValue());
        String systemLogValue = baseSystemParameterService.getValue(ParamConstants.Log.KEEP_SYSTEM_MONTHS.getValue());
        String metricValue = baseSystemParameterService.getValue(ParamConstants.Log.KEEP_METRIC_MONTHS.getValue());
        cleanOperatedLog(operatedLogValue, "操作日志", "ce-file-api-logs", OperatedLog.class);
        cleanOperatedLog(loginLogValue, "登录日志", "ce-file-api-logs", OperatedLog.class);
        cleanOperatedLog(systemLogValue, "系统日志", "ce-file-system-logs", SystemLog.class);
        cleanOperatedLog(metricValue, "监控数据", "ce-perf-metric-monitor-data", PerfMetricMonitorData.class);
    }

    private void cleanOperatedLog(String m, String logType, String index, Class<?> clazz) {
        try {
            LogUtil.debug(String.format("开始清理%s.", logType));
            int months = 3;
            if (StringUtils.isBlank(m)) {
                LogUtil.info(String.format("未设置%s保存月数，使用默认值: %s", logType, months));
            } else {
                months = Integer.parseInt(m);
            }
            LogUtil.debug(String.format("开始清理%s个月前的%s.", months, logType));
            logService.deleteEsData(index, months, clazz);
            LogUtil.debug(String.format("%s清理完成.", logType));
        } catch (Exception e) {
            LogUtil.error(String.format("清理%s个月前%s失败:%s", m, logType, e.getMessage()));
            e.printStackTrace();
        }
    }


}
