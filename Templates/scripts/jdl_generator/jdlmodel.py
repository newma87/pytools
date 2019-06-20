#-*- encoding: utf-8 -*-
def aliasWord(word):
    if not word or word == "":
        return
    word = word[:1].lower() + word[1:]
    newWord = u""
    for c in word:
        if c <= 'Z' and c >= 'A':
            newWord += "_" + c.lower()
        else:
            newWord += c
    return newWord

'''
扩展属性
'''
class PExtends:
    def __init__(self):
        self.alias = None
        self.indexName = None
        self.isUnique = False
        self.uniqueName = None
        self.isPrimary = False
        self.isNullable = True
        self.min = None
        self.max = None
        self.default = None
        # extra
        self.isAutoIncrease = False
    
    def __repr__(self):
        return u"<alias: {0}, indexName: {1}, isUnique: {2}, isPrimary: {3}, isNullable: {4}, min: {5}, max: {6}, default: {7}, isAutoIncrease: {8}>"\
        .format(self.alias, self.indexName, self.isUnique, self.isPrimary, self.isNullable, self.min, self.max, \
        self.default, self.isAutoIncrease).encode('utf-8').strip()
    
'''
基础类型
'''
class PBase(object):
    def __init__(self, name, type):
        self.name = None
        self.type = type
        self.comment = None
        self.value = None
        self.extend = PExtends()

        if name:
            self.setName(name)

    def setName(self, name):
        self.name = name
        self.extend.alias = aliasWord(name)

    def setValue(self, value):
        self.value = value

    def setComment(self, comment):
        self.comment = comment

AUTO_GENERATE_ID = u"id"
'''
POJO类定义
'''
class PClass(PBase):
    def __init__(self, name = None, autoId = True):
        super(PClass, self).__init__(name, u"class")
        self.properties = []
        self.propertyIndex = {}
        self.primary = None

        if autoId:
            prop = PProperty.autoGenerateId()
            self.addProperty(prop)

    def removeProperty(self, name):
        if not name in self.propertyIndex:
            print(u"removeProperty: class {0} has not property name {1}".format(self.name, name))
            return
        index = self.propertyIndex[name]
        del self.properties[index]
        # reset propertyIndex
        self.propertyIndex.clear()
        for index in range(0, len(self.properties)):
            prop = self.properties[index]
            self.propertyIndex[prop.name] = index

    def __removePrimary(self):
        if not self.primary:
            return
        
        if self.primary == AUTO_GENERATE_ID:  # 去除自动生成的主键
            self.removeProperty(AUTO_GENERATE_ID)
        else:
            prop = self.getPrimaryProperty()
            prop.setPrimary(False)
        
        self.primary = None

    def resetPrimary(self, property):
        assert(property.extend.isPrimary == True)
        self.__removePrimary()
        self.primary = property.name

    def addProperty(self, property):
        property.setParent(self)
        self.propertyIndex[property.name] = len(self.properties)
        self.properties.append(property)

        if property.extend.isPrimary:
            self.resetPrimary(property)

    def hasProperty(self, name):
        return name in self.propertyIndex
        
    def getProperty(self, name):
        if self.hasProperty(name):
            index = self.propertyIndex[name]
            return self.properties[index]
        else:
            print(u"getProperty: class {0} has not property name {1}".format(self.name, name))
            return None
    
    def getPrimaryProperty(self):
        if not self.primary:
            return None
        return self.getProperty(self.primary)

    def setAlias(self, alias):
        if alias:
            self.extend.alias = aliasWord(alias)

    def __repr__(self):
        #return u"name: {0}, type: {1}, comment: {2}, primary_prop: {3}, properties: {4}, alias:{5} " \
        #.format(self.name, self.type, self.comment, self.primary, self.properties, self.extend.alias)

        return u"<name: {0}, type: {1}, comment: {2}, primary_prop: {3}, alias: {4}, property length: {5}, property_index: {6}>"\
        .format(self.name, self.type, self.comment, self.primary, self.extend.alias, len(self.properties), self.propertyIndex).encode("utf-8").strip()
