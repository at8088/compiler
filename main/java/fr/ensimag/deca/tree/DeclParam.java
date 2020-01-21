/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author ricletm
 */
public class DeclParam extends AbstractDeclParam {
    private AbstractIdentifier type;
    private AbstractIdentifier name;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier name) {
        this.type = type;
        this.name = name;
    }
    
    @Override
    protected Type verifyDeclParam(DecacCompiler compiler) throws ContextualError {
        assert(type != null);
        assert(name != null);
        Type localType = type.verifyType(compiler);
        if (localType.isVoid())   {
            throw new ContextualError("Le type doit être différent de null", type.getLocation());
        }

        ParamDefinition localParam = new ParamDefinition(localType, name.getLocation());
        name.setDefinition(localParam);

        return localType;
    }

    @Override
    protected EnvironmentExp verifyDeclParamPass3(DecacCompiler compiler, EnvironmentExp envExp)
            throws ContextualError {
        assert (type != null);
        assert (name != null);
        Type localType = type.verifyType(compiler);
        envExp.getCurrent().put(name.getName(), name.getExpDefinition());
        return envExp;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
    }
    
}
