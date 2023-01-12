import { defineStore } from "pinia";

interface HomeStoreObject {
  show: boolean;
}

export const useHomeStore = defineStore({
  id: "home",
  state: (): HomeStoreObject => ({
    show: false,
  }),
  getters: {
    showHome(state: any): boolean {
      return state.show;
    },
    isBase(state: any): boolean {
      return import.meta.env.VITE_APP_NAME === "base";
    },
  },
  actions: {
    setShow(show: boolean) {
      this.show = show;
    },
  },
});
