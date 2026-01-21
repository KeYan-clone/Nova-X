# ❓ Nacos 配置中心常见问题（FAQ）

## 🔧 配置相关

### Q1: 如何修改 MySQL、Redis、Kafka 的地址？

**答**：在 Nacos 控制台编辑 `common.yaml`：

1. 登录 http://localhost:8848/nacos（账号密码：nacos/nacos）
2. 点击「配置管理」→「配置列表」
3. 切换到 `dev` 命名空间
4. 找到 `common.yaml`，点击「编辑」
5. 修改以下配置：

```yaml
datasource:
  host: 你的MySQL地址  # 例如：192.168.1.100
  port: 3306
  username: root
  password: your_password

redis:
  host: 你的Redis地址  # 例如：192.168.1.101
  port: 6379
  password: your_redis_password

kafka:
  bootstrap-servers: 你的Kafka地址:9092  # 例如：192.168.1.102:9092
```

6. 点击「发布」
7. ✅ 所有服务自动刷新配置，无需重启！

---

### Q2: 配置修改后需要重启服务吗？

**答**：大部分配置支持**热更新**（无需重启）：

✅ **支持热更新**：
- 日志级别
- 限流阈值
- JWT 过期时间
- 开关类配置
- 业务参数

❌ **需要重启**：
- 数据库连接信息（数据源配置）
- 服务端口
- Nacos 服务器地址
- 连接池配置

**如何重启单个服务**：
```powershell
# 找到服务进程
Get-Process -Name java | Where-Object {$_.CommandLine -like "*account-service*"}

# 停止进程
Stop-Process -Id <进程ID>

# 重新启动
java -jar account-service-1.0.0.jar
```

---

### Q3: 如何为不同环境（开发/测试/生产）配置不同的参数？

**答**：使用 Nacos 的**命名空间**功能：

1. **创建不同的命名空间**：
   ```
   dev      → 开发环境
   test     → 测试环境
   pre-prod → 预发布环境
   prod     → 生产环境
   ```

2. **在每个命名空间中上传相同的配置文件**，但内容不同：

   **dev 命名空间的 common.yaml**：
   ```yaml
   datasource:
     host: localhost  # 开发环境使用本地数据库
   logging:
     level:
       com.novax: DEBUG  # 开发环境开启 DEBUG 日志
   ```

   **prod 命名空间的 common.yaml**：
   ```yaml
   datasource:
     host: prod-mysql.example.com  # 生产环境使用远程数据库
   logging:
     level:
       com.novax: WARN  # 生产环境使用 WARN 日志
   ```

3. **启动服务时指定命名空间**：
   ```bash
   # 开发环境
   java -jar -Dspring.cloud.nacos.config.namespace=dev account-service.jar

   # 生产环境
   java -jar -Dspring.cloud.nacos.config.namespace=prod account-service.jar
   ```

---

### Q4: 配置文件中的敏感信息（如密码）如何加密？

**答**：使用 Nacos 的**配置加密**功能：

#### 方法一：Nacos 自带加密（推荐）

1. 在 Nacos 控制台编辑配置时，使用特殊语法：
   ```yaml
   datasource:
     password: ENC(加密后的密文)
   ```

2. 在 Nacos 启动时配置加密密钥：
   ```bash
   docker run -e NACOS_ENCRYPTION_KEY=your-secret-key nacos/nacos-server
   ```

#### 方法二：Jasypt 加密

1. 添加依赖（已在 common-core 中）：
   ```xml
   <dependency>
       <groupId>com.github.ulisesbocchio</groupId>
       <artifactId>jasypt-spring-boot-starter</artifactId>
   </dependency>
   ```

2. 生成加密密文：
   ```bash
   java -cp jasypt-1.9.3.jar \
     org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI \
     input="mypassword" password="encryption-key" algorithm=PBEWithMD5AndDES
   ```

3. 在配置中使用：
   ```yaml
   datasource:
     password: ENC(加密后的密文)
   ```

