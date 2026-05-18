<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.funny.framework</groupId>
        <artifactId>springboot3-starter-parent</artifactId>
        <version>1.0.1-RELEASE</version>
    </parent>

    <groupId>${groupId}</groupId>
    <artifactId>${artifactId}</artifactId>
    <version>${version}</version>
    <packaging>pom</packaging>
    <#noparse><name>${project.artifactId}</name></#noparse>
    <description>${description}</description>

    <modules>
        <module>${artifactId}-web</module>
        <module>${artifactId}-api</module>
    </modules>

    <properties>
        <maven.compiler.target>17</maven.compiler.target>
        <maven.compiler.source>17</maven.compiler.source>
        <java.version>17</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>${groupId}</groupId>
                <artifactId>${artifactId}-web</artifactId>
                <#noparse><version>${project.version}</version></#noparse>
            </dependency>
            <dependency>
                <groupId>${groupId}</groupId>
                <artifactId>${artifactId}-api</artifactId>
                <#noparse><version>${project.version}</version></#noparse>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-archetype-plugin</artifactId>
                <version>3.2.0</version>
            </plugin>
        </plugins>
    </build>
</project>
