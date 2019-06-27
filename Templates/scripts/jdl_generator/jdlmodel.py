#-*- encoding: utf-8 -*-
from jdlobject import PProperty, PClass, PExtends, PEnum

class TabelModel:
    def __init__(self):
        self.name = None
        self.remark = None
        self.columns = []

        self.indexConstraints = []       # 索引（包含唯一索引）列表
        self.primaryConstraint = []
        self.foreignerConstraints = []   # 外键列表

    def __getName(self, props):
        names = []
        for prop in props:
            names.append(prop.extend.alias)
        return u"_".join(names)

    def mapper(self, classModel):
        self.name = classModel.extend.alias
        self.remark = classModel.comment

        for prop in classModel.properties:
            if prop.relationship is not None:       # 外键属性时
                if prop.isLeader:
                    foreigner = ForeignerModel()
                    foreigner.mapper(prop)
                    self.foreignerConstraints.append(foreigner)
                    if not prop.relationship == "ManyToMany":
                        col = ColumnModel()
                        col.name = foreigner.baseColumn
                        col.baseTabelName = foreigner.tableName
                        col.type = foreigner.baseColumnType
                        col.isNullable = True
                        if prop.relationship == "OneToOne":
                            col.isUnique = True
                        self.columns.append(col)
            else:
                col = ColumnModel()
                col.mapper(prop)
                self.columns.append(col)

        # 唯一索引
        uniques = classModel.getUniqueConstraints()
        for _, props in uniques.items():
            im = IndexModel()
            im.mapper("uk_" + self.__getName(props), props, True)
            self.indexConstraints.append(im)

        # 普通索引    
        indexes = classModel.getIndexConstraints()
        print(u"count: {0}, keys: {1}".format(len(indexes), indexes.keys()))
        for key, props in indexes.items():
            im = IndexModel()
            im.mapper("idx_" + self.__getName(props), props)
            self.indexConstraints.append(im)
            #print (im)
        #print (self.indexConstraints)

        # 多主键
        primaries = classModel.getPrimaryConstraints()
        for _, props in primaries.items():
            pm = PrimaryKeyModel()
            pm.mapper("pk_" + self.__getName(props), props)
            self.primaryConstraint.append(pm)

class PrimaryKeyModel:
    def __init__(self):
        self.tableName = None
        self.refPropsName = None
        self.keyName = None
    
    def mapper(self, name, props):
        if (len(props) > 0):
            self.keyName = name
            self.tableName = props[0].parent.extend.alias
            self.refPropsName = []

            for prop in props:
                self.refPropsName.append(prop.extend.alias)

class IndexModel:
    def __init__(self):
        self.tableName = None   # 关系所在的表
        self.indexName = None   # 索引名字
        self.refProps = None    # 索引关联的属性名列表, 元素为tuple类型(name, type)
        self.unique = False     # 是否为唯一索引

    def mapper(self, name, props, unique = False):
        self.refProps = []
        if (len(props) > 0):
            self.unique = unique
            self.tableName = props[0].parent.extend.alias
            self.indexName = name

            for prop in props:
                if (prop.relationship is None):
                    self.refProps.append((prop.extend.alias, ColumnModel.getColumnType(prop)))
                else:
                    # 当该属性为关系类型时，则它的真实类型为关联外表的主键类型
                    primary = prop.slaveClass.getPrimaryProperty()
                    self.refProps.append((prop.extend.alias, ColumnModel.getColumnType(primary)))
    
    def __repr__(self):
        return u"<tablename: {0} indexName: {1} unique: {2} properties: {3}>".format(self.tableName, self.indexName, self.unique, self.refProps)

class ForeignerModel:
    def __init__(self):
        self.relationship = None            # 关系

        self.tableName = None               # 所在表的表名

        self.baseColumn = None              # 主字段名字
        self.baseColumnType = None            # 主字段的类型
        self.referenceTable = None          # 主字段对应的表
        self.referenceColumn = None         # 主字段对应的表中字段
        self.keyName = None                 # key名

        self.slaveColumn = None             # 从字段名字
        self.slaveColumnType = None         # 从字段类型
        self.slaveColumnRefTable = None     # 从字段对应的表
        self.slaveColumnRefColumn = None    # 从字段对应的表中字段
        self.slaveKeyName = None            # 从关系key名

    def __getKeyName(self, tableName, columnsName):
        return "fk_" + tableName + "_" + columnsName
    
    def mapper(self, prop):
        assert(prop.isLeader)
        otherClass = prop.parent        # prop所对属的类，是另一个字段所对应的类
        baseClass = prop.slaveClass     # prop的对方类，就是本prop所对应的表类

        basePrimary = baseClass.getPrimaryProperty()
        otherPrimary = otherClass.getPrimaryProperty()

        self.relationship = prop.relationship
        if prop.relationship == u"ManyToMany":
            self.tableName = prop.relationshipAlias

            self.baseColumn = prop.extend.alias
            self.baseColumnType = ColumnModel.getColumnType(basePrimary)
            self.referenceTable = baseClass.extend.alias
            self.referenceColumn = basePrimary.extend.alias
            self.keyName = self.__getKeyName(self.tableName, self.baseColumn)

            self.slaveColumn = prop.slavePropAlias
            self.slaveColumnType= ColumnModel.getColumnType(otherPrimary)
            self.slaveColumnRefTable = otherClass.extend.alias
            self.slaveColumnRefColumn = otherPrimary.extend.alias
            self.slaveKeyName = self.__getKeyName(self.tableName, self.slaveColumn)
        elif prop.relationship == u"ManyToOne" or prop.relationship == u"OneToOne":
            self.tableName = prop.parent.extend.alias

            self.baseColumn = prop.extend.alias
            self.baseColumnType = ColumnModel.getColumnType(basePrimary)
            self.referenceTable = baseClass.extend.alias
            self.referenceColumn = basePrimary.extend.alias
            self.keyName = self.__getKeyName(self.tableName, self.baseColumn)
        else: 
            print (u"warning: not supported for relationship \'{0}\'".format(prop.relationship))

class ColumnModel:
    @staticmethod
    def getColumnType(prop):
        '''
        JDL 类型转化为 liquebase 的 xml 类型
        '''
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
            print(u"warnning: unkown type '" + prop.type + "' of property name '" + prop.name + "'")
        return result

    def __init__(self):
        self.baseTabelName = None   # 表名
        self.name = None            # 字段名
        self.type = None            # 字段类型
        self.remarks = None         # 字段注解

        self.default = None         # 默认值
        self.isUnique = None        # 唯一
        self.isNullable = None      # 允空
        self.isPrimary = None       # 主键
        self.isAutoIncrease = None  # 自增长

    def mapper(self, prop):
        self.baseTabelName = prop.parent.name
        self.name = prop.extend.alias
        self.type = ColumnModel.getColumnType(prop)
        self.default = prop.extend.default
        self.isNullable = prop.extend.isNullable
        self.isUnique = prop.extend.isUnique
        self.isAutoIncrease = prop.extend.isAutoIncrease
        self.isPrimary = prop.extend.isPrimary
        self.remarks = prop.comment
