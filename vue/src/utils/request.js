import axios from "axios";
import {ElMessage} from "element-plus";

// 建立 Axios 實例
const request = axios.create({
    baseURL: 'http://localhost:9090', // 後端接口地址
    timeout: 30000                   // 超時時間 30 秒
})

// Request 攔截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    return config
}, error => {
    return Promise.reject(error)
});

// Response 攔截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果回傳的是字串，嘗試解析成 JSON
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        if (error.response.status === 404) {
            ElMessage.error('未找到請求接口')
        } else if (error.response.status === 500) {
            ElMessage.error('系統異常，請查看後端控制台報錯')
        } else {
            console.error(error.message)
        }
        return Promise.reject(error)
    }
)

export default request