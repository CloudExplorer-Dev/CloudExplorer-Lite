<script setup lang='ts'>
import { defineProps, onMounted, watch, ref } from 'vue';
import * as echarts from 'echarts';

const props = defineProps({
  smooth: {
    type: Boolean,
    default: () => false
  },
  chartsType: {
    type: String,
    default: () => "line"
  },
  color: {
    type: String,
    default: () => "#3E80BD"
  },
  data: {
    type: Object,
    default: () => {}
  },
  xConfig: {
    type: Object,
    default: () => {}
  },
  chartsTitle: {
    type: String,
    default: () => ""
  },
  chartsSeriesName: {
    type: String,
    default: () => ""
  },
  yDataUnit: {
    type: String,
    default: () => ""
  }
})

const guid = () => {
  return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}

const getUuid = (): string => {
  const uuid = (guid()+guid()+"-"+guid()+"-"+guid()+"-"+guid()+"-"+guid()+guid()+guid());
  return uuid;
}

const uuid = getUuid()

// chart实例
let myChart: any

const initEcharts = (): void => {
  /**
   * 多个图表会被后面的覆盖，这里使用uui区分
   */
  const chartDom = document.getElementById(`echarts-${uuid}`)!;
  myChart = echarts.init(chartDom);
}


onMounted(() => {
  initEcharts();
  setEchartsData();
})

const setEchartsData = () => {
  myChart.setOption({
    xAxis: {
      type: 'category',
      data: props.data.xData,
      axisLabel:{
        formatter:function(timestamp:any) {
          return timestampToTime(timestamp);
        },
        rotate:0,
        interval: 'auto'
      },
      ...props.xConfig
    },
    yAxis: {
      type: 'value',
      axisLabel:{
        formatter: function (val:any) {
          return yUnitConversion(val);
        }
      }
    },
    legend: {
      data: [props.chartsSeriesName],
      y:'bottom',
    },
    title: {
      text: props.chartsTitle
    },
    tooltip: {
      show: true,
      trigger: 'axis',
      formatter: function(params:any){
        const unit = ref<string>();
        if(props.yDataUnit === "Byte/s" || props.yDataUnit === "bit/s"){
          unit.value = changeByte(params[0].value);
          debugger;
        } else {
          unit.value = params[0].value + props.yDataUnit;
        }
        const tooltipText = timestampToTime(params[0].name)+"<br/>"+params[0].marker+" "+ params[0].seriesName+" "+unit.value;
        return tooltipText;
      },
      axisPointer: {
        lineStyle: {
          type:"solid"
        }
      },
    },
    series: [
      {
        name: props.chartsSeriesName,
        showSymbol: false,
        symbol: 'circle',
        data: props.data.yData,
        type: props.chartsType,
        smooth: props.smooth
      }
    ],
    color: [
      props.color
    ]
  });
}

const yUnitConversion = (val:any)=>{
  if(props.yDataUnit === "Byte/s" || props.yDataUnit === "bit/s"){
    return changeByte(val);
  } else{
    return val + " "+ props.yDataUnit;
  }

}

const changeByte = (byte: number) => {
  let size = "";
  if (byte < 1 * 1024) {
    size = `${byte.toFixed(2)}B`;
  } else if (byte < 1 * 1024 * 1024) {
    size = `${(byte / 1024).toFixed(2)}KB`;
  } else if (byte < 1 * 1024 * 1024 * 1024) {
    size = `${(byte / (1024 * 1024)).toFixed(2)}MB`;
  } else {
    size = `${(byte / (1024 * 1024 * 1024)).toFixed(2)}GB`;
  }
  // 转成字符串
  const sizeStr = `${size}`;
  // 获取小数点处的索引
  const index = sizeStr.indexOf(".");
  // 获取小数点后两位的值
  const dou = sizeStr.substr(index + 1, 2);
  if (dou == "00") {
    // 后两位是否为00，如果是则删除00
    return sizeStr.substring(0, index) + sizeStr.substr(index + 3, 2);
  }
  return size;
};

/**
 * 时间戳转时间格式
 * @param timestamp
 */
const timestampToTime = (timestamp:any) =>{
  let date = new Date(timestamp * 1);
  let Y = date.getFullYear() + '-';
  let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
  let D = (date.getDate() < 10 ? '0'+ date.getDate() : date.getDate()) + ' ';
  let h = (date.getHours() < 10 ? '0'+ date.getHours() : date.getHours()) + ':';
  let m = (date.getMinutes() < 10 ? '0'+ date.getMinutes() : date.getMinutes()) + ':';
  let s = (date.getSeconds() < 10 ? '0'+ date.getSeconds() : date.getSeconds());
  return Y+M+D+h+m+s;
}

//监听data变化
watch( props.data, () => {
  setEchartsData()
})

</script>
<template>
  <div :id="`echarts-${uuid}`" style=" width: 100%; height: 15rem;padding-top:10px"></div>
</template>
