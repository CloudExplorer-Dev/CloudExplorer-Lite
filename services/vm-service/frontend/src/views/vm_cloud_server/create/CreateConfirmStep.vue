<template>
  <base-container
    style="
      --ce-base-container-height: auto;
      margin-bottom: 0;
      --ce-base-container-form-width: 100%;
    "
  >
    <template #form>
      <base-container v-for="group in groups" :key="group.group">
        <template #header>
          <span>
            {{ group.name }}
          </span>
        </template>
        <template #header_content>
          <p class="description">
            {{ group.description }}
          </p>
        </template>
        <template #content>
          <template v-for="form in group.forms" :key="form.index">
            <template
              v-if="
                form.confirmSpecial &&
                !form.label &&
                form.confirmPosition === 'TOP'
              "
            >
              <!--   无label的特殊组件           -->
              <component
                ref="formItemRef"
                :is="form.inputType"
                :model-value="getDisplayValue(form)"
                :field="form.field"
                :all-data="allData"
                :form-item="form"
                :otherParams="{}"
                :confirm="true"
              />
            </template>
          </template>
          <!--暂时支持阿里云新的UI-->
          <template
            v-if="
              props.cloudAccount?.platform !== 'fit2cloud_vsphere_platform' &&
              props.cloudAccount?.platform !== 'fit2cloud_openstack_platform'
            "
          >
            <detail-page
              :content="getGroupFormDetail(group)"
              :item-width="'33.33%'"
              :item-bottom="'28px'"
            ></detail-page>
          </template>
          <template v-else>
            <el-descriptions
              :column="group.items"
              direction="vertical"
              :data-var="(_width = 100 / group.items)"
            >
              <el-descriptions-item
                label="云账号"
                v-if="group.group === 0"
                :width="_width + '%'"
              >
                <component
                  style="margin-top: 3px; width: 16px; height: 16px"
                  :is="platformIcon[cloudAccount?.platform]?.component"
                  v-bind="platformIcon[cloudAccount?.platform]?.icon"
                  :color="platformIcon[cloudAccount?.platform]?.color"
                  size="16px"
                  v-if="cloudAccount?.platform"
                ></component>
                <span style="margin-left: 10px">{{ cloudAccount?.name }}</span>
              </el-descriptions-item>
              <template v-for="form in group.forms" :key="form.index">
                <template v-if="form.label && checkShow(form)">
                  <el-descriptions-item
                    :label="form.label"
                    :span="form.confirmItemSpan"
                    :width="_width * form.confirmItemSpan + '%'"
                    :data-var="(form._display = getDisplayValue(form))"
                  >
                    <template v-if="!form.confirmSpecial">
                      <div class="description-inline">
                        <span
                          v-html="form._display"
                          :title="fieldValueMap[form.field]"
                        />
                      </div>
                      <span v-if="form.unit">{{ form.unit }}</span>
                    </template>
                    <template v-else>
                      <!--   有label的特殊组件           -->
                      <component
                        ref="formItemRef"
                        :is="form.inputType"
                        :model-value="form._display"
                        :all-data="allData"
                        :field="form.field"
                        :form-item="form"
                        :otherParams="{}"
                        :confirm="true"
                      />
                    </template>
                  </el-descriptions-item>
                </template>
              </template>
            </el-descriptions>
          </template>
          <template v-for="form in group.forms" :key="form.index">
            <template
              v-if="
                form.confirmSpecial &&
                !form.label &&
                form.confirmPosition === 'BOTTOM'
              "
            >
              <!--   无label的特殊组件           -->
              <component
                ref="formItemRef"
                :is="form.inputType"
                :model-value="getDisplayValue(form)"
                :field="form.field"
                :all-data="allData"
                :form-item="form"
                :otherParams="{}"
                :confirm="true"
              />
            </template>
          </template>
        </template>
      </base-container>
    </template>
  </base-container>
</template>

<script setup lang="ts">
import type {
  FormViewObject,
  ConfirmAnnotation,
  FormView,
} from "@commons/components/ce-form/type";
import { computed, h, ref } from "vue";
import _ from "lodash";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import PlatformIconVue from "@commons/components/detail-page/PlatformIcon.vue";
import { platformIcon } from "@commons/utils/platform";

