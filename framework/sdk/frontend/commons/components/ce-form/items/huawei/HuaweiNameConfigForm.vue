<template v-loading="_loading">
  <template v-if="!confirm">
    <el-form
      ref="ruleFormRef"
      label-width="130px"
      label-suffix=":"
      label-position="top"
      style="width: 100%"
      :model="_data"
      :show-message="false"
      :size="''"
      :scroll-to-error="true"
    >
      <template v-for="(item, index) in _data" :key="index">
        <div class="box-title">
          {{ index === 0 ? "主机" : "主机 " + index }}
        </div>
        <div class="box-content">
          <el-form-item :rules="nameRules" :prop="'[' + index + '].name'">
            <ce-regex-tooltip
              :ref="'nameInputRef' + index"
              :description="'主机名称必须同时符合以下规则'"
              :model-value="item.name"
              :rules="nameRules"
            >
              <el-input placeholder="请输入云主机名称" v-model="item.name" />
            </ce-regex-tooltip>
          </el-form-item>
          <el-form-item
            :rules="hostNameRules"
            :prop="'[' + index + '].hostname'"
          >
            <tooltip
              :ref="'hostNameInputRef' + index"
              :description="'Hostname必须同时符合以下规则'"
              :model-value="item.hostname"
              :rules="hostNameRules"
            >
              <el-input placeholder="请输入Hostname" v-model="item.hostname" />
            </tooltip>
          </el-form-item>
        </div>
      </template>
    </el-form>
  </template>
  <template v-else>
    <div class="detail-box">
      <template v-for="(o, i) in modelValue" :key="i">
        <el-descriptions :title="'主机' + (i + 1)">
          <el-descriptions-item>
            <detail-page
              :content="getModelValueDetail(o)"
              :item-width="'33.33%'"
              :item-bottom="'28px'"
            ></detail-page>
          </el-descriptions-item>
        </el-descriptions>
      </template>
    </div>
  </template>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { type FormInstance } from "element-plus";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import Tooltip from "@commons/components/ce-regex-tooltip/index.vue";

interface ServerInfo {
  name?: string;
  hostname?: string;
}

const props = defineProps<{
  modelValue?: Array<ServerInfo>;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

// 校验实例对象
const ruleFormRef = ref<FormInstance>();
const _loading = ref<boolean>(false);

const nameRules = [
  {
    message: "云主机名称不能为空",
    trigger: "blur",
    pattern: "\\S",
    regex: "\\S",
    required: true,
  },
  {
    message:
      "只能由中文字符、英文字母、数字及“_”、“-”、“.”组成,且长度为[1-64]个字符。",
    trigger: "blur",
    pattern: "^[\u4E00-\u9FA5\\w.-]{1,64}$",
    regex: "^[\u4E00-\u9FA5\\w.-]{1,64}$",
  },
];
const hostNameRules = [
  {
    message: "Hostname" + "不能为空",
    trigger: "blur",
    pattern: "\\S",
    regex: "\\S",
    required: true,
  },
  {
    message:
      "长度为 [1-64] 个字符,允许使用点号(.)分隔字符成多段,每段允许使用大小写字母、数字或连字符(-),但不能连续使用点号(.)或连字符(-),不能以点号(.)或连字符(-)开头或结尾,不能出现(.-)和(-.)。",
    trigger: "blur",
    pattern: "^(?!.*(?:\\.\\.|--))[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*$",
    regex: "^(?!.*(?:\\.\\.|--))[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*$",
  },
];

const _data = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const activeTab = ref(0);

/**
 * 监听数量变化，获取值
 */
watch(
  () => props.allData.count,
  (count) => {
    activeTab.value = count - 1;
    setServers(count);
  }
);

function setServers(count: number | undefined) {
  if (count !== undefined) {
    if (_data.value) {
      if (_data.value.length > count) {
        _data.value = _.slice(_data.value, 0, count - _data.value.length);
      } else if (_data.value.length < count) {
        const temp = [];
        for (let i = 0; i < count - _data.value.length; i++) {
          temp.push({});
        }
        _data.value = _.concat(_data.value, temp);
      }
    }
  }
}

/**
 * 校验方法
 */
function validate(): Promise<boolean> {
  //执行自定义的校验方法（需要组件实现validate方法并defineExpose暴露）
  if (ruleFormRef.value) {
    return ruleFormRef.value.validate();
  } else {
    return new Promise((resolve, reject) => {
      return reject(true);
    });
  }
}

onMounted(() => {
  setServers(props.allData.count);
});

defineExpose({
  validate,
  field: props.field,
});
function getModelValueDetail(item: any) {
  const result: Array<any> = [];
  if (item) {
    result.push({ label: "云主机名称", value: item.name });
    result.push({ label: "Hostname", value: item.hostname });
  }
  return result;
}
</script>
<style lang="scss" scoped>
.box-content {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  padding: 18px;
  background-color: #f7f9fc;
  border-radius: 4px;
  .el-form-item {
    margin-bottom: 0;
    margin-right: 0;
    padding: 0px 2px 0px 8px;
    width: 100%;
    .el-form-item__content .custom-popover {
      width: 100%;
    }
  }

  .delete-icon {
    cursor: pointer;
    margin-left: 8px;
  }
}
.add-button {
  margin: 10px;
}
.detail-box {
  margin-top: 8px;
  :deep(.el-descriptions__header) {
    margin-bottom: 0 !important;
  }
}
</style>