'''
属性字段
'''
class PProperty(PBase):
    @staticmethod
    def autoGenerateId():
        result = PProperty(AUTO_GENERATE_ID, u"Long")
        result.setPrimary()
        return result
    
    @staticmethod
    def foreignerProperty(name, relation, foreignerClassName, foreignerPropertyName = None, alias = None, foreignerPropertyAlias = None):
        if relation.endswith(u"Many"):
            pType = u"Set<{0}>".format(foreignerClassName)
        else:
            pType = foreignerClassName

        result = PProperty(name, pType)
        result.setAlias(alias)
        result.setForeigner(relation, foreignerClassName, foreignerPropertyName, foreignerPropertyAlias)
        return result

    def __init__(self, name = None, type = None, parent = None):
        super(PProperty, self).__init__(name, type)
        self.parent = parent
        # 外键
        self.foreignerTableName = None
        self.isForeignerKey = False
        self.isForeignerMaster = False
        self.relationship = None
        self.foreignerClassName = None
        self.foreignerPropertyName = None
        self.foreignerPropertyAlias = None

    def setParent(self, parent):
        self.parent = parent

    def setAlias(self, alias):
        if alias:
            self.extend.alias = aliasWord(alias)

    def setForeignerTableName(self, name):
        self.foreignerTableName = name

    def setAutoIncrease(self, enable = True):
        self.extend.isAutoIncrease = enable

    def setForeignerPropertyAlias(self, alias):
        if alias:
            self.foreignerPropertyAlias = alias

    def setForeigner(self, relationship, foreignerClassName, foreignerPropertyName = None, foreignerPropertyAlias = None):
        self.isForeignerKey = True
        #self.setIndex()
        self.relationship = relationship
        self.foreignerClassName = foreignerClassName
        self.foreignerPropertyName = foreignerPropertyName
        self.foreignerPropertyAlias = foreignerPropertyAlias
        self.isForeignerMaster = relationship.startswith(u"Many")

    def setIsForeignerMaster(self, yesOrNo):
        self.isForeignerMaster = yesOrNo

    def setRangeLength(self, min, max):
        self.extend.min = min
        self.extend.max = max
    
    def setFixedLength(self, length):
        self.setRangeLength(length, length)

    def isFixedLength(self):
        return self.extend.min == self.extend.max and self.extend.max is not None

    def getFixedLength(self):
        return self.extend.max

    def setDefault(self, value):
        self.extend.default = value
        self.extend.isNullable = False

    def setIndex(self, enable = True):
        if (enable):
            self.extend.indexName = u"INDEX_" + self.extend.alias.upper()
        else:
            self.extend.indexName = None

    def setUnique(self, enable = True):
        self.extend.isUnique = enable
        self.extend.uniqueName = u"u_{0}_{1}".format(self.parent.name, self.name).lower()
    
    def setPrimary(self, enable = True):
        self.extend.isPrimary = enable
        if self.type == "Long" or self.type == "Integer":
            self.extend.isAutoIncrease = enable
        else:
            self.extend.isAutoIncrease = False

    def __repr__(self):
        return u" name: {0}, type: {1}, comment: {2}, isForeignerKey: {3}, isForeignerMaster: {4}, foreignerTableName: {5}, \
        foreignerClassName: {6}, foreignerPropertyName: {7}, foreignerPropertyAlias: {8}, extends: {9}" \
        .format(self.name, self.type, self.comment, self.isForeignerKey, self.isForeignerMaster, self.foreignerTableName, \
        self.foreignerClassName, self.foreignerPropertyName, self.foreignerPropertyAlias, self.extend).encode("utf-8").strip()
'''
枚举
'''
class PEnum(PBase):
    def __init__(self, name = None):
        super(PEnum, self).__init__(name, u"enum")
        self.properties = []
        self.indexes = {}

    def addEnumValue(self, name, value = None):
        if not value:
            if len(self.properties) == 0:
                value = 0
            else:
                value = self.properties[-1].value + 1
        else:
            if isinstance(value, str) or isinstance(value, unicode):
                value = int(value)

        obj = None
        if name in self.indexes:
            obj = self.properties[self.indexes[name]]
        else:
            obj = PBase(name, u"Integer")
            self.indexes[name] = len(self.properties)
            self.properties.append(obj)

        obj.setValue(value)
        return obj

    def appendEnumComment(self, name, comment):
        if not name in self.indexes:
            print(u"Not found enum value \"{0}\" to append comment".format(name))
        obj = self.properties[self.indexes[name]]
        obj.setComment(comment)
        return obj

    def getEnumValue(self, name):
        if (name in self.indexes):
            return self.properties[self.indexes[name]]
        print(u"Can't found property name \"%\" for enum", name, self.name)
        return None

    def getEnumList(self):
        result = []
        for item in self.properties:
            result.append(item.name)
        return result

    def __repr__(self):
        temp = []
        for p in self.properties:
            if p.value is not None:
                temp.append(u"{0}={1}".format(p.name, p.value))
            else:
                temp.append(u"{0}".format(p.name))
        return u"[ {0} ]".format(u",".join(temp))

def getFromListByName(name, classes):
    return [cls for cls in classes if cls.name == name][0]

