<template>
  <el-container class="operation-wrapper">
    <el-header class="el-header">
      <el-steps :active="active" finish-status="success" align-centert="true">
        <el-step
          v-for="step in stepInfos"
          :key="step.step"
          :title="step.name"
        />
      </el-steps>
    </el-header>
    <el-main ref="catalog_container" class="el-main">
      <div>
        <p class="description">{{ steps[active + 1]?.description }}</p>
        <base-container
          v-loading="loading"
          style="
            --ce-base-container-height: auto;
            margin-bottom: 0;
            --ce-base-container-form-width: 100%;
          "
        >
          <template
            #form
            v-if="steps[active + 1] && active !== steps.length - 2"
          >
            <base-container
              v-for="group in steps[active + 1]?.groups"
              :key="group.group"
            >
              <template #header>
                <span>
                  {{ group.name }}
                </span>
              </template>
              <template #header_content>
                <el-tooltip :content="group.description">
                  <ce-icon
                    v-if="group.description"
                    class="maybe-icon"
                    size="14px"
                    code="icon-maybe_outlined"
                  />
                </el-tooltip>
              </template>
              <template #content>
                <CeFormItem
                  ref="ceForms"
                  :inline="group.inline"
                  :other-params="otherParams"
                  :group-id="group.group.toFixed()"
                  v-model:form-view-data="group.forms"
                  v-model:all-form-view-data="formData.forms"
                  v-model="data[group.group.toFixed()]"
                  :all-data="formatData"
                  @optionListRefresh="optionListRefresh"
                ></CeFormItem>
              </template>
            </base-container>
          </template>
        </base-container>
        <template v-if="active === steps.length - 2">
          <CreateConfirmStep
            :cloud-account="cloudAccount"
            :all-data="formatData"
            :all-form-view-data="formData"
          />
        </template>
      </div>
    </el-main>
    <el-footer class="el-footer">
      <div class="footer">
        <div class="footer-form">
          <template v-if="hasFooterForm">
            <CeFormItem
              ref="ceForms_0"
              :other-params="otherParams"
              :inline="true"
              group-id="0"
              v-model:form-view-data="steps[0].groups[0].forms"
              v-model:all-form-view-data="formData.forms"
              v-model="data['0']"
              :all-data="formatData"
              footer-location-center="false"
              @optionListRefresh="optionListRefresh"
              :disabled="active + 1 === steps.length - 1"
            ></CeFormItem>
          </template>
        </div>
        <div class="footer-center">
          <template v-if="hasFooterForm">
            <CeFooterFormItem
              ref="ceForms_0"
              :other-params="otherParams"
              group-id="0"
              v-model:form-view-data="steps[0].groups[0].forms"
              v-model:all-form-view-data="formData.forms"
              v-model="data['0']"
              :all-data="formatData"
              footer-location-center="false"
            ></CeFooterFormItem>
          </template>
        </div>
        <div class="footer-btn">
          <el-button @click="cancel()"> 取消 </el-button>
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
import { ElMessage } from "element-plus";

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
import CreateConfirmStep from "./CreateConfirmStep.vue";
import type { CloudAccount } from "@commons/api/cloud_account/type";

import {
  computed,
  onBeforeUnmount,
  onMounted,
  ref,
  type Ref,
  watch,
} from "vue";
import { useRouter } from "vue-router";
import type { CreateServerRequest } from "@/api/vm_cloud_server/type";
import { createServer } from "@/api/vm_cloud_server";
import { useI18n } from "vue-i18n";
import CeFooterFormItem from "@/views/vm_cloud_server/create/CeFooterFormItem.vue";
import bus from "@commons/bus";

const { t } = useI18n();
const useRoute = useRouter();

const loading: Ref<boolean> | undefined = ref<boolean>(false);

const formData = ref<FormViewObject>();

const active = ref(0);

const ceForms = ref<Array<InstanceType<typeof CeFormItem> | null>>([]);
const ceForms_0 = ref<InstanceType<typeof CeFormItem> | null>(null);

const catalog_container = ref<any>(null);

const data = ref<SimpleMap<any>>({});

const formatData = computed(() => {
  return _.assign({}, ..._.values(data.value));
});

const cloudAccount = ref<CloudAccount | null>(null);

