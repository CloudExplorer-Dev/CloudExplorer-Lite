function format(currency?: number): string | undefined {
  return currency?.toLocaleString("zh-CN", {
    style: "currency",
    currency: "CNY",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
}

const util = {
  format,
};

export default util;