class TabelModel:
    def __init__(self):
        self.name = None
        self.remark = None
        self.columns = []
        self.IndexConstraint = []

        self.foreignerConstraint = []

    def mapper(self, classModel, classes):
        self.name = classModel.extend.alias
        self.remark = classModel.comment

        for prop in classModel.properties:
            if prop.isForeignerKey:
                if prop.isForeignerMaster:
                    foreigner = ForeignerModel()
                    foreigner.mapper(prop, classes)
                    self.foreignerConstraint.append(foreigner)
                    if not prop.relationship == "ManyToMany":
                        col = ColumnModel()
                        col.name = foreigner.baseColumn
                        col.baseTabelName = foreigner.baseTable
                        col.type = foreigner.baseColumnType
                        col.isNullable = True
                        if prop.relationship == "OneToOne":
                            col.isUnique = True
                        self.columns.append(col)
            else:
                if prop.extend.indexName is not None:
                    index = IndexModel()
                    index.mapper(prop)
                    self.IndexConstraint.append(index)

                col = ColumnModel()
                col.mapper(prop)
                self.columns.append(col)
        return self.foreignerConstraint

class IndexModel:
    def __init__(self):
        self.name = None
        self.baseTabelName = None
        self.baseColumn = None
        self.baseColumnType = None

    def mapper(self, prop):
        assert(prop.extend.indexName is not None)
        self.baseTabelName = prop.parent.extend.alias
        self.baseColumn = prop.name
        self.baseColumnType = ColumnModel.getColumnType(prop)
        self.name = prop.extend.indexName

class ForeignerModel:
    def __init__(self):
        self.relationship = None
        self.referenceTable = None
        self.referenceColumnType = None
        self.referenceColumn = None
        self.referenceColumnRef = None
        self.baseTable = None
        self.baseColumnType = None
        self.baseColumn = None
        self.baseColumnRef = None
        self.name = None

    def mapper(self, prop, classes):
        assert(prop.isForeignerMaster)
        refClass = getFromListByName(prop.foreignerClassName, classes)
        baseClass = prop.parent

        basePrimary = baseClass.getPrimaryProperty()
        refPrimary = refClass.getPrimaryProperty()

        self.relationship = prop.relationship
        if prop.relationship == "ManyToMany":
            self.baseTable = baseClass.extend.alias
            self.baseColumnRef = basePrimary.extend.alias
            self.baseColumn = prop.extend.alias #u"{0}_{1}".format(self.baseTable, basePrimary.extend.alias).lower()
            self.baseColumnType = ColumnModel.getColumnType(basePrimary)
            self.referenceTable = refClass.extend.alias
            self.referenceColumnRef = refPrimary.extend.alias
            self.referenceColumn = prop.foreignerPropertyAlias #u"{0}_{1}".format(self.referenceTable, refPrimary.extend.alias).lower()
            self.referenceColumnType = ColumnModel.getColumnType(refPrimary)
            self.name = prop.foreignerTableName
        else:
            self.baseTable = baseClass.extend.alias
            self.baseColumn = prop.extend.alias
            self.baseColumnType = ColumnModel.getColumnType(refPrimary)
            self.referenceTable = refClass.extend.alias
            self.referenceColumn = refPrimary.extend.alias
            self.name = u"fk_{0}_{1}".format(self.baseTable, self.baseColumn).lower()

class ColumnModel:
    @staticmethod
    def getColumnType(prop):
        result = None
        if prop.type == u"String":
            if prop.isFixedLength(): 
                result = u"varchar({0})".format(prop.getFixedLength())
            else:
                if prop.extend.max is not None:
                    result = u"varchar({0})".format(prop.extend.max)
                else:
                    result = u"varchar(255)"
        elif prop.type == u"Integer":
            result = u"int"
        elif prop.type == u"Long":
            result = u"bigint"
        elif prop.type == u"Instant":
            result = u"timestamp"
        elif prop.type == u"Boolean":
            result = u"boolean"
        elif prop.type == u"Float":
            result = u"float"
        elif prop.type == u"Double":
            result = u"double"
        elif prop.type == u"Blob":
            result = u"blob"
        elif prop.type == u"TextBlob":
            result = u"clob"
        elif prop.type == u"BigDecimal":
            result = u"decimal"
        if result is None:
            print("warnning: unkown type '" + prop.type + "' of property name '" + prop.name + "'")
        return result

    def __init__(self):
        self.baseTabelName = None
        self.name = None
        self.type = None
        self.remarks = None

        self.default = None
        self.isUnique = None
        self.isNullable = None
        self.isPrimary = None
        self.isAutoIncrease = None

    def mapper(self, prop):
        self.baseTabelName = prop.parent.name
        self.name = prop.extend.alias
        self.type = ColumnModel.getColumnType(prop)
        self.default = prop.extend.default
        self.isNullable = prop.extend.isNullable
        self.isUnique = prop.extend.isUnique
        self.uniqueName = prop.extend.uniqueName
        self.isAutoIncrease = prop.extend.isAutoIncrease
        self.isPrimary = prop.extend.isPrimary
        self.remarks = prop.comment
