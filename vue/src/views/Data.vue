<template>
    <div class="card" style="margin-bottom: 5px;">
        <el-input placeholder="请输入内容" v-model="data.input" :prefix-icon="Search" style="width: 240px;"></el-input>
        <el-button type="primary" style="margin-left: 10px;">查詢</el-button>
        <el-button type="success" style="margin-left: 10px;">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px;">
        <el-button type="primary" style="margin-left: 10px;">新增</el-button>
        <el-button type="warning" style="margin-left: 10px;">批量刪除</el-button>
        <el-button type="info" style="margin-left: 10px;">導入</el-button>
        <el-button type="success" style="margin-left: 10px;">導出</el-button>
    </div>
    <div class="card">
        <el-table :data="data.tableData" style="width: 100%" stripe>
            <el-table-column prop="date" label="日期"></el-table-column>
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column prop="address" label="地址"></el-table-column>
        </el-table>
        <!-- 分頁組件開始 -->
        <div style="margin-top: 20px; text-align: right;">
            <el-pagination
            v-model:current-page="data.pageNum"
            v-model:page-size="data.pageSize"
            :page-sizes="[5, 10, 15, 20]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.pageTotal"
            />
        </div>
        <!-- 分頁組件結束 -->
    </div>
    
</template>

<style scoped></style>

<script setup>
import { reactive } from "vue";
import { Search } from "@element-plus/icons-vue";

const data = reactive({
    input: "",
    tableData: [
        { id:1, date: '2024-06-01', name: '張三', address: '北京市' },
        { id:2, date: '2024-06-02', name: '李四', address: '上海市' },
        { id:3, date: '2024-06-03', name: '王五', address: '廣州市' },
    ],
    pageNum: 1,
    pageSize: 10,
    pageTotal: 40,
});
</script>