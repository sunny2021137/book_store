<template>
  <h1>第一次寫vue!</h1>
  <div>
    <el-input v-model="data.input_name" type="text" :prefix-icon="Search"/>
    <span>輸入={{data.input_name}}</span>
  </div>
  <div>
    <span v-if="data.input_name === '佩奇'">他是佩奇</span>
    <span v-else-if="data.input_name === '喬治'">他是喬治</span>
    <span v-else>他是其他豬</span>
  </div>
  <select name="number" id="number" v-model="data.select_number">
    <option v-for="value in data.numbers" :key="value" :value="value">{{ value }}</option>
  </select> {{ data.select_number }}


  <div>
    {{ b }} {{a}}
    <button v-on:click="click_add_one">認同+1</button>
    <button @click="a = 1">重置</button>

    <div @click="alert_number(number)" v-for="number in data.numbers">{{number}}</div>
  </div>

  <div>
    <div v-bind:style="data.red_box"></div>
    <div :style="data.red_box"></div>
    <img :src="data.my_img" alt="">
  </div>

  <el-button type="primary" :style="{color: 'red'}">Primary</el-button>
  <el-icon :size="20" style="top:4px">
    <Edit />
  </el-icon>
  1000

  <el-button type="danger" :icon="Delete" circle />

  <el-button type="primary" @click="router.back()">返回</el-button>
</template>

<style scoped></style>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { Delete, Search } from '@element-plus/icons-vue'
import router from '@/router/index.js'

const a = ref(1);
const b = ref("認同數量：");

const data = reactive({
  input_name: "佩奇",
  version: "3.2.47",
  select_number: null,
  numbers: [23, 3, 5],
  red_box: {
    width: "100px",
    height: "100px",
    backgroundColor: "red",
    margin: "10px"
  },
  my_img: "https://www.runoob.com/wp-content/uploads/2017/01/vue.png",
  id: router.currentRoute.value.query.id,
  name: router.currentRoute.value.query.name
});

console.log("傳參id=" + data.id);
console.log("傳參name=" + data.name);

onMounted(() => {
  console.log("Vue已經掛載了");
});



const click_add_one = () => {
  a.value += 1;
};

const alert_number = (number) => {
  alert("你點擊了" + number);
}
</script>