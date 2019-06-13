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
        #print(u"comment {0}".format(args[0].value))
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
            primary = False
            comment = None
            for ch in tree.children:
                if ch.data == "alias":
                    alias.append(ch.children[0].value)
                elif ch.data == "length":
                    for t in ch.children:
                        length.append(t.value)
                elif ch.data == "default":
                    nullable = False
                    if len(ch.children) > 0:
                        default = ch.children[0].value
                elif ch.data == "unique":
                    unique = True
                elif ch.data == "primary":
                    primary = True
                elif ch.data == "comment":
                    comment = ch.children[0].value
            self.context.addComment(alias, length, default, nullable, unique, primary, comment, args[0])

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
