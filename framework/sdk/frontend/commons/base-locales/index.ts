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
  const context = import.meta.glob("./lang/*.ts", {
    eager: true,
    import: "default",
  });
  const languages: Record<string, any> = {};
  const langs = Object.keys(context);
  for (const key of langs) {
    const lang = context[key];
    const name = key.replace(/(\.\/lang\/|\.ts)/g, "");
    languages[name] = lang;
  }
  return languages;
}

export const i18n = createI18n({
  legacy: false, // set `false`, to use Composition API
  locale: getLanguage(),
  messages: loadLanguages(),
});

export async function loadLocaleMessages(locale: string) {
  // 动态加载各类语言文件
  const messages = await import(`./lang/${locale}.ts`);
  // 设置默认语言
  i18n.global.setLocaleMessage(locale, messages.default);

  return nextTick();
}

export const setLanguage = (lang: string) => {
  if (i18n.global.locale.value !== lang) {
    loadLocaleMessages(lang);
    localStorage.setItem("language", lang);
    i18n.global.locale.value = lang; // if（i18n.mode === "legacy"） i18n.global.locale = lang；
  }
};

export default {
  install: (app: App) => {
    app.use(i18n);
  },
};
