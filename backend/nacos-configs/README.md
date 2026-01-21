# Nacos 配置中心使用指南

## 📚 配置文件说明

本目录包含所有需要上传到 Nacos 的配置文件模板。

### 配置文件清单

| 配置文件                   | Data ID                  | 用途                              | 是否必需 |
| -------------------------- | ------------------------ | --------------------------------- | -------- |
| `common.yaml`              | common.yaml              | 公共配置（MySQL、Redis、Kafka等） | ✅ 必需   |
| `account-service.yaml`     | account-service.yaml     | 账户服务配置                      | ✅ 必需   |
| `station-service.yaml`     | station-service.yaml     | 站点服务配置                      | ✅ 必需   |
| `gateway-service.yaml`     | gateway-service.yaml     | 网关服务配置                      | ✅ 必需   |
| `iot-gateway-service.yaml` | iot-gateway-service.yaml | IoT网关服务配置                   | ⚠️ 可选   |

## 🚀 快速开始

### 1️⃣ 启动 Nacos 服务器

```bash
# 使用 Docker Compose 启动
cd backend
docker-compose up -d nacos

# 等待 Nacos 启动完成（约30秒）
docker logs -f nova-x-nacos
```

### 2️⃣ 登录 Nacos 控制台

- 访问地址：http://localhost:8848/nacos
- 默认账号：`nacos`
- 默认密码：`nacos`

### 3️⃣ 创建命名空间

1. 点击左侧菜单 **「命名空间」**
2. 点击右上角 **「新建命名空间」**
3. 填写：
   - 命名空间ID：`dev`
   - 命名空间名：`开发环境`
4. 点击确定

### 4️⃣ 上传配置文件

在 **「配置管理」** > **「配置列表」** 中：

#### 上传公共配置
1. 点击 **「+」** 创建配置
2. 填写：
   - **Data ID**：`common.yaml`
   - **Group**：`DEFAULT_GROUP`
   - **配置格式**：`YAML`
   - **配置内容**：复制 `common.yaml` 的内容
3. 点击发布

#### 上传各服务配置
重复上述步骤，依次上传：
- `account-service.yaml`
- `station-service.yaml`
- `gateway-service.yaml`
- `iot-gateway-service.yaml`
- 其他服务配置...

### 5️⃣ 修改配置值

在 Nacos 中编辑 `common.yaml`，修改中间件地址：

```yaml
# 修改 MySQL 地址
datasource:
  host: 你的MySQL服务器IP  # 例如：192.168.1.100
  port: 3306
  username: your_username
  password: your_password

# 修改 Redis 地址
redis:
  host: 你的Redis服务器IP  # 例如：192.168.1.101
  port: 6379
  password: your_password  # 如果有密码

# 修改 Kafka 地址
kafka:
  bootstrap-servers: 你的Kafka服务器IP:9092  # 例如：192.168.1.102:9092
```

点击 **「发布」** 后，所有服务会自动刷新配置（无需重启）。

## 📝 配置更新

### 动态刷新
Nacos 支持配置热更新，修改配置后：
- ✅ **自动生效**：所有使用 `@RefreshScope` 注解的配置
- ⚠️ **需重启**：数据源、连接池等基础配置

### 配置回滚
Nacos 保留配置历史，可以随时回滚到之前的版本。

## 🔐 安全建议

1. **生产环境**请修改 Nacos 默认密码
2. 将敏感信息（如数据库密码）使用 Nacos 的**配置加密**功能
3. 为不同环境创建不同的命名空间（dev、test、prod）
4. 使用 **ACL** 限制配置的访问权限

## 🛠️ 环境隔离

```
Namespace: dev          → 开发环境
Namespace: test         → 测试环境
Namespace: prod         → 生产环境
Namespace: pre-prod     → 预发布环境
```

每个环境使用相同的 Data ID 和 Group，但配置内容不同。

## 📖 更多信息

- [Nacos 官方文档](https://nacos.io/zh-cn/docs/quick-start.html)
- [Spring Cloud Alibaba 文档](https://spring-cloud-alibaba-group.github.io/github-pages/2023/zh-cn/index.html)
