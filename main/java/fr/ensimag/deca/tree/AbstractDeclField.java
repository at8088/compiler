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
import fr.ensimag.deca.context.FieldDefinition;

/**
 *
 * @author ricletm
 */
public abstract class AbstractDeclField extends Tree {
    /**
     * Etape B : A FAIRE
     *
     * @param compiler
     * @param superClass
     * @param currentClass
     * @param index
     * @throws fr.ensimag.deca.context.ContextualError
     */
    protected abstract void verifyDeclField(DecacCompiler compiler,
                                            ClassDefinition superClass, ClassDefinition currentClass, int index)
            throws ContextualError;


    public abstract void verifyDeclFieldPass3(DecacCompiler compiler, EnvironmentExp envExp,
                                     ClassDefinition currentClass) throws ContextualError;

}