?start: text
text: alias~0..3 [length] [default] special* [comment]
alias: "(" CNAME ")"
length: "[" INT ["," INT] "]"
default: "<" [HEX_DIGIT|CNAME|ESCAPED_STRING] ">"
special: "^" NAME_STR?        -> unique
        | "%" NAME_STR?          -> index
        | "@" NAME_STR?        -> primary
comment: /[^\(\[\<\>\^\@\%].+ | [^ ].+/

HEX_DIGIT: /[a-fA-F0-9]+/
NAME_STR: /[a-fA-F0-9_]+/

%import common.INT
%import common.ESCAPED_STRING
%import common.CNAME
%import common.NEWLINE
%import common.WS

%ignore WS
