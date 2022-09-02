import { defineStore } from "pinia";
import { useUserStore } from "./user";
import { getCurrentUserPlainPermissions } from "@commons/api/permission";

interface PermissionObject {
  permissions: Array<string>;
}

export const usePermissionStore = defineStore({
  id: "permission",
  state: (): PermissionObject => ({
    permissions: [],
  }),
  getters: {
    userPermissions(state: any): Array<string> {
      return state.permissions;
    },
  },
  actions: {
    async refreshPermissions() {
      const userStore = useUserStore();
      if (!userStore.isLogin) {
        return;
      }
      if (!userStore.currentRole) {
        return;
      }
      const permissions = (await getCurrentUserPlainPermissions()).data;
      this.permissions = permissions ? permissions : [];
    },
  },
});
