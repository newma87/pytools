# JAVA 工程模版生成

1. 目录文件
```
│  main.py              # 主python文件
│  README.md
│  start.bat            # bat脚本
│  sword.exe            # 本工具集成的exe运行文件，在win10上可以正常运行
│  sword.py             
│  variants.conf        # 变量参数配置文件
│
├─pyinstaller           # pyinstaller工具的运行目录，运行生成sword.exe文件
│      readme.md
│      sword.ico        # exe文件的ico
│
└─Templates             # 代码模版目录
```

2. 运行命令行参数
```
usage: sword.exe [-h] [-c CONFIGURE] [-n PROJECT_NAME] [-p PACKAGE]
                 [-t TEMPLATE]
                 target

Create java template project

positional arguments:
  target                Target path directory                               # 生成的目标目录，如果不存在会自动创建目录

optional arguments:
  -h, --help            show this help message and exit
  -c CONFIGURE, --configure CONFIGURE                                       # json格式的配置文件，用来配置模版中的变量的替换值
                        Configure file, this config can be overrided by
                        specialfied argument
  -n PROJECT_NAME, --project-name PROJECT_NAME                              # 项目名称，如果configure文件中配置了则此参数不必配置；若同时配置了，则该参数配置优先
                        Project name for file and any config setting
  -p PACKAGE, --package PACKAGE                                             # 项目包名，如果configure文件中配置了则此参数不必配置；若同时配置了，则该参数配置优先
                        Code package and package path
  -t TEMPLATE, --template TEMPLATE                                          # 工程模版代码文件所在目录
                        Source code template directory
```

##### 注意json配置文件中，key和字符串值需要用双引号括起来

3. 模版文件编辑说明

    3.1 变量替换
    > 变量参数格式为"{{变量名}}"，工具会将变量名与configure josn文件中的配置进行配对，如果匹配上了，则该处替换为configure文件中的值
    
    如：
    - configure文件    -> {"ProjectName": "HelloWorld"}
    - 模版文件中       -> <project.name>{{ProjectName}}</project.name>
    - 替换后           -> <project.name>HelloWorld</project.name>

    3.2 变量修正函数
    
    3.2.1 {{变量名 | 修正函数1 | 修正函数2 ...}}
    > 当变量名替换时，会依序经过函数1,函数2等等的处理后得到最终数值
    
    > 当变量名用于文件目录名的时候，分隔符需要用"."来代替（因为文件和目录名不能出现"|"符号） 
    
    3.2.2 修正函数定义
    > function 函数名(value) { return 最终数值 }

    > 函数传入的参数为变量数值或者是上一个修正函数的返回数值；最终的值为最后一个修正函数的返回值

    3.2.3 修正函数注册
    > 通过Sword.registryFunction(name, func)函数，可以将自定义的修正函数注册到系统中。
   
    如：
    ```
    Sword.registryFunction('square', lambda x: x * x) # 注册自定义修正函数
    ```
    在模版文件中，对变量进行修正：{{MyValue | square}}，修正函数名为注册函数的传入的name参数

4. exe文件的生成

    4.1 安装pyinstaller
        ```
        pip install -i https://pypi.douban.com/simple/ pyinstaller
        ```

    4.2 运行pyinstaller
        ```
        pyinstaller -F -i exe文件的图件.ico main.py
        ```

    4.3 生成的exe文件
        dist目录下存放生成的目标文件