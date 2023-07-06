package com.fit2cloud.vm.entity.request;

import lombok.Data;
import com.fit2cloud.vm.entity.F2CDisk;

import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/10/13 9:51 PM
 */
@Data
public class BaseDiskCreateRequest extends BaseDiskRequest {

    private List<F2CDisk> disks;

}
