# -*- encoding: utf8 -*-

import codecs
from lark import Lark, UnexpectedInput

class GrammerParser:
    def __init__(self, grammarFile = None):
        self.grammar = None
        self.parser = None
        self.tree = None

        if grammarFile:
            self.initFromFile(grammarFile)

    def init(self, grammar):
        self.grammar = grammar
        self.parser = Lark(self.grammar)
        return True

    def initFromFile(self, grammarFile):
        result = False
        message = None
        try: 
            fp = codecs.open(grammarFile, "r", "utf-8")
            result = self.init(fp.read())
        except IOError:
            result = False
            message = u"open file \"" + grammarFile + u"\" failed"
        finally:
            fp.close()

        return result, message

    def loadTree(self, text):
        result = None
        message = None
        try:
            self.tree = self.parser.parse(text)
            result = self.tree
        except UnexpectedInput as ex:
            result = None
            message = ex.message
        return result, message
    
    def loadTreeFromFile(self, textFile):
        result = None
        message = None
        try:
            fp = codecs.open(textFile, "r", "utf-8")
            result, message = self.loadTree(fp.read())
        except IOError:
            result = None
            message = u"open file \"" + textFile + u"\"failed"
        finally:
            fp.close()
        return result, message
