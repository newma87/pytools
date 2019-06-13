?start: text
text: alias~0..3 [length] [default] special* [comment]
alias: "(" CNAME ")"
length: "[" INT ["," INT] "]"
default: "<" [HEX_DIGIT|CNAME|ESCAPED_STRING] ">"
special: "^"        -> unique
        | "@"       -> primary
comment: /[^\(\[\<\^\@ ].+/

HEX_DIGIT: /[a-fA-F0-9]+/

%import common.INT
%import common.ESCAPED_STRING
%import common.CNAME
%import common.NEWLINE
%import common.WS

%ignore WS
