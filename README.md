# funny-springboot3

基于 Spring Boot 3 的企业级开发脚手架，提供通用 Starter 组件和代码生成器，开箱即用。

## 技术栈

- Java 21 + Spring Boot 3.5.4 + Maven
- MyBatis-Plus 3.5.9 + Druid + dynamic-datasource
- OpenFeign 4.3.0 / XXL-Job 3.3.1 / COLA Extension 4.3.2
- FastJSON / Log4j2 

## 模块结构

```
funny-springboot
├── funny-springboot-starter                # 框架 Starter 组件（发布到 Maven 仓库）
│   ├── starter-parent                      # BOM：统一管理所有依赖版本
│   ├── starter-web                         # Web 自动配置：FastJSON、XSS 过滤、全局异常、XXL-Job、Feign、Redis
│   └── starter-dao                         # DAO 自动配置：MyBatis-Plus、Druid、动态数据源、MySQL
├── funny-springboot-codegen                # 代码生成器（Spring Boot 应用）
└── funny-springboot-example                # 示例工程（演示 Starter 用法）
    ├── example-api                         # API 接口 + DTO（客户端契约）
    └── example-web                         # 实现：Controller、Service、DAO
```

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+

### 构建

```bash
# 构建全部模块
mvn clean install

# 仅构建 Starter（修改 Starter 代码后需先发布，下游模块才能编译）
cd funny-springboot-starter && mvn clean deploy
```

### 运行

```bash
# 启动代码生成器
cd funny-springboot-codegen && mvn spring-boot:run

# 启动示例应用
cd funny-springboot-example/example-web && mvn spring-boot:run
```

## Starter 组件说明

### starter-web

引入即可自动获得以下能力：

| 能力 | 说明 |
|------|------|
| FastJSON 消息转换 | 替代 Jackson，自动配置 HTTP 消息转换器 |
| XSS 过滤 | 全局 XSS 攻击防护 |
| 全局异常处理 | 统一异常拦截，返回标准错误响应 |
| XXL-Job 执行器 | 配置 `framework.job.admin.addresses` + `framework.job.executor.appname` 后自动注册 |
| Feign 支持 | OpenFeign 远程调用自动配置 |
| Spring4Shell 防护 | `framework.web.useDisallowedField=true` 开启，禁止 `class.*` 字段绑定 |

### starter-dao

引入即可自动获得以下能力：

| 能力 | 说明 |
|------|------|
| MyBatis-Plus | ORM 框架，内置通用 Mapper |
| Druid 连接池 | 阿里 Druid 数据库连接池 |
| 动态数据源 | 基于 dynamic-datasource 的多数据源支持 |
| MySQL | MySQL 驱动自动配置 |

## 代码生成器

`funny-springboot-codegen` 模块提供基于 Freemarker 模板的代码生成能力，支持：

- Spring Boot 2 / Spring Boot 3 项目脚手架
- 单体架构（monolith）项目模板
- 通过接口调用生成完整项目 ZIP 包

## 使用 Starter

在项目 `pom.xml` 中引入：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.funny</groupId>
            <artifactId>funny-springboot-starter-parent</artifactId>
            <version>1.0.1-RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <!-- Web Starter -->
    <dependency>
        <groupId>com.funny</groupId>
        <artifactId>funny-springboot-starter-web</artifactId>
    </dependency>

    <!-- DAO Starter -->
    <dependency>
        <groupId>com.funny</groupId>
        <artifactId>funny-springboot-starter-dao</artifactId>
    </dependency>
</dependencies>
```

## API 风格

- 统一 GET/POST，禁用 PUT/PATCH
- 所有响应封装在 `ApiResult<T>` / `CommonResult<T>` 中
- Controller 实现接口模块（api module）中定义的 API 接口
- 全局异常由 `GlobalExceptionAdvice` 统一拦截

## 相关仓库

- [funny-framework](../funny-framework) — 基础框架核心库（`funny-bom` 管理内部依赖）
