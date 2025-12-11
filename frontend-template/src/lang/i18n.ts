import i18n from "i18next"
import {initReactI18next} from "react-i18next";
import zhLang from './cn/cn'
import jpLang from './jp/jp'
import enLang from './en/en'
// import LanguageDetector from 'i18next-browser-languagedetector';
const langDefaultValue = 'zh' || window.localStorage.getItem('lang') || 'zh'
window.localStorage.setItem('lang', langDefaultValue)
console.debug("langDefaultValue", langDefaultValue)
i18n
  // 注入 react-i18next 实例
  .use(initReactI18next)
  // 初始化 i18next
  // 配置参数的文档: https://www.i18next.com/overview/configuration-options
  .init({
    debug: true,
    fallbackLng: langDefaultValue,
    interpolation: {
      escapeValue: false,
    },
    detection: {
      order: ['querystring', 'navigator', 'localStorage'],
      lookupQuerystring: 'lang',
    },
    resources: {
      zh: {
        translation: zhLang
      },
      "zh-CN": {
        translation: zhLang
      },
      "en-US": {
        translation: enLang
      },
      "ja-JP": {
        translation: jpLang
      },
    }
  })
export default i18n;
