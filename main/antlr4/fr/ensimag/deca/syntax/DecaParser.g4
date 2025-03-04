parser grammar DecaParser;

options {
    // Default language but name it anyway
    //
    language  = Java;

    // Use a superclass to implement all helper
    // methods, instance variables and overrides
    // of ANTLR default methods, such as error
    // handling.
    //
    superClass = AbstractDecaParser;

    // Use the vocabulary generated by the accompanying
    // lexer. Maven knows how to work out the relationship
    // between the lexer and parser and will build the
    // lexer before the parser. It will also rebuild the
    // parser if the lexer changes.
    //
    tokenVocab = DecaLexer;

}

// which packages should be imported?
@header {
    import fr.ensimag.deca.tree.*;
    import java.io.PrintStream;
    import fr.ensimag.deca.tools.SymbolTable ;
    import fr.ensimag.deca.tools.SymbolTable.Symbol;
    import fr.ensimag.deca.DecacCompiler;
}

@members {
    private SymbolTable sTable = new SymbolTable();      
    @Override
    protected AbstractProgram parseProgram() {
        return prog().tree;
    }
    
    public String getFileName(){
        if (this.getDecacCompiler().getSource() != null) {
            return this.getDecacCompiler().getSource().getName();
        } else {
            return "";
        }
    }
}

prog returns[AbstractProgram tree]
    : list_classes main EOF {
            assert($list_classes.tree != null);
            assert($main.tree != null);
            $tree = new Program($list_classes.tree, $main.tree);
            setLocation($tree, $list_classes.start);
        }
    ;

main returns[AbstractMain tree]
    : /* epsilon */ {
            $tree = new EmptyMain();
        }        
    | block {
            assert($block.decls != null);
            assert($block.insts != null);
            $tree = new Main($block.decls, $block.insts);
            setLocation($tree, $block.start);
        }
    ;

block returns[ListDeclVar decls, ListInst insts]
    : OBRACE list_decl list_inst CBRACE {
            assert($list_decl.tree != null);
            assert($list_inst.tree != null);
            $decls = $list_decl.tree;
            $insts = $list_inst.tree;
        }
    ;

list_decl returns[ListDeclVar tree]
@init   {
            $tree = new ListDeclVar();
        }
    : decl_var_set[$tree]*
    ;

decl_var_set[ListDeclVar l]
    : type list_decl_var[$l,$type.tree] SEMI
    ;

list_decl_var[ListDeclVar l, AbstractIdentifier t]
    : dv1=decl_var[$t] {
        $l.add($dv1.tree);
        } (COMMA dv2=decl_var[$t] {
            $l.add($dv2.tree);
        }
      )*
    ;

decl_var[AbstractIdentifier t] returns[AbstractDeclVar tree]
    @init {
            Initialization init = null;
        }
    : i=ident {
            
        }
      (EQUALS e=expr {
             init = new Initialization($e.tree);
             Location initLoc = new Location($EQUALS.getLine() , $EQUALS.getCharPositionInLine() , getFileName());
             init.setLocation(initLoc);
        }
      )? {
            if(init == null){
                $tree = new DeclVar($t , $i.tree , new NoInitialization());
            }else{
                $tree = new DeclVar($t , $i.tree , init);
            }
            setLocation($tree,$ident.start);
        }
    ;

list_inst returns[ListInst tree]
@init {
        $tree = new ListInst();
        
    }
    : (inst {
            assert($inst.tree != null); 
            $tree.add($inst.tree);
            
        }
      )*
    ;

