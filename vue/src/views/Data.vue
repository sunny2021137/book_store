<template>
    <div id="bar_card" class="card" style="flex:1; height: 400px;"></div>
    <div id="line_card"class="card" style="flex:1; height: 400px;"></div>
</template>

<style scoped></style>

<script setup>
import { reactive, onMounted } from "vue";
import * as echarts from 'echarts';
import request from "@/utils/request.js";

const barOption = {
    title: {
        text: '部門員工人數統計'
    },
    legend: {
        data: ['人數'] // 圖例
    },
    tooltip: {},
    xAxis: {
        data: [] // x軸data和series的data對應
    },
    yAxis: {},
    series: [
        {
            name: '人數',
            type: 'bar', // 柱状圖
            data: [],
            itemStyle: {
                normal: {
                    color: function(params) {
                        let colorList = ['#C1232B', '#B5C334', '#FCCE10', '#E87C25', '#27727B', '#FE8463', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD'];
                        return colorList[params.dataIndex % colorList.length];
                    }
                }
            }
        },
    ]
}

onMounted(() => {
    // 基于准备好的dom，初始化echarts实例
	var barChart = echarts.init(document.getElementById('bar_card'));
	// 請求後端獲取數據
    request.get('/barData').then(res => {
        barOption.xAxis.data = res.data.department;
        barOption.series[0].data = res.data.count;
        barChart.setOption(barOption);
    })
})


const data = reactive({
});

</script>