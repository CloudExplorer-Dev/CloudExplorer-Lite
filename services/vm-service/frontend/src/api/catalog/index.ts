import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { FormViewObject } from "@commons/components/ce-form/type";

import type { Ref } from "vue";
import type { Good } from "@/api/catalog/type";

function getCreateServerForm(
  accountId: string,
  loading?: Ref<boolean>
): Promise<Result<FormViewObject>> {
  return get(`api/server/catalog/form/${accountId}`, null, loading);
}

function getGoods(loading?: Ref<boolean>): Promise<Result<Array<Good>>> {
  return get(`api/server/catalog/goods`, null, loading);
}

const CatalogApi = {
  getCreateServerForm,
  getGoods,
};
export default CatalogApi;
