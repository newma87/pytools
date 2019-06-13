## <center>JDL语法自动解析工具</center>

本脚本用**python2.7**写的，运行**main.py**后可以根据传入的**JDL**文件，自动生成**JPA**文件
本工具在注释部分对JDL语法进行了扩展，通过注释部分可以更好的控制库的内容

### 运行需求

**安装`python2.7`**
[百度安装经验](https://jingyan.baidu.com/article/c910274be14d64cd361d2dd8.html)

**安装最新版本的`pip`(pip@ >= 10.0.1)**
检测pip版本，是否大于10.0.1
```
pip --version
```
手动安装pip（window多数需要手动安装）
```
curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
python get-pip.py
```
[python官方PIP安装文档](https://pip.pypa.io/en/stable/installing/)

**安装完成后，使用`pip`安装相关依赖**
```bash
pip install -r requirements.txt
```

### 工具使用

`main.py`为入口模块

运行以下命令查看参数
```bash
python main.py -h 
```

_代码生成的模版文件放在`tempalte`目录_

_**JDL**及其扩展语法的**BNF**文件则放在`grammar`目录_

### JDL语法：
[JDL Studio 语法](https://start.jhipster.tech/jdl-studio/)

### 注释部分扩展语法：
+ `()` 括号指定数据库中的表名或者字段名
+ `[]` 中括号指定数据库字段的长度或者长度范围
+ `<>` 尖括号指定数据库字段的默认值或者不为空
+ `^`  代表该字段为唯一
+ `@`  代表该字段为主键

**括号：** `(name）* 3`

    括号中的内容代表数据库库的表名或者数据库的字段名
<font color='red'>*在`ManyToMany`的属性注释中，该括号可连续出现最多3次，分别是指定左数据表的关联字段名、右数据表的关联字段名，以及中间表表名*</font>

**中括号：** `[ n[, m] ]`

    当中括号中只有一个数字，则代表该字段是固定长度为n的字段
    当中括号中有两个数字`n`和`m`，代表该字段长度在范围`n`到`m`之间

**尖括号** `< [default] >` 

    当尖括号出现时，代表该字段不能为空
    当尖括号中有数值时，则该数值为该字段的默认值

**上标号** `^`

    当该标号出现时，代表该字段为唯一索引字段

**@标号** `@`

    当该标号出现时，代表该字段为主键字段（默认为`id`字段，该字段系统自动创建)，如果字段的值类型为数字类型时，该字段默认为自动增长字段 
