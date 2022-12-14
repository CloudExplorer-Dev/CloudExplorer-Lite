package com.fit2cloud.provider.impl.tencent.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * @author : LiuDi
 * @date : 2022/11/25 17:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TencentDiskTypeDTO {

    List<TencentDiskType> systemDiskTypes;

    List<TencentDiskType> dataDiskTypes;

    @Data
    public class TencentDiskType {
        private String diskType;
        private String diskTypeName;
        private Long minDiskSize;
        private Long maxDiskSize;

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            TencentDiskType tencentDiskType = (TencentDiskType) o;
            return Objects.equals(diskType, tencentDiskType.diskType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(diskType);
        }
    }
}
