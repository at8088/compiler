package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author gl22
 * @date 01/01/2020
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    private ListDeclVar declVariables;
    private ListInst insts;
    
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        LOG.setLevel(Level.OFF);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        EnvironmentExp localEnv = new EnvironmentExp(null);
        this.declVariables.verifyListDeclVariable(compiler, localEnv
                , null /* ICI METTRE L'ENVIRONEMENT DES CLASSES */);
        // Main renvoie un void
        this.insts.verifyListInst(compiler, localEnv, null, 
                compiler.getTypeFromString(DecacCompiler.VOID));
        LOG.debug("verify Main: end");
        
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler){
        compiler.addComment("Variable declarations and initializations:");
        this.declVariables.codeGenListDeclVar(compiler , false);
        compiler.addComment("Beginning of main instructions:");
        insts.codeGenListInst(compiler);
        compiler.addComment("End of main instructions.");
        compiler.addInstruction(new HALT());
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
    

}
