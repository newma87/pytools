#-*- encoding: utf-8 -*-
import argparse
import os, collections
from sword import Sword

import random
import string
import json
import codecs

def generate_activation_code(len=16, n=1):
    '''生成n个长度为len的随机序列码'''
    random.seed()
    chars = string.ascii_letters + string.digits
    return [''.join([random.choice(chars) for _ in range(len)]) for _ in range(n)]

def usage():
    parser = argparse.ArgumentParser(description='Create java template project')
    parser.add_argument('-c', '--configure', help="Configure file, this config can be overrided by specialfied argument")
    parser.add_argument('-n', '--project-name', help = "Project name for file and any config setting")
    parser.add_argument('-p', '--package', help = "Code package and package path")
    parser.add_argument('-t', '--template', help = "Source code template directory")
    parser.add_argument('target', help='Target path directory')
    arg = parser.parse_args()
    return arg

def copyDir(source, target):
    lists = os.listdir(source)
    for item in lists:
        path = os.path.join(source, item)
        tarPath = os.path.join(target, item)
        if os.path.isfile(path):
            # copy file
            #print("copy file" + path + " to " + tarPath)
            try:
                with codecs.open(path, 'rb') as srcFile:
                    try:
                        with codecs.open(tarPath, 'wb') as tarFile:
                            while True:
                                content = srcFile.read(1024*1024*10)
                                if len(content) == 0:
                                    break
                                tarFile.write(content)
                    except UnicodeDecodeError as err:
                        print("Open file " + tarPath + " error: " + str(err))
            except UnicodeDecodeError as err:
                print("Open file " + path + " error: " + str(err))
        else:
            if not os.path.exists(tarPath):
                os.mkdir(tarPath)
            copyDir(path, tarPath)

def RandomPassword(value):
    return generate_activation_code(len=16)[0]

def RandomSecret(value):
    return generate_activation_code(len=64)[0]

def main():
    args = usage()
    context = {}
    if args.configure:
        with codecs.open(args.configure, 'r', encoding = 'utf-8') as conf:
            context = json.load(conf)

    if args.project_name:
        context["ProjectName"] = args.project_name
    if args.package:
        context["Package"] = args.package
    if not "Seed" in context:
        context["Seed"] = ""

    templateDir = args.template if args.template else "Templates"
    targetDir = args.target if args.target else "."

    if not os.path.exists(targetDir):
        Sword.mkdir(targetDir)

    sword = Sword()
    sword.registryFunction("randomPassword", RandomPassword)
    sword.registryFunction("randomSecret", RandomSecret)
    print ("Begin to render directory \"" + templateDir + "\" to \"" + targetDir + "\"")
    file_lists = os.listdir(templateDir)
    for items in file_lists:
        path = os.path.join(templateDir, items)
        tarPath = os.path.join(targetDir, items)
        if os.path.isfile(path):
            try:
                target = sword.renderLine(tarPath, context)
                sword.renderFile(path, target, context)
            except KeyError as err:
                print("Parse key error: " + str(err) + ", file name: \" "+ path + "\"")
        else:
            if not os.path.exists(tarPath):
                os.mkdir(tarPath)
            if items != "scripts":
                sword.renderDir(path, tarPath, context)
            else:
                print("Copy directory \"" + path + "\" to \"" + tarPath + "\"")
                copyDir(path, tarPath)
    print ("End of render!")

if __name__ == '__main__':
    main()