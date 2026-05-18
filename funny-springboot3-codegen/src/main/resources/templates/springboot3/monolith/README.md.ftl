
## 第一步使用命令创建骨架项目
```shell
mvn archetype:create-from-project -Darchetype.properties=./archetype.properties
```

## 第二步安装骨架项目到本地仓库
进入到target/generated-sources/archetype下执行命令
```shell
mvn clean install
```

## 第三步使用骨架项目创建新的项目

```shell
mvn archetype:generate \
-DarchetypeGroupId=com.funny.framework \
-DarchetypeArtifactId=template-archetype \
-DarchetypeVersion=1.0 \
-DgroupId=com.example \
-DartifactId=my-springboot-app \
-Dversion=1.0.0-SNAPSHOT

```