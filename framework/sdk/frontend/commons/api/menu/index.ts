import { get } from "@commons/request";
import type { Menu } from "./type";
import type { Result } from "@commons/request/Result";
import type { Ref } from "vue";
import type { SimpleMap } from "@commons/api/base/type";

export function listMenus(
  loading?: Ref<boolean>
): Promise<Result<SimpleMap<Array<Menu>>>> {
  return get("/api/menus", null, loading);
}
