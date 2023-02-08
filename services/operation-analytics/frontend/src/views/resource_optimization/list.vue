<template>
  <div class="header">
    <el-row :gutter="12">
      <el-col :span="6" v-for="o in optimizeSuggests" key="o.code">
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <div class="boxCenter">
            <!--添加角标-->
            <div class="AngleOfTheBox" v-if="o.checked">
              <span>当前</span>
            </div>
            <div class="BtnOfTheBox" @click="selectOptimizeType(o)">
              <Setting @click="dialogFormVisible = true"></Setting>
            </div>
            <div class="CenterTheBox">
              <span
                ><span style="font-size: 24px">{{ o.value }}</span
                >台</span
              >
            </div>
            <div class="BottomTheBox" :style="{ 'background-color': o.color }">
              <span>{{ o.name }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
  <div>
    <div class="log-table">
      <ce-table
        v-loading="tableLoading"
        :columns="columns"
        :data="tableData"
        :tableConfig="tableConfig"
        row-key="id"
        height="100%"
        ref="table"
      >
        <!--        <el-table-column type="selection" />-->
        <el-table-column
          min-width="150"
          :show-overflow-tooltip="true"
          prop="instanceName"
          :label="$t('commons.name')"
        >
          <template #default="scope">
            <!--        <span @click="" class="name-span-class">-->
            {{ scope.row.instanceName }}
            <!--        </span>-->
          </template>
        </el-table-column>
        <el-table-column
          min-width="150"
          prop="accountName"
          column-key="accountIds"
          :label="$t('commons.cloud_account.native')"
          :filters="cloudAccount"
        >
          <template #default="scope">
            <div style="display: flex">
              <component
                style="margin-top: 3px; width: 16px; height: 16px"
                :is="platformIcon[scope.row.platform]?.component"
                v-bind="platformIcon[scope.row.platform]?.icon"
                :color="platformIcon[scope.row.platform]?.color"
                size="16px"
                v-if="scope.row.platform"
              ></component>
              <span style="margin-left: 10px">{{ scope.row.accountName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          min-width="100"
          prop="optimizeSuggest"
          label="优化建议"
        ></el-table-column>
        <el-table-column
          min-width="150"
          prop="content"
          label="建议原因"
        ></el-table-column>
        <el-table-column
          min-width="150"
          prop="instanceTypeDescription"
          :label="$t('commons.cloud_server.instance_type')"
        ></el-table-column>
        <el-table-column
          min-width="150"
          prop="cpuAverage"
          label="CPU平均使用率(%)"
        ></el-table-column>
        <el-table-column
          min-width="150"
          prop="cpuMaximum"
          label="CPU最大使用率(%)"
          :show="false"
        ></el-table-column>
        <el-table-column
          min-width="150"
          prop="memoryAverage"
          label="内存平均使用率(%)"
        ></el-table-column>
        <el-table-column
          min-width="150"
          prop="memoryMaximum"
          label="内存最大使用率(%)"
          :show="false"
        ></el-table-column>
        <template #buttons>
          <fu-table-column-select type="icon" :columns="columns" size="small" />
        </template>
      </ce-table>
    </div>
  </div>

  <el-dialog
    v-model="dialogFormVisible"
    :title="currentTitle"
    :width="
      currentType === 'payment' || currentType === 'recovery' ? '45%' : '32%'
    "
  >
    <el-form :model="dialogFormData" :align="'center'">
      <!--      降配-->
      <div v-if="currentType === 'derating'">
        <div style="display: flex">
          <el-form-item>
            <div style="width: 200px; text-align: right"><span>过去</span></div>
            <span style="width: 48px"></span>
            <el-input-number
              v-model="dialogFormData.days"
              controls-position="right"
              autocomplete="off"
            />
            <span>天</span>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item>
            <el-select v-model="dialogFormData.cpuMaxRate">
              <el-option label="CPU平均使用率" value="false" />
              <el-option label="CPU最大使用率" value="true" />
            </el-select>
            <span style="padding: 10px">小于</span>
            <el-form-item>
              <el-input-number
                v-model="dialogFormData.cpuRate"
                min="1"
                max="100"
                step="1"
                controls-position="right"
                autocomplete="off"
              />
              <span>%</span>
            </el-form-item>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item>
            <el-radio-group v-model="dialogFormData.conditionOr">
              <el-radio-button label="OR" />
              <el-radio-button label="AND" />
            </el-radio-group>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item>
            <el-select v-model="dialogFormData.memoryMaxRate">
              <el-option label="内存平均使用率" value="false" />
              <el-option label="内存最大使用率" value="true" />
            </el-select>
            <span style="padding: 10px">小于</span>
            <el-form-item>
              <el-input-number
                v-model="dialogFormData.memoryRate"
                min="1"
                max="100"
                step="1"
                controls-position="right"
                autocomplete="off"
              />
              <span>%</span>
            </el-form-item>
          </el-form-item>
        </div>
      </div>
      <!--      升配-->
      <div v-if="currentType === 'upgrade'">
        <div style="display: flex">
          <el-form-item>
            <div style="width: 200px; text-align: right"><span>过去</span></div>
            <span style="width: 48px"></span>
            <el-input-number
              v-model="dialogFormData.days"
              controls-position="right"
              autocomplete="off"
            />
            <span>天</span>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item>
            <el-select v-model="dialogFormData.cpuMaxRate">
              <el-option label="CPU平均使用率" value="false" />
              <el-option label="CPU最大使用率" value="true" />
            </el-select>
            <span style="padding: 10px">大于</span>
            <el-form-item>
              <el-input-number
                v-model="dialogFormData.cpuRate"
                controls-position="right"
                autocomplete="off"
              />
              <span>%</span>
            </el-form-item>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item>
            <el-radio-group v-model="dialogFormData.conditionOr">
              <el-radio-button label="OR" />
              <el-radio-button label="AND" />
            </el-radio-group>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item>
            <el-select v-model="dialogFormData.memoryMaxRate">
              <el-option label="内存平均使用率" value="false" />
              <el-option label="内存最大使用率" value="true" />
            </el-select>
            <span style="padding: 10px">大于</span>
            <el-form-item>
              <el-input-number
                v-model="dialogFormData.memoryRate"
                controls-position="right"
                autocomplete="off"
              />
              <span>%</span>
            </el-form-item>
          </el-form-item>
        </div>
      </div>
      <!--      变更付费方式-->
      <div v-if="currentType === 'payment'">
        <div style="display: flex">
          <el-form-item>
            <span>包年包月资源</span>
            <el-select v-model="dialogFormData.cycleContinuedRunning">
              <!--              <el-option label="持续开机" value="true" />-->
              <el-option label="持续关机" value="false" />
            </el-select>
            <span style="padding: 10px">大于</span>
            <el-form-item>
              <el-input-number
                v-model="dialogFormData.cycleContinuedDays"
                min="1"
                step="1"
                controls-position="right"
                autocomplete="off"
              />
              <span>天，建议转按需按量</span>
            </el-form-item>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item>
            <span>按需按量资源</span>
            <el-select v-model="dialogFormData.volumeContinuedRunning">
              <el-option label="持续开机" value="true" />
              <!--              <el-option label="持续关机" value="false" />-->
            </el-select>
            <span style="padding: 10px">大于</span>
            <el-form-item>
              <el-input-number
                v-model="dialogFormData.volumeContinuedDays"
                min="1"
                step="1"
                controls-position="right"
                autocomplete="off"
              />
              <span>天，建议转包年包月</span>
            </el-form-item>
          </el-form-item>
        </div>
      </div>
      <!--      回收-->
      <div v-if="currentType === 'recovery'">
        <div style="display: flex">
          <el-form-item>
            <el-select v-model="dialogFormData.continuedRunning">
              <!--              <el-option label="持续开机" value="true" />-->
              <el-option label="持续关机" value="false" />
            </el-select>
            <span style="padding: 10px">大于</span>
            <el-form-item>
              <el-input-number
                v-model="dialogFormData.continuedDays"
                min="1"
                step="1"
                controls-position="right"
                autocomplete="off"
              />
              <span>天，闲置资源，建议回收</span>
            </el-form-item>
          </el-form-item>
        </div>
      </div>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSearchParams()"> 保存 </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from "vue";
import _ from "lodash";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import type { SimpleMap } from "@commons/api/base/type";
import { platformIcon } from "@commons/utils/platform";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import type { VmCloudServerVO } from "@/api/server_analysis/type";
import ResourceOptimizationViewApi from "@/api/resource_optimization";
import type { OptimizeSuggest } from "@commons/api/resource_optimization/type";
import {useRouter} from "vue-router";
const useRoute = useRouter();
const { t } = useI18n();
const table = ref<any>(null);
const columns = ref([]);
const tableData = ref<Array<VmCloudServerVO>>([]);
const tableLoading = ref<boolean>(false);
const cloudAccount = ref<Array<SimpleMap<string>>>([]);
const dialogFormVisible = ref(false);
const dialogFormData = ref<any>();
const currentTitle = ref<string>("降配查询策略");
const currentType = ref<string>("derating");
//默认查询参数
const paramOptimizationRequestMap: Map<string, any> = new Map();
paramOptimizationRequestMap.set("derating", {
  conditionOr: "OR",
  optimizeSuggest: "derating",
  days: 10,
  cpuRate: 30,
  cpuMaxRate: "false",
  memoryRate: 30,
  memoryMaxRate: "false",
});
paramOptimizationRequestMap.set("upgrade", {
  conditionOr: "OR",
  optimizeSuggest: "upgrade",
  days: 10,
  cpuRate: 80,
  cpuMaxRate: "false",
  memoryRate: 80,
  memoryMaxRate: "false",
});
paramOptimizationRequestMap.set("payment", {
  optimizeSuggest: "payment",
  cycleContinuedRunning: "false",
  cycleContinuedDays: 10,
  volumeContinuedRunning: "false",
  volumeContinuedDays: 10,
});
paramOptimizationRequestMap.set("recovery", {
  optimizeSuggest: "recovery",
  continuedRunning: "false",
  continuedDays: 30,
});

const optimizeSuggests = ref<Array<OptimizeSuggest>>([]);
optimizeSuggests.value.push({
  checked: true,
  color: "#FF9899",
  name: "建议降配云主机",
  code: "derating",
  value: 0,
  data: [],
});
optimizeSuggests.value.push({
  checked: false,
  color: "#00A1E6",
  name: "建议升配云主机",
  code: "upgrade",
  value: 0,
  data: [],
});
optimizeSuggests.value.push({
  checked: false,
  color: "#FEB75C",
  name: "建议变更付费方式云主机",
  code: "payment",
  value: 0,
  data: [],
});
optimizeSuggests.value.push({
  checked: false,
  color: "#D763B7",
  name: "建议回收云主机",
  code: "recovery",
  value: 0,
  data: [],
});

const selectOptimizeType = (o: any) => {
  currentTitle.value = o.name + "查询策略";
  currentType.value = o.code;
  getSearchParams(o.code);
};

const saveSearchParams = () => {
  window.localStorage.setItem(
    dialogFormData.value.optimizeSuggest,
    JSON.stringify(dialogFormData.value)
  );
  dialogFormVisible.value = false;
  optimizeSuggests.value.forEach((value) => {
    value.checked = false;
    if (value.code === dialogFormData.value.optimizeSuggest) {
      value.checked = true;
    }
  });
  search(new TableSearch());
};

const getSearchParams = (code: any) => {
  if (window.localStorage.getItem(code)) {
    const str = window.localStorage.getItem(code);
    if (str) {
      try {
        dialogFormData.value = JSON.parse(str);
      } catch (e) {
        console.error("get default dialogFormData error", e);
        dialogFormData.value = paramOptimizationRequestMap.get(code);
      }
    }
  } else {
    dialogFormData.value = paramOptimizationRequestMap.get(code);
  }
};

/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  const d = _.find(optimizeSuggests.value, ["code", currentType.value]);
  getSearchParams(d?.code);
  _.merge(params, dialogFormData.value);
  ResourceOptimizationViewApi.listServer(
    {
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    },
    tableLoading
  ).then((res) => {
    if (dialogFormData.value.optimizeSuggest === currentType.value) {
      const d = _.find(optimizeSuggests.value, ["code", currentType.value]);
      d ? (d.value = res.data.total) : "";
      tableData.value = res.data.records;
      tableConfig.value.paginationConfig?.setTotal(
        res.data.total,
        tableConfig.value.paginationConfig
      );
      tableConfig.value.paginationConfig?.setCurrentPage(
        res.data.current,
        tableConfig.value.paginationConfig
      );
    }
  });
};

/**
 * 页面挂载
 */
onMounted(() => {
  const code = useRoute.currentRoute.value.query.code;
  optimizeSuggests.value.forEach((value) => {
    getSearchParams(value.code);
    const params = TableSearch.toSearchParams(new TableSearch());
    _.merge(params, dialogFormData.value);
    ResourceOptimizationViewApi.listServer({
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
      tableLoading,
    }).then((res) => {
      value.value = res.data.total;
      value.data = res.data.records;
       if (!code && value.code === "derating") {
         currentType.value = value.code;
        tableData.value = res.data.records;
        tableConfig.value.paginationConfig?.setTotal(
          res.data.total,
          tableConfig.value.paginationConfig
        );
        tableConfig.value.paginationConfig?.setCurrentPage(
          res.data.current,
          tableConfig.value.paginationConfig
        );
      }
    });
  });
  if(code){
    currentType.value = code as string;
    useRoute.currentRoute.value.query.code = null;
    dialogFormVisible.value = false;
    optimizeSuggests.value.forEach((value) => {
      value.checked = false;
      if (value.code === currentType.value) {
        value.checked = true;
      }
    });
    search(new TableSearch());
  }
  searchCloudAccount();
});
onBeforeUnmount(() => {});

const searchCloudAccount = () => {
  BaseCloudAccountApi.listAll().then((result) => {
    if (result.data.length > 0) {
      result.data.forEach(function (v) {
        const ca = { text: v.name, value: v.id };
        cloudAccount.value.push(ca);
      });
    }
  });
};

/**
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: t("commons.btn.search", "查找"),
    components: [],
    searchOptions: [
      {
        label: t("commons.name", "名称"),
        value: "instanceName",
      },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});
</script>

<style scoped lang="scss">
.boxCenter {
  height: 150px;
  overflow: hidden;
  position: relative;
  //按钮
  button {
    position: absolute;
    display: inline-block;
    color: #f7ba2a;
    width: 100%;
    bottom: 5px;
    right: 0;
    text-align: center;
  }
  //角标主要样式
  .AngleOfTheBox {
    position: absolute;
    padding: 0 5px;
    display: flex;
    align-items: center;
    width: 15%;
    height: 26px;
    color: #fff;
    background-color: #ff9899;
    //文字
    span {
      position: absolute;
      display: inline-block;
      color: #fff;
      width: 100%;
      bottom: 5px;
      left: 0;
      text-align: center;
    }
  }
  .AngleOfTheBox::after {
    content: "";
    position: absolute;
    left: 100%;
    top: 0;
    border-color: transparent transparent transparent #ff9899;
    border-width: 13px 0 13px 13px;
    border-style: solid;
  }
  .BtnOfTheBox {
    cursor: pointer;
    position: absolute;
    padding: 0 5px;
    display: flex;
    align-items: center;
    width: 20px;
    height: 26px;
    right: 0;
  }
  .CenterTheBox {
    border-radius: 1px;
    width: 100%;
    height: 30px;
    position: absolute;
    bottom: 70px;
    text-align: center;
  }
  .BottomTheBox {
    border-radius: 1px;
    width: 100%;
    height: 40px;
    position: absolute;
    bottom: 0px;
    text-align: center;
    line-height: 40px;
  }
}
.log-table {
  padding-top: 10px;
  width: 100%;
  height: calc(100vh - 100px);
}
.el-dialog__body {
  display: flex;

  justify-content: center;

  align-content: center;
}
</style>
