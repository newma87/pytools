@REM Auto jpa code generator script
@REM Requirment meeting.jdl in project documents/1.database/meeting.jdl 
@REM Created by newma<newma@live.cn>

@echo off
set ROOT_DIR=%~dp0..\..
set PROJ_DIR=%ROOT_DIR%\projects\meeting
set SCRIPT_DIR=%ROOT_DIR%\scripts\jdl_generator
set PACKAGE=cn.com.sailfish.meeting

echo "Auto generating jpa code and liquibase xml file ..."
python "%SCRIPT_DIR%\main.py" "%ROOT_DIR%\documents\1.database\meeting.jdl" -p %PACKAGE% -w "%PROJ_DIR%"  -t "%SCRIPT_DIR%\template" -g "%SCRIPT_DIR%\grammar"

if NOT %errorlevel% == 1 (
    echo "Generate code done!"
)
