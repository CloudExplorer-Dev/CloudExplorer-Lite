function format(decimal?: number): string | undefined {
  return decimal?.toLocaleString("zh-CN", {
    style: "percent",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
}

const util = {
  format,
};

export default util;
