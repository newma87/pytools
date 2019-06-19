import os
import re
import codecs

BIN_EXT = [".jar", ".exe", ".png", ".jpeg", ".ico", ".so", ".dll", ".a", ".war"]

class Sword(object):
    def __init__(self):
        self.__LINE__ = 0
        self.__FILE__ = ""
        self.__FUNCS__ = {
            'lowercase': Sword.lowercase,
            'uppercase': Sword.uppercase,
            'split': Sword.split
        }
    @staticmethod
    def lowercase(value):
        return value.lower()

    @staticmethod
    def uppercase(value):
        return value.upper()

    @staticmethod
    def split(value):
        lists = value.split('.')
        return os.path.sep.join(lists)
    
    @staticmethod
    def get_file_extension(filename):
        arr = os.path.splitext(filename)
        return arr[len(arr) - 1]

    @staticmethod
    def copyFile(source, target):
        print("copy file" + source + " to " + target)
        with codecs.open(source, 'rb') as srcFile:
            with codecs.open(target, 'wb') as tarFile:
                while True:
                    content = srcFile.read(1024*1024*10)
                    if len(content) == 0:
                        break
                    tarFile.write(content)

    @staticmethod
    def copyDir(source, target):
        lists = os.listdir(source)
        for item in lists:
            path = os.path.join(source, item)
            tarPath = os.path.join(target, item)
            if os.path.isfile(path):
                Sword.copyFile(path, tarPath)
            else:
                if not os.path.exists(tarPath):
                    os.mkdir(tarPath)
                Sword.copyDir(path, tarPath)

    @staticmethod
    def mkdir(newdir):
        """works the way a good mkdir should :)
            - already exists, silently complete
            - regular file in the way, raise an exception
            - parent directory(ies) does not exist, make them as well
        """
        if os.path.isdir(newdir):
            pass
        elif os.path.isfile(newdir):
            raise OSError("a file with the same name as the desired " \
                        "dir, '%s', already exists." % newdir)
        else:
            head, tail = os.path.split(newdir)
            if head and not os.path.isdir(head):
                Sword.mkdir(head)
            if tail:
                os.mkdir(newdir)

    def registryFunction(self, name, func):
        self.__FUNCS__[name] = func
    
    def getFunction(self, name):
        return self.__FUNCS__[name]

    def getDebugInfo(self):
        return r"file: " + self.__FILE__ + ", line: " + str(self.__LINE__)

    def renderLine(self, line, context):
        def repl(matched):
            value = r""
            try:
                lists = re.split('\||\.', matched.group(1))
                value = context[lists[0].strip()]
                for i in range(1, len(lists)):
                    func = self.getFunction(lists[i].strip())
                    value = func(value)
            except KeyError as err:
                print(r"Parse line \"" + line + "\" encount key error: " + str(err) + ", " + self.getDebugInfo())
            return value
        return re.sub(r'\{\{([a-zA-Z0-9_|\.]+)\}\}', lambda x: repl(x), line)

    def renderFile(self, srcPath, tarPath, context):
        try:
            with codecs.open(srcPath, 'r', encoding = 'utf-8') as srcFile:
                self.__FILE__ = srcPath
                with codecs.open(tarPath, 'w', encoding = 'utf-8') as tarFile:
                    lineNum = 0
                    for line in srcFile:
                        self.__LINE__ = lineNum
                        result = self.renderLine(line, context)
                        tarFile.write(result)
                        #print(result, file=tarFile, end="")
                        lineNum += 1
        except IOError as err:
            print(r'Render file "' + srcPath + '" to "' + tarPath + '" error: ' + str(err))
        
    def renderDir(self, srcDir, tarDir, context):
        def exploreDir(myDir, parent = ""):
            lists = os.listdir(myDir)
            for i in range(0, len(lists)):
                subPath = lists[i]

                path = os.path.join(myDir, subPath)
                tarPath = self.renderLine(os.path.join(tarDir, parent, subPath), context)

                if os.path.isfile(path):
                    if (Sword.get_file_extension(path) in BIN_EXT):
                        Sword.copyFile(path, tarPath)
                    else:
                        print(r"processing file \"" + path + "\"")
                        self.renderFile(path, tarPath, context)
                else:
                    if not os.path.exists(tarPath):
                        Sword.mkdir(tarPath)
                    exploreDir(path, parent = os.path.join(parent, subPath))

        exploreDir(srcDir)

if __name__ == '__main__':
    from main import RandomPassword, RandomSecret
    s = Sword()
    context = {
        'ProjectName':'meeting',
        'Package': 'cn.com.sailfish',
        'Seed': 'sfe966we5t7465a4sdf63e87t3sd'
    }

    s.registryFunction("randomPassword", RandomPassword)
    s.registryFunction("randomSecret", RandomSecret)
    s.renderDir(r"test", r"result", context)
