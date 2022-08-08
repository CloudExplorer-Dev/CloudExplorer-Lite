import { createProdMockServer } from "vite-plugin-mock/es/createProdMockServer";
import module from "./module"; //引入定义的mock模拟接口
import user from "./user";
export function setupMock(mocks: any) {
  const mockList = [];
  Object.keys(mocks).forEach((key) => {
    return mockList.push(...mocks[key].default);
  });
  mockList.push(...module);
  mockList.push(...user);
  // 这个是用来注册mock的，当在生产中使用mock时，很重要
  createProdMockServer(mockList);
}
