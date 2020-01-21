package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl22
 * @date 01/01/2020
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    @Override
    public void decompile(IndentPrintStream s) {
        getLOG().debug("LIST DECLVAR");
        for (AbstractDeclVar declVar : getList()) {
            declVar.decompile(s);
            s.println(";");
        }
    }
    
    public void codeGenListDeclVar(DecacCompiler compiler , boolean isLocal){
        for(AbstractDeclVar decl : this.getList()){
            ((DeclVar)decl).codeGenDeclVar(compiler , isLocal);
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("start ListDeclVar::verifyListDeclVariable");
        for(AbstractDeclVar declVar : this.getList()){
            declVar.verifyDeclVar(compiler, localEnv, currentClass);
        }
        LOG.debug("stop ListDeclVar::verifyListDeclVariable");
    }

    void verifyListDeclVariablePass3(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp envExpSup,
                                ClassDefinition currentClass) throws ContextualError {
        LOG.debug("start ListDeclVar::verifyListDeclVariable");
        for(AbstractDeclVar declVar : this.getList()){
            declVar.verifyDeclVarPass3(compiler, envExpSup, envExp, currentClass);
        }
        LOG.debug("stop ListDeclVar::verifyListDeclVariable");
    }
}
