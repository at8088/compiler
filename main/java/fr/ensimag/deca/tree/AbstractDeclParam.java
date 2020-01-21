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
import fr.ensimag.deca.context.Type;

/**
 *
 * @author ricletm
 */
public abstract class AbstractDeclParam extends Tree {
    /**
     * Etape B : A FAIRE
     * @param compiler
     * @throws fr.ensimag.deca.context.ContextualError
     */    
    protected abstract Type verifyDeclParam(DecacCompiler compiler)
            throws ContextualError;

    protected abstract EnvironmentExp verifyDeclParamPass3(DecacCompiler compiler, EnvironmentExp envExp)
            throws ContextualError;
}
