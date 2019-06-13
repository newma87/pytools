# <center>{{ProjectName}}</center>

## 版本信息
v1.0.0

## 第三方软件包依赖

MySQL >= v5.7

liquibase

elasticsearch

## 开发环境搭建

1. 安装MySQL

2. 安装Java 1.8以上版本

2. 安装Python2.7以及pip安装工具（开发过程中需要用到python脚本来生成部分代码）

## 工程代码插件工具使用

1. **数据库迭代管理工具liquibase**

   - 在项目启动过程中，将自动调用liquibase生成数据库结构；自启动的配置在[application-${profile}.yml](src/main/resources/application-dev.yml)的文件中。

   - liquibase的数据库xml文件放在[src/main/resources/changelog](src/main/resources/changelog)文件夹下

   - 另外，可以通过maven的liquibase插件，手动生成数据库结构。配置文件为[src/main/resources/liquibase.properties](src/main/resources/liquibase.properties)。手动调用命令行如下:
```bash
    mvnw.cmd liquibase:update
```
2. **单元测试工具JaCoco使用**

    集成Jacoco的Maven插件。手动运行以下代码（注意所有单元测试都必须要通过）
```bash
    mvnw.cmd clean test jacoco:report
```
_运行后代码覆盖率的结果保存在[target\site\jacoco\index.html](target\site\jacoco\index.html)_

## dev环境说明

_未完待续_

## prod环境说明

_未完待续_

## 部署配置

_未完待续_
