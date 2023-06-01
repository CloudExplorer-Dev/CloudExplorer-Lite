<script setup lang="ts">
import { computed } from "vue";
import {
  cronTypes,
  hoursAndMinute,
  hoursAndMinuteAndSecond,
  minuteAndSecond,
  SimpleCron,
} from "@commons/utils/cron";
import _ from "lodash";

const props = defineProps<{
  cron?: string;
}>();

const simpleCron = computed<SimpleCron>(() => {
  return new SimpleCron(_.defaultTo(props.cron, "0 0 * * * ? *"));
});

const cronType = computed(() => {
  return _.get(cronTypes, simpleCron.value.cronType);
});

const formattedCron = computed(() => {
  const array: Array<string> = [];
  _.forEach(cronType.value.children, (type) => {
    if (type === minuteAndSecond) {
      const minute = _.padStart(simpleCron.value.minute[0].toString(), 2, "0");
      const second = _.padStart(simpleCron.value.second[0].toString(), 2, "0");
      array.push(minute + "分" + second + "秒");
    } else if (type === hoursAndMinuteAndSecond || type === hoursAndMinute) {
      const hours = _.padStart(simpleCron.value.hours[0].toString(), 2, "0");
      const minute = _.padStart(simpleCron.value.minute[0].toString(), 2, "0");
      const second = _.padStart(simpleCron.value.second[0].toString(), 2, "0");
      array.push(hours + "时" + minute + "分" + second + "秒");
    } else {
      let options = [];
      const _type = _.get(cronTypes, type);
      if (typeof _type.selects === "function") {
        options = _type.selects();
      } else {
        options = _type.selects;
      }
      const _list = _.map(
        _.filter(options, (o) => {
          if (type === 5) {
            return _.includes(simpleCron.value["dayOfWeek"], o.value);
          } else if (type === 3) {
            return _.includes(simpleCron.value["dayOfMonth"], o.value);
          }
          return false;
        }),
        (value) => value.key
      );
      array.push(_.join(_list, ","));
    }
  });
  return _.join(array, " ");
});
</script>

<template>{{ cronType.key }}/ {{ formattedCron }}</template>

<style scoped lang="scss"></style>
