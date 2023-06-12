package com.fit2cloud.controller.request.datastore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author jianneng
 * @date 2022/12/11 19:03
 **/
@Accessors(chain = true)
@Data
public class DatastoreRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8484185034593083260L;

    @Schema(title = "存储器名称")
    private String datastoreName;
}
