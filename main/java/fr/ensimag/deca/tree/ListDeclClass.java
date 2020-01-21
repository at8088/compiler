package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl22
 * @date 01/01/2020
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        assert(getList() != null);
        LOG.debug("verify listClass: start");

        for (AbstractDeclClass declClass: getList()) {
            declClass.verifyClass(compiler);
        }

        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     * @param compiler
     * @throws fr.ensimag.deca.context.ContextualError
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        assert(getList() != null);
        LOG.debug("verify listClassMembers : start");

        for (AbstractDeclClass declClass: getList()) {
            declClass.verifyClassMembers(compiler);
        }

        LOG.debug("verify listClass: end");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        assert(getList() != null);
        LOG.debug("verify listClassBody : start");
        for (AbstractDeclClass declClass: getList()){
            declClass.verifyClassBody(compiler);
        }
        LOG.debug("verify listClassBody : stop");
    }

    public void codeGenListDeclClassMethodTable(DecacCompiler compiler){
        DecacCompiler.superObjet.codeGenMethodTable(compiler);
        for (AbstractDeclClass declClass: this.getList()) {
            ((DeclClass)declClass).codeGenMethodTable(compiler);
        }
    }

    public void codeGenListDeclClassFields(DecacCompiler compiler){

    }


}
