# README

- 账号 admin/admin123
- 此版本sql滞后master,需要先执行本地sql

## 平台简介

基于RuoYi 3.2版修改

已完成修改：

1、Mybatis替换成Mybatis-Plus 3.0.7.1

2、加入lombok插件

3、代码生成模板修改，使用lombok注解和MybatisPlus通用Crud接口

4、logback日志设置成彩色高亮

5、模块热插拔（新模块只需导入ruoyi-common、ruoyi-framework依赖，使用模块时只需在ruoyi-admin中添加对应模块的依赖即可，不用再将controller和html文件拷贝到admin模块）

待完成：

1、控制层除了页面请求，其他接口返回通用实体Result（方便后期前后端分离）

2、菜单新增或修改时，排序值自动重排序（如设置菜单排序值1，则排序值小于等于1的自动加一）


使用说明：

1、在代码生成页面配置自己的 开发者名称、模块名称（com.ruoyi.xxx）和表前缀等参数,点击应用

2、使用代码生成器生成代码后，新建Module，将生成的代码复制到新模块中，目录是对应的，不用修改

3、在新模块中加入
```
<dependencies>
	<!-- 通用工具-->
	<dependency>
		<groupId>com.ruoyi</groupId>
		<artifactId>ruoyi-common</artifactId>
		<version>${ruoyi.version}</version>
	</dependency>

	<!-- 核心模块-->
	<dependency>
		<groupId>com.ruoyi</groupId>
		<artifactId>ruoyi-framework</artifactId>
		<version>${ruoyi.version}</version>
	</dependency>
</dependencies>
```
4、在ruoyi-admin模块中导入新模块依赖（热插拔，不使用的模块直接注释掉就可以）
```
<dependency>
    <groupId>com.ruoyi</groupId>
    <artifactId>xxx-xxx(你的模块名)</artifactId>
    <version>${ruoyi.version}</version>
</dependency>
```
5、在数据中执行自动生成的菜单sql

6、启动项目

其他功能请查看RuoYi原版介绍

RuoYi地址：

源码地址：https://gitee.com/y_project/RuoYi

演示地址：http://ruoyi.vip  

文档地址：http://doc.ruoyi.vip
