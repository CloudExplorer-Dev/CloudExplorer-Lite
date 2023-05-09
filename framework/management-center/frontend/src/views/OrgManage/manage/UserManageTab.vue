<template>
  <el-container>
    <ce-table
      localKey="userManageTable"
      ref="table"
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      height="100%"
      table-layout="auto"
      :span-method="rowSpanSetter"
      v-loading="userLoading"
    >
      <template #toolbar>
        <el-button
          @click="addUser"
          type="primary"
          v-if="permissionStore.hasPermission('[management-center]USER:EDIT')"
        >
          添加用户
        </el-button>
      </template>
      <el-table-column prop="username" :label="$t('user.username')" fixed>
        <template #default="scope">
          <div class="custom-column">
            <a
              style="cursor: pointer; color: var(--el-color-primary)"
              @click="showUserDetail(scope.row)"
            >
              {{ scope.row.username }}
            </a>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="name" :label="$t('user.name')">
        <template #default="scope">
          <div class="custom-column">
            {{ scope.row.name }}
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('user.role')">
        <template #default="scope">
          <div class="custom-column">
            {{ scope.row.roleTable[0].roleName }}
          </div>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right">
        <template #default="scope">
          <el-button
            text
            type="primary"
            v-if="permissionStore.hasPermission('[management-center]USER:EDIT')"
            @click="removeUserRole(scope.row)"
          >
            移除用户
          </el-button>
        </template>
      </el-table-column>
    </ce-table>
  </el-container>
</template>
<script setup lang="ts">
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useI18n } from "vue-i18n";
import { ref, watch } from "vue";
import { useRouter } from "vue-router";
import CeDrawer from "@commons/components/ce-drawer/index.vue";
import UserApi from "@/api/user";
import { roleConst } from "@commons/utils/constants";
import {
  User,
  type AddUserRoleObject,
  type AddUserRoleRequest,
} from "@/api/user/type";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import {
  ElMessage,
  ElMessageBox,
  type FormInstance,
  type MessageBoxData,
} from "element-plus";
import _ from "lodash";

const props = defineProps<{
  sourceId?: string;
  type?: "WORKSPACE" | "ORGANIZATION" | "CE_BASE";
}>();

class MUser extends User {
  roleTable: Array<any>;
  rowSpan: number;

  constructor(user: User, skipParseRoleMap?: boolean) {
    super(
      user.id,
      user.username,
      user.name,
      user.email,
      user.phone,
      user.password,
      user.roleMap
    );
    this.rowSpan = 0;
    this.roleTable = [];
    if (!skipParseRoleMap) {
      this.roleTable = UserApi.getUserRoleSourceList(
        UserApi.getUserRoleList(_.defaultTo(user.roleMap, {}))
      );
    }
  }
}

const permissionStore = usePermissionStore();
const { t } = useI18n();
const ceDrawerRef = ref<InstanceType<typeof CeDrawer>>();
const router = useRouter();
const table = ref();
const tableData = ref<Array<MUser>>();
const userLoading = ref<boolean>(false);
const columns = ref([]);

const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);

  if (props.type === "WORKSPACE") {
    params.workspaceId = props.sourceId;
    params.parentRole = roleConst.user;
  } else if (props.type === "ORGANIZATION") {
    params.organizationId = props.sourceId;
    params.parentRole = roleConst.orgAdmin;
  } else {
    params.parentRole = roleConst.admin;
  }

  UserApi.pageUser(
    {
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    },
    userLoading
  ).then((res) => {
    const _list = _.map(res.data.records, (u) => new MUser(u));
    const resultList: Array<MUser> = [];

    _.forEach(_list, (mUser) => {
      const _roleTable = _.filter(mUser.roleTable, (rt) => {
        if (rt.parentRole === params.parentRole) {
          if (rt.parentRole === roleConst.admin) {
            return true;
          } else if (rt.parentRole === roleConst.orgAdmin) {
            return rt.source === params.organizationId;
          } else {
            return rt.source === params.workspaceId;
          }
        } else {
          return false;
        }
      });

      for (let i = 0; i < _roleTable.length; i++) {
        const u = new MUser(mUser, true);
        if (i === 0) {
          u.rowSpan = _roleTable.length;
        } else {
          u.rowSpan = 0;
        }
        u.roleTable = [_roleTable[i]];
        resultList.push(u);
      }
    });

    tableData.value = resultList;
    tableConfig.value.paginationConfig?.setTotal(
      res.data.total,
      tableConfig.value.paginationConfig
    );
    tableConfig.value.paginationConfig?.setCurrentPage(
      res.data.current,
      tableConfig.value.paginationConfig
    );
  });
};

const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    search: search,
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [
      { label: t("user.username"), value: "username" },
      { label: t("user.name"), value: "name" },
      { label: t("user.email"), value: "email" },
    ],
  },
  paginationConfig: new PaginationConfig(),
});

function rowSpanSetter({
  row,
  column,
  rowIndex,
  columnIndex,
}: {
  row: any;
  column: any;
  rowIndex: number;
  columnIndex: number;
}) {
  if (columnIndex === 0 || columnIndex === 1) {
    return {
      rowspan: row.rowSpan,
      colspan: 1,
    };
  } else {
    return {
      rowspan: 1,
      colspan: 1,
    };
  }
}

function removeUserRole(user: MUser) {
  ElMessageBox.confirm(t("user.delete_role_confirm"), {
    confirmButtonText: t("commons.message_box.confirm"),
    cancelButtonText: t("commons.btn.cancel"),
    type: "warning",
  })
    .then(() => {
      UserApi.removeUserRole(
        user.id,
        user.roleTable[0].roleId,
        user.roleTable[0].source,
        userLoading
      )
        .then((res) => {
          ElMessage.info(t("commons.msg.delete_success"));
          search(new TableSearch());
        })
        .catch((err) => {
          console.error(err);
        });
    })
    .catch(() => {
      ElMessage.info(t("commons.msg.delete_canceled"));
    });
}

const emit = defineEmits(["addUser"]);

function addUser() {
  emit("addUser", { id: props.sourceId, type: props.type });
}

function showUserDetail(user: MUser) {
  router.push({ name: "user_detail", params: { id: user.id } });
}

function refreshList() {
  search(new TableSearch());
}

watch(
  () => [props.sourceId, props.type],
  ([sourceId, type]) => {
    //console.log([sourceId, type]);
    if (type) {
      refreshList();
    }
  },
  { immediate: true }
);

defineExpose({ refreshList });
</script>
<style lang="scss"></style>
