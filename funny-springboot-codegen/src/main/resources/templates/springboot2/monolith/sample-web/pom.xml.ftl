<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<parent>
		<groupId>${groupId}</groupId>
		<artifactId>${artifactId}</artifactId>
		<version>${version}</version>
	</parent>
	<modelVersion>4.0.0</modelVersion>
	<packaging>jar</packaging>

	<artifactId>${artifactId}-web</artifactId>

	<dependencies>
		<dependency>
			<groupId>${groupId}</groupId>
			<artifactId>${artifactId}-api</artifactId>
		</dependency>
		<dependency>
			<groupId>com.funny.framework</groupId>
			<artifactId>springboot-starter-dao</artifactId>
		</dependency>
		<dependency>
			<groupId>com.funny.framework</groupId>
			<artifactId>springboot-starter-task</artifactId>
		</dependency>
		<dependency>
			<groupId>com.funny.framework</groupId>
			<artifactId>springboot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
			<exclusions>
				<exclusion>
					<artifactId>spring-boot-starter-logging</artifactId>
					<groupId>org.springframework.boot</groupId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
			<exclusions>
				<exclusion>
					<artifactId>spring-boot-starter-logging</artifactId>
					<groupId>org.springframework.boot</groupId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

<#--		<#if subModules.urm??>-->
<#--			<dependency>-->
<#--				<groupId>com.funny.framework</groupId>-->
<#--				<artifactId>urm-starter</artifactId>-->
<#--				<version>1.0.0-SNAPSHOT</version>-->
<#--			</dependency>-->
<#--		</#if>-->
	</dependencies>


	<build>
		<#noparse><finalName>${project.artifactId}</finalName></#noparse>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-source-plugin</artifactId>
				<executions>
					<execution>
						<id>attach-sources</id>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</project>
