# Vue + Spring Boot 後台管理系統

一個前後端分離的管理系統範例專案：
- 後端使用 **Spring Boot + MyBatis + MySQL**
- 前端使用 **Vue 3 + Vite + Element Plus**

支援管理員/員工登入、員工/部門/文章/管理員 CRUD、圖表統計、檔案上傳下載、Excel 匯入匯出等功能。

---

## 專案特色

- 前後端分離架構（`springboot` + `vue`）
- 統一回傳格式 `Result { code, msg, data }`
- 多模組管理功能：員工、部門、文章、管理員
- 統計視覺化（柱狀圖/折線圖/餅圖，ECharts）
- 員工資料 Excel 匯出/匯入
- 檔案上傳、下載與 wangEditor 圖片上傳

---

## 技術棧

### 後端（`springboot`）
- Java 25（`pom.xml` 設定）
- Spring Boot 3.5.10
- MyBatis + PageHelper
- MySQL
- Hutool、Apache POI

### 前端（`vue`）
- Vue 3
- Vite
- Vue Router
- Element Plus
- Axios
- ECharts
- wangEditor

---

## 專案結構

```text
vueboot/
├─ springboot/                 # 後端服務（預設 9090）
│  ├─ pom.xml
│  └─ src/main/
│     ├─ java/com/example/springboot/
│     │  ├─ controller/        # API 控制器
│     │  ├─ service/           # 業務邏輯
│     │  ├─ mapper/            # MyBatis Mapper 介面
│     │  ├─ entity/            # 實體類
│     │  ├─ common/            # Result、CorsConfig
│     │  └─ exception/         # 全域例外處理
│     └─ resources/
│        ├─ application.yaml
│        └─ mapper/*.xml
├─ vue/                        # 前端（預設 5173）
│  ├─ package.json
│  └─ src/
│     ├─ views/                # 頁面（登入、管理、統計等）
│     ├─ router/               # 路由設定
│     └─ utils/request.js      # Axios 封裝
└─ files/                      # 後端檔案上傳目錄
```

---

## 主要功能

- 帳號與權限
  - 登入：`POST /login`（角色：`ADMIN`、`EMP`）
  - 註冊：`POST /register`（員工註冊）
  - 修改密碼：`PUT /updatePassword`

- 基礎資料管理
  - 管理員：`/admin/*`
  - 員工：`/employee/*`
  - 部門：`/department/*`
  - 文章：`/article/*`

- 擴充功能
  - 圖表資料：`/barData`、`/lineData`、`/pieData`
  - 檔案：`/files/upload`、`/files/download/{fileName}`、`/files/wang/upload`
  - 員工 Excel：`GET /employee/export`、`POST /employee/import`

---

## 快速開始

## 1) 環境需求

- JDK 25（或自行調整 `springboot/pom.xml` 的 `java.version`）
- Maven 3.9+
- Node.js 20+
- MySQL 8+

## 2) 建立資料庫

後端預設連線在 `springboot/src/main/resources/application.yaml`：

- Host: `localhost`
- Port: `3306`
- DB: `xm-pro`
- User: `root`
- Password: `123456`

請先建立資料庫 `xm-pro`，並匯入你的資料表結構與測試資料（本專案目前未附 SQL 檔）。

## 3) 啟動後端

```bash
cd springboot
mvn spring-boot:run
```

預設埠號：`http://localhost:9090`

## 4) 啟動前端

```bash
cd vue
npm install
npm run dev
```

預設開發網址：`http://localhost:5173`

---

## 前後端串接說明

前端 Axios 設定於 `vue/src/utils/request.js`，預設：

- `baseURL = http://localhost:9090`
- 已封裝 request/response 攔截器
- 後端若回傳 `404/500`，前端會用 Element Plus 訊息提示

---

## 打包部署

### 後端

```bash
cd springboot
mvn clean package
java -jar target/springboot-0.0.1-SNAPSHOT.jar
```

### 前端

```bash
cd vue
npm run build
```

輸出目錄在 `vue/dist`，可部署到 Nginx 或其他靜態伺服器。

---

## 常見問題

- **登入失敗 / API 報錯**
  - 先確認資料庫連線、表結構與初始帳號是否正確。

- **前端呼叫不到後端**
  - 確認後端是否已啟動於 `9090`，並檢查 `request.js` 的 `baseURL`。

- **跨域問題**
  - 專案已在後端 `CorsConfig` 放寬 CORS 設定；若部署到正式環境，建議改為白名單網域。

- **Java 版本不符**
  - 若本機無 JDK 25，可將 `pom.xml` 的 `java.version` 調整為你實際使用版本（例如 17 或 21），再重新編譯。
