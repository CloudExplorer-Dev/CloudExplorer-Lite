import Config from "@commons/utils/constants";

const TokenKey = Config.CE_TOKEN_KEY;
const RoleKey = Config.CE_ROLE_KEY;
const SourceKey = Config.CE_SOURCE_KEY;
const LangKey = Config.CE_LANG_KEY;

export function getToken() {
  return localStorage.getItem(TokenKey);
}

export function setToken(token: string) {
  return localStorage.setItem(TokenKey, token);
}

export function removeToken() {
  localStorage.removeItem(TokenKey);
}

export function getRole() {
  return localStorage.getItem(RoleKey);
}

export function setRole(role: string) {
  return localStorage.setItem(RoleKey, role);
}

export function removeRole() {
  localStorage.removeItem(RoleKey);
}

export function getSource() {
  return localStorage.getItem(SourceKey);
}

export function setSource(source: string) {
  return localStorage.setItem(SourceKey, source);
}

export function removeSource() {
  localStorage.removeItem(SourceKey);
}

export function getLang() {
  return localStorage.getItem(LangKey);
}

export function setLang(lang: string) {
  return localStorage.setItem(LangKey, lang);
}

export function removeLang() {
  localStorage.removeItem(LangKey);
}

const authStorage = {
  getToken,
  setToken,
  removeToken,
  getRole,
  setRole,
  removeRole,
  getSource,
  setSource,
  removeSource,
  getLang,
  setLang,
  removeLang,
};

export default authStorage;
