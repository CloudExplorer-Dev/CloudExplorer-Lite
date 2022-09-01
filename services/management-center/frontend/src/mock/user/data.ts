import Mock from "mockjs";
const Random = Mock.Random;
const getMockData = (num: number) => {
  const data = [];
  for (let i = 0; i < 15; i++) {
    const user: any = {
      id: Random.id(), // 随机id
      name: Random.cname(), //随机生成中文名字
      email: Random.email(),
      createTime: Random.datetime(), //随机生成日期时间
      source: "local",
      password: Random.string(),
      active: Random.boolean(),
      phone: Random.integer(),
    };
    data.push(user);
  }
  return data;
};
export default getMockData(1000);
