import { defineStore } from "pinia";
export const moduleStore = defineStore({
  id: "module",

  state: () => ({
    currentMudule: {},
    modules: {},
  }),
  getters: {},
  actions: {},
});
