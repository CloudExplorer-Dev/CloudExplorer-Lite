package com.fit2cloud.common.utils;

import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.response.OrganizationTree;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author:张少虎
 * @Date: 2022/8/30  1:47 PM
 * @Version 1.0
 * @注释:
 */
public class OrganizationUtil {
    /**
     * 将数据转换为树状数据
     *
     * @param source 组织原始数据
     * @return 组织树状数据
     */
    public static List<OrganizationTree> toTree(List<Organization> source) {
        return toTree(source, (organizationTrees) -> organizationTrees.stream().filter(organizationTree -> StringUtils.isEmpty(organizationTree.getPid())).toList());
    }

    /**
     * 将数据转换为树状数据
     *
     * @param source 组织原始数据
     * @return 组织树状数据
     */
    @SneakyThrows
    public static List<OrganizationTree> toTree(List<Organization> source, Function<List<OrganizationTree>, List<OrganizationTree>> postHandler) {
        List<OrganizationTree> organizationTrees = source.stream().map(organization -> {
            OrganizationTree organizationTree = new OrganizationTree();
            BeanUtils.copyProperties(organization, organizationTree);
            return organizationTree;
        }).toList();
        for (OrganizationTree organization : organizationTrees) {
            if (StringUtils.isNotEmpty(organization.getPid())) {
                Optional<OrganizationTree> first = organizationTrees.stream().filter(org -> {
                    return org.getId().equals(organization.getPid());
                }).findFirst();
                if (first.isPresent()) {
                    if (CollectionUtils.isNotEmpty(first.get().getChildren())) {
                        first.get().getChildren().add(organization);
                    } else {
                        first.get().setChildren(new ArrayList<>() {{
                            add(organization);
                        }});
                    }
                }
            }
        }
        return postHandler.apply(organizationTrees);
    }
}
