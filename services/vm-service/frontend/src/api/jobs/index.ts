import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { Ref } from "vue";
import type { ListJobRequest, JobInfo } from "@/api/jobs/type";

export function listJobs(
  req: ListJobRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<JobInfo>>> {
  return get("api/jobs/page", req, loading);
}
const JobsApi = {
  listJobs,
};
export default JobsApi;
