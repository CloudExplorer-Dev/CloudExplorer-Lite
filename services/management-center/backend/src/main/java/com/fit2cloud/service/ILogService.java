package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.common.log.entity.OperatedLogVO;
import com.fit2cloud.common.log.entity.SystemLogVO;
import com.fit2cloud.controller.request.es.PageOperatedLogRequest;
import com.fit2cloud.controller.request.es.PageSystemLogRequest;

/**
 * @author jianneng
 * @date 2022/9/23 16:33
 **/
public interface ILogService {

    IPage<SystemLogVO> systemLogs(PageSystemLogRequest request);

    IPage<OperatedLogVO> operatedLogs(PageOperatedLogRequest request);


}
