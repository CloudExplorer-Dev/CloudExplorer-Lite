import { defineStore } from "pinia";
import type { Platform } from "@commons/api/cloud_account/type";
import cloudAccountApi from "@commons/api/cloud_account/index";

interface PlatformStoreObj {
  platformList: Array<Platform>;
  isInit: boolean;
}

export const usePlatformStore = defineStore({
  id: "platform",
  state: (): PlatformStoreObj => ({
    platformList: [],
    isInit: false,
  }),
  getters: {
    platforms(state: any) {
      return state.platformList;
    },
  },
  actions: {
    init() {
      if (!this.isInit) {
        this.isInit = true;
        this.refresh();
      }
    },
    async refresh() {
      const platform = await cloudAccountApi.getPlatformAll();
      this.platformList = platform.data;
    },
  },
});
