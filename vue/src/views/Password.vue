<template>
    <div style="display: flex; justify-content: center;">
        <div class="card" style="width: 50%; padding: 40px 20px;">
            <el-form ref="formRef" :rules="data.rules" :model="data.form">
                <el-form-item label="原密碼" required prop="password">
                    <el-input show-password type="password" v-model="data.form.password" autocomplete="off" prefix-icon="Lock" />
                </el-form-item>
                <el-form-item label="新密碼" required prop="newPassword">
                    <el-input show-password type="password" v-model="data.form.newPassword" autocomplete="off" prefix-icon="Lock" />
                </el-form-item>
                <el-form-item label="確認新密碼" required prop="confirmPassword">
                    <el-input show-password type="password" v-model="data.form.confirmPassword" autocomplete="off" prefix-icon="Lock" />
                </el-form-item>
            </el-form>
            <div style="text-align: center; margin-top: 30px;">
                <el-button size="large" style="width: 100%;" type="primary" @click="updatePassword">更新密碼</el-button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";


const formRef = ref();

const validatePass2 = (rule, value, callback) => {
    if (!value) {
        callback(new Error('請確認新密碼'))
    } else if (value !== data.form.newPassword) {
        callback(new Error("兩次輸入的新密碼不一致"))
    } else {
        callback()
    }
}

const data = reactive({
    user: JSON.parse(localStorage.getItem("xm-pro-user"), null),
    form: {},
    rules: {
        password: [
            { required: true, message: "請輸入密碼", trigger: "blur" }
        ],
        newPassword: [
            { required: true, message: "請輸入新密碼", trigger: "blur" }
        ],
        confirmPassword: [
            {validator: validatePass2, trigger: "blur"}
        ]
    } 
});

const updatePassword = () => {
    formRef.value.validate(valid => {
        if(valid){
            data.form.id = data.user.id;
            data.form.role = data.user.role;
            request.put("/updatePassword", data.form).then(res => {
                if(res.code === "200"){
                    ElMessage.success("更新成功");
                    // 刪除緩存
                    localStorage.removeItem("xm-pro-user");
                    // 跳轉到登錄頁面
                    setTimeout(() => {
                        location.href = "/login";
                    }, 500);
                }else {
                    ElMessage.error(res.msg);
                }
            });
        }
    });
}
</script>
