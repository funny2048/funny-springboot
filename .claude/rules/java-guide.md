## 1. 数据库设计规范

- 表名与字段统一使用 `snake_case` ，表名不使用复数名词，表名、字段名必须使用小写字母或数字，禁止出现数字开头，禁止两个下划线中间只出现数字

- 表达是与否概念的字段，必须使用 is_xxx 的方式命名，数据类型是 unsigned tinyint（1 表示是，0 表示否）。

- 小数类型为 decimal，禁止使用 float 和 double。

- 如果存储的字符串长度几乎相等，使用 char 定长字符串类型。

- varchar 是可变长字符串，不预先分配存储空间，长度不要超过 5000，如果存储长度

  大于此值，定义字段类型为 text，独立出来一张表，用主键来对应，避免影响其它字段索引效

  率。

- 所有表必须包含以下4个通用字段：

> `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
>
> `created_stime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
>
> `modified_stime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
>
> `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 正常 1 删除',

- 手机号、密码、用户真实姓名、身份证号、银行卡号、个人住址、员工姓名、公司税务标识、属于敏感信息，必须设计为加密存储，字段后缀 `_encrypt`，手机号额外增加 `_hash` 字段。
- 主键索引名为 `pk_字段名`；唯一索引名为 `uk_字段名`；普通索引名则为` idx_字段名`。
- 超过三个表禁止 join ，如需join，必须需要确认，多表 join 必须要注意表索引、SQL 性能。

## 2. 数据ORM约束

- 禁止跨数据库联表查询
- Mapper 返回值禁止直接使用 Map/List<Map>，必须定义 DTO
- 更新操作禁止直接把查出来的完整实体传入更新，必须新建一个实体对象，只设置 `id` + 需要更新的字段，再调用修改方法。
- Service 层禁止使用 QueryWrapper、LambdaQueryWrapper写的自定义查询和修改
- 复杂查询必须使用 Mapper 自定义 SQL
- 禁止生成以下内容, DELETE or TRUNCATE or DROP 表, UPDATE 无 WHERE,

## 3. 多数据源约束

当使用多数据源时：

- Service 层存在事务注解时，必须检查 Mapper 是否正确标注数据源
- 防止数据源切换失效

## 4. 事务与一致性

- 多表操作必须开启事务
- 事务内禁止：
    - RPC 调用
    - MQ 发送
    - Redis 操作


目的：避免长事务与资源阻塞

## 5. 架构与分层

必须遵循四层结构：

- Controller：参数校验与请求编排
- Service：业务逻辑处理
- Manager: 事务处理层
- Dao：数据库与中间件操作


约束：

- 推荐的调用顺序为Controller—Service—Manager—Dao，简单的查询为减少代码量也可以直接Controller—Dao，禁止逆向调用，比如Manager—Service。
- Controller 不得包含业务逻辑
- Manager、Dao 只允许里有数据库的操作，不应该有业务逻辑处理

## 6.性能规约

- 禁止用 Apache Beanutils 进行属性的 copy，优先使用 Spring BeanUtils。
- 禁止在方法体内定义：Pattern pattern = Pattern.compile(“规则”);  在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。

## 7.API 返回规范

- 所有接口返回值必须使用 `com.funny.framework.core.result.ApiResult<T>` 包装，标准字段仅允许：`code`、`msg`、`data`
- 成功返回统一使用：`ApiResult.success(data)`,失败返回统一使用：`ApiResult.fail(code,msg)`
- 禁止新增 `Result`、`Response`、`xxxResult`等重复返回模型
- 禁止返回 `Map`、`Object`、字符串等非标准结构

## 8.错误码约束

- 错误码必须来源于 `com.funny.framework.core.enums.BasicErrorCode` 或其子类
- 禁止手写 magic number 错误码 , 动态拼接错误码

## 9.异常处理

- 禁止将异常的堆栈信息通过接口直接返回。
- 捕获异常的时候必须打印error日志，示例：logger.error("xxx error,param={}",JSON.toJSONString(param), e);
- 参数校验失败且无需继续流程时，优先使用 `ApiResult.fail()` 返回

## 10.安全规约

- 隶属于用户个人的页面或者功能必须进行权限控制校验。防止没有做水平权限校验就可随意访问、修改、删除别人的数据，比如查看他人的私信内容。
- 用户敏感数据禁止直接展示，必须对展示数据进行脱敏。中国大陆个人手机号码显示为:137****0969，隐藏中间 4 位，防止隐私泄露。

## 11. 幂等与关键业务

以下场景必须保证幂等性：

- 下单 / 支付 / 退款 / 结算 /
- 优惠券 / 积分 / 活动
- 广告计划 / 广告单元 / 广告创意


要求：

- 请求里必须有唯一requestId，根据requestId / 业务ID 做分布式锁和幂等处理设计

## 12. 日志

- 调用外部接口必须记录：请求 URL ，请求参数，请求 Header（如有），响应结果
- Controller层 第一行必须日志打印 入参，如果参数大于3个，必须设计为DTO。

## 13. 代码规范

- 当满足调用方 ≥ 2，条件分支 ≥ 3 必须抽象，抽象为独立 Service 方法，避免重复逻辑扩散。

## 14. 禁止事项

- 禁止硬编码,比如中间件的配置，第三方接口的地址等。
- 在 CLAUDE.md 中记录密码
- 在 Git 提交中包含凭证
- 不要读取`application-*.yml`、`*.env`、`*.properties`、`*.config`配置文件里的中间件配置链接信息和密码信息