4. 启动服务时提供解密密钥：
   ```bash
   java -jar -Djasypt.encryptor.password=encryption-key account-service.jar
   ```

---

## 🚨 故障排查

### Q5: 服务启动失败，提示 "Unable to connect to Nacos"？

**排查步骤**：

1. **检查 Nacos 是否启动**：
   ```powershell
   docker ps | findstr nacos
   ```
   应该看到 `nova-x-nacos` 容器在运行。

2. **检查 Nacos 健康状态**：
   ```powershell
   curl http://localhost:8848/nacos/v1/console/health/readiness
   ```
   应该返回 `"status": "UP"`。

3. **检查网络连通性**：
   ```powershell
   telnet localhost 8848
   ```

4. **查看 Nacos 日志**：
   ```powershell
   docker logs nova-x-nacos
   ```

5. **检查服务配置中的 Nacos 地址**：
   ```yaml
   spring:
     cloud:
       nacos:
         config:
           server-addr: localhost:8848  # 确认地址正确
   ```

---

### Q6: 服务启动成功，但找不到配置？

**排查步骤**：

1. **检查命名空间是否正确**：
   ```yaml
   spring:
     cloud:
       nacos:
         config:
           namespace: dev  # 确认命名空间
   ```

2. **检查配置是否已上传到 Nacos**：
   - 登录 Nacos 控制台
   - 切换到对应的命名空间（如 `dev`）
   - 查看配置列表中是否存在 `common.yaml` 和服务专属配置

3. **检查配置的 Data ID 和 Group**：
   ```yaml
   spring:
     cloud:
       nacos:
         config:
           file-extension: yaml  # 确认扩展名
           group: DEFAULT_GROUP  # 确认分组
   ```

4. **查看服务启动日志**：
   ```
   [Nacos Config] dataId=common.yaml, group=DEFAULT_GROUP
   ```
   如果看不到此日志，说明配置加载失败。

5. **启用详细日志**：
   ```yaml
   logging:
     level:
       com.alibaba.nacos: DEBUG
   ```

---

### Q7: 数据库连接失败？

**排查步骤**：

1. **检查 MySQL 是否启动**：
   ```powershell
   docker ps | findstr mysql
   ```

2. **测试数据库连接**：
   ```powershell
   docker exec -it nova-x-mysql mysql -uroot -ppassword
   ```

3. **检查 Nacos 中的数据库配置**：
   ```yaml
   datasource:
     host: localhost
     port: 3306
     username: root
     password: password
   ```

4. **检查数据库是否存在**：
   ```sql
   SHOW DATABASES LIKE 'nova_x_%';
   ```

5. **初始化数据库**（如果不存在）：
   ```powershell
   cd backend/scripts
   docker exec -i nova-x-mysql mysql -uroot -ppassword < sql/account-service-init.sql
   ```

---

### Q8: Redis 连接失败？

**排查步骤**：

1. **检查 Redis 是否启动**：
   ```powershell
   docker ps | findstr redis
   ```

2. **测试 Redis 连接**：
   ```powershell
   docker exec -it nova-x-redis redis-cli ping
   # 应该返回 PONG
   ```

3. **检查 Nacos 中的 Redis 配置**：
   ```yaml
   redis:
     host: localhost
     port: 6379
     password:  # 如果有密码
     database: 0
   ```

4. **如果 Redis 有密码**：
   ```powershell
   docker exec -it nova-x-redis redis-cli -a your_password ping
   ```

---

### Q9: Kafka 连接失败？

**排查步骤**：

1. **检查 Kafka 是否启动**：
   ```powershell
   docker ps | findstr kafka
   ```
   应该看到 `nova-x-kafka` 和 `nova-x-zookeeper`。

2. **检查 Kafka 主题**：
   ```powershell
   docker exec -it nova-x-kafka kafka-topics --bootstrap-server localhost:9092 --list
   ```

