spring:
  datasource:
    dynamic:
      druid: # 全局druid参数，绝大部分值和默认保持一致。(现已支持的参数如下,不清楚含义不要乱设置)
        # 连接池的配置信息
        # 初始化大小，最小，最大
        initial-size: 5
        min-idle: 5
        maxActive: 20
        # 配置获取连接等待超时的时间
        maxWait: 5000
        # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        timeBetweenEvictionRunsMillis: 60000
        # 配置一个连接在池中最小生存的时间，单位是毫秒
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        # 打开PSCache，并且指定每个连接上PSCache的大小
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        # 'wall'用于 用于预防 SQL 注入的filter
        filters: wall
        filter:
          config:
            enable: true
        connectTimeout: 5000
        socketTimeout: 60000 #socketTimeout建议为业务能够容忍的最大超时时间，不确定的情况下可适当调大，比如120s,即设置为120000
      datasource:
        master:
          url: jdbc:mysql://localhost:3306/framework?useAffectedRows=true&characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&serverTimezone=Asia/Shanghai
          username: root
          password: Astack@123
          driver-class-name: com.mysql.cj.jdbc.Driver
          # 多数据源配置
          #multi-datasource1:
          #url: jdbc:mysql://localhost:3306/jeecg-boot2?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
          #username: root
          #password: root
          #driver-class-name: com.mysql.cj.jdbc.Driver
  #redis 配置
  redis:
    host: 127.0.0.1
    database: 0
    password: ''
    port: 6379
    jedis:
      pool:
        max-active: 8
        max-idle: 8
        max-wait: -1
        min-idle: 0
    timeout: 0
#Mybatis输出sql日志
logging:
  level:
    com.funny.framework.springboot.sample.dao.mapper: DEBUG
  config: classpath:log4j2/log4j2-test.xml
  file:
    path: /data/logs/applog
mybatis-plus:
  configuration:
    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
framework:
  job:
    accessToken: ''
    admin:
      addresses: ${jobRegisterTest}
    executor:
      address: ''
      appname: <#noparse>${spring.application.name}</#noparse>
      ip: ''
      logpath: <#noparse>${logging.file.path}/jobhandler</#noparse>
      logretentiondays: 30
      port: 9999