#-*- encoding: utf-8 -*-
from enum import Enum

AUTO_GENERATE_ID = u"id"        # 数据库中自动生成的主键名称

PRIMARY = 0 # 主键
INDEX = 1   # 索引键
UNIQUE = 2  # 唯一键

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
        self.alias = None           # 数据库中的名称
        self.isUnique = False       # 是否唯一
        self.isPrimary = False      # 是否主键
        self.isNullable = True      # 是否为空
        self.min = None             # 最小值
        self.max = None             # 最大值
        self.default = None         # 默认值
        # extra
        self.isAutoIncrease = False    # 是否自增长
    
    def __repr__(self):
        return u"PExtends<alias: {0}, isUnique: {1}, isPrimary: {2}, isNullable: {3}, min: {4}, max: {5}, default: {6}, isAutoIncrease: {7}>"\
        .format(self.alias, self.isUnique, self.isPrimary, self.isNullable, self.min, self.max, self.default, self.isAutoIncrease).strip()
    
'''
基础类型
'''
class PBase(object):
    def __init__(self, name, type):
        self.name = None            # 名称
        self.type = type            # 类型
        self.comment = None         # 注释
        self.value = None           # 默认值
        self.extend = PExtends()    # 扩展属性

        if name:
            self.setName(name)

    def setName(self, name):
        self.name = name
        self.extend.alias = aliasWord(name)

    def setValue(self, value):
        self.value = value

    def setComment(self, comment):
        self.comment = comment
    
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
    def referenceProperty(relation, cls, otherCls, name, alias = None,  otherPropertyName = None, otherPropertyAlias = None):
        '''
        Relationship 关联属性
        @cls: 自身的类
        @otherCls：对手的类
        @name:  自身名称
        @alias: 自身别名
        @otherPropertyName：对手的属性名
        @otherPropertyAlias：对手的别名

        如：relationship ManyToMany {
            Left{rightName} to Right{leftName} // (right_name) (left_name)
        }
        对于rightName的属性：cls = Left, name = rightName, otherCls = Right, otherPropertyName = leftName, alias = right_name, otherPropertyAlias = left_name
        对于leftName的属性: cls = Right, name = leftName, otherCls = Left, otherPropertyName = rightName, alias = left_name, otherPropertyAlias = right_name
        '''
        if relation.endswith(u"Many"):
            pType = u"Set<{0}>".format(otherCls.name)
        else:
            pType = otherCls.name

        result = PProperty(name, pType)
        result.setAlias(alias)
        result.setRelationship(relation, otherCls, otherPropertyName, otherPropertyAlias)
        return result

    def __init__(self, name = None, type = None, parent = None):
        super(PProperty, self).__init__(name, type)
        self.parent = parent                # 所属的class
        # 关系定义
        self.relationship = None            # 关系
        self.relationshipAlias = None       # 关系别名（ManyToMany时将作为中间表名）
        self.isLeader = False               # 是否在关系处于主要地位，这将影响是否在parent class中显示该属性

        self.slaveClass = None              # 关系另一端的表类
        self.slavePropName = None           # 关系另一端的属性名
        self.slavePropAlias = None          # 关系另一端的属性别名

    def setParent(self, parent):
        self.parent = parent

    def setAlias(self, alias):
        if alias:
            self.extend.alias = aliasWord(alias)

    def setRelationshipAlias(self, alias):
        self.relationshipAlias = alias

    def setSlavePropertyAlias(self, alias):
        if alias:
            self.slavePropAlias = alias

    def setRelationship(self, relationship, slaveClass, slavePropName = None, slavePropAlias = None):
        self.relationship = relationship
        self.slaveClass = slaveClass
        self.slavePropName = slavePropName
        self.slavePropAlias = slavePropAlias

    def setLeader(self, yesOrNo = True):
        self.isLeader = yesOrNo
        if (self.isLeader and self.relationship == u"ManyToOne"): # ManyToOne时，并且为leader时，需要为其建立索引
            self.setIndex()

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
        if (self.parent is None):
            raise RuntimeError(u'Set property \'{0}\' index before it add to parent class'.format(self.name))

        keyName = self.extend.alias.lower()
        self.parent.removeConstraint(INDEX, keyName)
        if (enable):
            self.parent.addConstraint(INDEX, keyName, self)

    def setUnique(self, enable = True):
        self.extend.isUnique = enable
    
    def setPrimary(self, enable = True):
        self.extend.isPrimary = enable
        if (enable and (self.type == "Long" or self.type == "Integer")):
            self.extend.isAutoIncrease = enable
        else:
            self.extend.isAutoIncrease = False

    def __repr__(self):
        return u"PProperty<name: {0}, type: {1}, comment: {2}, parent:{3}, isLeader: {4}, relationshipAlias: {5}, slaveClassName: {6}, slavePropName: {7}, slavePropAlias: {8}, extends: {9}>" \
        .format(self.name, self.type, self.comment, self.parent.name if self.parent is not None else u"None", self.isLeader, self.relationshipAlias, self.slaveClass.name if self.slaveClass is not None else u"None", self.slavePropName, self.slavePropAlias, self.extend).strip()

