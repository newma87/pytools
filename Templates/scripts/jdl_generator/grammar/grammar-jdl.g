start: object+
?object: KEY_WORD CNAME body
body: "{" [comment] field* "}"
field: CNAME KEY_TYPE [","] [comment] WS            -> property
    |  reference "to" reference [","] [comment] WS  -> relation
    |  ENUM_VALUE ["=" DIGIT] [","] [comment] WS      -> enum
reference: CNAME [column]
?column: "{" CNAME "}" 
comment: "//" LINE

ENUM_VALUE: /[A-Z]+/
LINE: /(.)+/
KEY_WORD: "entity"
        | "relationship"
        | "enum"
KEY_TYPE: "String"
        | "Long"
        | "Integer"
        | "Instant"
        | "Boolean"
        | "Float"
        | "Double"
        | "TextBlob"
        | "Blob"
        | "BigDecimal"
NEW_LINE: (CR?LF)+

%import common.ESCAPED_STRING
%import common.INT
%import common.CR
%import common.LF
%import common.CNAME
%import common.DIGIT
%import common.WS

%ignore WS