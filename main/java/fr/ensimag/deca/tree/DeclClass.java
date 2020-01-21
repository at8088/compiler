package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl22
 * @date 01/01/2020
 */
public class DeclClass extends AbstractDeclClass {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    private AbstractIdentifier ident;
    private AbstractIdentifier extend;
    private ListDeclField fields;
    private ListDeclMethod methods;
    protected Label[] tableEtiquettes;
    private AbstractDeclClass extendClass;

    public DeclClass(AbstractIdentifier ident, AbstractIdentifier extend, ListDeclField fields, ListDeclMethod methods) {
        this.ident = ident;
        this.extend = extend;
        this.fields = fields;
        this.methods = methods;
    }

    public void setExtendClass(AbstractDeclClass extendClass) {
        this.extendClass = extendClass;
    }

    public AbstractIdentifier getExtend() {
        return extend;
    }

    public AbstractIdentifier getIdent() { return ident; }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        ident.decompile(s);
        if (!(extend instanceof NoExtends)) {
            s.print(" extends ");
            extend.decompile(s);
        }
        s.println(" {");
        s.indent();
        fields.decompile(s);
        methods.decompile(s);
        s.unindent();
        s.println("}");
    }

    public Label[] getTableEtiquettes() {
        return tableEtiquettes;
    }

    public void addLabels() {
        this.tableEtiquettes = new Label[this.ident.getClassDefinition().getNumberOfMethods()];
        for (AbstractDeclMethod method : methods.getList()) {
            MethodDefinition methDef = ((DeclMethod)method).getName().getMethodDefinition();
            tableEtiquettes[methDef.getIndex() - 1] =  methDef.getLabel();
        }
        for(int index = 0 ; index < tableEtiquettes.length ; index ++){
            if(tableEtiquettes[index]  == null){
                tableEtiquettes[index] = ((DeclClass)extendClass).getTableEtiquettes()[index];
            }
        }
    }

    public void codeGenMethodTable(DecacCompiler compiler){
        compiler.addComment("Code de la table des méthodes de la classe "+ this.toString());
        compiler.addInstruction(new LEA(new RegisterOffset(compiler.getCptVTable() , Register.GB) , Register.R0));
        compiler.setCptVTable(compiler.getIndiceDansPile());
        RegisterOffset gbWithOffset = new RegisterOffset(compiler.getIndiceDansPile() , Register.GB);
        compiler.getTableAddrClasses().put(this.toString() , gbWithOffset);
        compiler.addInstruction(new STORE(Register.R0 , gbWithOffset));
        compiler.incIndiceDansPile(1);
        for(Label label : this.tableEtiquettes){
            // ajouter des commentaires pour les méthodes héritées
            gbWithOffset = new RegisterOffset(compiler.getIndiceDansPile() , Register.GB);
            compiler.addInstruction(new LOAD(new LabelOperand(label) , Register.R0));
            compiler.addInstruction(new STORE(Register.R0 ,gbWithOffset ));
            compiler.incIndiceDansPile(1);
        }
    }
    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        assert(extend != null);
        assert(ident != null);

        //passe 1 :
        ClassDefinition superClass = extend.verifyClass(compiler);
        
        compiler.declareClass(ident.getName(), getLocation(), superClass);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError{
        assert(extend != null);
        assert(ident!= null);

        ClassDefinition superClass = extend.verifyClass(compiler);
        ClassDefinition currentClass = ident.verifyClass(compiler);
        
        this.fields.verifyListDeclField(compiler, superClass, currentClass);
        this.methods.verifyListDeclMethod(compiler, superClass, currentClass);
        
        /* affectation */
        // compiler.getEnvTypes().get(extend.getName());
        // compiler.getEnvTypes().get(ident.getName()).
    }

    //passe 3

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        assert(extend != null);
        assert(ident != null);
        LOG.debug("DeclClass::verifyClassBody start");

        ClassDefinition superClass = extend.verifyClass(compiler);
        ClassDefinition currentClass = ident.verifyClass(compiler);

        fields.verifyListDeclFieldPass3(compiler, superClass.getMembers(), currentClass);
        methods.verifyListDeclMethodPass3(compiler, superClass.getMembers(), currentClass);

        LOG.debug("DeclClass::verifyClassBody stop");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, false);
        extend.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        ident.iter(f);
        extend.iter(f);
        fields.iter(f);
        methods.iter(f);
    }

    @Override
    public String toString(){
        return this.ident.toString();
    }

    public void codeGenFieldsInit(DecacCompiler compiler){
        compiler.addLabel(new Label("init."+this.toString()));
        if (extendClass instanceof SuperObject) {
            compiler.addInstruction(new LOAD(new RegisterOffset(-2 , Register.LB) , Register.R1));
            for (AbstractDeclField declField : this.fields.getList()) {
                ((DeclField)declField).codeGenField(compiler);
            }
        } else {

            compiler.addInstruction(new TSTO(this.ident.getClassDefinition().getNumberOfFields() + 2*compiler.getBsrNbr() +
                    compiler.getParamCpt() +  compiler.getIndiceDansPile()));
            compiler.addInstruction(new BOV(new Label("pile_pleine")));
            compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB) , Register.R1));
            for (AbstractDeclField declField : this.fields.getList()) {
                ((DeclField)declField).codeGenInitZero(compiler);
                compiler.addInstruction(new STORE(Register.R0 , new RegisterOffset( ((DeclField)declField).getField().getFieldDefinition().getIndex(),Register.R1)));
            }
            compiler.addInstruction(new PUSH(Register.R1));
            compiler.incSavedRegsCpt();
            compiler.addInstruction(new BSR(new Label("init."+this.extend.toString())));
            compiler.incBsrNbr();
            compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));
            compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB) , Register.R1));
            for (AbstractDeclField declField : this.fields.getList()) {
                ((DeclField)declField).codeGenInitExplicite(compiler);
            }
        }
        compiler.addInstruction(new RTS());
    }

    public void codeGenMethods(DecacCompiler compiler )  {
        for(AbstractDeclMethod declMethod : this.methods.getList()){
            ((DeclMethod)declMethod).codeGenMethod(compiler ,this.toString());
        }
    }
}