'''
对象定义，对应entity或者一个表名
'''
class PClass(PBase):
    def __init__(self, name = None, autoId = True):
        super(PClass, self).__init__(name, u"class")
        self.properties = []    # 属性列表
        self.propertyIndex = {} # 属性索引表，为方便按属性名获取属性

        self.constraints = [{}, {}, {}]   # 联合键(0为主键,1为索引,2为唯一) 每个元素又是一个字典，以键名作为key

        self.primary = None     # 单一主键

        if autoId:
            prop = PProperty.autoGenerateId()
            self.addProperty(prop)

    def removeProperty(self, name):
        if not name in self.propertyIndex:
            print(u"removeProperty: class \'{0}\' has not property name \'{1}\'".format(self.name, name))
            return
        index = self.propertyIndex[name]
        del self.properties[index]
        # 重新生成属性索引表
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
        if (len(self.constraints[PRIMARY]) > 0):
            print(u"can't add multi primary key; primary key \'{0}\' already exist!".format(self.constraints[PRIMARY].keys()[0]))
            return

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
            print(u"getProperty: class \'{0}\' has not property name \'{1}\'".format(self.name, name))
            return None
    
    def getPrimaryProperty(self):
        '''
        获取单个主键
        '''
        if not self.primary:
            return None
        return self.getProperty(self.primary)

    def setAlias(self, alias):
        if alias:
            self.extend.alias = aliasWord(alias)
    
    def getConstraints(self, index):
        if index >= len(self.constraints) :
            print(u"getConstraints failed for unknown contraint type \'{0}\'".format(index))
            return 
        uniKeys = self.constraints[index]
        return uniKeys

    def getPrimaryConstraints(self):
        return self.getConstraints(PRIMARY)

    def getIndexConstraints(self):
        return self.getConstraints(INDEX)

    def getUniqueConstraints(self):
        return self.getConstraints(UNIQUE)

    def addConstraint(self, index, keyName, prop):
        #print (u"add constraints: {0} {1} {2}".format(index, keyName, prop.name))
        if index >= len(self.constraints) :
            print(u"add contraint failed for unknown contraint type \'{0}\'".format(index))
            return

        uniKeys = self.constraints[index]
        props = []
        if keyName in uniKeys:
            props = uniKeys[keyName]
        else:
            if (index == PRIMARY):
                if (self.primary is not None or len(uniKeys) > 0):
                    print(u"can't add multi primary key for key name \'{0}\'".format(keyName))
                    return
            uniKeys[keyName] = props
        props.append(prop)

    def addPrimaryConstraint(self, keyName, prop):
        self.addConstraint(PRIMARY, keyName, prop)
    
    def addIndexConstraint(self, keyName, prop):
        self.addConstraint(INDEX, keyName, prop)
    
    def addUniqueConstraint(self, keyName, prop):
        self.addConstraint(UNIQUE, keyName, prop)
    
    def removeConstraint(self, index, keyName, prop = None):
        if index >= len(self.constraints) :
            print(u"remove contraint failed for unknown contraint type \'{0}\'".format(index))
            return

        uniKeys = self.constraints[index]
        if keyName in uniKeys:
            if (prop is None):
                del uniKeys[keyName]
            else:
                props = uniKeys[keyName]
                props.remove(prop)
                if len(props) == 0:
                    del uniKeys[keyName]

    def __repr__(self):
        constraints = []
        for c in self.constraints:
            con = {}
            for key, props in c.items():
                names = []
                for prop in props:
                    names.append(prop.name)
                con[key] = names
            constraints.append(con)
            
        return u"PClass<name: {0}, type: {1}, comment: {2}, primary: {3}, alias: {4}, properties: {5}, uniIndex: {6}, uniUnique: {7}, uniPrimary: {8}>"\
        .format(self.name, self.type, self.comment, self.primary, self.extend.alias, self.properties, constraints[INDEX], constraints[UNIQUE], constraints[PRIMARY]).strip()

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

if __name__ == '__main__':
    prop = PProperty(u"name", u"String")
 
    user = PClass(u"User")
    role = PClass(u"Role")

    user.addProperty(prop)
    prop.setIndex()
    
    lprop = PProperty.referenceProperty(u"ManyToMany", user, role, "role", "role_name", "user", "user_id")
    rprop = PProperty.referenceProperty(u"ManyToMany", role, user, "user", "user_id", "role", "role_name")

    user.addProperty(lprop)
    role.addProperty(rprop)
    lprop.setLeader()

    print(user)
    print(role)