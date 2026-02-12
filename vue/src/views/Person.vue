<template>
    <div style="display: flex; justify-content: center;">
        <div class="card" style="width: 50%; padding: 40px 20px;">
            <el-form ref="formRef" :rules="data.rules" :model="data.form" style="padding-top: 20px; padding-right: 50px;">
                <el-form-item label="帳號" prop="username" label-width="80px">
                    <el-input disabled v-model="data.form.username" autocomplete="off" placeholder="請輸入帳號" />
                </el-form-item>
                <el-form-item label="名稱" prop="name" label-width="80px">
                    <el-input v-model="data.form.name" autocomplete="off" placeholder="請輸入名稱" />
                </el-form-item>
                <!-- 身分是員工時才顯示 -->
                <div v-if="data.user.role=== 'EMP'">
                    <el-form-item label="性別" label-width="80px">
                        <el-radio-group v-model="data.form.sex">
                            <el-radio label="男" value="男">男</el-radio>
                            <el-radio label="女" value="女">女</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="工號" prop="no" label-width="80px">
                        <el-input disabled v-model="data.form.no" autocomplete="off" placeholder="請輸入工號" />
                    </el-form-item>
                    <el-form-item label="年齡" label-width="80px">
                        <el-input-number v-model="data.form.age" :min="18" autocomplete="off" placeholder="請輸入年齡" />
                    </el-form-item>
                    <el-form-item label="介紹" label-width="80px">
                        <el-input type="textarea" :rows="3" v-model="data.form.description" autocomplete="off" placeholder="請輸入介紹" />
                    </el-form-item>
                </div>
                
            </el-form>
            <div style="text-align: center; margin-top: 40px;">
                <el-button @click="updateUser" type="primary" style="padding: 10px 30px;">更新個人信息</el-button>
            </div>
        </div>
    </div>
    
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

const formRef = ref();


const data = reactive({
    user: JSON.parse(localStorage.getItem("xm-pro-user"), null),
    form: {},
    rules: {
        username: [
            { required: true, message: "請輸入帳號", trigger: "blur" }
        ],
        name: [
            { required: true, message: "請輸入名稱", trigger: "blur" }
        ],
        no: [
            { required: true, message: "請輸入工號", trigger: "blur" }
        ]
    }
});

if(data.user.role === "EMP"){
    request.get("employee/selectById/"+data.user.id).then(res => {
        data.form = res.data;
    });
}else {
    // 管理員本來就只有基本數據，不用再查資料庫了
    data.form = data.user;
}

const emit = defineEmits(["updateUser"]);

const updateUser = () => {
    if(data.user.role === "EMP"){
        request.put("employee/update", data.form).then(res => {
            if(res.code === "200"){
                ElMessage.success("更新成功");
                // 更新緩存
                localStorage.setItem("xm-pro-user", JSON.stringify(data.form));
                // 觸發更新父組件(Manager)中的用戶信息(右上角用戶名稱)
                emit("updateUser");
            }else {
                ElMessage.error(res.msg);
            }
        });
    }else {
        request.put("admin/update", data.form).then(res => {
            if(res.code === "200"){
                ElMessage.success("更新成功");
                // 更新緩存
                localStorage.setItem("xm-pro-user", JSON.stringify(data.form));
                // 觸發更新父組件(Manager)中的用戶信息(右上角用戶名稱)
                emit("updateUser");
            }else {
                ElMessage.error(res.msg);
            }
        });
    }
}
</script>