const props = defineProps<{
  cloudAccount?: CloudAccount;
  allData?: any;
  allFormViewData?: FormViewObject;
}>();

interface Form extends ConfirmAnnotation {
  forms?: Array<FormView>;
}

const fieldValueMap = ref({});

const groups = computed(() => {
  const tempList = _.sortBy(
    _.values(props.allFormViewData?.confirmAnnotationMap),
    (group) => group.group
  );
  const list: Array<Form> = [];
  tempList.forEach((group) => {
    list.push({
      ...group,
      forms: _.clone(
        _.filter(
          props.allFormViewData?.forms,
          (form) => form.confirmGroup === group.group
        )
      ),
    });
  });

  return list;
});

function initFieldValueMap() {
  groups.value.forEach((group) => {
    group.forms?.forEach((form) => {
      fieldValueMap.value[form.field] = getDisplayValue(form);
    });
  });
}
initFieldValueMap();

function checkShow(currentItem: any) {
  let isShow = currentItem.label;
  if (currentItem.relationShows && currentItem.relationShowValues) {
    isShow = currentItem.relationShows.every((i: string) =>
      currentItem.relationShowValues.includes(
        _.get(props.allData, i) === true ? "true" : _.get(props.allData, i)
      )
    );
  }
  if (isShow) {
    isShow = currentItem.footerLocation === 0;
  }
  return isShow;
}

/**
 * 处理列表中取值展示
 * @param form
 */
function getDisplayValue(form: FormView) {
  const value = _.get(props.allData, form.field);
  let result: any = _.clone(value);
  if (!form.confirmSpecial) {
    if (
      form.optionList &&
      form.optionList instanceof Array &&
      form.optionList.length > 0
    ) {
      if (value instanceof Array) {
        form.valueItem = form.optionList.filter((o) =>
          _.includes(
            value,
            _.get(o, form.valueField ? form.valueField : "value")
          )
        );
      } else {
        form.valueItem = _.find(
          form.optionList,
          (o) => value === _.get(o, form.valueField ? form.valueField : "value")
        );
      }

      if (form.formatTextField && form.textField) {
        let temp = _.replace(form.textField, /{/g, "{form.valueItem['");
        temp = _.replace(temp, /}/g, "']}");
        result = eval("`" + temp + "`");
      } else {
        if (value instanceof Array) {
          result = "";
          result = _.join(
            _.map(form.valueItem, (v) =>
              _.get(v, form.textField ? form.textField : "label")
            ),
            ","
          );
        } else {
          result = _.get(
            form.valueItem,
            form.textField ? form.textField : "label"
          );
        }
      }
    }
  }
  if (
    result == undefined ||
    (typeof result === "string" && _.trim(result).length === 0)
  ) {
    if (
      form.optionList &&
      typeof form.optionList === "string" &&
      form.optionList.length > 0
    ) {
      return form.optionList;
    } else {
      return `<span style="color: var(--el-text-color-secondary)">空</span>`;
    }
  } else if (typeof result === "boolean") {
    return result ? "开启" : "关闭";
  } else {
    return result;
  }
}

function getGroupFormDetail(group: any) {
  const result: Array<any> = [];
  const forms = group.forms;
  if (group.group === 0) {
    result.push({
      label: "云账号",
      value: props.cloudAccount?.name,
      render: () => {
        return h(PlatformIconVue, {
          platform: props.cloudAccount?.platform,
        });
      },
    });
  }
  forms.forEach((form: any) => {
    if (form.label && checkShow(form)) {
      result.push({
        label: form.label + (form.unit ? "(" + form.unit + ")" : ""),
        value: getDisplayValue(form),
      });
    }
  });
  return result;
}
</script>

<style lang="scss" scoped>
:deep(.el-descriptions__label) {
  color: #8f959e;
}

.description-inline {
  display: inline-flex;
  flex-direction: row;
  justify-content: space-between;
  span {
    max-width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
</style>
