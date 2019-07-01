?start: text
text: extends [" "+ extends] [LINE_END]
extends: words+ | comment
words: alias
     | length  
     | default 
     | unique 
     | index 
     | primary
alias: "(" CNAME ")"
length: "[" INT ["," INT] "]"
default: "<" [SIGNED_NUMBER|CNAME|ESCAPED_STRING] ">"
unique: "^" [NAME_STR]
index: "%" [NAME_STR] 
primary: "@" [NAME_STR]
comment: /[^\(\)\[\]\<\>\^\%\@\s][^\r\n|.]+/

NAME_STR: /[a-fA-F0-9_]+/

LINE_END: /[\r]+/

%import common.INT
%import common.SIGNED_NUMBER
%import common.ESCAPED_STRING
%import common.CNAME
%import common.WS

%ignore WS