inst returns[AbstractInst tree]
    : e1=expr SEMI {
            assert($e1.tree != null);
            $tree = $e1.tree;
            setLocation($tree , $expr.start); 
        }
    | SEMI {
            $tree = new NoOperation();
            Location loc = new Location($SEMI.getLine(),$SEMI.getCharPositionInLine(),getFileName());
            $tree.setLocation(loc);
        }
    | PRINT OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(false, $list_expr.tree );
            Location loc = new Location($PRINT.getLine(),$PRINT.getCharPositionInLine(),getFileName());
            $tree.setLocation(loc);
        }
    | PRINTLN OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(false,$list_expr.tree);
            Location locPrntln = new Location($PRINTLN.getLine(),$PRINTLN.getCharPositionInLine(),getFileName());
            $tree.setLocation(locPrntln);
        }
    | PRINTX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(true,$list_expr.tree);
            Location loc = new Location($PRINTX.getLine(),$PRINTX.getCharPositionInLine(),getFileName());
            $tree.setLocation(loc);
        }
    | PRINTLNX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(true,$list_expr.tree);
            Location loc = new Location($PRINTLNX.getLine(),$PRINTLNX.getCharPositionInLine(),getFileName());
            $tree.setLocation(loc);
        }
    | if_then_else {
            assert($if_then_else.tree != null);
            $tree = $if_then_else.tree;
            setLocation($tree , $if_then_else.start);
        }
    | WHILE OPARENT condition=expr CPARENT OBRACE body=list_inst CBRACE {
            assert($condition.tree != null);
            assert($body.tree != null);
            $tree = new While($condition.tree, $body.tree);
            Location loc = new Location($WHILE.getLine(),$WHILE.getCharPositionInLine(),getFileName());
            $tree.setLocation(loc);
        }
    | RETURN expr SEMI {
            assert($expr.tree != null);
            $tree = new Return($expr.tree);
            setLocation($tree , $expr.start);
        }
    ;

if_then_else returns[IfThenElse tree]
@init {
       // on stockera dans elseBlock la liste des inst des blocks else du premier if
       // et des éventuels elseif.
       ListInst elseBlock = new ListInst();
       // lastIf sert à pointer vers la list des inst du dernier block ifthenelse
       ListInst lastIf = null;	
    }
    : if1=IF OPARENT condition=expr CPARENT OBRACE li_if=list_inst CBRACE {
            // si on reconnait un if then on crée un noeud ifthen else
            // avec un block else vide
            $tree = new IfThenElse($condition.tree, $li_if.tree, elseBlock);
            Location if1Loc = new Location($if1.getLine(),
                                    $if1.getCharPositionInLine(), getFileName());
            $tree.setLocation(if1Loc);
            lastIf = elseBlock;
        }
      (ELSE elsif=IF OPARENT elsif_cond=expr CPARENT OBRACE elsif_li=list_inst CBRACE {
            Location elsifLoc = new Location($elsif.getLine(),
                    $elsif.getCharPositionInLine(), getFileName());
            $tree.addElseIf($elsif_cond.tree, $elsif_li.tree, elsifLoc);
        }
      )*
      (ELSE OBRACE li_else=list_inst CBRACE {
            lastIf.setList($list_inst.tree.getList());
        }
      )?
    ;

list_expr returns[ListExpr tree]
@init   {
            $tree = new ListExpr();
        }
    : (e1=expr {
            assert( $e1.tree != null );
            $tree.add($e1.tree);
            setLocation($tree , $e1.start);
        }
       (COMMA e2=expr {
            assert( $e2.tree != null); 
            $tree.add($e2.tree);
            setLocation($e2.tree , $e2.start);
        }
       )* )?
    ;

expr returns[AbstractExpr tree]
    : assign_expr {
            assert($assign_expr.tree != null);
            $tree = $assign_expr.tree ; 
            setLocation($tree , $assign_expr.start);
        }  
    ;

assign_expr returns[AbstractExpr tree]
    : e=or_expr (
        /* condition: expression e must be a "LVALUE" */ {
            
            if (! ($e.tree instanceof AbstractLValue)) {
                throw new InvalidLValue(this, $ctx);
            }
        }
        EQUALS e2=assign_expr {
            assert($e.tree != null);
            assert($e2.tree != null);
            $tree = new Assign((AbstractLValue)$e.tree , $e2.tree);
            setLocation($tree , $e2.start);
        }
      | /* epsilon */ {
            assert($e.tree != null);
            $tree = $e.tree ;
            setLocation($tree , $e.start);
        }
      )
    ;

