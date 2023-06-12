package com.fit2cloud.request.pub;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  1:48 PM
 * @Version 1.0
 * @注释:
 */
@Accessors(chain = true)
@Data
public class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -2264727710892316792L;

    @Schema(title = "当前页", required = true)
    @Min(value = 0, message = "{i18n.page.current_page.min}")
    @NotNull(message = "{i18n.page.current_page.is.not.empty}")
    private Integer currentPage;

    @Schema(title = "每页大小", required = true)
    @Min(value = 0, message = "{i18n.page.page.size.min}")
    @NotNull(message = "{i18n.page.page.size.is.not.empty}")
    private Integer pageSize;
}