function next() {
  if (data.value[0].count != null) {
    const promises = [];
    _.forEach(ceForms.value, (formRef: InstanceType<typeof CeFormItem>) => {
      promises.push(formRef.validate());
    });
    if (ceForms_0.value) {
      promises.push(ceForms_0.value.validate());
    }

    //console.log(promises);
    Promise.all(_.flatten(promises)).then((ok) => {
      //console.log(ok);
      active.value++;
      if (active.value > steps.value.length - 2) {
        active.value = steps.value.length - 2;
      }
      //定位到最上面
      catalog_container.value?.$el?.scrollTo(0, 0);
    });
  }
}

function before() {
  active.value--;
  if (active.value < 0) {
    active.value = 0;
  }
  //定位到最上面
  catalog_container.value?.$el?.scrollTo(0, 0);
}
function submit() {
  console.log(data.value);
  const req: CreateServerRequest = {
    accountId: props.accountId,
    createRequest: JSON.stringify(formatData.value),
    fromInfo: JSON.stringify(formData.value),
  };
  createServer(req, loading).then((ok) => {
    ElMessage.success(t("commons.msg.op_success"));
    useRoute.push({ name: "server_list" });
  });
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

const stepInfos = computed<Array<StepObj>>(() => {
  return _.filter(steps.value, (s) => s.step > 0);
});

const hasFooterForm = computed<boolean>(() => {
  return (
    steps.value[0]?.groups[0]?.forms !== undefined &&
    active.value !== steps.value?.length - 1
  );
});

const otherParams = computed(() => {
  return { ...cloudAccount.value, accountId: cloudAccount.value?.id };
});

/**
 * 接收子组件传递过来需要刷新optionList的field名
 * @param field
 */
function optionListRefresh(field: string) {
  //console.log(field);
  //找到field对应的组
  const form = _.find(formData.value?.forms, (view) => view.field === field);
  const groupId = form?.group?.toFixed();

  //调用子组件对应的刷新方法
  if (ceForms.value && groupId) {
    if (groupId === "0") {
      ceForms_0.value?.optionListRefresh(field, formatData.value);
    } else {
      (
        _.find(ceForms.value, (ceForm: InstanceType<typeof CeFormItem>) => {
          //console.log(ceForm);
          return ceForm.groupId === groupId;
        }) as InstanceType<typeof CeFormItem>
      )?.optionListRefresh(field, formatData.value);
    }
  }
}

interface StepObj extends StepAnnotation {
  groups: Array<GroupObj>;
}

interface GroupObj extends GroupAnnotation {
  forms: Array<FormView>;
}

onBeforeUnmount(() => {
  bus.emit("update::style", {});
});

onMounted(() => {
  bus.emit("update::style", {
    "--ce-main-content-padding-bottom": "0px",
    "--ce-main-content-margin-bottom": "0px",
  });
  BaseCloudAccountApi.getCloudAccount(props.accountId, loading).then(
    (result) => {
      cloudAccount.value = result.data;

      CatalogApi.getCreateServerForm(props.accountId, loading).then(
        (result) => {
          data.value["0"] = {};
          _.forEach(result.data?.groupAnnotationMap, (g) => {
            data.value[g.group.toFixed()] = {};
          });
          formData.value = result.data;
          //console.log(result.data);
        }
      );
    }
  );
});
</script>

<style lang="scss" scoped>
.operation-wrapper {
  height: 100%;
  margin: 0 -24px 0 -24px;
  .maybe-icon {
    margin-left: 4.67px;
  }
  .el-header {
    height: 60px;
    // padding: 0 10% 0 10%;
    .el-step__head {
      display: flex;
      align-items: center;
    }
    .el-step__title {
      margin-right: 10px;
    }
    .el-step__line {
      flex: 1;
      margin-left: 10px;
    }
  }
  .el-main {
    padding: 24px 24px !important;
    height: calc(100vh - 155px);
    overflow-y: auto;
    position: relative;
  }
  .el-footer {
    border-top: 1px solid var(--el-border-color);
    box-shadow: 0 -3px 4px rgba(30, 35, 41, 0.1);
    z-index: 2000;
    height: 95px;
    width: 100%;
    .footer {
      margin-top: 10px;
      display: flex;
      justify-content: start;
      flex-direction: row;
      align-items: center;
      flex-wrap: wrap;
      .footer-form {
        min-width: 200px;
        width: 21.33%;
      }
      .footer-center {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        justify-content: start;
        width: 33.33%;
      }
      .footer-btn {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        justify-content: flex-end;
        width: 45.33%;
      }
    }
  }
}
</style>