3. **检查 Nacos 中的 Kafka 配置**：
   ```yaml
   kafka:
     bootstrap-servers: localhost:9092
   ```

4. **测试 Kafka 连接**：
   ```powershell
   # 创建测试主题
   docker exec -it nova-x-kafka kafka-topics --create \
     --bootstrap-server localhost:9092 \
     --topic test \
     --partitions 1 \
     --replication-factor 1
   ```

---

## 📦 部署相关

### Q10: 如何在生产环境部署？

**答**：参考 [PRODUCTION.md](PRODUCTION.md)，关键步骤：

1. **创建生产环境命名空间**：`prod`

2. **上传生产环境配置**：
   - 使用生产环境的 MySQL、Redis、Kafka 地址
   - 修改日志级别为 WARN 或 ERROR
   - 配置加密敏感信息
   - 调整连接池参数

3. **配置环境变量**：
   ```bash
   export NACOS_ADDR=prod-nacos.example.com:8848
   export NACOS_NAMESPACE=prod
   ```

4. **启动服务**：
   ```bash
   java -jar \
     -Xms2g -Xmx4g \
     -Dspring.cloud.nacos.config.server-addr=$NACOS_ADDR \
     -Dspring.cloud.nacos.config.namespace=$NACOS_NAMESPACE \
     account-service.jar
   ```

5. **使用 Docker Compose 或 Kubernetes 编排**。

---

### Q11: 如何备份和恢复 Nacos 配置？

**备份**：

```bash
# 方法一：使用 Nacos API 导出
curl "http://localhost:8848/nacos/v1/cs/configs?export=true&tenant=dev&group=DEFAULT_GROUP" > nacos-backup.zip

# 方法二：备份 Nacos 数据库
docker exec nova-x-mysql mysqldump -uroot -ppassword nacos_config > nacos_config_backup.sql
```

**恢复**：

```bash
# 方法一：在控制台手动导入配置

# 方法二：恢复数据库
docker exec -i nova-x-mysql mysql -uroot -ppassword nacos_config < nacos_config_backup.sql
```

---

### Q12: Nacos 控制台密码忘记了怎么办？

**答**：重置 Nacos 密码：

```bash
# 进入 Nacos 数据库
docker exec -it nova-x-mysql mysql -uroot -ppassword nacos_config

# 重置密码（新密码为 nacos）
UPDATE users SET password='$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu' WHERE username='nacos';

# 或者重新插入用户
DELETE FROM users WHERE username='nacos';
INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);
```

---

## 💡 最佳实践

### Q13: 配置文件应该如何组织？

**答**：推荐的配置组织方式：

```
common.yaml                    # 所有服务共享的配置
├── datasource.*               # 数据库配置
├── redis.*                    # Redis 配置
├── kafka.*                    # Kafka 配置
├── elasticsearch.*            # ES 配置
├── logging.*                  # 日志配置
└── security.jwt.*             # JWT 配置

{service-name}.yaml            # 服务专属配置
├── spring.datasource.url      # 覆盖数据库名
├── mybatis-plus.*             # MyBatis 配置
└── 业务相关配置

{service-name}-{profile}.yaml  # 环境相关配置（可选）
└── 特定环境的覆盖配置
```

---

### Q14: 多少个服务应该共享一个配置文件？

**答**：
- ✅ **公共配置（common.yaml）**：所有服务共享
  - 数据库连接信息
  - Redis 配置
  - Kafka 配置
  - 日志配置

- ✅ **服务专属配置**：每个服务独立
  - 数据库名称
  - MyBatis 配置
  - 业务参数

- ⚠️ **避免过度拆分**：不要为每个微小差异创建单独的配置文件

---

需要更多帮助？查看：
- 📖 [快速启动指南](QUICKSTART.md)
- 🏭 [生产环境配置](PRODUCTION.md)
- 📚 [Nacos 官方文档](https://nacos.io/zh-cn/docs/quick-start.html)