or_expr returns[AbstractExpr tree]
    : e=and_expr {
            assert($e.tree != null);
            $tree = $e.tree ;
            setLocation($tree , $e.start);
        }
    | e1=or_expr OR e2=and_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Or($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
       }
    ;

and_expr returns[AbstractExpr tree]
    : e=eq_neq_expr {
            assert($e.tree != null);
            $tree = $e.tree ;
            setLocation($tree , $e.start);
        }
    |  e1=and_expr AND e2=eq_neq_expr {
            assert($e1.tree != null);                         
            assert($e2.tree != null);
            $tree = new And($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
        }
    ;

eq_neq_expr returns[AbstractExpr tree]
    : e=inequality_expr {
            assert($e.tree != null);
            $tree = $e.tree ;
        }
    | e1=eq_neq_expr EQEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Equals($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
        }
    | e1=eq_neq_expr NEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new NotEquals($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
        }
    ;

inequality_expr returns[AbstractExpr tree]
    : e=sum_expr {
            assert($e.tree != null);
            $tree = $e.tree ;
        }
    | e1=inequality_expr LEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new LowerOrEqual($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
        }
    | e1=inequality_expr GEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new GreaterOrEqual($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
        }
    | e1=inequality_expr GT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Greater($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
        }
    | e1=inequality_expr LT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Lower($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
        }
    | e1=inequality_expr INSTANCEOF type {
            assert($e1.tree != null);
            assert($type.tree != null);
            //$tree = new Equals($e1.tree.get , $e2.tree);
            //setLocation($tree , $e1.start);
        }
    ;


sum_expr returns[AbstractExpr tree]
    : e=mult_expr {
            assert($e.tree != null);
            $tree = $e.tree ;
        }
    | e1=sum_expr PLUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Plus($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
            
        }
    | e1=sum_expr MINUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Minus($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
        }
    ;

mult_expr returns[AbstractExpr tree]
    : e=unary_expr {
            assert($e.tree != null);
            $tree = $e.tree ;
        }
    | e1=mult_expr TIMES e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Multiply($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
        }
    | e1=mult_expr SLASH e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Divide($e1.tree , $e2.tree);
            setLocation($tree , $e1.start);
        }
    | e1=mult_expr PERCENT e2=unary_expr {
            assert($e1.tree != null);                                                                          
            assert($e2.tree != null);
             $tree = new Modulo($e1.tree , $e2.tree); 
            setLocation($tree , $e1.start);
        }
    ;

unary_expr returns[AbstractExpr tree]
    : op=MINUS e=unary_expr {
            assert($e.tree != null);
            $tree = new UnaryMinus($e.tree);
            setLocation($tree , $e.start);
        }
    | op=EXCLAM e=unary_expr {
            assert($e.tree != null);
            $tree = new Not($e.tree);
            setLocation($tree , $e.start);
        }
    | select_expr {
            assert($select_expr.tree != null);
            $tree = $select_expr.tree;
            setLocation($tree , $select_expr.start);
        }
    ;

select_expr returns[AbstractExpr tree]
    : e=primary_expr {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree , $e.start);
        }
    | e1=select_expr DOT i=ident {
            assert($e1.tree != null);
            assert($i.tree != null);
        }
        (o=OPARENT args=list_expr CPARENT {
            // we matched "e1.i(args)" // Method call ?
            assert($args.tree != null);
            $tree = new MethodCall($e1.tree, $i.tree, $args.tree);
            setLocation($tree, $e1.start);
            
        }
        | /* epsilon */ {
            // we matched "e.i"
            $tree = new Selection($e1.tree, $i.tree);
            setLocation($tree, $e1.start);
        }
        )
    ;

