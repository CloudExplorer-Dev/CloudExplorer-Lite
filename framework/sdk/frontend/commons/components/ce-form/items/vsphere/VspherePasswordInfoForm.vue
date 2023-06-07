<template>
  <template v-if="!confirm">
    <div>
      <el-form
        ref="ruleFormRef"
        label-position="top"
        style="margin-bottom: 18px"
        require-asterisk-position="right"
        :model="_data"
      >
        <el-form-item
          label="登录方式"
          :rules="[
            {
              message: '登录方式' + '不能为空',
              trigger: 'change',
              required: true,
            },
          ]"
        >
          <el-radio-group v-model="_data.type">
            <el-radio-button label="NONE">默认密码</el-radio-button>
            <el-radio-button label="LINUX">自定义Linux密码</el-radio-button>
            <el-radio-button label="WINDOWS">自定义Windows密码</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <template v-if="_data.type === 'NONE'">
          <el-form-item label="登录名"> 默认为模板的用户名</el-form-item>
          <el-form-item label="密码"> 默认为模板的密码</el-form-item>
        </template>

        <template v-if="_data.type === 'LINUX'">
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item
                :rules="[
                  {
                    message: '模板用户名' + '不能为空',
                    trigger: 'blur',
                    required: true,
                  },
                ]"
                prop="imageUser"
                label="模板用户名"
              >
                <el-input
                  v-model.trim="_data.imageUser"
                  placeholder="请输入模板用户名"
                  autocomplete="new-password"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                :rules="[
                  {
                    message: '模板密码' + '不能为空',
                    trigger: 'blur',
                    required: true,
                  },
                ]"
                prop="imagePassword"
                label="模板密码"
              >
                <el-input
                  v-model.trim="_data.imagePassword"
                  show-password
                  placeholder="请输入模板密码"
                  autocomplete="new-password"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item
                :rules="[
                  {
                    message: '用户名' + '不能为空',
                    trigger: 'blur',
                    required: true,
                  },
                ]"
                prop="loginUser"
                label="自定义用户名（参数占位符: @[LOGIN_USER]）"
              >
                <el-input
                  v-model.trim="_data.loginUser"
                  placeholder="请输入用户名"
                  autocomplete="new-password"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                :rules="[
                  {
                    message: '密码' + '不能为空',
                    trigger: 'blur',
                    required: true,
                  },
                  {
                    message:
                      '密码' +
                      '格式必须符合: 字符个数大于等于8，包含英文大小写、数字及特殊字符 ~!@#$%^&*()_+=?.',
                    trigger: ['blur', 'change'],
                    validator: checkPassword,
                  },
                ]"
                prop="loginPassword"
                label="自定义密码（参数占位符: @[NEW_PASSWORD]）"
              >
                <el-input
                  v-model.trim="_data.loginPassword"
                  show-password
                  placeholder="请输入密码"
                  autocomplete="new-password"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <div
            style="
              width: 100%;
              margin-bottom: 8px;
              line-height: 22px;
              display: flex;
            "
          >
            <div>
              设置密码脚本，云主机创建完成后会执行以下脚本，请确保模板用户名/密码正确
            </div>
          </div>
          <el-form-item
            :rules="[
              {
                message: '脚本' + '不能为空',
                trigger: 'blur',
                required: true,
              },
            ]"
            prop="script"
          >
            <el-input
              v-model="_data.script"
              type="textarea"
              :rows="5"
              placeholder="Linux虚拟机会通过此脚本设置密码"
            />
          </el-form-item>
        </template>

        <template v-if="_data.type === 'WINDOWS'">
          <el-form-item
            :rules="[
              {
                message: '密码' + '不能为空',
                trigger: 'blur',
                required: true,
              },
              {
                message:
                  '密码' +
                  '格式必须符合: 字符个数大于等于8，包含英文大小写、数字及特殊字符 ~!@#$%^&*()_+=?.',
                trigger: ['blur', 'change'],
                validator: checkPassword,
              },
            ]"
            prop="loginPassword"
            label="登录密码"
          >
            <el-input
              v-model.trim="_data.loginPassword"
              show-password
              placeholder="请输入密码"
            />
          </el-form-item>
        </template>
      </el-form>
    </div>
  </template>
  <template v-else>
    <el-descriptions direction="vertical" :column="3">
      <el-descriptions-item label="登录方式" width="33.33%">
        <template v-if="modelValue?.type === 'NONE'"> 默认密码 </template>
        <template v-if="modelValue?.type === 'LINUX'">
          自定义Linux密码
        </template>
        <template v-if="modelValue?.type === 'WINDOWS'">
          自定义Windows密码
        </template>
      </el-descriptions-item>
      <el-descriptions-item
        label="登录名"
        width="33.33%"
        v-if="modelValue?.type === 'NONE'"
      >
        默认为模板的用户名
      </el-descriptions-item>
      <el-descriptions-item
        label="密码"
        width="33.33%"
        v-if="modelValue?.type === 'NONE'"
      >
        默认为模板的密码
      </el-descriptions-item>
    </el-descriptions>
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed, ref } from "vue";
import type { FormInstance } from "element-plus";
import _ from "lodash";

const ruleFormRef = ref<FormInstance>();

interface PasswordObject {
  type?: "NONE" | "LINUX" | "WINDOWS";

  imagePassword?: string;

  imageUser?: string;

  loginUser?: string;

  loginPassword?: string;

  script?: string;

  programPath?: string;
}

const props = defineProps<{
  modelValue?: PasswordObject;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

const _data = computed({
  get() {
    if (!props.modelValue) {
      emit("update:modelValue", {
        type: "NONE",
        programPath: "/bin/bash",
        script:
          "#!/bin/bash\necho '@[NEW_PASSWORD]' | passwd --stdin @[LOGIN_USER]",
      });
      return { type: "NONE" } as PasswordObject;
    }
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

function validate(): Promise<boolean> {
  if (ruleFormRef.value) {
    return ruleFormRef.value.validate();
  } else {
    return new Promise((resolve, reject) => {
      return reject(true);
    });
  }
}

function checkPassword(rule: any, value: string, callback: any) {
  console.log(value);
  let valid = true;
  if (value.length < 8) {
    valid = false;
  }
  //大写字母
  if (!new RegExp(/[A-Z]/g).test(value)) {
    valid = false;
  }
  //小写字母
  if (!new RegExp(/[a-z]/g).test(value)) {
    valid = false;
  }
  //数字
  if (!new RegExp(/\d/g).test(value)) {
    valid = false;
  }
  //特殊字符
  let _value = _.replace(value, /[A-Z]/g, "");
  _value = _.replace(_value, /[a-z]/g, "");
  _value = _.replace(_value, /\d/g, "");
  //剩下的字符进行判断
  if (
    _value.length === 0 ||
    !_.every(_value, (a) => _.includes("~!@#$%^&*()_+=?.", a))
  ) {
    valid = false;
  }

  if (valid) {
    callback();
  } else {
    callback(new Error("格式错误"));
  }
}

defineExpose({
  validate,
  field: props.field,
});
</script>
<style lang="scss" scoped></style>
