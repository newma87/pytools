# -*- encoding: utf8 -*-
import os
from grammar import GrammerParser
from jdlparser import JDLParser
from generator import JAVACodeGenerator, LiquiBaseGenerator

import argparse

def usage():
    parser = argparse.ArgumentParser(description='Auto generate java jpa code and liquibase xml changelog from jdl language script')
    parser.add_argument('jdlFile', help='Path to jdl script file')
    parser.add_argument('-w', '--work-dir', help = "Save generated code directory")
    parser.add_argument('-p', '--package', help = "JPA code package")
    parser.add_argument('-t', '--template', help = "Template file store path")
    parser.add_argument('-g', "--grammar", help = "Grammar file store path")
    parser.add_argument('-r', "--repository", help = "Generator repository file", action = "store_true")
    parser.add_argument('-x', "--liquibase-xml", help = "Generator liquibase xml file", action = "store_true")
    parser.add_argument('-T', '--tag', help="Prefix tag for xml file name")
    arg = parser.parse_args()
    return arg

def main():
    args = usage()

    grammarDir = args.grammar if args.grammar else "grammar"
    jdlGrammar = os.path.join(grammarDir, u"grammar-jdl.g")
    commentGrammar = os.path.join(grammarDir, u"grammar-extend.g")
    grammer = GrammerParser(jdlGrammar)

    tag = args.tag
    if (tag is None):
        _, tail = os.path.split(args.jdlFile)
        tag = tail.split('.')[0]
    tree, message = grammer.loadTreeFromFile(args.jdlFile)
    if not tree:
        print(message)
        return

    # parser jdl file with grammar
    parser = JDLParser(commentGrammar)
    parser.parse(tree)

    # generator java code
    generator = JAVACodeGenerator(args.work_dir if args.work_dir else ".", args.template if args.template else "template", args.package if args.package else "cn.com.sailfish", args.repository)
    parser.codeGenerate(generator)

    # generator liquibase xml code
    if args.liquibase_xml:
        xmlGenerator = LiquiBaseGenerator(args.work_dir if args.work_dir else ".", args.template if args.template else "template", tag = tag)
        parser.codeGenerate(xmlGenerator)

if __name__ == '__main__':
    main()