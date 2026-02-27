<template>
    <div class="card" style="margin-bottom: 5px;">
        <el-input placeholder="请输入内容" v-model="data.title" :prefix-icon="Search" style="width: 240px;"></el-input>
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
            <el-table-column type="selection" />
            <el-table-column prop="title" label="標題"></el-table-column>
            <el-table-column label="封面">
                <template #default="scope">
                    <el-image
                    style="width: 100px; height: 60px"
                    :src="scope.row.img"
                    :preview-src-list=[scope.row.img]
                    preview-teleported 
                    alt="img"
                    />
                </template>
            </el-table-column>
            <el-table-column prop="description" label="敘述" show-overflow-tooltip></el-table-column>
            <el-table-column prop="content" label="內容">
                <template #default="scope">
                    <el-button @click="view(scope.row.content)" type="primary" size="small">查看內容</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="time" label="時間"></el-table-column>
            <el-table-column label="操作">
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
    <el-dialog v-model="data.formVisible" title="文章信息" width="50%" destroy-on-close>
        <el-form :rules="data.rules" :model="data.form" style="padding-top: 20px; padding-right: 50px;">
            <el-form-item label="標題" prop="title" label-width="80px">
                <el-input :disabled="data.form.id" v-model="data.form.title" autocomplete="off" placeholder="請輸入標題" />
            </el-form-item>
            <el-form-item label="封面" label-width="80px">
                <el-upload
                    action="http://localhost:9090/files/upload"
                    list-type="picture"
                    :on-success="handleImgSuccess">
                    <el-button type="primary">上傳封面</el-button>
                </el-upload>
            </el-form-item>
            <el-form-item label="敘述" prop="description" label-width="80px">
                <el-input type="textarea" v-model="data.form.description" autocomplete="off" placeholder="請輸入敘述" />
            </el-form-item>
            <el-form-item label="內容" label-width="80px">
                <div style="border: 1px solid #ccc">
                    <Toolbar
                        style="border-bottom: 1px solid #ccc"
                        :editor="editorRef"
                        :mode="mode"
                    />
                    <Editor
                        style="height: 500px; overflow-y: hidden;"
                        v-model="data.form.content"
                        :defaultConfig="editorConfig"
                        :mode="mode"
                        @onCreated="handleCreated"
                    />
                </div>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="data.formVisible = false">取消</el-button>
                <el-button type="primary" @click="save">保存</el-button>
            </div>
        </template>
    </el-dialog>
    <el-dialog v-model="data.viewVisible" title="文章內容" width="50%" destroy-on-close>
        <div style="padding: 20px;" v-html="data.content"></div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="data.viewVisible = false">取消</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<style scoped></style>

<script setup>
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { reactive, ref, onBeforeUnmount, shallowRef, onMounted } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { Search, Edit, Delete } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";

const data = reactive({
    title: null,
    tableData: [],
    pageNum: 1,
    pageSize: 10,
    pageTotal: 0,

    formVisible: false,
    form: {},

    ids: [], // 存放選中行的id

    viewVisible: false,
    content: null,
});

// 编辑器实例，必须用 shallowRef
const baseUrl = "http://localhost:9090/";
const editorRef = shallowRef()
const mode = 'default'
const editorConfig = { MENU_CONF: {} }

// 配置圖片菜單
editorConfig.MENU_CONF['uploadImage'] = {
    server: baseUrl + 'files/wang/upload',
    fieldName: 'file',
}
// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
    const editor = editorRef.value
    if (editor == null) return
    editor.destroy()
})
const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}


const load = () => {
    request.get("/article/selectPage", {
        params:{
            title: data.title,
            pageNum: data.pageNum,
            pageSize: data.pageSize,
        }
    }).then(res => {
        data.tableData = res.data.list;
        data.pageTotal = res.data.total;
    })
}
const reset = () => {
    data.title = null;
    load();
}

const handleAdd = () => {
    data.formVisible = true;
    data.form = {};
}

const save = () => {
    data.formVisible = false;
    // 判斷是新增還是修改，根據id是否有值來判斷
    data.form.id ? update() : add();
        
}

const handleBatchDelete = () => {
    if(data.ids.length === 0){
        ElMessage.warning("請至少選擇一行");
        return;
    }
    ElMessageBox.confirm("確定要刪除選中的文章嗎？", "提示", {
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
    request.delete("/article/deleteBatch", { data: ids }).then(res => {
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
    request.delete("/article/deleteById/" + id).then(res => {
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
    request.put("/article/update", data.form).then(res => {
        if(res.code === "200"){
            ElMessage.success("保存成功");
            load();
        }else{
            ElMessage.error(res.message);
        }
    })
}

const add = () => {
    request.post("/article/add", data.form).then(res => {
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

const handleImgSuccess = (res) => {
    data.form.img = res.data;
};

const view = (content) => {
    data.content = content;
    data.viewVisible = true;
}

load();
</script>


