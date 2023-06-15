<template>
  <el-drawer
    v-model="drawer"
    :title="packagePriceBillingId ? '编辑套餐' : '添加套餐'"
    direction="rtl"
    size="600px"
    :before-close="close"
  >
    <el-form
      :model="form"
      label-position="top"
      require-asterisk-position="right"
      label-width="100px"
      ref="formRef"
      :rules="rules"
    >
      <el-form-item label="套餐名称" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="套餐配置" prop="config">
        <div class="config">
          <el-row
            :gutter="20"
            v-for="(row, index) in packageConfigFieldList"
            :key="index"
          >
            <el-col :span="12" v-for="(col, index) in row" :key="index">
              <el-form-item
                :key="col.field"
                :label="col.fieldLabel"
                :rules="requiredVerification(col.fieldLabel)"
                :prop="`config.${col.field}`"
              >
                <el-input
                  v-number="{
                    max: 10240,
                    min: 1,
                    fixed: 3,
                    type: 'int',
                    isNull: true,
                  }"
                  v-model="form.config[col.field]"
                >
                  <template #append>
                    <span>{{ col.unitLabel }}</span>
                  </template></el-input
                ></el-form-item
              >
            </el-col>
          </el-row>
          <div style="margin-top: 16px">
            当前配置按单价计费价格参考
            <span class="price"
              >{{ onDemandPrice }}元/小时 &nbsp;&nbsp;&nbsp;
              {{ monthlyPrice }}元/月</span
            >
          </div>
        </div>
      </el-form-item>
      <el-form-item label="套餐价格" prop="price">
        <div class="config">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="按需按量套餐计费" :prop="`price.onDemand`">
                <el-input
                  v-number="{
                    max: 100000000,
                    min: 0,
                    fixed: 3,
                    type: 'float',
                    isNull: true,
                  }"
                  v-model="form.price.onDemand"
                  ><template #append>
                    <span> 元/小时</span>
                  </template></el-input
                ></el-form-item
              >
              <div class="price_preview">
                <span> 价格参考</span><span>{{ monthPriceView }}元/月</span>
              </div>
            </el-col>
            <el-col :span="12">
              <el-form-item label="包年包月套餐计费" :prop="`price.monthly`">
                <el-input
                  v-number="{
                    max: 100000000,
                    min: 0,
                    fixed: 3,
                    type: 'float',
                    isNull: true,
                  }"
                  v-model="form.price.monthly"
                  ><template #append>
                    <span>元/月</span>
                  </template></el-input
                ></el-form-item
              >
              <div class="price_preview">
                <span> 价格参考</span><span>{{ hourProceView }}元/小时</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close()">{{ $t("commons.btn.cancel") }} </el-button>
      <el-button type="primary" v-if="packagePriceBillingId" @click="operate()">
        编辑
      </el-button>
      <el-button type="primary" v-else @click="operate()"> 添加 </el-button>
    </template>
  </el-drawer>
</template>
<script setup lang="ts">
import { ref, computed } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { splitArray } from "@commons/utils/commons";
import type {
  BillingField,
  BillingFieldMeta,
  PackagePriceBillingPolicy,
} from "@/api/billing_policy/type";
import type { SimpleMap } from "@commons/api/base/type";
import { Decimal } from "decimal.js";
import { nanoid } from "nanoid";
import { ElMessage } from "element-plus";

/**
 * 抽屉打开
 */
const drawer = ref<boolean>(false);
/**
 * form表单对象
 */
const formRef = ref<FormInstance>();
/**
 * 主键id 用于判断是 编辑页还是添加页
 */
const packagePriceBillingId = ref<string>();

const props = defineProps<{
  /**
   * 添加套餐计费
   */
  addPackageBilling: (
    packagePriceBillingPolicy: PackagePriceBillingPolicy
  ) => boolean;
  /**
   * 修改套餐计费
   */
  editPackageBilling: (
    id: string,
    packagePriceBillingPolicy: PackagePriceBillingPolicy
  ) => boolean;
  /**
   * 按量
   */
  onDemandBillingPolicy: Array<BillingField>;
  /**
   * 包年包月
   */
  monthlyBillingPolicy: Array<BillingField>;
  /**
   * 字段元数据
   */
  fieldMeta: SimpleMap<BillingFieldMeta>;
}>();

const form = ref<{
  name: string;
  config: SimpleMap<number>;
  price: { onDemand: number; monthly: number };
}>({
  name: "",
  config: {},
  price: { onDemand: 0, monthly: 0 },
});

/**
 * 包年包月字段列表
 */
const packageConfigFieldList = computed(() => {
  const fieldList = Object.keys(props.fieldMeta).map((key) => {
    return { field: key, ...props.fieldMeta[key] };
  });
  fieldList.sort((pre, next) => pre.order - next.order);
  return splitArray(fieldList, 2);
});

