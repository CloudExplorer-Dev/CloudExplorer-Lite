<template v-loading="_loading">
  <div style="width: 100%">
    <template v-if="!confirm">
      <el-form
        ref="ruleFormRef"
        label-suffix=":"
        label-position="left"
        style="margin-bottom: 18px"
        :model="_data"
      >
        <div v-for="(item, index) in _data" :key="index" class="row">
          <div style="padding-bottom: 6px; padding-top: 6px">
            {{ "主机 " + (index + 1) }}
          </div>
          <el-row :gutter="12" class="info-row">
            <el-col :span="23">
              <el-row :gutter="12">
                <el-col :span="12">
                  <el-form-item
                    :rules="{
                      message: '云主机名称' + '不能为空',
                      trigger: 'blur',
                      required: true,
                    }"
                    :prop="'[' + index + '].name'"
                  >
                    <el-input
                      v-model.trim="item.name"
                      placeholder="请输入云主机名称"
                    >
                      <template #prefix>
                        <div>云主机名称</div>
                      </template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item
                    :rules="[
                      {
                        message: 'Hostname' + '不能为空',
                        trigger: 'blur',
                        required: true,
                      },
                      {
                        message:
                          'Hostname' +
                          '只能包含小写字母、大写字母、数字、横线且是合法的FQDN',
                        trigger: 'blur',
                        pattern: /^[A-Za-z]+[A-Za-z0-9\-]*[A-Za-z0-9]$/,
                      },
                    ]"
                    :prop="'[' + index + '].hostname'"
                  >
                    <el-input
                      v-model.trim="item.hostname"
                      placeholder="请输入Hostname"
                    >
                      <template #prefix>
                        <div>Hostname</div>
                      </template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-col>
            <el-col :span="1">
              <!--              <CeIcon
                style="cursor: pointer; margin-left: 9px"
                size="16px"
                code="icon_delete-trash_outlined1"
                v-if="!defaultDisks[index]"
                @click="remove(index)"
                color="#6E748E"
              />-->
            </el-col>
          </el-row>
        </div>
      </el-form>
    </template>
    <template v-else>
      <template v-for="(o, i) in modelValue" :key="i">
        <el-descriptions direction="vertical" :column="3">
          <template #title>
            <div class="title">
              {{ "主机 " + (i + 1) }}
            </div>
          </template>
          <el-descriptions-item label="云主机名称" width="33.33%">
            {{ o.name }}
          </el-descriptions-item>
          <el-descriptions-item label="Hostname" width="33.33%">
            {{ o.hostname }}
          </el-descriptions-item>
          <el-descriptions-item width="33.33%"> </el-descriptions-item>
        </el-descriptions>
      </template>
    </template>
  </div>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { type FormInstance } from "element-plus";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import CeIcon from "@commons/components/ce-icon/index.vue";

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
</script>
<style lang="scss" scoped>
.add-button {
  margin: 10px;
}
:deep(.el-form-item) {
  margin-bottom: 0;
}
.title {
  font-style: normal;
  font-weight: 500;
  font-size: 14px;
  line-height: 22px;
}
.row {
  width: 100%;
}
.info-row {
  width: 100%;
  background: #f7f9fc;
  border-radius: 4px;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  height: 46px;
  padding-left: 12px;
  padding-right: 12px;
  margin-bottom: 6px;
}
</style>
