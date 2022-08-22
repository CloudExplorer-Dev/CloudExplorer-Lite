import Mock from "mockjs";
const Random = Mock.Random;
const getMockData = (num: number) => {
  const data = [];
  for (let i = 0; i < 1000; i++) {
    const org: any = {
      id: Random.id(), // 随机id
      name: Random.cname(), //随机生成中文名字
      description: Random.cname(),
      createTime: Random.datetime(), //随机生成日期时间
      pid:
        Random.natural(0, 5) < 2 || data.length === 0
          ? null
          : data[Random.natural(0, data.length - 1)].id,
      level: 0,
    };
    data.push(org);
  }
  return data;
};
export default getMockData(1000);
