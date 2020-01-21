/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author ricletm
 */
public class ListDeclParam extends TreeList<AbstractDeclParam>{

    @Override
    public void decompile(IndentPrintStream s) {
        boolean firstParam = true;
        for (AbstractDeclParam declParam : getList()) {
            if (firstParam) {
                firstParam = false;
            } else {
                s.print(", ");
            }
            declParam.decompile(s);
        }
    }
    
    public Signature verifyListParam(DecacCompiler compiler) 
            throws ContextualError {
        assert(getList() != null);
        Signature sign = new Signature();
        for ( AbstractDeclParam declParam : getList())  {
            Type localType = declParam.verifyDeclParam(compiler);
            sign.add(localType);
        }
        return sign;
    }

    public EnvironmentExp verifyListParamPass3(DecacCompiler compiler)
            throws ContextualError {
        assert(getList() != null);
        EnvironmentExp envExp = new EnvironmentExp(null);
        for ( AbstractDeclParam declParam : getList())  {
            declParam.verifyDeclParamPass3(compiler, envExp);
        }
        return envExp;
    }

}
