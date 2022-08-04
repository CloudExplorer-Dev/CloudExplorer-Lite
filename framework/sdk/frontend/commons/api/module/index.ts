import { get } from "../../request";
import { Module } from "./type";
import Result from "../../request/Result";
export const listRuningModules = () => {
  const modules: Promise<Result<Module>> = get("/api/list/modules");
  return modules;
};
