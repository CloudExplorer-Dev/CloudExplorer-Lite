<script setup lang="ts">
import { ref } from "vue";

const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});

interface ApiKey {
  accessKey: string;
  secretKey: string;
  status: string;
  createTime: string;
}

const tableData: ApiKey[] = [
  {
    accessKey: "ak",
    secretKey: "sk",
    status: "active",
    createTime: "2016-05-03",
  },
];

const handleDelete = (row: ApiKey) => {
  console.log(row);
};

const handleSwitch = (row: ApiKey) => {
  console.log(row);
};
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    title="API Keys"
    width="35%"
    destroy-on-close
  >
    <el-table :data="tableData">
      <el-table-column prop="accessKey" label="Access Key" width="180">
        <template #default="scope">
          <div>{{ scope.row.accessKey }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="secretKey" label="Secret Key" width="180" />
      <el-table-column prop="status" :label="$t('commons.status')" width="80">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="active"
            inactive-value="disabled"
            @change="handleSwitch(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        :label="$t('commons.create_time')"
        width="110"
      />
      <el-table-column :label="$t('commons.operation')">
        <template #default="scope">
          <el-button
            icon="delete"
            type="danger"
            @click="handleDelete(scope.row)"
            circle
          >
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <footer style="text-align: center; margin-top: 30px">
      <el-button @click="dialogVisible = false">{{
        $t("commons.btn.create")
      }}</el-button>
      <el-button type="primary" @click="dialogVisible = false">{{
        $t("commons.btn.view_api")
      }}</el-button>
    </footer>
  </el-dialog>
</template>
