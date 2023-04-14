package com.fit2cloud.utils;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.controller.response.BarTreeChartData;
import com.fit2cloud.controller.response.TreeNode;
import com.fit2cloud.response.OrganizationTree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author jianneng
 * @date 2022/12/21 16:33
 **/
public class OperationUtils {

    private static final Long WEEK = 168L;
    private static final Long THREE_MONTH = 93L * 24L;
    private static final Long SIX_MONTH = 186L * 24L;
    private static final Long YEAR = 12L * 30L * 24L;

    private OperationUtils() {
    }

    /**
     * 获取时间间隔
     *
     * @param start 开始时间
     * @param end   结束时间
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
     *
     * @param time         要格式化的时间
     * @param intervalUnit 间隔单位
     * @return String
     */
    public static String getTimeFormat(String time, CalendarInterval intervalUnit) {
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DateTimeFormatter formatter = null;
        if (CalendarInterval.Hour.equals(intervalUnit)) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        } else if (CalendarInterval.Day.equals(intervalUnit)) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else if (CalendarInterval.Month.equals(intervalUnit)) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        } else if (CalendarInterval.Year.equals(intervalUnit)) {
            formatter = DateTimeFormatter.ofPattern("yyyy");
        }
        assert formatter != null;
        return formatter.format(dateTime);
    }

    /**
     * 初始化组织或工作空间的数据
     *
     * @param initList             初始化的数据
     * @param orgWorkspaceDataList 授权数据
     */
    public static void initOrgWorkspaceAnalysisData(List<BarTreeChartData> initList, List<BarTreeChartData> orgWorkspaceDataList) {
        initList.forEach(v -> {
            List<BarTreeChartData> tmp = orgWorkspaceDataList.stream().filter(c -> StringUtils.equalsIgnoreCase(v.getId(), c.getId())).toList();
            if (CollectionUtils.isNotEmpty(tmp)) {
                v.setValue(tmp.get(0).getValue());
            }
        });
    }

    /**
     * 将工作空间作为组织子级
     *
     * @param workspaceMap 工作按照组织ID分组
     * @param org          组织
     */
    public static void workspaceToOrgChildren(Map<String, List<BarTreeChartData>> workspaceMap, BarTreeChartData org) {
        if (workspaceMap.get(org.getId()) != null) {
            List<BarTreeChartData> wk = workspaceMap.get(org.getId());
            Long wkVmCount = wk.stream().mapToLong(BarTreeChartData::getValue).sum();
            org.setValue(org.getValue() + wkVmCount);
            org.getChildren().addAll(wk);
        }
    }

    /**
     * 将组织自己的数据添加到子级作为未授权数据
     *
     * @param currentOrg 当前组织
     */
    public static void setSelfToChildren(BarTreeChartData currentOrg) {
        BarTreeChartData children = new BarTreeChartData();
        BeanUtils.copyProperties(currentOrg, children);
        children.setChildren(new ArrayList<>());
        children.setGroupName("available");
        children.setName(currentOrg.getName() + "(未授权)");
        if (children.getValue() > 0) {
            currentOrg.getChildren().add(children);
        }
    }

    /**
     * @param tree     组织树
     * @param getCount 获取当前组织/工作空间统计数据
     * @return 统计后数据
     */
    public static List<TreeNode> orgCount(List<OrganizationTree> tree, BiFunction<@Null String, @Null List<String>, Integer> getCount, int allCount) {
        // 转换树
        List<TreeNode> treeNodes = orgCount(tree, getCount);
        // 添加未授权数据
        appendNotAuth(treeNodes, null, allCount);
        return treeNodes;
    }

    /**
     * 添加未授权数据
     *
     * @param treeNodes 组织树对象
     * @param prentNode 父级对象
     * @param all       所有数据
     */
    public static void appendNotAuth(List<TreeNode> treeNodes, TreeNode prentNode, int all) {
        if (Objects.nonNull(prentNode)) {
            if (prentNode.value > (CollectionUtils.isEmpty(prentNode.children) ? 0 : prentNode.children.stream().mapToInt(TreeNode::getValue).sum())) {
                TreeNode treeNode = new TreeNode();
                treeNode.setValue(prentNode.value - prentNode.children.stream().mapToInt(TreeNode::getValue).sum());
                treeNode.setName(prentNode.name + "(" + "未授权" + ")");
                treeNode.setGroupName("org");
                treeNode.setPid(prentNode.getId());
                if (CollectionUtils.isEmpty(prentNode.children)) {
                    prentNode.children = new ArrayList<>(List.of(treeNode));
                } else {
                    prentNode.children.add(treeNode);
                }
            }
        } else {
            if (all > treeNodes.stream().mapToInt(TreeNode::getValue).sum()) {
                TreeNode treeNode = new TreeNode();
                treeNode.setValue(all - treeNodes.stream().mapToInt(TreeNode::getValue).sum());
                treeNode.setName("未授权");
                treeNode.setGroupName("org");
                treeNode.setPid(null);
                treeNodes.add(treeNode);
            }
        }
        for (TreeNode treeNode : treeNodes) {
            if (CollectionUtils.isNotEmpty(treeNode.children)) {
                appendNotAuth(treeNode.children, treeNode, all);
            }
        }

    }

    /**
     * 转换树结果,并赋值
     *
     * @param tree     原始组织工作空间树
     * @param getCount 指定组织和工作空间id 查询数量
     * @return 转换后的结果
     */
    public static List<TreeNode> orgCount(List<OrganizationTree> tree, BiFunction<@Null String, @Null List<String>, Integer> getCount) {
        return new ArrayList<>(tree.stream().map(t -> {
            TreeNode treeNode = toBaseTreeNode(t);
            appendChildren(t, treeNode, getCount);
            treeNode.value = treeNode.value + getCount.apply(t.getId(), CollectionUtils.isEmpty(t.getWorkspaces()) ? List.of() : t.getWorkspaces().stream().map(Workspace::getId).toList());
            appendWorkspaces(getCount, t, treeNode);
            return treeNode;
        }).toList());
    }

    private static TreeNode workspacesToTreeNode(String prentId, Workspace workspace, BiFunction<@Null String, @Null List<String>, Integer> getOrgCount) {
        TreeNode treeNode = new TreeNode();
        treeNode.setChildren(null);
        treeNode.setName(workspace.getName() + "(" + "工作空间" + ")");
        treeNode.setGroupName("workspace");
        treeNode.setId(workspace.getId());
        treeNode.setValue(getOrgCount.apply(null, List.of(workspace.getId())));
        treeNode.setPid(prentId);
        return treeNode;
    }

    /**
     * 添加子节点
     *
     * @param organizationTree 组织对象
     * @param prentNode        父级
     */
    private static void appendChildren(OrganizationTree organizationTree, TreeNode prentNode, BiFunction<String, List<String>, Integer> getCount) {
        if (CollectionUtils.isNotEmpty(organizationTree.getChildren())) {
            prentNode.setChildren(new ArrayList<>());
            for (OrganizationTree child : organizationTree.getChildren()) {
                TreeNode treeNode = toBaseTreeNode(child);
                prentNode.getChildren().add(treeNode);
                appendChildren(child, treeNode, getCount);
                treeNode.value = treeNode.value + getCount.apply(child.getId(), CollectionUtils.isEmpty(child.getWorkspaces()) ? List.of() : child.getWorkspaces().stream().map(Workspace::getId).toList());
                appendWorkspaces(getCount, child, treeNode);
            }
            prentNode.value = prentNode.children.stream().mapToInt(TreeNode::getValue).sum();
        }
    }

    /**
     * 添加工作空间数据
     *
     * @param getCount         获取组织/工作空间数量
     * @param organizationTree 组织对象
     * @param treeNode         组织对应的树节点
     */
    private static void appendWorkspaces(BiFunction<String, List<String>, Integer> getCount, OrganizationTree organizationTree, TreeNode treeNode) {
        List<TreeNode> workspaceNode = organizationTree.getWorkspaces().stream().map(item -> workspacesToTreeNode(organizationTree.getId(), item, getCount)).toList();
        if (CollectionUtils.isEmpty(treeNode.children)) {
            treeNode.children = new ArrayList<>(workspaceNode);
        } else {
            treeNode.children.addAll(workspaceNode);
        }
    }


    /**
     * 将组织树节点转换为 TreeNode节点 只转换基础数据 不进行递归转换
     *
     * @param organizationTree 组织树节点
     * @return TreeNode节点
     */
    private static TreeNode toBaseTreeNode(OrganizationTree organizationTree) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(organizationTree.getId());
        treeNode.setPid(organizationTree.getPid());
        treeNode.setName(organizationTree.getName());
        treeNode.setGroupName("org");
        return treeNode;
    }

}
