# 🚀 Nova-X 使用 Nacos 配置中心快速启动指南

## 📋 前置要求

- ✅ Docker 和 Docker Compose 已安装
- ✅ Maven 3.6+ 已安装
- ✅ JDK 21 已安装

## 🎯 完整启动流程

### 步骤 1：启动基础设施（3分钟）

```powershell
# 进入项目目录
cd C:\Users\keyan\projects\vscode\Nova-X\backend

# 启动所有中间件（MySQL、Redis、Nacos、Kafka等）
docker-compose up -d

# 查看启动状态
docker-compose ps

# 等待所有服务健康检查通过（约30秒-1分钟）
docker-compose logs -f nacos
# 看到 "Nacos started successfully" 后按 Ctrl+C 退出日志查看
```

### 步骤 2：配置 Nacos（5分钟）

#### 方式 A：手动配置（推荐第一次使用）

1. **访问 Nacos 控制台**
   ```
   浏览器打开：http://localhost:8848/nacos
   账号：nacos
   密码：nacos
   ```

2. **创建命名空间**
   - 左侧菜单点击「命名空间」
   - 点击右上角「新建命名空间」
   - 命名空间ID：`dev`
   - 命名空间名：`开发环境`
   - 点击「确定」

3. **上传配置文件**
   - 左侧菜单点击「配置管理」→「配置列表」
   - 切换到 `dev` 命名空间
   - 点击「+」创建配置

   **上传 common.yaml**（最重要）：
   ```
   Data ID: common.yaml
   Group: DEFAULT_GROUP
   配置格式: YAML
   配置内容: 复制 backend/nacos-configs/common.yaml 的全部内容
   ```
   点击「发布」

   **依次上传其他配置**：
   - `account-service.yaml`
   - `station-service.yaml`
   - `gateway-service.yaml`
   - `iot-gateway-service.yaml`

4. **修改数据库和中间件地址**（如果不使用 localhost）

   编辑 `common.yaml`，修改以下配置：
   ```yaml
   datasource:
     host: 你的MySQL地址  # 默认 localhost
     username: root
     password: root

   redis:
     host: 你的Redis地址  # 默认 localhost
     password: # 如果有密码

   kafka:
     bootstrap-servers: 你的Kafka地址:9092  # 默认 localhost:9092
   ```
   点击「发布」

#### 方式 B：自动上传脚本（快速）

```powershell
cd C:\Users\keyan\projects\vscode\Nova-X\backend\nacos-configs

# 运行上传脚本
.\upload-configs.ps1

# 如果 Nacos 不在本地，指定地址
.\upload-configs.ps1 -NacosAddr "192.168.1.100:8848" -Namespace "dev"
```

### 步骤 3：编译项目（5分钟）

```powershell
cd C:\Users\keyan\projects\vscode\Nova-X\backend

# 清理并编译所有服务
mvn clean package -DskipTests

# 如果编译成功，会看到：
# [INFO] BUILD SUCCESS
# [INFO] Total time: XX:XX min
```

### 步骤 4：启动服务（按顺序）

#### 4.1 启动网关服务（必需）

```powershell
# 方式 1：使用 java -jar
cd backend/infrastructure/gateway-service
java -jar target/gateway-service-1.0.0.jar

# 方式 2：设置自定义 Nacos 地址
java -jar target/gateway-service-1.0.0.jar --spring.cloud.nacos.server-addr=192.168.1.100:8848
```

看到日志输出：
```
Started GatewayServiceApplication in XX seconds
```
表示启动成功！

#### 4.2 启动业务服务（并行启动）

**打开新的 PowerShell 窗口**，依次启动：

```powershell
# 账户服务
cd backend/services/account-service
java -jar target/account-service-1.0.0.jar
```

```powershell
# 站点服务
cd backend/services/station-service
java -jar target/station-service-1.0.0.jar
```

```powershell
# 设备服务
cd backend/services/device-service
java -jar target/device-service-1.0.0.jar
```

```powershell
# 会话服务
cd backend/services/session-service
java -jar target/session-service-1.0.0.jar
```

其他服务同理...

### 步骤 5：验证服务（2分钟）

#### 5.1 检查服务注册

访问 Nacos 控制台：
```
http://localhost:8848/nacos
点击「服务管理」→「服务列表」
```

应该看到已注册的服务：
- ✅ gateway-service
- ✅ account-service
- ✅ station-service
- ✅ device-service
- ...

#### 5.2 测试网关

```powershell
# 测试网关健康检查
curl http://localhost:9000/actuator/health

# 应该返回：
# {"status":"UP"}
```

#### 5.3 测试 API

```powershell
# 测试账户服务（通过网关）
curl http://localhost:9000/api/v1/users/test

# 测试站点服务
curl http://localhost:9000/api/v1/stations?lat=39.9&lon=116.4
```

## 🎉 启动成功！

现在你的 Nova-X 平台已经成功运行，所有配置都从 Nacos 动态加载。

## 🔄 修改配置（零停机）

1. 访问 Nacos 控制台
2. 编辑 `common.yaml` 或具体服务的配置
3. 点击「发布」
4. 服务会自动刷新配置（无需重启！）

## 🛠️ 常见问题

### Q1: 服务启动失败，提示连接不上 Nacos？

**解决**：检查 Nacos 是否启动
```powershell
docker ps | findstr nacos
# 应该看到 nova-x-nacos 容器在运行
```

### Q2: 服务启动失败，提示找不到配置？

**解决**：确认 Nacos 中已创建配置
- 检查命名空间是否为 `dev`
- 检查 `common.yaml` 是否已上传
- 检查服务专属配置是否已上传

### Q3: 连接数据库失败？

**解决**：检查 MySQL 是否启动，并检查配置
```powershell
docker ps | findstr mysql
# 测试连接
docker exec -it nova-x-mysql mysql -uroot -ppassword
```

如果使用外部 MySQL，修改 Nacos 中的 `common.yaml`：
```yaml
datasource:
  host: 你的MySQL地址
  username: 你的用户名
  password: 你的密码
```

### Q4: 如何快速重启所有服务？

```powershell
# 停止所有 Java 进程（Windows）
Get-Process -Name java | Stop-Process -Force

# 重新启动（使用批处理脚本更方便）
```

## 📚 下一步

- 🔐 配置 JWT 认证
- 🚨 配置监控告警
- 📊 查看 Druid 监控面板：http://localhost:8081/druid
- 📈 配置 Grafana 监控大屏
- 🔧 调整 Nacos 配置实现零停机配置更新

## 💡 专业提示

### 使用环境变量覆盖配置

```powershell
# 临时修改 Nacos 地址（不影响配置文件）
$env:NACOS_ADDR="192.168.1.100:8848"
java -jar account-service-1.0.0.jar

# 使用不同的命名空间
$env:NACOS_NAMESPACE="test"
java -jar account-service-1.0.0.jar
```

### 生产环境部署

参考 [PRODUCTION.md](PRODUCTION.md) 了解生产环境配置最佳实践。

---

**需要帮助？** 查看详细文档或提交 Issue。
