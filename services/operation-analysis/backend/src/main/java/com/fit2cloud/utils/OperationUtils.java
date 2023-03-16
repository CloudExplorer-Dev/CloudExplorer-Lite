package com.fit2cloud.utils;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import com.fit2cloud.controller.response.BarTreeChartData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jianneng
 * @date 2022/12/21 16:33
 **/
public class OperationUtils {

    private static final Long WEEK = 168L;
    private static final Long THREE_MONTH = 93L*24L;
    private static final Long SIX_MONTH = 186L*24L;
    private static final Long YEAR = 12L*30L*24L;

    private OperationUtils(){}

    /**
     * 获取时间间隔
     * @param start 开始时间
     * @param end 结束时间
     * @return CalendarInterval
     */
    public static CalendarInterval getCalendarIntervalUnit(long start, long end) {
        // 计算时间差值，转为小时(/1000 / 60 / 60)
        long hours = (end - start) / 3600000;
        if (hours <= WEEK) {
            // 小于一周
            return CalendarInterval.Hour;
        }
        // 小于3个月
        if (hours <= THREE_MONTH) {
            return CalendarInterval.Day;
        }
        // 小于6个月
        if (hours <= SIX_MONTH) {
            return CalendarInterval.Month;
        }
        // 小于12月
        if (hours <= YEAR) {
            return CalendarInterval.Month;
        } else {
            return CalendarInterval.Year;
        }
    }

    /**
     * 根据时间间隔获取时间格式
     * intervalUnit为小时时，返回时间格式到小时
     * @param time 要格式化的时间
     * @param intervalUnit 间隔单位
     * @return String
     */
    public static String getTimeFormat(String time, CalendarInterval intervalUnit){
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DateTimeFormatter formatter = null;
        if (CalendarInterval.Hour.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        } else if(CalendarInterval.Day.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else if(CalendarInterval.Month.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        } else if(CalendarInterval.Year.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy");
        }
        assert formatter != null;
        return formatter.format(dateTime);
    }

    /**
     * 初始化组织或工作空间的数据
     * @param initList 初始化的数据
     * @param orgWorkspaceDataList 授权数据
     */
    public static void initOrgWorkspaceAnalysisData(List<BarTreeChartData> initList, List<BarTreeChartData> orgWorkspaceDataList) {
        initList.forEach(v->{
            List<BarTreeChartData> tmp = orgWorkspaceDataList.stream().filter(c-> StringUtils.equalsIgnoreCase(v.getId(),c.getId())).toList();
            if(CollectionUtils.isNotEmpty(tmp)){
                v.setValue(tmp.get(0).getValue());
            }
        });
    }

    /**
     * 将工作空间作为组织子级
     * @param workspaceMap 工作按照组织ID分组
     * @param org 组织
     */
    public static void workspaceToOrgChildren(Map<String, List<BarTreeChartData>> workspaceMap, BarTreeChartData org) {
        if(workspaceMap.get(org.getId())!=null){
            List<BarTreeChartData> wk = workspaceMap.get(org.getId());
            Long wkVmCount = wk.stream().mapToLong(BarTreeChartData::getValue).sum();
            org.setValue(org.getValue()+wkVmCount);
            org.getChildren().addAll(wk);
        }
    }

    /**
     * 将组织自己的数据添加到子级作为未授权数据
     * @param currentOrg 当前组织
     */
    public static void setSelfToChildren(BarTreeChartData currentOrg){
        BarTreeChartData children = new BarTreeChartData();
        BeanUtils.copyProperties(currentOrg,children);
        children.setChildren(new ArrayList<>());
        children.setGroupName("available");
        children.setName(currentOrg.getName()+"(未授权)");
        if(children.getValue()>0){
            currentOrg.getChildren().add(children);
        }
    }

}
