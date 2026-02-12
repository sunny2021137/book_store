<template>
  <!-- elinput必須有v-model -->
  <el-input clearable v-model="data.input" style="width: 240px; height: 20px;" placeholder="請輸入文字" :prefix-icon="Search"/> {{ data.input }}
  <div>
    <RouterLink to="/manager/test">Go to Test</RouterLink>
    <el-button type="primary" @click="router.push('/manager/test')">跳轉到test，可返回</el-button>
    <el-button type="primary" @click="router.replace('/manager/test')">跳轉到test，不可返回</el-button>
    <RouterView />
  </div>

  <div>
    <!-- 方法一 -->
    <el-button type="primary" @click="router.push('/manager/test?id=123&name=abc')">傳參123,abc</el-button>
    <!-- 方法二 -->
    <el-button type="primary" @click="router.push({path:'/manager/test', query:{id: 456, name: 'def'}})">傳參456,def</el-button>
  </div>
</template>


<script setup>
import { reactive } from "vue";
import { Search } from '@element-plus/icons-vue'
import { RouterLink, RouterView } from "vue-router";
import router from '@/router/index.js'
import request from "@/utils/request.js"

const data = reactive({
  input: null,
  employeeList: []

});

// request.get("/employee/selectAll").then( res => {
//     console.log(res);
//     data.employeeList = res.data;
// })
</script>