primary_expr returns[AbstractExpr tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
            setLocation($tree , $ident.start);
        }
    | m=ident OPARENT args=list_expr CPARENT {
            assert($args.tree != null);
            assert($m.tree != null);
            $tree = new MethodCall(new This(), $m.tree, $list_expr.tree);
            setLocation($tree, $m.start);
        }
    | OPARENT expr CPARENT {
            assert($expr.tree != null);
            $tree = $expr.tree;
            setLocation($tree , $expr.start);
        }
    | READINT OPARENT CPARENT {
            $tree = new ReadInt();
            Location readintLoc = new Location($READINT.getLine() , 
                                 $READINT.getCharPositionInLine(),getFileName());
            $tree.setLocation(readintLoc);
        }
    | READFLOAT OPARENT CPARENT {
            $tree = new ReadFloat();
            Location readfloatLoc = new Location($READFLOAT.getLine() ,
                                 $READFLOAT.getCharPositionInLine(),getFileName());
            $tree.setLocation(readfloatLoc);
        }
    | NEW i=ident OPARENT CPARENT {
            assert($ident.tree != null);
            $tree = new New($i.tree);
            setLocation($tree, $i.start);
        }
    | cast=OPARENT type CPARENT OPARENT expr CPARENT {
            assert($type.tree != null);
            assert($expr.tree != null);
            // A FAIRE
        }
    | literal {
            assert($literal.tree != null);
            $tree = $literal.tree;
            setLocation($tree , $literal.start);
        }
    ;

type returns[AbstractIdentifier tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
            setLocation($tree , $ident.start);
        }
    ;

literal returns[AbstractExpr tree]
    : INT {
            $tree = new IntLiteral(Integer.parseInt($INT.text));
            Location locInt = new Location($INT.getLine(),
                                            $INT.getCharPositionInLine(),getFileName());
            $tree.setLocation(locInt);
        }
    | FLOAT {
            $tree = new FloatLiteral(Float.parseFloat($FLOAT.getText()));
            Location locFloat = new Location($FLOAT.getLine(), $FLOAT.getCharPositionInLine(),getFileName() );
            setLocation($tree, $FLOAT);
        }
    | STRING {
            String str = $STRING.getText();
            $tree = new StringLiteral(str.substring(1, str.length() - 1)); 
            Location locString = new Location($STRING.getLine(),
                                    $STRING.getCharPositionInLine(),getFileName());
            $tree.setLocation(locString);
        }
    | TRUE {
            $tree = new BooleanLiteral(true);
            Location locTrue = new Location($TRUE.getLine(),
                                    $TRUE.getCharPositionInLine(),getFileName());
            $tree.setLocation(locTrue);
        }
    | FALSE {
            $tree = new BooleanLiteral(false);
            Location locFalse = new Location($FALSE.getLine(),
                                    $FALSE.getCharPositionInLine(),getFileName());
            $tree.setLocation(locFalse);
        }
    | THIS {
            $tree = new This();
            Location locThis = new Location($THIS.getLine(),
                                    $THIS.getCharPositionInLine(), getFileName());
            $tree.setLocation(locThis);
        }
    | NULL {
            $tree = new NullLiteral(); 
            Location locNull = new Location($NULL.getLine(),
                                    $NULL.getCharPositionInLine(),getFileName());
            $tree.setLocation(locNull);
        }
    ;

ident returns[AbstractIdentifier tree]
    : IDENT {
            SymbolTable t = new SymbolTable();
            $tree = new Identifier(t.create($IDENT.getText()));
            Location identLoc = new Location($IDENT.getLine() , 
                                   $IDENT.getCharPositionInLine() , getFileName());
            $tree.setLocation(identLoc);
        }
    ;

/****     Class related rules     ****/

list_classes returns[ListDeclClass tree]
@init   {
            $tree = new ListDeclClass();
        }
    :  (class_decl {
                    assert($class_decl.tree != null);
                    $tree.add($class_decl.tree);
                }
       )*
    ;

class_decl returns[AbstractDeclClass tree]
    : CLASS name=ident superclass=class_extension OBRACE body=class_body CBRACE {
            assert($name.tree != null);
            //assert($superclass.tree != null);
            assert($body.treeField != null);
            assert($body.treeMethod != null);
            $tree = new DeclClass($ident.tree, $superclass.tree, $body.treeField, $body.treeMethod);
            setLocation($tree, $name.start);
        }
    ;

