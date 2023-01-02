package com.fit2cloud.provider.impl.aliyun.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * @author : LiuDi
 * @date : 2022/12/15 16:49
 */
@Data
public class AliyunDiskTypeDTO {
    List<AliyunDiskTypeDTO.AliyunDiskType> systemDiskTypes;

    List<AliyunDiskTypeDTO.AliyunDiskType> dataDiskTypes;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AliyunDiskType {
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
            AliyunDiskTypeDTO.AliyunDiskType aliyunDiskType = (AliyunDiskTypeDTO.AliyunDiskType) o;
            return Objects.equals(diskType, aliyunDiskType.diskType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(diskType);
        }
    }
}
