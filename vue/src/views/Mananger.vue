<template>
    <!-- header start -->
    <div style="height: 60px; background-color: #337ecc; display: flex;">
        <div style="color: #fff; align-items: center; display: flex; padding-left: 20px;">
            <img src="@/assets/polar-bear.png" style="width: 36px; margin-right: 10px;">
            <span style="font-size: 20px;">後臺管理系統</span>
        </div>
        <div style="flex:1"></div>
        <div style="width: fit-content; display: flex; align-items: center; padding-right: 20px">
            <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="avatar" style="width: 32px; height: 32px;">
            <span style="color: #fff; margin-left: 10px;">{{data.user.name}}</span>
        </div>
    </div>
    <!-- header end -->

    <!-- middle start -->
    <div style="display: flex;">
        <!-- sidebar start -->
        <div style="width: 200px; min-height: calc(100vh - 60px); border-right: 1px solid #ccc;">
            <el-menu router :default-active="router.currentRoute.value.path" :default-openeds="['1']">
                <el-menu-item index="/manager/home"> <el-icon><House /></el-icon>系統首頁</el-menu-item>
                <el-menu-item index="/manager/data"><el-icon><DataAnalysis /></el-icon>數據統計</el-menu-item>
                <el-sub-menu index="1">
                    <template #title>
                        <el-icon><User /></el-icon>
                        <span>用戶管理</span>
                    </template>
                    <el-menu-item>管理員信息</el-menu-item>
                    <el-menu-item index="/manager/employee">員工信息</el-menu-item>
                </el-sub-menu>
                <el-menu-item index="/manager/person"><el-icon><UserFilled /></el-icon>個人信息</el-menu-item>
                <el-menu-item index="/login"><el-icon><SwitchButton /></el-icon>退出登錄</el-menu-item>
            </el-menu>
        </div>
        <!-- sidebar end -->
    
        <!-- content start -->
        <div style="flex: 1; padding: 20px; background-color: #f0f7fd;">
            <RouterView />
        </div>
        <!-- content end -->
    </div>
    <!-- middle end -->
    
</template>

<style scoped>
.el-menu-item.is-active {
    background-color: #d5e7fa !important;
    color: #1263b9 !important;
}
</style>

<script setup>
import { reactive } from "vue";
import { useRouter } from 'vue-router';

const router = useRouter();

const data = reactive({
    user: JSON.parse(localStorage.getItem("xm-pro-user"), null),
});
</script>