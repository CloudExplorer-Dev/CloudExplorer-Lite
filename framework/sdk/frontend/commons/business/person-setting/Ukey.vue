<script setup lang="ts">
import { onMounted, ref } from "vue";
import {
  createApiKeys,
  deleteApiKeys,
  getApiKeys,
  updateApiKeys,
} from "@commons/api/user";
import { useClipboard, useDateFormat } from "@vueuse/core";
import { ElMessage, ElMessageBox } from "element-plus";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const { copy } = useClipboard();

const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});

interface ApiKey {
  id: string;
  accessKey: string;
  secretKey: string;
  status: string;
  createTime: string;
}

const tableData = ref<ApiKey[]>();

const formatted = (time: number) => {
  return useDateFormat(time, "YYYY-MM-DD HH:mm:ss").value;
};

const handleDelete = (row: ApiKey) => {
  ElMessageBox.confirm(
    t("commons.message_box.confirm_delete"),
    t("commons.message_box.alert"),
    {
      confirmButtonText: t("commons.message_box.confirm"),
      cancelButtonText: t("commons.btn.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      deleteApiKeys(row).then(() => {
        ElMessage.success(t("commons.msg.delete_success"));
        getUKeys();
      });
    })
    .catch(() => {
      ElMessage.info(t("commons.btn.cancel"));
    });
};

const handleSwitch = (row: ApiKey) => {
  updateApiKeys(row).then(() => {
    ElMessage(t("commons.msg.op_success"));
  });
};

const handleCreate = () => {
  createApiKeys().then(() => {
    getUKeys();
  });
};

const handleView = () => {
  dialogVisible.value = false;
  //TODO 跳转到API页面
};

const handleCopy = (data: any) => {
  copy(data)
    .then(() => {
      ElMessage.success(t("commons.msg.success", [t("commons.btn.copy")]));
    })
    .catch(() => {
      ElMessage.error(t("commons.msg.fail", [t("commons.btn.copy")]));
    });
};

const getUKeys = () => {
  getApiKeys().then((res) => {
    tableData.value = res.data.apiKeys;
  });
};

onMounted(() => {
  getUKeys();
});
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    title="API Keys"
    width="40%"
    destroy-on-close
  >
    <el-table :data="tableData">
      <el-table-column prop="accessKey" label="Access Key" width="180">
        <template #default="scope">
          <div class="key-container">
            <div class="key">
              {{ scope.row.accessKey }}
            </div>
            <el-tooltip
              class="box-item"
              effect="dark"
              :content="$t('commons.btn.copy')"
              placement="bottom"
            >
              <el-icon class="copy" @click="handleCopy(scope.row.accessKey)"
                ><copyDocument
              /></el-icon>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="secretKey" label="Secret Key" width="150">
        <template #default="scope">
          <el-popover placement="right" width="200" height="50" trigger="click">
            <div class="key-container">
              <div class="key">{{ scope.row.secretKey }}</div>
              <div>
                <el-tooltip
                  class="box-item"
                  effect="dark"
                  :content="$t('commons.btn.copy')"
                  placement="bottom"
                >
                  <el-icon class="copy" @click="handleCopy(scope.row.secretKey)"
                    ><copyDocument
                  /></el-icon>
                </el-tooltip>
              </div>
            </div>
            <template #reference>
              <el-link type="primary">{{ $t("commons.btn.display") }}</el-link>
            </template>
          </el-popover>
        </template>
      </el-table-column>
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
        width="160"
      >
        <template #default="scope">
          <span>{{ formatted(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
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
      <el-button @click="handleCreate">{{
        $t("commons.btn.create")
      }}</el-button>
      <el-button type="primary" @click="handleView">{{
        $t("commons.btn.view_api")
      }}</el-button>
    </footer>
  </el-dialog>
</template>

<style lang="scss" scoped>
.key-container {
  display: flex;
  align-items: center;
  .key {
    margin-right: 5px;
  }
  .copy {
    color: var(--el-color-primary);
    cursor: pointer;
  }
}
</style>
