# MYSQL开发设计规范

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
- 主键索引名为 `pk_字段名`；唯一索引名为 `uniq_字段名`；普通索引名则为` idx_字段名`。
- 超过三个表禁止 join ，如需join，必须需要确认，多表 join 必须要注意表索引、SQL 性能。
- 禁止使用外键约束和检查约束；