class_extension returns[AbstractIdentifier tree]
    : EXTENDS i=ident {
            assert($i.tree != null);
            $tree = $i.tree;
            setLocation($tree, $i.start);
        }
    | /* epsilon */ {
            $tree = DecacCompiler.superObjet.getIdent();
            //$tree = new NoExtends();
            $tree.setLocation(Location.BUILTIN);
        }
    ;

class_body returns[ListDeclField treeField, ListDeclMethod treeMethod]
@init {
        $treeField = new ListDeclField();
        $treeMethod = new ListDeclMethod();
    }
    : (m=decl_method {
            assert($m.tree != null);
            $treeMethod.add($m.tree);
        }
      | decl_field_set[$treeField]
      )*
    ;

decl_field_set[ListDeclField list]
    : v=visibility t=type list_decl_field[$list, $v.tree, $t.tree]
      SEMI
    ;

visibility returns[Visibility tree]
    : /* epsilon */ {
            $tree = Visibility.PUBLIC;
        }
    | PROTECTED {
            $tree = Visibility.PROTECTED;
        }
    ;

list_decl_field[ListDeclField list, Visibility visi, AbstractIdentifier typ]
// Voir pour visibilité
    : dv1=decl_field[$visi, $typ] {
            assert($dv1.tree != null);
            $list.add($dv1.tree);
        }(COMMA dv2=decl_field[$visi, $typ] {
            assert($dv2.tree != null);
            $list.add($dv2.tree);
        }
      )*
    ;

decl_field[Visibility visi, AbstractIdentifier typ] returns[AbstractDeclField tree]
@init {
        Initialization init = null;
    }
    : i=ident {
            assert($i.tree != null);
        }
      (EQUALS e=expr {
            assert($e.tree != null);
            init = new Initialization($e.tree);

        }
      )? {
            if (init == null) {
                $tree = new DeclField($visi, $typ, $i.tree, new NoInitialization());
            } else {
                $tree = new DeclField($visi, $typ, $i.tree, init);
                setLocation(init, $e.start);
            }
            setLocation($tree, $i.start);
        }
    ;

decl_method returns[AbstractDeclMethod tree]
@init {
        AbstractMethodBody mbody;
        boolean isString = false;
    }
    : t=type i=ident OPARENT params=list_params CPARENT (b=block {
            assert($t.tree != null);
            assert($i.tree != null);
            assert($params.tree != null);
            assert($b.insts != null);
            assert($b.decls != null);
            mbody = new MethodBody($b.decls, $b.insts);
        }
      | ASM OPARENT code=multi_line_string CPARENT SEMI {
            assert($t.tree != null);
            assert($i.tree != null);
            assert($params.tree != null);
            assert($code.text != null);
            mbody = new MethodAsmBody(new StringLiteral($code.text));
            isString = true;
        }
      ) {
            $tree = new DeclMethod($t.tree, $i.tree, $params.tree, mbody);
            if (isString) {
                setLocation($tree, $code.start);
                setLocation(mbody, $code.start);
            } else {
                setLocation($tree, $type.start);
                setLocation(mbody, $b.start);
            }
        }
    ;

list_params returns[ListDeclParam tree]
@init {
        $tree = new ListDeclParam();
    }
    : (p1=param {
            assert($p1.tree != null);
            $tree.add($p1.tree);
        } (COMMA p2=param {
            assert($p2.tree != null);
            $tree.add($p2.tree);
        }
      )*)?
    ;
    
multi_line_string returns[String text, Location location]
    : s=STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    | s=MULTI_LINE_STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    ;

param returns[AbstractDeclParam tree]
    : t=type i=ident {
            assert($t.tree != null);
            assert($i.tree != null);
            $tree = new DeclParam($t.tree, $i.tree);
            setLocation($tree, $t.start);
        }
    ;
