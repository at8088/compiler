package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import java.util.HashMap;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl22
 * @date 01/01/2020
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        LOG.setLevel(Level.DEBUG);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        // Definition of env_types_predef
        // A mettre autre part. Indépendant à un fichier.
        
        // Classes TODO -> during pass 1 and 2.
        
        // Passe 1
        // This first environment must contained env_types_predef
        LOG.debug("verify program: pass 1 ...");
        classes.verifyListClass(compiler);
        LOG.debug("verify program: pass 1 OK");

        // Passe 2
        LOG.debug("verify program: pass 2 ...");
        classes.verifyListClassMembers(compiler);
        LOG.debug("verify program: pass 2 OK");

        // Passe 3
        LOG.debug("verify program: pass 3 ...");
        this.classes.verifyListClassBody(compiler);
        this.main.verifyMain(compiler);
        LOG.debug("verify program: pass 3 OK");
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // Construction des tables des étiquettes.
        for (AbstractDeclClass declclass : getClasses().getList()) {
            ((DeclClass)declclass).setExtendClass(getDeclClass(((DeclClass)declclass).getExtend()));
            ((DeclClass)declclass).addLabels();
        }
        compiler.addComment("Construction des tables des méthodes");
        this.getClasses().codeGenListDeclClassMethodTable(compiler);
        compiler.addComment("Main program");
        //codegen main
        main.codeGenMain(compiler);
        // test débordement
        compiler.getProgram().addFirst(new ADDSP(compiler.getDeclVarNbr() + compiler.getIndiceDansPile()));
        if(! compiler.getCompilerOptions().getNoCheck()){
            compiler.getProgram().addFirst(new BOV(new Label("pile_pleine")));
        }
        compiler.getProgram().addFirst(new TSTO(compiler.getDeclVarNbr() + compiler.getIndiceDansPile()
                        + compiler.getParamCpt() + compiler.getSavedRegsCpt() + 2*compiler.getBsrNbr()));
        //codegen init champs et méthodes
        compiler.superObjet.codeGenMethods(compiler);
        for(AbstractDeclClass declClass : getClasses().getList()){
            ((DeclClass)declClass).codeGenFieldsInit(compiler);
            ((DeclClass)declClass).codeGenMethods(compiler);
        }

        if( !compiler.getCompilerOptions().getNoCheck() ){
            this.addStackOverFlowErrorBlock(compiler);
            this.addArithOverFlowBlock(compiler);
            this.addNullPointerDereferencingBlock(compiler);
            this.addInputErrorBlock(compiler);
            this.addHeapFullBlock(compiler);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }

    private AbstractDeclClass getDeclClass(AbstractIdentifier ident ){
        for(AbstractDeclClass declClass : getClasses().getList()){
            if(declClass.toString().equalsIgnoreCase(ident.toString())){
                return  declClass;
            }
        }
        return DecacCompiler.superObjet;
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
    
    private void addStackOverFlowErrorBlock(DecacCompiler compiler){
        compiler.addComment("traitant de l'erreur du débordement de la pile :");
        compiler.addLabel(new Label("pile_pleine"));
        compiler.addInstruction(new WSTR("Erreur : Pile pleine."));
        compiler.addInstruction(new ERROR());
    }
    
    private void addArithOverFlowBlock(DecacCompiler compiler){
        compiler.addComment("traitant de l'erreur du débordement arithmétique :");
        compiler.addLabel(new Label("debordement_arith"));
        compiler.addInstruction(new WSTR("Erreur : Débordement arithmétique"));
        compiler.addInstruction(new ERROR());
    }
    
    private void addNullPointerDereferencingBlock(DecacCompiler compiler){
        compiler.addComment("traitant de l'erreur du déréférencement d'un pointeur null :");
        compiler.addLabel(new Label("dereferencement_null"));
        compiler.addInstruction(new WSTR("Erreur : Déréférencement d'un pointeur null"));
        compiler.addInstruction(new ERROR());
    }

    private void addInputErrorBlock(DecacCompiler compiler) {
        compiler.addComment("traitant de l'erreur d'une entrée invalide");
        compiler.addLabel(new Label("entree_invalide"));
        compiler.addInstruction(new WSTR("Erreur : Débordement arithmétique ou entrée invalide syntaxiquement."));
        compiler.addInstruction(new ERROR());
    }
    private void addHeapFullBlock(DecacCompiler compiler) {
        compiler.addComment("traitant de l'erreur du tas plein");
        compiler.addLabel(new Label("tas_plein"));
        compiler.addInstruction(new WSTR("Erreur : Tas plein"));
        compiler.addInstruction(new ERROR());
    }
    
}
