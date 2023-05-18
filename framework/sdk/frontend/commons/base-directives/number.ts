import type { App } from "vue";
const defaultMin = 0;
const defaultMax = Number.MAX_SAFE_INTEGER;
export default {
  install: (app: App) => {
    app.directive("number", {
      beforeUpdate(el: any, binding: any) {
        document.getElementsByTagName;
        const input: HTMLInputElement = el.getElementsByTagName("input")[0];
        input.onkeyup = function () {
          // 重置数据
          input.value = resetNumber(input.value, binding.value).toString();
          // 下发事件
          trigger(input, "input");
        };
        input.onblur = function () {
          // 重置数据
          input.value = resetNumber(input.value, binding.value).toString();
          // 下发事件
          trigger(input, "input");
        };
      },
    });
  },
};

/**
 * 重置数据
 * @param inputValue 输入值
 * @param binding    binding值
 * @returns 重置后的值
 */
const resetNumber = (
  inputValue: string,
  binding: { max: number; min: number }
) => {
  const numberValue = parseInt(inputValue);
  if (isNaN(numberValue)) {
    return binding.min || defaultMin;
  } else if (numberValue < (binding.min || defaultMin)) {
    return binding.min || defaultMin;
  } else if (numberValue > (binding.max || defaultMax)) {
    return binding.max || defaultMax;
  } else {
    return numberValue;
  }
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
