package com.fit2cloud.provider.entity.request;

import com.fit2cloud.provider.entity.F2CDisk;
import lombok.Data;

import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/10/13 9:51 PM
 */
@Data
public class BaseDiskCreateRequest extends BaseDiskRequest {

    private List<F2CDisk> disks;

}
