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

/**
 *
 * @author ricletm
 */
public abstract class AbstractDeclMethod extends Tree {
    /**
     * Etape B : A FAIRE
     * @param compiler
     * @param currentClass
     * @param index
     * @return Renvoie l'index suivant
     * @throws fr.ensimag.deca.context.ContextualError
     */    
    protected abstract int verifyDeclMethod(DecacCompiler compiler,
            ClassDefinition currentClass, int index)
            throws ContextualError;

    protected abstract void verifyDeclMethodPass3(DecacCompiler compiler,
                                                  EnvironmentExp envExp, ClassDefinition currentClass)
            throws ContextualError;
}