const checkNumber = (number: string | number) => {
  return /^(\d+|\d+\.\d+|\d+\.)$/.test(number.toString());
};
/**
 * 包年包月费用预测
 */
const monthlyPrice = computed(() => {
  return props.monthlyBillingPolicy
    .map((field) => {
      return form.value.config[field.field] && field.price
        ? new Decimal(
            checkNumber(form.value.config[field.field])
              ? form.value.config[field.field]
              : 0
          ).mul(field.price)
        : new Decimal(0);
    })
    .reduce((pre, next) => pre.add(next))
    .toFixed(3);
});

/**
 * 按需费用预测
 */
const onDemandPrice = computed(() => {
  return props.onDemandBillingPolicy
    .map((field) => {
      return form.value.config[field.field] && field.price
        ? new Decimal(
            checkNumber(form.value.config[field.field])
              ? form.value.config[field.field]
              : 0
          ).mul(field.price)
        : new Decimal(0);
    })
    .reduce((pre, next) => pre.add(next))
    .toFixed(3);
});

/**
 * 获取当前月份的天数
 */
const currentDays = () => {
  const date = new Date();
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const d = new Date(year, month, 0);
  return d.getDate();
};

/**
 * 月份预估价钱
 */
const monthPriceView = computed(() => {
  return form.value.price.onDemand || form.value.price.onDemand === 0
    ? new Decimal(
        checkNumber(form.value.price.onDemand) ? form.value.price.onDemand : 0
      )
        .mul(new Decimal(24))
        .mul(currentDays())
        .toFixed(3)
    : "-";
});

/**
 * 小时预估价钱
 */
const hourProceView = computed(() => {
  return form.value.price.monthly || form.value.price.monthly === 0
    ? new Decimal(
        checkNumber(form.value.price.monthly) ? form.value.price.monthly : 0
      )
        .div(new Decimal(24).mul(new Decimal(currentDays())))
        .toFixed(3)
    : "-";
});

/**
 * 必填字段校验
 * @param fieldLabel 字段提示
 */
const requiredVerification = (fieldLabel: string) => {
  return {
    required: true,
    message: `${fieldLabel}必填`,
    trigger: "blur",
  };
};

/**
 * 规则对象
 */
const rules = ref<FormRules>({
  name: [{ required: true, message: "套餐名称必填", trigger: "blur" }],
  config: [{ required: true, message: "套餐配置必填", trigger: "blur" }],
  price: [{ required: true, message: "套餐价格必填", trigger: "blur" }],
});

/**
 * 操作按钮
 */
const operate = () => {
  formRef.value?.validate().then(() => {
    const packagePriceBillingPolicy: PackagePriceBillingPolicy = {
      id: nanoid(),
      name: form.value.name,
      onDemand: form.value.price.onDemand,
      monthly: form.value.price.monthly,
      billPolicyFields: Object.keys(props.fieldMeta).map((key) => {
        return {
          field: key,
          number: parseInt(form.value.config[key].toString()),
        };
      }),
    };
    if (packagePriceBillingId.value) {
      if (
        props.editPackageBilling(
          packagePriceBillingId.value,
          packagePriceBillingPolicy
        )
      ) {
        close();
      } else {
        ElMessage.error("该配置套餐已存在,请勿重复添加");
      }
    } else {
      if (props.addPackageBilling(packagePriceBillingPolicy)) {
        close();
      } else {
        ElMessage.error("该配置套餐已存在,请勿重复添加");
      }
    }
  });
};

/**
 * 打开添加套餐计费抽屉
 */
const open = (packagePriceBillingPolicy?: PackagePriceBillingPolicy) => {
  if (packagePriceBillingPolicy) {
    form.value.price = {
      onDemand: packagePriceBillingPolicy.onDemand,
      monthly: packagePriceBillingPolicy.monthly,
    };
    form.value.config = packagePriceBillingPolicy.billPolicyFields
      .map((item) => ({ [item.field]: item.number }))
      .reduce((pre, next) => ({ ...pre, ...next }));
    form.value.name = packagePriceBillingPolicy.name;
    packagePriceBillingId.value = packagePriceBillingPolicy.id;
  } else {
    form.value = {
      name: "",
      config: {},
      price: { onDemand: 0, monthly: 0 },
    };
    packagePriceBillingId.value = undefined;
  }
  drawer.value = true;
};

/**
 * 关闭抽屉
 */
const close = () => {
  drawer.value = false;
};

defineExpose({ open, close });
</script>
<style lang="scss" scoped>
.config {
  width: 100%;
  background: #f7f9fc;
  border-radius: 6px;
  padding: 16px;
}
.price {
  margin-left: 24px;
  color: #f54a45;
  font-weight: 500;
  font-size: 14px;
  line-height: 22px;
}
.price_preview {
  margin-top: 16px;
  display: flex;
  justify-content: space-between;
  span {
    &:last-child {
      color: #f54a45;
    }
  }
}
</style>
