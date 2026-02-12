<template>
    <div class="card" style="margin-bottom: 5px;">
        <el-input placeholder="请输入内容" v-model="data.name" :prefix-icon="Search" style="width: 240px;"></el-input>
        <el-button @click="load" type="primary" style="margin-left: 10px;">查詢</el-button>
        <el-button @click="reset" type="success" style="margin-left: 10px;">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px;">
        <el-button @click="handleAdd" type="primary" style="margin-left: 10px;">新增</el-button>
        <el-button @click="handleBatchDelete" type="warning" style="margin-left: 10px;">批量刪除</el-button>
    </div>
    <div class="card">
        <!-- selection-change: 當選中行發生變化時觸發 -->
        <el-table :data="data.tableData" style="width: 100%" stripe @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="username" label="帳號"></el-table-column>
            <el-table-column prop="name" label="名稱"></el-table-column>
            <el-table-column label="操作" width="120">
                <template #default="scope">
                    <el-button @click="handleUpdate(scope.row)" type="primary" :icon="Edit" circle></el-button>
                    <el-button @click="handleDelete(scope.row.id)" type="danger" :icon="Delete" circle></el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分頁組件開始 -->
        <div style="margin-top: 20px; text-align: right;">
            <el-pagination
            @size-change="load" 
            @current-change="load" 
            v-model:current-page="data.pageNum"
            v-model:page-size="data.pageSize"
            :page-sizes="[5, 10, 15, 20]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.pageTotal"
            />
        </div>
        <!-- 分頁組件結束 -->
    </div>
    <!-- destroy-on-close: 當對話框關閉時，銷毀對話框內的內容 -->
    <el-dialog v-model="data.formVisible" title="管理員信息" width="500" destroy-on-close>
        <el-form ref="formRef" :rules="data.rules" :model="data.form" style="padding-top: 20px; padding-right: 50px;">
            <el-form-item label="帳號" prop="username" label-width="80px">
                <el-input v-model="data.form.username" autocomplete="off" placeholder="請輸入帳號" />
            </el-form-item>
            <el-form-item label="名稱" prop="name" label-width="80px">
                <el-input v-model="data.form.name" autocomplete="off" placeholder="請輸入名稱" />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="data.formVisible = false">取消</el-button>
                <el-button type="primary" @click="save">保存</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<style scoped></style>

<script setup>
import { reactive, ref } from "vue";
import { Search, Edit, Delete } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import router from '@/router/index.js'

const data = reactive({
    name: null,
    tableData: [],
    pageNum: 1,
    pageSize: 10,
    pageTotal: 0,

    formVisible: false,
    form: {},
    rules: {
        username: [
            { required: true, message: "請輸入帳號", trigger: "blur" } // trigger: "blur"表示在輸入框失去焦點時觸發校驗
        ],
        name: [
            { required: true, message: "請輸入名稱", trigger: "blur" }
        ],
    },

    ids: [], // 存放選中行的id
});

const formRef = ref();

const load = () => {
    request.get("/admin/selectPage", {
        params:{
            name: data.name,
            pageNum: data.pageNum,
            pageSize: data.pageSize,
        }
    }).then(res => {
        data.tableData = res.data.list;
        data.pageTotal = res.data.total;
    })
}
const reset = () => {
    data.name = null;
    load();
}

const handleAdd = () => {
    data.formVisible = true;
    data.form = {};
}

const save = () => {
    // validate方法會觸發el-form-item裡的校驗規則，並且會返回一個布爾值，表示校驗是否通過
    formRef.value.validate((valid) => {
        if(valid){
            data.formVisible = false;
            // 判斷是新增還是修改，根據id是否有值來判斷
            data.form.id ? update() : add();
        }else{
            ElMessage.error("請檢查輸入是否正確");
        }
    })
}

const add = () => {
    request.post("/admin/add", data.form).then(res => {
        if(res.code === "200"){
            ElMessage.success("保存成功");
            load();
        }else{
            ElMessage.error(res.message);
        }
    })
}

const handleBatchDelete = () => {
    if(data.ids.length === 0){
        ElMessage.warning("請至少選擇一行");
        return;
    }
    ElMessageBox.confirm("確定要刪除選中的管理員嗎？", "提示", {
        confirmButtonText: "確定",
        cancelButtonText: "取消",
        type: "warning",
    }).then(() => {
        deleteBatch(data.ids);
    }).catch(() => {
        ElMessage.info("已取消刪除");
    });
}
// 需要用{data: ids}的形式傳參，因為delete請求的參數默認是放在url上的，而我們需要把id放在請求體裡
const deleteBatch = (ids) => {
    request.delete("/admin/deleteBatch", { data: ids }).then(res => {
        if(res.code === "200"){
            ElMessage.success("刪除成功");
            load();
        }else{
            ElMessage.error(res.message);
        }
    })
}
const handleDelete = (id) => {
    ElMessageBox.confirm("確定要刪除嗎？", "提示", {
        confirmButtonText: "確定",
        cancelButtonText: "取消",
        type: "warning",
    }).then(() => {
        deleteById(id);
    }).catch(() => {
        ElMessage.info("已取消刪除");
    });
}
const deleteById = (id) => {
    request.delete("/admin/deleteById/" + id).then(res => {
        if(res.code === "200"){
            ElMessage.success("刪除成功");
            load();
        }else{
            ElMessage.error(res.message);
        }
    })
}
const handleUpdate = (row) => {
    // 深拷貝，避免直接去修改tableData裡的數據，導致表格的數據直接被修改(即便沒按保存也會被改)
    data.form = JSON.parse(JSON.stringify(row));
    data.formVisible = true;
}

const update = () => {
    request.put("/admin/update", data.form).then(res => {
        if(res.code === "200"){
            ElMessage.success("保存成功");
            load();
        }else{
            ElMessage.error(res.message);
        }
    })
}

const handleSelectionChange = (rows) => { //rows是選中行的數組
    // map方法會返回一個新的數組，裡面的元素是對原數組元素調用函數處理後的結果
    data.ids = rows.map(row => row.id);
}


load();
</script>