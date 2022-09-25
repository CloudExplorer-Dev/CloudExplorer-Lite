package com.fit2cloud;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit2cloud.common.es.ElasticsearchProvide;
import com.fit2cloud.common.log.entity.OperatedLogVO;
import com.fit2cloud.common.log.entity.SystemLogVO;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.controller.request.es.PageOperatedLogRequest;
import com.fit2cloud.controller.request.es.PageSystemLogRequest;
import com.fit2cloud.service.ILogService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Iterator;

/**
 * @Author:张少虎
 * @Date: 2022/9/1  5:36 PM
 * @Version 1.0
 * @注释:
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ManagementApplication.class)
@TestPropertySource(locations = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
})
public class LogTest {


    @Resource
    private ElasticsearchProvide provide;

    @Resource
    private ILogService logService;

    // 判断索引是否存在
    @Test
    public void indexIsExist () throws IOException {
        boolean booleanResponse = provide.existsIndex("ce-file-system-logs");

        System.out.println("索引是否存在: {"+booleanResponse+"}");
    }

    @Test
    public void searchByQueryPage () throws Exception {
        PageOperatedLogRequest request = new PageOperatedLogRequest();
        request.setCurrentPage(1);
        request.setPageSize(1000);
        request.setType("loginLog");
        IPage<OperatedLogVO> page = logService.operatedLogs(request);
        System.out.println(">>>>>>"+JsonUtil.toJSONString(page));
    }

    public static void main(String[] args) {
        PageSystemLogRequest request = new PageSystemLogRequest();
        request.setCurrentPage(1);
        request.setPageSize(2);
        Query.Builder query = new Query.Builder();
        String paramsJson = JsonUtil.toJSONString(request);
        ObjectNode params = JsonUtil.parseObject(paramsJson);
        Iterator<String> fieldNames = params.fieldNames();
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        while (fieldNames.hasNext()){
            String name = fieldNames.next();
            JsonNode jsonNode = params.get(name);
            String value = jsonNode.asText();
            if(StringUtils.isNotEmpty(value) && !StringUtils.equalsIgnoreCase("null",value)){
                boolQuery.must(m -> m.match(u -> u.field(name).query(v->v.stringValue(value))));
            }
        }
        query.bool(boolQuery.build());
        System.out.println("");
    }


    @Test
    public void searchByQuery () throws Exception {
        Page<SystemLogVO> page = new Page<>(1, 2, false);
        SearchRequest.Builder builder = provide.requestBuilder("ce-file-system-logs");
        //分页
        builder.from(1).size(2);
        //条件
        Query.Builder query = new Query.Builder();
        //详情
        //query.fuzzy(f->f.field("msg").value(v->v.stringValue("time out")).fuzziness("2"));
        //等级
        query.bool(b->b.must(m -> m.match(u -> u.field("level").query(v->v.stringValue("ERROR")))));
        //Page response = provide.searchByQuery("ce-file-system-logs",builder, SystemLogVO.class,page);
        //System.out.println(">>>>>>"+JsonUtil.toJSONString(response));
    }
    @Test
    public void save() {
        LogUtil.debug("这个是debug日志");
        LogUtil.info("这个是info日志");
        LogUtil.error("这个是error日志");
        LogUtil.warn("这个是warn日志");
    }

}
