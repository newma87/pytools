#!/bin/bash

# Auto jpa code generator script
# Requirment meeting.jdl in project documents/1.database/meeting.jdl 
# Created by newma<newma@live.cn>

basepath=$(cd `dirname $0`; pwd)
ROOT_DIR=${basepath}/../..
PROJ_DIR=${ROOT_DIR}/projects/meeting
SCRIPT_DIR=${ROOT_DIR}/scripts/jdl_generator
PACKAGE=cn.com.sailfish.meeting

echo "Auto generating jpa code and liquibase xml file ..."
python ${SCRIPT_DIR}/main.py ${ROOT_DIR}/documents/1.database/meeting.jdl -p ${PACKAGE} -w ${PROJ_DIR}  -t ${SCRIPT_DIR}/template -g ${SCRIPT_DIR}/grammar

if [ $? -ne 0 ]; then
    exit $?
else
    echo "Generate code done!"
fi
