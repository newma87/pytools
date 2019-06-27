# -*- encoding: utf8 -*-
from jdlobject import PClass, PProperty, PEnum, PExtends, aliasWord

def reverseWord(word, pot):
    rel = word.split(pot)
    return u"{0}{1}{2}".format(rel[1], pot, rel[0])

def lowerCaption(word):
    f = lambda s: s[:1].lower() + s[1:] if s else s
    return f(word)

def upperCaption(word):
    f = lambda s: s[:1].upper() + s[1:] if s else s
    return f(word)

class ParserContext:
    def __init__(self):
        self.curFiledName = None
        self.curObjectName = None
        self.curObjectType = None
        self.curObjectComment = None
        self.curRelateTableName = None

        self.classes = {}
    
    def addObject(self, type, name):
        self.curObjectType = type.value
        self.curObjectName = name.value
        self.curFiledName = None  # 退出属性
        self.curRelateTableName = None  # 退出关系

        if self.curObjectType == u"relationship":
            return

        newCls = None
        if self.curObjectType == u"entity":
            newCls = PClass(self.curObjectName)
        elif self.curObjectType == u"enum":
            newCls = PEnum(self.curObjectName)
        self.classes[self.curObjectName] = newCls

    def __addCommenToObject(self, aliases = [], comment = None, token = None):
        if self.curObjectType == u"enum" or self.curObjectType == u"entity":
            curCls = self.classes[self.curObjectName]
            if not curCls:
                raise SyntaxError(u"There is not define for class name \"{0}\"".format(self.curObjectName))
            
            curCls.setComment(comment)
            if len(aliases) > 0:
                curCls.setAlias(aliases[0])
        elif self.curObjectType == u"relationship":
            if len(aliases) > 0:
                self.curRelateTableName = aliases[0]
            pass

    def __parseKeyComment(self, prop, ownerClass, unique, uniqueName, index, indexName, primary, primaryName):
        # 添加unique
        if (not unique or uniqueName is None):
            prop.setUnique(unique)
        else:
            ownerClass.addUniqueConstraint(uniqueName, prop)
        # 添加index
        if (index):
            ownerClass.addIndexConstraint(prop.extend.alias if indexName is None else indexName, prop)   
                    
        # 添加primary
        if (not primary or primaryName is None):       
            prop.setPrimary(primary)
            if (primary):
                ownerClass.resetPrimary(prop)
        else:
            ownerClass.addPrimaryConstraint(primaryName, prop)
        
    def __addCommentToField(self, aliases, lengths, default, nullable, unique, uniqueName, index, indexName, primary, primaryName, comment, token):
        #print(u"add comment {0} for field {1}".format(comment, self.curFiledName))
        if self.curObjectType == u"enum":
            curClass = self.classes[self.curObjectName]
            curProperty = curClass.appendEnumComment(self.curFiledName, comment)
        elif self.curObjectType == u"entity":
            curClass = self.classes[self.curObjectName]
            curProperty = curClass.getProperty(self.curFiledName)
            curProperty.setComment(comment)
            if (aliases is not None) and (len(aliases) > 0):
                curProperty.setAlias(aliases[0])
            if (lengths is not None):
                if len(lengths) == 1:
                    curProperty.setFixedLength(lengths[0])
                elif len(lengths) > 1:
                    curProperty.setRangeLength(lengths[0], lengths[1])
            if (default is not None) or not nullable:
                curProperty.setDefault(default)

            self.__parseKeyComment(curProperty, curClass, unique, uniqueName, index, indexName, primary, primaryName)

        elif self.curObjectType == u"relationship":
            leftClsName, leftPropName, rightClsName, rightPropName = self.curFiledName
            leftCls = self.classes[leftClsName]
            rightCls = self.classes[rightClsName]
            leftProp = leftCls.getProperty(leftPropName)
            rightProp = rightCls.getProperty(rightPropName)

            if len(aliases) > 0:  # 左关联的别名
                leftProp.setAlias(aliases[0])
                rightProp.setSlavePropertyAlias(aliases[0])
            if len(aliases) > 1:    # 右关联的别名
                rightProp.setAlias(aliases[1])
                leftProp.setSlavePropertyAlias(aliases[1])
            
            # 处理关系中的指定的
            if self.curObjectName == "ManyToOne":
                self.__parseKeyComment(leftProp, leftCls, unique, uniqueName, index, indexName, None, None)

        else:
            print(u"unprocess comment for object type {0}".format(self.curObjectType))

    def addComment(self, aliases = [], lengths = [], default = None, nullable = True, unique = False, uniqueName = None, index = False, indexName = None, primary = False, primaryName = None, comment = None, token = None):
        if self.curFiledName == None:    # object comment
            self.__addCommenToObject(aliases, comment, token)
        else: # property comment
            self.__addCommentToField(aliases, lengths, default, nullable, unique, uniqueName, index, indexName, primary, primaryName, comment, token)

    def addProperty(self, name, type):
        curCls = self.classes[self.curObjectName]
        if not curCls:
            raise SyntaxError(u"There is not define for class name \"{0}\"".format(self.curObjectName))
        
        self.curFiledName = name.value # 进入属性
        prop = PProperty(name.value, type.value)
        curCls.addProperty(prop)

    def __checkSyntaxError(self, leftTokenName, leftTokenProp, rightTokenName, rightTokenProp):
        leftClassName = leftTokenName.value
        if not leftClassName in self.classes or not self.classes[leftClassName].type == u"class":
            msg = u"Unknown entity name {0}. line {1} col {2}".format(leftClassName, leftTokenName.line, leftTokenName.column)
            raise SyntaxError(msg)
        leftClass = self.classes[leftClassName]
        if leftTokenProp and leftClass.hasProperty(leftTokenProp.value):
            msg = u"entity {0} already has property {1}. line {2} col {3}".format(leftClassName, leftTokenProp.value, leftTokenProp.line, leftTokenProp.column)
            raise SyntaxError(msg)
        rightClassName = rightTokenName.value
        if not rightClassName in self.classes or not self.classes[rightClassName].type == u"class":
            msg = u"Unknown entity name {0}. line {1} col {2}".format(rightClassName, rightTokenName.line, rightTokenName.column)
            raise SyntaxError(msg)
        rightClass = self.classes[rightClassName]
        if rightTokenProp and rightClass.hasProperty(rightTokenProp.value):
            msg = u"entity {0} already has property {1}. line {2} col {3}".format(rightClassName, rightTokenProp.value, rightTokenProp.line, rightTokenProp.column)
            raise SyntaxError(msg)

    def __completeVariable(self, leftTokenName, leftTokenProp, rightTokenName, rightTokenProp):
        if not leftTokenName or not rightTokenName:
            raise SyntaxError(u"In relationship definity, entity name must be specified")
        rightName = rightTokenName.value
        leftName = leftTokenName.value
        if not leftTokenProp:
            leftProp = lowerCaption(rightName)
        else:
            leftProp = leftTokenProp.value
        if not rightTokenProp:
            rightProp = lowerCaption(leftName)
        else:
            rightProp = rightTokenProp.value
        return leftName, leftProp, rightName, rightProp

    def addRelation(self, leftTokenName, leftTokenProp, rightTokenName, rightTokenProp):
        assert(leftTokenName is not None)
        assert(rightTokenName is not None)
        if not self.curObjectType == u"relationship":
            raise SyntaxError(u"Syntax Errror: relationship {0} is not defined correctly. line {1} column {2}".format(leftTokenName.value, leftTokenName.line, leftTokenName.column))

        self.__checkSyntaxError(leftTokenName, leftTokenProp, rightTokenName, rightTokenProp)
        # 补全缺省的值
        leftName, leftProp, rightName, rightProp = self.__completeVariable(leftTokenName, leftTokenProp, rightTokenName, rightTokenProp)

        self.curFiledName = (leftName, leftProp, rightName, rightProp) # 进入属性

        # 生成默认的alias
        leftClass = self.classes[leftName]
        rightClass = self.classes[rightName]
        leftAliase = u"{0}_{1}".format(aliasWord(leftProp), aliasWord(rightClass.primary))
        rightAliase = u"{0}_{1}".format(aliasWord(rightProp), aliasWord(leftClass.primary))

        # 生成外键时，会自动根据Many的标签指定是否为master Foreigner key。对于ManyToMay OneToOne则需要特殊处理
        lProp = PProperty.referenceProperty(self.curObjectName, leftClass, rightClass, leftProp, leftAliase, rightProp, rightAliase)
        lProp.setRelationshipAlias(u"{0}_{1}".format(aliasWord(leftName), aliasWord(rightName)))
        rProp = PProperty.referenceProperty(reverseWord(self.curObjectName, u"To"), rightClass, leftClass, rightProp, rightAliase, leftProp, leftAliase)
        rProp.setRelationshipAlias(u"{0}_{1}".format(aliasWord(rightName), aliasWord(leftName)))
        
        leftClass.addProperty(lProp)
        rightClass.addProperty(rProp)

        # ManyToMany和OneToOne，以左边为master Foreigner key
        if self.curObjectName == u"ManyToMany" or self.curObjectName == u"OneToOne" or self.curObjectName == u"ManyToOne":
            lProp.setLeader(True)
            rProp.setLeader(False)
            if self.curRelateTableName is not None:  # 当relationship后面的注释有指定表名时，将修改中间表名
                lProp.setRelationshipAlias(self.curRelateTableName)
                rProp.setRelationshipAlias(self.curRelateTableName)

    def addEnum(self, name, value = None):
        curCls = self.classes[self.curObjectName]
        if not curCls:
            raise SyntaxError(u"There is not define for class name \"{0}\"".format(self.curObjectName))

        self.curFiledName = name.value # 进入属性
        #print(u"add enum field {0}".format(self.curFiledName))
        curCls.addEnumValue(self.curFiledName, value.value if value else None)