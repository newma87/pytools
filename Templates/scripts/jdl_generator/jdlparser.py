# -*- encoding: utf8 -*-
import codecs
from jdlcontext import ParserContext
from lark import Tree
from grammar import GrammerParser

class JDLParser:
    def __init__(self, commentGrammar):
        self.context = ParserContext()
        self.commentGrammar = commentGrammar

    def getClasses(self):
        return self.context.classes

    def codeGenerate(self, generator):
        generator.generate(self.context.classes.values())

    def __default__(self, args):
        pass

    def parse(self, tree):
        getattr(self, tree.data, self.__default__)(tree.children)

        for sub in tree.children:
            if isinstance(sub, Tree):
                self.parse(sub)

    ########################### Tree Process #######################################

    def object(self, args):
        #print(u"Object {0}".format(args[1].value))
        self.context.addObject(args[0], args[1])

    def comment(self, args):
        gp = GrammerParser(self.commentGrammar)
        tree, message = gp.loadTree(args[0].value)
        if tree == None: # 解析注释失败时，把注释完整地放到comment中
            print(u'parseing comment(line:{0}, col:{1}) error: {2}'.format(args[0].line, args[0].column, message))
            self.context.addComment(comment=args[0].value)
        else:
            alias = []
            length = []
            default = None
            nullable = True
            unique = False
            uniqueName = None
            index = False
            indexName = None
            primary = False
            primaryName = None
            comment = None
            for extends in tree.children:
                for child in extends.children:
                    if child.data == "words":
                        item = child.children[0]
                        if (item.data == "alias"):
                            alias.append(item.children[0].value)
                        elif (item.data == "length"):
                            length.append(item.children[0].value)
                            if (len(item.children) > 1):
                                length.append(item.children[1].value)
                        elif (item.data == "default"):
                            nullable = False
                            if (len(item.children) > 0):
                                default = item.children[0].value
                        elif (item.data == "unique"):
                            unique = True
                            if (len(item.children) > 0):
                                uniqueName = item.children[0].value
                        elif (item.data == "primary"):
                            primary = True
                            if (len(item.children) > 0):
                                primaryName = item.children[0].value
                        elif (item.data == "index"):
                            index = True
                            if (len(item.children) > 0):
                                indexName = item.children[0].value
                    elif child.data == "comment":
                        if (len(child.children) > 0):
                            comment = child.children[0].value
            #print(alias, length, default, nullable, unique, uniqueName, index, indexName, primary, primaryName, comment)
            self.context.addComment(alias, length, default, nullable, unique, uniqueName, index, indexName, primary, primaryName, comment, args[0])

    def property(self, args):
        #print(u"property {0}".format(args[0].value))
        self.context.addProperty(args[0], args[1])
    
    def relation(self, args):
        left = args[0]
        leftName = left.children[0]
        leftProp = None
        if len(left.children) > 1:
            leftProp = left.children[1]
        right = args[1]
        rightName = right.children[0]
        rightProp = None
        if len(right.children) > 1:
            rightProp = right.children[1]
        self.context.addRelation(leftName, leftProp, rightName, rightProp)
    
    def enum(self, args):
        value = None
        if args[1].type == u"DIGIT":
            value = args[1]
        self.context.addEnum(args[0], value)
