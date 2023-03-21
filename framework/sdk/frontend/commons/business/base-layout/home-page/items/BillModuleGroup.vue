<script lang="ts" setup>
import { computed, onMounted, ref } from "vue";
import BillViewApi from "@commons/api/bil_view";
import CeIcon from "@commons/components/ce-icon/index.vue";
import { useRouter } from "vue-router";

const router = useRouter();

/**
 *当前月份
 */
const currentMonth = computed<string>(() => {
  return (
    new Date().getFullYear().toString() +
    "-" +
    ((new Date().getMonth() + 1).toString().length === 0
      ? "0" + (new Date().getMonth() + 1).toString()
      : (new Date().getMonth() + 1).toString())
  );
});

const currentMonthCost = ref<number>(0);
const currentYearCost = ref<number>(0);

const loading1 = ref(false);
const loading2 = ref(false);

function getMonthCost() {
  BillViewApi.getExpenses("MONTH", currentMonth.value, loading1).then(
    (data) => {
      currentMonthCost.value = data.data;
    }
  );
}
function getYearCost() {
  BillViewApi.getExpenses(
    "YEAR",
    new Date().getFullYear().toString(),
    loading2
  ).then((data) => {
    currentYearCost.value = data.data;
  });
}

function jump() {
  router.push("/finance-management#/bill_view");
}

onMounted(() => {
  getMonthCost();
  getYearCost();
});
</script>
<template>
  <div class="info-card no-padding">
    <div class="info-card no-bottom-border">
      <div class="title">
        费用概览
        <span class="right-info" @click="jump">
          更多
          <CeIcon code="icon_right_outlined" size="14px" />
        </span>
      </div>
      <div>
        <el-row :gutter="8">
          <el-col :span="12">
            <div class="base-div">
              <div class="subtitle">本月花费</div>
              <div class="value">
                {{
                  currentMonthCost?.toLocaleString("zh-CN", {
                    style: "currency",
                    currency: "CNY",
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  })
                }}
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="base-div">
              <div class="subtitle">本年花费</div>
              <div class="value">
                {{
                  currentYearCost?.toLocaleString("zh-CN", {
                    style: "currency",
                    currency: "CNY",
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  })
                }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <div class="divider"></div>
    <div class="info-card no-top-border">
      <div class="title sub-main-title">本月费用分布</div>
      <div></div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;

  .divider {
    height: 1px;
    width: 90%;
    margin-left: auto;
    margin-right: auto;
    background-color: rgba(31, 35, 41, 0.15);
  }

  .title {
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 24px;
    margin-bottom: 10px;

    .right-info {
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      color: #646a73;
      float: right;
      cursor: pointer;
    }
    .right-info:hover {
      color: var(--el-color-primary);
    }
  }

  .sub-main-title {
    line-height: 22px;
    font-size: 14px;
  }
  .base-div {
    padding: 8px;

    .subtitle {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      font-style: normal;
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      height: 28px;
      color: #646a73;
    }
    .value {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      font-style: normal;
      font-weight: 500;
      font-size: 20px;
      line-height: 28px;
      height: 28px;
      color: #1f2329;
      width: fit-content;
      min-width: 28px;
      vertical-align: center;
    }
  }
}

.info-card.no-padding {
  padding: 0;
}

.info-card.no-bottom-border {
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}
.info-card.no-top-border {
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
</style>
