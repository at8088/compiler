/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author ricletm
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod>{

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.decompile(s);
            s.println();
        }
    }
    
    public void verifyListDeclMethod(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition currentClass) throws ContextualError {
        
        EnvironmentExp localEnv = new EnvironmentExp(null);
        int index = 1;
        for (AbstractDeclMethod declMethod : getList()) {
            currentClass.incNumberOfMethods();
            index = declMethod.verifyDeclMethod(compiler, currentClass, index);
        }
    }

    public void verifyListDeclMethodPass3(DecacCompiler compiler, EnvironmentExp envExp, ClassDefinition currentClass) throws ContextualError {

        for (AbstractDeclMethod declMethod : getList())    {
            declMethod.verifyDeclMethodPass3(compiler, envExp, currentClass);
        }
    }
    
}
