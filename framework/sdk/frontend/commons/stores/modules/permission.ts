import { defineStore } from "pinia";
import { useUserStore } from "./user";
import { getCurrentUserPlainPermissions } from "@commons/api/permission";
import { hasRolePermission } from "@commons/base-directives/hasPermission";
import type { RequiredPermissions } from "@commons/api/menu/type";
import type { Ref } from "vue";

interface PermissionObject {
  permissions?: Array<string>;
}

export const usePermissionStore = defineStore({
  id: "permission",
  state: (): PermissionObject => ({
    permissions: undefined,
  }),
  getters: {
    userPermissions(state: any): Array<string> {
      return state.permissions;
    },
  },
  actions: {
    async refreshPermissions(loading?: Ref<boolean>) {
      //console.log("调用");
      const userStore = useUserStore();
      if (!userStore.isLogin) {
        return;
      }
      if (!userStore.currentRole) {
        return;
      }
      const permissions = (await getCurrentUserPlainPermissions(loading)).data;
      this.permissions = permissions ? permissions : [];
    },
    hasPermission(binding: any) {
      const userStore = useUserStore();
      let has = false;
      let checkValue: string | string[] | Array<RequiredPermissions> = "";
      let isArray = false;
      if (typeof binding === "string") {
        checkValue = binding;
      } else if (typeof binding.value === "string") {
        checkValue = binding.value;
      } else if (binding instanceof Array && binding) {
        checkValue = binding;
        isArray = true;
      } else if (binding.value instanceof Array && binding.value) {
        checkValue = binding.value;
        isArray = true;
      }
      if (!isArray) {
        has = this.userPermissions.some((item: string) => {
          return item === checkValue;
        });
      } else {
        if (typeof checkValue[0] === "string") {
          has = this.userPermissions.some((item: string) => {
            return (checkValue as Array<string>).includes(item);
          });
        } else {
          if (
            hasRolePermission(
              userStore.currentRole,
              this.userPermissions,
              checkValue as Array<RequiredPermissions>
            )
          ) {
            has = true;
          }
        }
      }
      return has;
    },
  },
});
