import type { App } from "vue";
const defaultMin = 0;
const defaultMax = Number.MAX_SAFE_INTEGER;
const defaultNumberType = "int";
// 保留俩位小数
const defaultNumberFixed = 2;
export default {
  install: (app: App) => {
    app.directive("number", {
      beforeUpdate(el: any, binding: any) {
        document.getElementsByTagName;
        const input: HTMLInputElement = el.getElementsByTagName("input")[0];
        input.onkeyup = function () {
          if (!checkValue(input.value, binding.value)) {
            // 重置数据
            input.value = resetNumber(input.value, binding.value);
            // 下发事件
            trigger(input, "input");
          }
        };
        input.onblur = function () {
          if (!checkValue(input.value, binding.value)) {
            // 重置数据
            input.value = resetNumber(input.value, binding.value);
            // 下发事件
            trigger(input, "input");
          }
        };
      },
    });
  },
};

const checkValue = (
  inputValue: string,
  binding: {
    max: number;
    min: number;
    type?: "int" | "float";
    fixed?: number;
    isNull?: boolean;
  }
) => {
  if (binding.isNull && !inputValue) {
    return true;
  }
  if (binding.type === "int") {
    if (/^\d*&/.test(inputValue)) {
      const value = parseInt(inputValue);
      if (value > binding.max || value < binding.min) {
        return false;
      }
    } else {
      return false;
    }
  } else {
    if (/^(\d+\.\d+|\d+\.|[1-9]\d*)$/.test(inputValue)) {
      const value = parseFloat(inputValue);
      if (value > binding.max || value < binding.min) {
        return false;
      }
      if (numberLength(inputValue) > (binding.fixed || defaultNumberFixed)) {
        return false;
      }
    } else {
      return false;
    }
  }
  return true;
};
const numberLength = (value: string) => {
  const split = value.toString().split(".");
  if (split.length > 1) {
    return split[1].length;
  } else {
    return 0;
  }
};
/**
 * 重置数据
 * @param inputValue 输入值
 * @param binding    binding值
 * @returns 重置后的值
 */
const resetNumber = (
  inputValue: string,
  binding: { max: number; min: number; type?: "int" | "float"; fixed?: number }
) => {
  let parse = parseInt;
  if ((binding.type || defaultNumberType) === "int") {
    parse = parseInt;
  } else {
    parse = parseF;
  }
  const numberValue = parse(inputValue);
  let resultValue = numberValue;
  if (isNaN(numberValue)) {
    resultValue = binding.min || defaultMin;
  } else if (numberValue < (binding.min || defaultMin)) {
    resultValue = binding.min || defaultMin;
  } else if (numberValue > (binding.max || defaultMax)) {
    resultValue = binding.max || defaultMax;
  } else {
    return binding.type === "float" &&
      numberLength(numberValue.toString()) >
        (binding.fixed || defaultNumberFixed)
      ? resultValue.toFixed(binding.fixed || defaultNumberFixed)
      : resultValue.toString();
  }
  return resultValue.toString();
};

const parseF = (number: string) => {
  return parseFloat(number);
};
/**
 * 下发event事件
 * @param el 标签
 * @param type 事件类型
 */
const trigger = (el: HTMLInputElement, type: string) => {
  const e = document.createEvent("HTMLEvents");
  // todo 未找到替代函数
  e.initEvent(type, true, true);
  el.dispatchEvent(e);
};
