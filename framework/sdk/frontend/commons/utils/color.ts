import tinycolor from "@ctrl/tinycolor";
/**
 *
 * @param baseColors  基本颜色
 * @param num         需要生成多少个颜色 num传多少返回值就是多少
 * @param disorganize 是否打乱颜色
 */
export const interpolationColor = (baseColors: Array<string>, num: number,disorganize:boolean) => {
  const result: Array<string> = [];
  const baseColorLength: number = baseColors.length;
  const baseMix = 30;
  if (num / baseColorLength <= 1) {
    return baseColors;
  } else {
    // 平均每个值需要插值数量
    const averageSaveNum = Math.floor(num / baseColorLength) - 1;
    // 前面多少个还需要多加一个
    const appendSaveNum = num % baseColorLength;

    baseColors.forEach((color: string, index) => {
      result.push(color);
      const end = index < appendSaveNum ? averageSaveNum + 1 : averageSaveNum;
      region(0, end).forEach((index) => {
        const tcolor = tinycolor(color)
          .mix(
            '#fff',
            (baseMix / end) * (index + 1)
          )
          .toHexString();
        result.push(tcolor);
      });
    });
  }
  if(disorganize){
    disorganizeColors(result);
  }
  return result;
};

/**
 * 打乱颜色
 * @param colors 颜色
 */
const disorganizeColors=(colors:Array<string>)=>{
  colors.sort(()=> Math.random() > 0.5 ? -1 : 1);
}


const region = (start: number, end: number) => {
  const region = [];
  for (let i = start; i < end; i++) {
    region.push(i);
  }
  return region;
};
