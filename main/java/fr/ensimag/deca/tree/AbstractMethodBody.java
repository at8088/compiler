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
public abstract class AbstractMethodBody extends Tree {
    // A compléter : Verify...
    public abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExp,
                                 EnvironmentExp EnvExpParam, ClassDefinition currentClass,
                                 Type type) throws ContextualError;
}
