# -*- encoding: utf8 -*-

import os
import time
import codecs
import jinja2

import xml.dom.minidom as minidom
from jdlobject import PProperty, PClass, PExtends, PEnum
from jdlmodel import TabelModel, ColumnModel, IndexModel, ForeignerModel

def mkdir(dirPath):
    if not os.path.exists(dirPath):
        os.makedirs(dirPath)
    return dirPath

def Captainal(val):
    f = lambda s: s[:1].upper() + s[1:] if s else s
    return f(val)

def JpaType(val):
    if val == "Blob":
        return "byte[]"
    elif val == "TextBlob":
        return "String"
    elif val == "BigDecimal":
        return "BigDecimal"
    return val

def renderFromTemplate(tpl_path , context):
    path, filename = os.path.split(tpl_path)
    env = jinja2.Environment(loader = jinja2.FileSystemLoader(path or '.'), trim_blocks = True, lstrip_blocks = True)
    env.filters['captain'] = Captainal
    env.filters['jpatype'] = JpaType
    return env.get_template(filename).render(context)

def renderToFile(template, codeFile, context):
    code = renderFromTemplate(template, context)
    fp = codecs.open(codeFile, "w", "utf-8")
    fp.write(code)
    fp.close()

class JAVACodeGenerator:
    def __init__(self, workDir = ".", templateDir = "template", package = "cn.com.sailfish", isRepository = False, author = "AutoCoder (maintained by newma<newma@live.cn>)"):
        print(u"Generate java code to \"{0}\", for package \"{1}\"".format(workDir, package))
        self.workDir = workDir or "."
        self.package = package
        self.author = author
        self.templateDir = templateDir
        self.isRepository = isRepository

    def __getGernerateDir(self, subDir):
        parent = self.workDir
        if self.package:
            dirs = self.package.split(".")
            parent = os.path.join(self.workDir, "src", "main", "java", *dirs)
        codeDir = os.path.join(parent, subDir)
        return mkdir(codeDir)

    def __makeDomainContext(self, model):
        context = {
            u'package': self.package,
            u'author': self.author,
            u'time': time.strftime(u"%Y-%m-%d %H:%M:%S", time.localtime()),
            u'name': model.name,
            u'alias': model.extend.alias,
            u'type': model.type,
            u'comment': model.comment,
            u'primary': model.getPrimaryProperty(),
            u'properties': model.properties
        }
        return context

    def __makeEnumContext(self, model):
        context = {
            u'package': self.package,
            u'author': self.author,
            u'time': time.strftime(u"%Y-%m-%d %H:%M:%S", time.localtime()),
            u'name': model.name,
            u'type': model.type,
            u'comment': model.comment,
            u'properties': model.properties
        }
        return context

    def __generateJava(self, templateName, subDir, context):
        template = os.path.join(self.templateDir, templateName)
        codeDir = self.__getGernerateDir(subDir)
        codeFile = os.path.join(codeDir, u"{0}.java".format(context[u"name"]))
        renderToFile(template, codeFile, context)

    def __makeRepositoryContext(self, model):
        prop = model.getPrimaryProperty()
        context = {
            u'package': self.package,
            u'author': self.author,
            u'time': time.strftime(u"%Y-%m-%d %H:%M:%S", time.localtime()),
            u'name': u"{0}Repository".format(model.name),
            u"domain": model.name,
            u"primaryType": prop.type,
            u"extra": "\n"
        }
        return context

    def generateEnum(self, model, templateName = "enum.tpl"):
        context = self.__makeEnumContext(model)
        self.__generateJava(templateName, "enum", context)

    def generateDomain(self, model, templateName = "domain.tpl"):
        context = self.__makeDomainContext(model)
        self.__generateJava(templateName, "domain", context)

    def __getExtraDataFromRepository(self, codeFile):
        fp = codecs.open(codeFile, "r", "utf-8")
        content = fp.read()
        fp.close()
        begin = content.index('> {')
        end = content.rfind('}')
        return content[begin + 3:end]

    def genrateRepository(self, model, templateName = "repository.tpl"):
        context = self.__makeRepositoryContext(model)
        codeDir = self.__getGernerateDir("repository")
        codeFile = os.path.join(codeDir, u"{0}.java".format(context[u"name"]))
        if (os.path.exists(codeFile)):
            context['extra'] = self.__getExtraDataFromRepository(codeFile)

        self.__generateJava(templateName, "repository", context)

    def generate(self, models):
        for model in models:
            self.generateSingle(model)

    def generateSingle(self, model):
        if model.type == u"class":
            self.generateDomain(model)
            if (self.isRepository):
                self.genrateRepository(model)
        elif model.type == u"enum":
            self.generateEnum(model)
        else:
            print(u"unsupport model type {0}".format(model.type))

class LiquiBaseGenerator:

    def __init__(self, workDir = ".", templateDir = "template", author = "AutoCoder (maintained by newma)", tag = ""):
        self.author = author
        self.templateDir = templateDir
        self.workDir = workDir or "."
        self.codeDir = os.path.join(self.workDir, "src", "main", "resources", "changelog")
        self.changeDir = os.path.join(self.codeDir, u"change")
        self.tag = tag

    def __generateXmlFile(self, index, templateFile, outputFile, models):
        context = {
            u"author": self.author,
            u"time": time.strftime(u"%Y-%m-%d %H:%M:%S", time.localtime()),
            u'id': u"{0}-{1}".format(time.strftime(u"%Y%m%d%H%M%S", time.localtime()), index),
            u"models": models
        }
        if (self.tag is None or self.tag is ""):
            outputFile = u"{0}.{1}".format(index, outputFile)
        else:
            outputFile = u"{0}.{1}_{2}".format(index, self.tag, outputFile)
        xmlFile = os.path.join(self.changeDir, outputFile)
        template = os.path.join(self.templateDir, templateFile)
        renderToFile(template, xmlFile, context)
        return os.path.join(u"change", outputFile)
    
    def generateXml(self, classes):
        files = []
        mkdir(self.changeDir)

        tblModels = []
        foreignerContraints = []
        for cls in classes: 
            if cls.type == "class":
                tbl = TabelModel()
                tbl.mapper(cls)
                tblModels.append(tbl)
                if len(tbl.foreignerConstraints) > 0:
                    foreignerContraints = foreignerContraints + tbl.foreignerConstraints

        files.append(self.__generateXmlFile(len(files) + 1, u"liquibase-changeset.tpl", u"initialize_table.xml", tblModels))

        if (len(foreignerContraints) > 0):
            files.append(self.__generateXmlFile(len(files) + 1, u"liquibase-reference.tpl", u"add_constraints.xml", foreignerContraints))
        return files

    def getFullFileList(self, xmlFile, files):
        if (os.path.exists(xmlFile)):
            dom = minidom.parse(xmlFile)
            root = dom.documentElement
            includes = root.getElementsByTagName(u'include')
            for node in includes:
                p = node.getAttribute(u'file')
                if not p in files:
                    files.append(p)
        return files

    def generateMaster(self, files):
        mkdir(self.codeDir)
        xmlFile = os.path.join(self.codeDir, u"master.xml")

        files = self.getFullFileList(xmlFile, files)

        template = os.path.join(self.templateDir, "liquibase-master.tpl")
        context = {
            u'author': self.author,
            u'time': time.strftime(u"%Y-%m-%d %H:%M:%S", time.localtime()),
            u'changelogs': files
        }
        renderToFile(template, xmlFile, context)

    def generate(self, models):
        files = self.generateXml(models)
        self.generateMaster(files)
