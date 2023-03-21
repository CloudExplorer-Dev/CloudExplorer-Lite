import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import _ from "lodash";
import { computed, type ComputedRef } from "vue";
import { store } from "@commons/stores";
import { useUserStore } from "@commons/stores/modules/user";

const moduleStore = useModuleStore(store);
const permissionStore = usePermissionStore(store);
const userStore = useUserStore(store);

export class BaseModuleInfo {
  icon: string;
  name: string;

  permissions: any;

  roles: string[];
  module: string;
  path: string;
  redirect: string;
  unit?: string;
  type?: "currency" | undefined;

  hasPermission: ComputedRef<boolean>;
  moduleActive: ComputedRef<boolean>;

  show: ComputedRef<boolean>;

  constructor(
    icon: string,
    name: string,
    roles: string[],
    permissions: any,
    module: string,
    path: string,
    redirect: string,
    unit?: string,
    type?: "currency" | undefined
  ) {
    this.icon = icon;
    this.name = name;
    this.roles = roles;
    this.module = module;
    this.path = path;
    this.redirect = redirect;
    this.unit = unit;
    this.type = type;
    this.permissions = permissions;

    this.hasPermission = computed<boolean>(() => {
      return (
        _.includes(this.roles, userStore.currentRole) &&
        permissionStore.hasPermission(this.permissions)
      );
    });
    this.moduleActive = computed<boolean>(() => {
      return _.some(
        moduleStore.runningModules,
        (module) => module.id === this.module
      );
    });
    this.show = computed<boolean>(() => {
      return this.hasPermission.value && this.moduleActive.value;
    });
  }
}
