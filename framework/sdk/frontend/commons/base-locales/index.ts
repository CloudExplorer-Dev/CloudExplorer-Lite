import type { App } from "vue";
import { nextTick } from "vue";
import { createI18n } from "vue-i18n";

export const getLanguage = () => {
  const cookieLanguage = localStorage.getItem("language");
  if (cookieLanguage) {
    return cookieLanguage.toLowerCase();
  }
  const language = navigator.language.toLowerCase();
  const locales = Object.keys(loadLanguages());
  for (const locale of locales) {
    if (language.indexOf(locale) > -1) {
      return locale;
    }
  }
  // 默认语言为简体中文
  return "zh-cn";
};

export function loadLanguages() {
  const context = import.meta.globEager("./lang/*.ts");
  const languages = {};
  const langs = Object.keys(context);
  for (const key of langs) {
    const lang = context[key].default;
    const name = key.replace(/(\.\/lang\/|\.ts)/g, "");
    languages[name] = lang;
  }
  return languages;
}

const i18n = createI18n({
  locale: getLanguage(),
  messages: loadLanguages(),
});

export async function loadLocaleMessages(locale) {
  // 动态加载各类语言文件
  const messages = await import(`./lang/${locale}.ts`);
  // 设置默认语言
  i18n.global.setLocaleMessage(locale, messages.default);

  return nextTick();
}

export const setLanguage = (lang) => {
  if (i18n.global.locale !== lang) {
    loadLocaleMessages(lang);
    localStorage.setItem("language", lang);
    if (i18n.mode === "legacy") {
      i18n.global.locale = lang;
    } else {
      i18n.global.locale.value = lang;
    }
  }
};

// 组合翻译，例如 key 为'请输入{0}'，keys 为 login.username，则自动将 keys 翻译并替换到 {0} {1}...
export const $tm = (key: string, ...keys: string[]) => {
  const values: string[] = [];
  for (const k of keys) {
    values.push(i18n.global.t(k));
  }
  return i18n.global.t(key, values);
};

// 忽略警告，即：不存在Key直接返回Key
export const $tk = (key) => {
  const hasKey = i18n.global.te(key);
  if (hasKey) {
    return i18n.global.t(key);
  }
  return key;
};

// export const $t = (key, value) => {
//   return i18n.global.t(key, value);
// };

export default {
  install:(app:APP) =>{
    app.use(i18n)
    // app.config.globalProperties.$t = $t;
    // app.config.globalProperties.$tm = $tm;
    // app.config.globalProperties.$tk = $tk;
}
};

