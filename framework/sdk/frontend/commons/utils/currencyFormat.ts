function format(
  currency?: number,
  currencyType?:
    | "USD"
    | "JPY"
    | "CNY"
    | "EUR"
    | "GBP"
    | "KRW"
    | "HKD"
    | "AUD"
    | "CAD"
    | "RUB"
): string | undefined {
  return currency?.toLocaleString("zh-CN", {
    style: "currency",
    currency: currencyType ? currencyType : "CNY",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
}

const util = {
  format,
};

export default util;
