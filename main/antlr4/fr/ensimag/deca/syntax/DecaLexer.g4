lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.
//DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.

COMMENT_LINE: '//' (~('\n'))* {skip();};
COMMENT_BLOCK: '/*' .*? '*/' {skip();};
fragment KEYWORD: ( 'asm'| 'class'| 'extends'| 'else' | 'false' | 'if'
         | 'instanceof' | 'new' | 'null' | 'readInt' | 'readFloat'
         | 'print' | 'println' | 'printlnx' | 'printx' | 'protected'
         | 'return'| 'this' | 'true' |  'while' );
fragment LETTER: ('a'.. 'z'|'A'..'Z');
fragment DIGIT: ('0'..'9');   
OBRACE : '{';
CBRACE : '}';
SEMI : ';';
COMMA : ',';
EQUALS : '=';
PRINT : 'print';
PRINTLN : 'println';
PRINTLNX : 'printlnx';
PRINTX : 'printx';
OPARENT : '(';
CPARENT : ')';
WHILE : 'while';
RETURN : 'return';
IF : 'if';
ELSE : 'else';
OR : '||';
AND : '&&';
EQEQ : '==';
NEQ : '!=';
LEQ : '<=';
GEQ : '>=';
GT : '>';
LT : '<';
INSTANCEOF : 'instanceof';
PLUS : '+';
MINUS : '-';
TIMES : '*';
SLASH : '/';
PERCENT : '%';
EXCLAM : '!';
DOT : '.';
READINT : 'readInt';
READFLOAT : 'readFloat';
NEW : 'new';
TRUE : 'true';
FALSE : 'false';
THIS : 'this';
NULL : 'null';
EXTENDS : 'extends';
CLASS : 'class';
PROTECTED : 'protected';
ASM : 'asm';
IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*  ;
fragment NUM :  DIGIT+;
fragment SIGN :  '+' | '-';
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM DOT NUM;
fragment FLOATDEC : (DEC | DEC EXP)('F' | 'f' | /* epsilon */);
fragment DIGITHEX : DIGIT | 'a' .. 'f' | 'A' .. 'F';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | /* epsilon */);
FLOAT : (FLOATDEC | FLOATHEX);
STRING : '"' ( STRING_CAR| '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR|EOL|'\\"' | '\\\\')* '"';
EOL : ('\n' | '\r'){skip();};
fragment STRING_CAR: ~('\\' | '"');
fragment FILENAME : (LETTER|DIGIT|'.'|'-'|'_')+;
INCLUDE : '#include'(' ')* '"' FILENAME'"';
fragment POSITIVE_DIGIT : '1' .. '9';
INT : '0' | POSITIVE_DIGIT DIGIT* ;
WS  :   ( ' '| '\t'| '\r'| '\n') {
              skip(); // avoid producing a token
          }
    ;
// DEFAULT: .;
//pour reconnaitre les caractères non autorisés dans Deca
// A FAIRE: génération erreur lexicale