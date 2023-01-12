package com.fit2cloud.controller.request.datastore;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;

/**
 * @author jianneng
 * @date 2022/12/11 18:46
 **/
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class PageDatastoreRequest extends DatastoreRequest implements PageOrderRequestInterface {

    @Serial
    private static final long serialVersionUID = 1562817563718863118L;

    @ApiModelProperty(value = "当前页", required = true)
    @Min(value = 0, message = "{i18n.page.current_page.min}")
    @NotNull(message = "{i18n.page.current_page.is.not.empty}")
    private Integer currentPage;

    @ApiModelProperty(value = "每页大小", required = true)
    @Min(value = 0, message = "{i18n.page.page.size.min}")
    @NotNull(message = "{i18n.page.page.size.is.not.empty}")
    private Integer pageSize;

    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;

}
