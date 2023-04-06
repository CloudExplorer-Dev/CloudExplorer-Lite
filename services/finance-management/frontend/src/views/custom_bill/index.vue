<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <ce-table
      localKey="customBillTable"
      v-loading="loading"
      height="100%"
      ref="table"
      :columns="columns"
      :data="dataList"
      :tableConfig="tableConfig"
      row-key="id"
    >
      <template #toolbar>
        <el-button type="primary" @click="addBillRule">创建</el-button>
      </template>
      <el-table-column type="selection" />
      <el-table-column prop="name" label="规则名称"> </el-table-column>
      <el-table-column prop="name" label="分组维度">
        <template #default="scope">
          <span>{{
            scope.row.groups && scope.row.groups.length > 0
              ? scope.row.groups.map((g: Group) => g.name).join(",")
              : "N/A"
          }}</span>
        </template>
      </el-table-column>
      <fu-table-operations v-bind="tableConfig.tableOperations" fix />
      <template #buttons>
        <CeTableColumnSelect :columns="columns" />
      </template>
    </ce-table>
  </layout-content>
  <el-dialog
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    v-model="billRuleDialogVisible"
    :title="billRuleFormType === 'ADD' ? '创建自定义账单' : '编辑自定义账单'"
    width="60%"
    style="min-width: 600px"
  >
    <el-form
      @submit.prevent
      label-position="top"
      :model="billRuleForm"
      ref="ruleFormRef"
      label-width="120px"
      class="form_container"
    >
      <el-form-item
        label="账单名称"
        prop="name"
        :rules="{
          message: '名称不能为空',
          trigger: 'blur',
          required: true,
        }"
      >
        <el-input v-model="billRuleForm.name" />
      </el-form-item>
      <el-form-item
        label="统计字段"
        prop="groups"
        :rules="{
          message: '分组维度不能为空',
          trigger: 'change',
          required: true,
          type: 'array',
          min: 1,
        }"
      >
        <BillRuleGroup
          ref="billRuleGroup"
          :groups="billRuleForm.groups"
          @add-rule-group="addRuleGroup"
          @delete-rule-group="deleteRuleGroup"
          @update-rule-group="updateRuleGroup"
        ></BillRuleGroup>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="() => (billRuleDialogVisible = false)">
          取消
        </el-button>
        <el-button type="primary" @click="saveOrUpdate(billRuleFormType)">
          保存
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
  TableOperations,
} from "@commons/components/ce-table/type";
import billRuleApi from "@/api/bill_rule";
import type { AddRule, BillRule, Group } from "@/api/bill_rule/type";
import BillRuleGroup from "@/components/bill_rule_group/index.vue";
import { nanoid } from "nanoid";
import type { FormInstance } from "element-plus";
import { ElMessage, ElMessageBox } from "element-plus";

/**
 *加载器
 */
const loading = ref<boolean>(false);

/**
 * 数据列表
 */
const dataList = ref<Array<BillRule>>([]);

/**
 * 表格对象
 */
const table = ref<any>();

/**
 * 账单对话框是否展示
 */
const billRuleDialogVisible = ref<boolean>(false);

/**
 * 账单规则对话框类型
 */
const billRuleFormType = ref<"ADD" | "EDIT">("ADD");
const editBillRuleRow = ref<BillRule>();
const billRuleGroup = ref<InstanceType<typeof BillRuleGroup> | null>();
/**
 * 账单规则表单
 */
const billRuleForm = ref<AddRule>({
  name: "",
  groups: [
    {
      id: nanoid(),
      field: "",
      name: "",
      missName: "其他",
    },
  ],
});

/**
 * 删除规则
 * @param id 需要删除的id
 */
const deleteRuleGroup = (id: string) => {
  billRuleForm.value.groups = billRuleForm.value.groups.filter(
    (g) => g.id !== id
  );
};

/**
 * 修改规则
 * @param id     id
 * @param field  字段
 * @param name   名称
 */
const updateRuleGroup = (id: string, field: string, name: string) => {
  const findRuleGroup = billRuleForm.value.groups.find((g) => g.id === id);
  if (findRuleGroup) {
    findRuleGroup.field = field;
    findRuleGroup.name = name;
  }
};
/**
 * 添加一个分组条件
 */
const addRuleGroup = () => {
  billRuleForm.value.groups.push({
    id: nanoid(),
    field: "",
    name: "",
    missName: "其他",
  });
};
/**
 * 校验实例对象
 */
const ruleFormRef = ref<FormInstance>();
const saveOrUpdate = (billRuleFormType: "ADD" | "EDIT") => {
  if (!ruleFormRef.value) return;
  Promise.all([billRuleGroup.value?.validate(), ruleFormRef.value.validate()])
    .then(() => {
      (billRuleFormType === "ADD"
        ? billRuleApi.addBillRule(billRuleForm.value)
        : billRuleApi.updateBillRule({
            ...billRuleForm.value,
            id: editBillRuleRow.value?.id as string,
          })
      ).then(() => {
        ElMessage.success(billRuleFormType === "ADD" ? "添加成功" : "修改成功");
        table.value?.search();
        billRuleDialogVisible.value = false;
      });
    })
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    .catch(() => {});
};
/**
 * 查询函数
 * @param condition 查询条件
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  billRuleApi
    .pageBillRules(
      tableConfig.value.paginationConfig.currentPage,
      tableConfig.value.paginationConfig.pageSize,
      params,
      loading
    )
    .then((ok) => {
      dataList.value = ok.data.records;
      tableConfig.value.paginationConfig?.setTotal(
        ok.data.total,
        tableConfig.value.paginationConfig
      );
      tableConfig.value.paginationConfig?.setCurrentPage(
        ok.data.current,
        tableConfig.value.paginationConfig
      );
    });
};

// 列表字段数据
const columns = ref([]);
// 编辑
const editBillRule = (row: BillRule) => {
  billRuleDialogVisible.value = true;
  billRuleFormType.value = "EDIT";
  billRuleForm.value.name = row.name;
  editBillRuleRow.value = row;
  billRuleForm.value.groups = row.groups.map((g) => {
    g.id = nanoid();
    return g;
  });
};

// 删除
const deleteBillRule = (row: BillRule) => {
  ElMessageBox.confirm(`确认删除:  ${row.name}`, "提示", {
    confirmButtonText: "删除",
    cancelButtonText: "取消",
    type: "warning",
  }).then((ok) => {
    billRuleApi.deleteBillRule(row.id).then(() => {
      ElMessage.success("删除成功");
      table.value?.search();
    });
  });
};
const addBillRule = () => {
  billRuleDialogVisible.value = true;
  billRuleFormType.value = "ADD";
  billRuleForm.value.name = "";
  billRuleForm.value.groups = [
    {
      id: nanoid(),
      field: "",
      name: "",
      missName: "其他",
    },
  ];
};
/**
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [{ label: "规则名称", value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      "编辑",
      "primary",
      editBillRule,
      "EditPen"
    ),
    TableOperations.buildButtons().newInstance(
      "删除",
      "primary",
      deleteBillRule,
      "Delete"
    ),
  ]),
});

onMounted(() => {
  /**
   * 组件挂载查询数据
   */
  search(table.value?.getTableSearch());
});
</script>
<style lang="scss" scoped>
.form_container {
  margin: 0 26px;
}
</style>
