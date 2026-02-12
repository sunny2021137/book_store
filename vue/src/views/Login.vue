<template>
    <div class="login-container">
        <div class="login-box">
            <div style="background-color: white; width: 350px; padding: 50px 30px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);">
                <el-form ref="formRef" :rules="data.rules" :model="data.form">
                    <div style="text-align: center; margin-bottom: 30px; font-size: 24px; font-weight: bold; color: blue;">歡迎登錄後台管理系統</div>
                    <el-form-item prop="username">
                        <el-input v-model="data.form.username" autocomplete="off" placeholder="請輸入帳號" prefix-icon="User" />
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input show-password type="password" v-model="data.form.password" autocomplete="off" placeholder="請輸入密碼" prefix-icon="Lock" />
                    </el-form-item>
                    <el-form-item prop="role">
                        <el-select v-model="data.form.role" placeholder="選擇角色" style="width: 100%;" size="large">
                            <el-option label="管理員" value="ADMIN"></el-option>
                            <el-option label="員工" value="EMP"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div style="text-align: center; margin-top: 30px;">
                    <el-button size="large" style="width: 100%;" type="primary" @click="login">登錄</el-button>
                </div>
                <div style="text-align: right; margin-top: 20px;">
                    <!-- text-decoration: none : 不顯示下划線 -->
                    還沒有帳號? 請<a href="/register" style="color: blue; text-decoration: none;">註冊</a>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import request from "@/utils/request";
import router from '@/router/index.js'


const data = reactive({
    form: {
        username: null,
        password: null,
        role: "ADMIN", // 默認選中管理員 
    },
    rules: {
        username: [
            { required: true, message: "請輸入帳號", trigger: "blur" }
        ],
        password: [
            { required: true, message: "請輸入密碼", trigger: "blur" }
        ]
    }
});

const formRef = ref();

const login = () => {
    formRef.value.validate((valid) => {
        if(valid){
            request.post("/login", data.form).then(res => {
                if(res.code === "200"){
                    // 將用戶信息存儲到本地存儲中，方便後續使用(json.stringify將對象轉換為字符串，json.parse將字符串轉換為對象)
                    localStorage.setItem("xm-pro-user", JSON.stringify(res.data));

                    ElMessage.success("登錄成功");
                    // 等0.5秒再跳轉
                    setTimeout(() => {
                        location.href = "/manager/home";
                    }, 500);
                    
                }else{
                    ElMessage.error(res.msg);
                }
            })
        }else{
            ElMessage.error("請檢查輸入是否正確");
        }
    })
}
</script>

<style scoped>
.login-container {
    height: 100vh;
    overflow: hidden;
    background-image: url('@/assets/background.jpg');
    background-size: cover;
    background-position: center;
}
.login-box {
    position: absolute;
    width: 50%;
    height: 100%;
    right: 0;
    display: flex;
    align-items: center;
}
</style>
