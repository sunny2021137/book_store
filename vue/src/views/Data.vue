<template>
    <div style="display: flex;">
        <div id="bar_card" class="card" style="flex:1; height: 400px;"></div>
        <div id="line_card"class="card" style="flex:1; height: 400px;"></div> 
    </div>
    <div style="display: flex;">
        <div id="pie_card"class="card" style="flex:1; height: 400px;"></div>
    </div>
    
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

const lineOption = {
    title: {
        text: '近7日文章數量統計'
    },
    legend: {
        trigger: 'item' // 圖例
    },
    tooltip: {},
    xAxis: {
        data: [] // x軸data和series的data對應
    },
    yAxis: {},
    series: [
        {
            name: '文章數量',
            type: 'line',
            smooth: true, // 平滑曲线
            data: [],
            
        },
    ]
}


const pieOption = {
    title: {
        text: '部門員工人數餅圖',
        left: 'center'
    },
    tooltip: {
        trigger: 'item'
    },
    legend: {
        orient: 'vertical',
        left: 'left'
    },
    series: [
        {
            name: '部門員工人數',
            type: 'pie',
            radius: '50%',
            label: {
                formatter: '{b}: {@2012} ({d}%)'
            },
            data: [],
            emphasis: {
                itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};
onMounted(() => {
    // 基于准备好的dom，初始化echarts实例
	var barChart = echarts.init(document.getElementById('bar_card'));
	// 請求後端獲取數據
    request.get('/barData').then(res => {
        barOption.xAxis.data = res.data.department;
        barOption.series[0].data = res.data.count;
        barChart.setOption(barOption);
    })

    // 基于准备好的dom，初始化echarts实例
	var lineChart = echarts.init(document.getElementById('line_card'));
	// 請求後端獲取數據
    request.get('/lineData').then(res => {
        lineOption.xAxis.data = res.data.date;
        lineOption.series[0].data = res.data.count;
        lineChart.setOption(lineOption);
    })

    // 基于准备好的dom，初始化echarts实例
	var pieChart = echarts.init(document.getElementById('pie_card'));
	// 請求後端獲取數據
    request.get('/pieData').then(res => {
        pieOption.series[0].data = res.data;
        pieChart.setOption(pieOption);
    })
})


const data = reactive({
});

</script>