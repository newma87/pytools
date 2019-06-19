# 通过pyinstaller 3.x 工具生成平台可执行文件

## 环境配置

1. 安装pip

2. 安装pyinstaller
```bash
pip install -r pyinstaller
```

## 生成可执行文件
1. 执行`py2exe.bat`脚本文件，将在上一级目录生成`jdlcompiler.exe`

    <font color='red'>_`lark`目录存放了`Lark(0.7.1)`的配置语法文件，如果出现与`Lark`当前版本不兼容时，请把`site-package\lark\grammars`目录下的文件拷贝到本目录的`lark\grammars`下面_</font>