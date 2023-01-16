package com.fit2cloud.controller.request.vm;

import lombok.Data;

import java.util.List;

/**
 * @author : LiuDi
 * @date : 2023/1/12 19:49
 */
@Data
public class BatchRecycleRequest {

    private List<String> recycleIds;

}
