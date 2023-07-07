<script setup lang="ts">
import CatalogApi from "@/api/catalog";

import PlatformIcon from "@commons/components/platform-icon/index.vue";
import { computed, onMounted, type Ref, ref } from "vue";
import { useRouter } from "vue-router";
import type { Good } from "@/api/catalog/type";
import _ from "lodash";
import { numberFormatter } from "@commons/utils/util";
import { ElMessage } from "element-plus";
import { Platform } from "@commons/utils/platform";

const loading: Ref<boolean> | undefined = ref<boolean>(false);
const useRoute = useRouter();
const goods = ref<Array<Good>>([]);
const platformType = ref("all");

const publicPlatforms = computed(() => {
  return _.filter(goods.value, (p) => p.publicCloud);
});

const privatePlatforms = computed(() => {
  return _.filter(goods.value, (p) => !p.publicCloud);
});

const platformGroups = computed(() => {
  const list = [];
  if (publicPlatforms.value.length > 0 && platformType.value !== "private") {
    list.push({
      name: "公有云",
      platforms: publicPlatforms.value,
    });
  }
  if (privatePlatforms.value.length > 0 && platformType.value !== "public") {
    list.push({
      name: "私有云",
      platforms: privatePlatforms.value,
    });
  }
  return list;
});

const platformOptions = ref<Array<{ label: string; value: string }>>([
  { label: "全部", value: "all" },
  { label: "公有云", value: "public" },
  { label: "私有云", value: "private" },
]);

onMounted(() => {
  CatalogApi.getGoods(loading).then((result) => {
    goods.value = result.data;
  });
});

function openCreatePage(good: Good) {
  // 判断云账户余额是否大于 0
  if (good.balance != undefined && good.balance !== "--" && good.balance <= 0) {
    ElMessage.error("所选云账号余额不足，请先去充值");
    return;
  }

  // 阿里云创建云主机要求余额大于 100
  if (
    good.balance != undefined &&
    good.platform == Platform.ALI &&
    good.balance < 100
  ) {
    ElMessage.error("所选阿里云账号余额不足 100，请先去充值");
    return;
  }

  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      "/catalog",
      `/create/${good.id}`
    ),
  });
}

const visible = ref(false); // 控制 tooltip 显示或者隐藏
const currentPlatformName = ref();
const spanRef = ref(); // 鼠标选中的元素
const showTips = (good: Good, e: Event) => {
  spanRef.value = e.currentTarget;
  const spanWidth = spanRef.value.offsetWidth;
  const spanTextWidth = spanRef.value.scrollWidth;
  if (spanTextWidth > spanWidth) {
    visible.value = true;
  }
  currentPlatformName.value = good.name;
};
</script>

<template>
  <el-row>
    <el-col :span="12">
      <span class="label">选择云账号</span>
    </el-col>
    <el-col :span="12" style="text-align: right">
      <ce-radio
        :dataList="platformOptions"
        v-model:activeValue="platformType"
      ></ce-radio>
    </el-col>
  </el-row>
  <el-row
    v-for="group in platformGroups"
    :key="group.name"
    class="platform-group"
  >
    <div class="platform-group-container">
      <span class="label">{{ group.name }}</span>
      <div class="platform-container">
        <div
          class="platform"
          v-for="(good, index) in group.platforms"
          :key="index"
        >
          <div class="platform-select-card" shadow="never">
            <div class="platform-content">
              <div class="platform-desc">
                <div class="platform-icon">
                  <PlatformIcon
                    :platform="good.platform"
                    style="width: 36px; height: 36px"
                  />
                </div>
                <div
                  class="platform-info"
                  :class="{ 'platform-info-flex': good.balance === '--' }"
                >
                  <div class="platform-name">
                    <span
                      @mouseover="showTips(good, $event)"
                      @mouseout="visible = false"
                      >{{ good.name }}</span
                    >
                  </div>
                  <div class="platform-balance" v-if="good.balance !== '--'">
                    <span class="text">余额</span>
                    <span class="number">{{
                      numberFormatter(good.balance)
                    }}</span
                    >元
                  </div>
                </div>
              </div>
              <div class="platform-resource">
                <div class="row">
                  <ce-icon
                    class="resource-icon"
                    size="12px"
                    code="icon_ecs_outlined"
                  />
                  云主机
                  <span>{{ numberFormatter(good.serverCount) }}</span>
                </div>
                <div class="row">
                  <ce-icon
                    class="resource-icon"
                    size="12px"
                    code="icon_storage_filled"
                  />
                  云磁盘
                  <span>{{ numberFormatter(good.diskCount) }}</span>
                </div>
              </div>
            </div>
            <div class="footer-btn" @click="openCreatePage(good)">
              <span class="create">立即创建</span>
            </div>
          </div>
        </div>

        <!-- 云账号名称提示信息 -->
        <el-tooltip
          v-if="currentPlatformName && visible"
          :content="currentPlatformName"
          placement="top"
          effect="light"
          virtual-triggering
          :virtual-ref="spanRef"
        />
      </div>
    </div>
  </el-row>
</template>
<style lang="scss" scoped>
.label {
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
}

.platform-group {
  padding-top: 24px;
  .platform-group-container {
    width: 100%;
    .platform-container {
      width: calc(100% + 12px);
      display: flex;
      flex-wrap: wrap;
      .platform {
        width: 25%;
        padding-top: 16px;
      }
    }
  }
}

.platform-select-card {
  width: calc(100% - 16px);
  min-width: 150px;
  height: 177px;
  border: 1px solid #dee0e3;
  border-radius: 4px;
  overflow: hidden;
}
.platform-icon {
  height: 36px;
  width: 36px;
  border-radius: 4px;
  background: #ffffff;
  border: 1px solid #dee0e3;
}

.platform-content {
  padding: 14px 16px 12px;
  .platform-desc {
    width: 100%;
    display: flex;
    .platform-info {
      width: calc(100% - 36px);
      margin-left: 8px;
      .platform-name {
        width: 100%;
        font-weight: 500;
        line-height: 18px;
        span {
          display: inline-block;
          max-width: calc(100% - 16px);
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
      }
      .platform-balance {
        font-size: 12px;
        font-weight: 400;
        line-height: 16px;
        color: #646a73;
        .number {
          font-weight: 500;
          color: #f54a45;
          margin-left: 16px;
          margin-right: 4px;
        }
      }
    }
    .platform-info-flex {
      display: flex;
      align-items: center;
    }
  }

  .platform-resource {
    margin-top: 12px;
    .row {
      height: 24px;
      line-height: 24px;
      font-size: 12px;
      font-weight: 400;
      color: #646a73;
      background: rgba(239, 240, 241, 0.5);
      border-radius: 4px;
      margin-bottom: 4px;
      .resource-icon {
        margin-left: 12px;
      }
      span {
        font-weight: 500;
        float: right;
        margin-right: 12px;
        color: #1f2329;
      }
    }
  }
}

.footer-btn {
  width: 100%;
  height: 46px;
  text-align: center;
  border-top: 1px solid #dee0e3;
  .create {
    font-weight: 400;
    line-height: 46px;
    color: var(--el-color-primary);
  }
}

.footer-btn:hover {
  background: #f5f8ff;
  cursor: pointer;
}
</style>
