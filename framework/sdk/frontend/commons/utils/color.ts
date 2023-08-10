import tinycolor from "@ctrl/tinycolor";
export const base12 = [
  "rgba(78, 131, 253, 1)",
  "rgba(150, 189, 255, 1)",
  "rgba(250, 211, 85, 1)",
  "rgba(255, 230, 104, 1)",
  "rgba(20, 225, 198, 1)",
  "rgba(76, 253, 224, 1)",
  "rgba(80, 206, 251, 1)",
  "rgba(118, 240, 255, 1)",
  "rgba(147, 90, 246, 1)",
  "rgba(220, 155, 255, 1)",
  "rgba(255, 165, 61, 1)",
  "rgba(255, 199, 94, 1)",
  "rgba(241, 75, 169, 1)",
  "rgba(255, 137, 227, 1)",
  "rgba(247, 105, 100, 1)",
  "rgba(255, 158, 149, 1)",
  "rgba(219, 102, 219, 1)",
  "rgba(254, 157, 254, 1)",
  "rgba(195, 221, 64, 1)",
  "rgba(217, 244, 87, 1)",
  "rgba(97, 106, 229, 1)",
  "rgba(172, 173, 255, 1)",
  "rgba(98, 210, 86, 1)",
  "rgba(135, 245, 120, 1)",
];
export const base0_12 = [
  "rgba(78, 131, 253, 1)",
  "rgba(250, 211, 85, 1)",
  "rgba(20, 225, 198, 1)",
  "rgba(80, 206, 251, 1)",
  "rgba(148, 90, 246, 1)",
  "rgba(255, 165, 61, 1)",
  "rgba(241, 75, 169, 1)",
  "rgba(247, 105, 100, 1)",
  "rgba(219, 102, 219, 1)",
  "rgba(195, 221, 64, 1)",
  "rgba(97, 106, 229, 1)",
  "rgba(98, 210, 86, 1)",
];

/**
 * 构建对应数量的颜色
 * @param num
 * @returns
 */
export function buildColors(num: number) {
  if (num <= 12) {
    return base0_12.slice(0, num);
  } else if (num <= 24) {
    return base12.slice(0, num);
  } else {
    return interpolationColor(base0_12, num);
  }
}
/**
 *
 * @param baseColors  基本颜色
 * @param num         需要生成多少个颜色 num传多少返回值就是多少
 * @param disorganize 是否打乱颜色
 */
export function interpolationColor(
  baseColors: Array<string>,
  num: number,
  disorganize?: boolean
): Array<string> {
  const result: Array<string> = [];
  const baseColorLength: number = baseColors.length;
  if (num / baseColorLength <= 1) {
    return baseColors;
  } else {
    // 平均每个值需要插值数量
    const averageSaveNum = Math.floor(num / baseColorLength);
    // 前面多少个还需要多加一个
    const appendSaveNum = num % baseColorLength;

    region(0, averageSaveNum).forEach((index) => {
      baseColors.forEach((color: string, innerIndex) => {
        if (index === 0) {
          result.push(color);
        } else {
          const tcolor = tinycolor(color)
            .mix(
              "#fff",
              (100 / (averageSaveNum + (innerIndex < appendSaveNum ? 1 : 0))) *
                index
            )
            .toHexString();
          result.push(tcolor);
        }
      });
    });
    baseColors.forEach((color: string, index) => {
      if (index < appendSaveNum) {
        const tcolor = tinycolor(color)
          .mix("#fff", (100 / (averageSaveNum + 1)) * averageSaveNum)
          .toHexString();
        result.push(tcolor);
      }
    });
  }
  if (disorganize) {
    disorganizeColors(result);
  }
  return result;
}

/**
 * 打乱颜色
 * @param colors 颜色
 */
const disorganizeColors = (colors: Array<string>) => {
  colors.sort(() => (Math.random() > 0.5 ? -1 : 1));
};

const region = (start: number, end: number) => {
  const region = [];
  for (let i = start; i < end; i++) {
    region.push(i);
  }
  return region;
};
