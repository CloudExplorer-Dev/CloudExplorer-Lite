import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { FormViewObject } from "@commons/components/ce-form/type";

import type { Ref } from "vue";

function getCreateServerForm(
  accountId: string,
  loading?: Ref<boolean>
): Promise<Result<FormViewObject>> {
  return get(`api/server/catalog/form/${accountId}`, null, loading);
}

const CatalogApi = {
  getCreateServerForm,
};
export default CatalogApi;
