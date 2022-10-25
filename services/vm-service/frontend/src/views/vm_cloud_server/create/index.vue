<template>
  <el-container class="catalog-container" v-loading="loading">
    <el-header>
      <el-steps :active="active" finish-status="success">
        <el-step
          v-for="step in _.filter(steps, (s) => s.step > 0)"
          :key="step.step"
          :title="step.name"
        />
      </el-steps>
    </el-header>
    <el-main>
      <p class="description">{{ steps[active + 1]?.description }}</p>

      data: {{ data }}
      <br />
      formatData: {{ formatData }}

      <template v-if="steps[active + 1] && active !== steps.length - 2">
        <layout-container
          v-for="group in steps[active + 1]?.groups"
          :key="group.group"
        >
          <template #header>
            <h4>{{ group.name }}</h4>
          </template>
          <template #header_content>
            <p class="description">
              {{ group.description }}
            </p>
          </template>
          <template #content>
            {{ group }}
            <CeFormItem
              ref="ceForms"
              :other-params="cloudAccount"
              :group-id="group.group.toFixed()"
              v-model:form-view-data="group.forms"
              v-model:all-form-view-data="formData.forms"
              v-model:data="data[group.group.toFixed()]"
              :all-data="formatData"
              @optionListRefresh="optionListRefresh"
            ></CeFormItem>
          </template>
        </layout-container>
      </template>

      <template v-if="active === steps.length - 2"> 确认页面</template>
    </el-main>
    <el-footer>
      <div class="footer">
        <div class="footer-form">
          <template v-if="steps[0]?.groups[0]?.forms">
            <CeFormItem
              ref="ceForms_0"
              :other-params="cloudAccount"
              group-id="0"
              v-model:form-view-data="steps[0].groups[0].forms"
              v-model:all-form-view-data="formData.forms"
              v-model:data="data['0']"
              :all-data="formatData"
            ></CeFormItem>
          </template>
        </div>

        <div class="footer-btn">
          <el-button @click="cancel()"> 取消</el-button>
          <el-button
            v-if="active + 1 < steps.length && active !== 0"
            @click="before()"
          >
            上一步：{{ steps[active].name }}
          </el-button>
          <el-button
            v-if="active + 1 < steps.length - 1"
            class="el-button--primary"
            @click="next()"
          >
            下一步：{{ steps[active + 2].name }}
          </el-button>
          <el-button
            v-if="active + 1 === steps.length - 1"
            class="el-button--primary"
            @click="submit()"
          >
            确认创建
          </el-button>
        </div>
      </div>
    </el-footer>
  </el-container>
</template>

<script setup lang="ts">
const props = defineProps<{
  accountId: string;
}>();

import CatalogApi from "@/api/catalog";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import _ from "lodash";
import type { SimpleMap } from "@commons/api/base/type";
import type {
  FormViewObject,
  GroupAnnotation,
  StepAnnotation,
  FormView,
} from "@commons/components/ce-form/type";
import CeFormItem from "./CeFormItem.vue";
import type { CloudAccount } from "@commons/api/cloud_account/type";

import { computed, onMounted, ref, type Ref } from "vue";
import { useRouter } from "vue-router";

const useRoute = useRouter();

const loading: Ref<boolean> | undefined = ref<boolean>(false);

const formData = ref<FormViewObject>();

const active = ref(0);

const ceForms = ref<Array<InstanceType<typeof CeFormItem> | null>>([]);
const ceForms_0 = ref<InstanceType<typeof CeFormItem> | null>(null);

const data = ref({});

const formatData = computed(() => {
  return _.assign({}, ..._.values(data.value));
});

const cloudAccount = ref<CloudAccount | null>(null);

function next() {
  if (active.value++ > steps.value.length - 2) {
    active.value = steps.value.length - 2;
  }
}

function before() {
  if (active.value-- < 0) {
    active.value = 0;
  }
}

function submit() {
  console.log(data.value);
}

function cancel() {
  useRoute.push({ name: "server_catalog" });
}

/**
 * 渲染用表单格式
 *
 */
const steps = computed<Array<StepObj>>(() => {
  const tempGroupMap = _.groupBy(
    formData.value?.forms,
    (formData) => formData.group
  );
  const tempStepMap = _.groupBy(
    formData.value?.forms,
    (formData) => formData.step
  );

  const tempStepAnnotationMap: SimpleMap<StepAnnotation> = {
    "0": { step: 0, name: "base", description: "" },
    ...formData.value?.stepAnnotationMap,
  };
  const tempGroupAnnotationMap: SimpleMap<GroupAnnotation> = {
    "0": { group: 0, name: "base", description: "" },
    ...formData.value?.groupAnnotationMap,
  };

  const tempSteps = _.map(tempStepAnnotationMap, (step) => {
    const groupIdsOfStep = _.uniq(
      _.map(_.get(tempStepMap, step.step.toFixed()), (form) =>
        form.group?.toFixed()
      )
    );
    const groupsOfStep = _.filter(tempGroupAnnotationMap, (group) =>
      _.includes(groupIdsOfStep, group.group.toFixed())
    );
    return {
      ...step,
      groups: _.sortBy(
        _.map(groupsOfStep, (group) => {
          return {
            ...group,
            forms: _.sortBy(
              _.get(tempGroupMap, group.group.toFixed()),
              (form) => form.index
            ),
          } as GroupObj;
        }),
        (group) => group.group
      ),
    } as StepObj;
  });

  if (tempSteps.length > 1) {
    tempSteps.push({
      step: tempSteps.length,
      name: "确认信息",
      description: "",
      groups: [],
    });
  }

  return _.sortBy(tempSteps, (step) => step.step);
});

/**
 * 接收子组件传递过来需要刷新optionList的field名
 * @param field
 */
function optionListRefresh(field: string) {
  console.log(field);
  //找到field对应的组
  const form = _.find(formData.value?.forms, (view) => view.field === field);
  const groupId = form?.group?.toFixed();
  console.log(groupId);
  //调用子组件对应的刷新方法
  if (ceForms.value && groupId) {
    console.log("in");

    if (groupId === "0") {
      ceForms_0.value?.optionListRefresh(field);
    } else {
      (
        _.find(ceForms.value, (ceForm: InstanceType<typeof CeFormItem>) => {
          console.log(ceForm);
          return ceForm.groupId === groupId;
        }) as InstanceType<typeof CeFormItem>
      )?.optionListRefresh(field);
    }
  }
}

interface StepObj extends StepAnnotation {
  groups: Array<GroupObj>;
}

interface GroupObj extends GroupAnnotation {
  forms: Array<FormView>;
}

onMounted(() => {
  BaseCloudAccountApi.getCloudAccount(props.accountId, loading).then(
    (result) => {
      cloudAccount.value = result.data;

      CatalogApi.getCreateServerForm(props.accountId, loading).then(
        (result) => {
          formData.value = result.data;
          console.log(result.data);
        }
      );
    }
  );
});
</script>

<style lang="scss" scoped>
.catalog-container {
  height: 100%;

  .footer {
    border-top: 1px solid var(--el-border-color);
    padding-top: 10px;
    padding-bottom: 10px;
    display: flex;
    justify-content: space-between;
    flex-direction: row;
    align-items: center;
    flex-wrap: wrap;

    .footer-form {
      min-width: 400px;
    }

    .footer-btn {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;
      justify-content: flex-end;
    }
  }

  .description {
    padding-left: 15px;
    font-size: smaller;
    color: #606266;
  }
}
</style>
