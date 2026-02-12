<template>
    <div class="login-container">
        <div class="login-box">
            <div style="background-color: white; width: 350px; padding: 50px 30px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);">
                <el-form ref="formRef" :rules="data.rules" :model="data.form">
                    <div style="text-align: center; margin-bottom: 30px; font-size: 24px; font-weight: bold; color: blue;">歡迎註冊</div>
                    <el-form-item prop="username">
                        <el-input v-model="data.form.username" autocomplete="off" placeholder="請輸入帳號" prefix-icon="User" />
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input show-password type="password" v-model="data.form.password" autocomplete="off" placeholder="請輸入密碼" prefix-icon="Lock" />
                    </el-form-item>
                    <el-form-item prop="confirmPassword">
                        <el-input show-password type="password" v-model="data.form.confirmPassword" autocomplete="off" placeholder="請確認密碼" prefix-icon="Lock" />
                    </el-form-item>
                </el-form>
                <div style="text-align: center; margin-top: 30px;">
                    <el-button size="large" style="width: 100%;" type="primary" @click="register">註冊</el-button>
                </div>
                <div style="text-align: right; margin-top: 20px;">
                    <!-- text-decoration: none : 不顯示下划線 -->
                    已有帳號? 請<a href="/login" style="color: blue; text-decoration: none;">登錄</a>
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

const validatePass2 = (rule, value, callback) => {
    if (!value) {
        callback(new Error('請確認密碼'))
    } else if (value !== data.form.password) {
        callback(new Error("兩次輸入的密碼不一致"))
    } else {
        callback()
    }
}
const data = reactive({
    form: {
        username: null,
        password: null,
        confirmPassword: null
    },
    rules: {
        username: [
            { required: true, message: "請輸入帳號", trigger: "blur" }
        ],
        password: [
            { required: true, message: "請輸入密碼", trigger: "blur" }
        ],
        confirmPassword: [
            { validator: validatePass2, trigger: "blur" }
        ]
    }
});

const formRef = ref();

const register = () => {
    formRef.value.validate((valid) => {
        if(valid){
            request.post("/register", data.form).then(res => {
                if(res.code === "200"){
                    ElMessage.success("註冊成功");
                    // 等0.5秒再跳轉
                    setTimeout(() => {
                        location.href = "/login";
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
