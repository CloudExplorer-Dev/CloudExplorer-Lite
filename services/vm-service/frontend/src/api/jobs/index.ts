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

export function getJobById(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<JobInfo>> {
  return get("/api/jobs", { id: id }, loading);
}

const JobsApi = {
  listJobs,
  getJobById,
};
export default JobsApi;
