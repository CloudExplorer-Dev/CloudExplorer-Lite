import { createProdMockServer } from "vite-plugin-mock/es/createProdMockServer";
import module from "./module"; //引入定义的mock模拟接口
import user from "./user";
export function setupMock() {
  // 这个是用来注册mock的，当在生产中使用mock时，很重要
  createProdMockServer([...module, ...user]);
}
