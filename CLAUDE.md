# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build all modules (root level)
mvn clean install

# Build starter only (must publish before codegen/example can compile)
cd funny-springboot3-starter && mvn clean deploy

# Run codegen app
cd funny-springboot3-codegen && mvn spring-boot:run

# Run example app
cd funny-springboot3-example/example-web && mvn spring-boot:run
```

Starter modules are published to Aliyun Maven repo (`funny-springboot3-starter-parent` version `1.0.1-RELEASE`). After changing starter code, redeploy before building downstream modules.

## Module Architecture

```
funny-springboot3 (root pom, imports fun-bom)
├── funny-springboot3-starter          # Framework starter library (published artifact)
│   ├── starter-parent                 # BOM: manages all dependency versions
│   ├── starter-web                    # Auto-config: FastJSON, XSS, global exception, XXL-Job, Feign, Redis
│   └── starter-dao                    # Auto-config: MyBatis-Plus, Druid, dynamic-datasource, MySQL
├── funny-springboot3-codegen          # Code generator Spring Boot app
│   └── src/main/resources/templates/  # Freemarker templates (springboot2/ × springboot3/ × monolith/layered)
└── funny-springboot3-example          # Demo app showing starter usage
    ├── example-api                    # API interfaces + DTOs (client contract)
    └── example-web                    # Implementation: controllers, services, DAO
```

## Key Patterns

- **Auto-configuration**: Both starters register via `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- **API-first controllers**: Controllers implement interfaces from the api module (e.g., `BrandSeriesController implements SeriesApi`)
- **Response wrapper**: All APIs return `ApiResult<T>` / `CommonResult<T>`
- **JSON**: FastJSON (not Jackson) — configured in `FrameworkWebAutoConfiguration`
- **Logging**: Log4j2 (not Logback) — all starters exclude `spring-boot-starter-logging`
- **Code generation**: Freemarker templates under `templates/{springbootVersion}/{appType}/` generate full project scaffolding into ZIP

## Tech Stack

- Java 21, Spring Boot 3.5.4, Maven
- MyBatis-Plus 3.5.9 + Druid + dynamic-datasource
- OpenFeign, XXL-Job 3.3.1, COLA extension 4.3.2
- `funny-bom` manages internal dependencies: `funny-framework-core`, `funny-framework-tool`, `funny-framework-crypto`, `funny-log-springboot-starter`, `funny-redis-springboot-starter`
- The `funny-framework` source is at `../funny-framework`

## Starter Web Auto-configured Beans

- `FrameworkWebAutoConfiguration`: FastJSON message converters, XSS filter, global exception handler, relaxed Tomcat query chars
- `FrameworkTaskAutoConfiguration`: XXL-Job executor (conditional on `framework.job.admin.addresses` + `framework.job.executor.appname`)
- `FrameworkDaoAutoConfiguration`: MyBatis-Plus DAO auto-configuration
- `ExtendedRequestMappingHandlerAdapter`: Disallows `class.*` fields in data binding (Spring4Shell mitigation, opt-in via `framework.web.useDisallowedField=true`)
