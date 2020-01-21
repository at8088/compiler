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
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author ricletm
 */
public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField declField : getList()) {
            declField.decompile(s);
            s.println(";");
        }
    }
    
    public void verifyListDeclField(DecacCompiler compiler, 
            ClassDefinition superClass, ClassDefinition currentClass) throws ContextualError {
        assert(getList() != null);
        int index = 1;
        for (AbstractDeclField field: getList()) {
            field.verifyDeclField(compiler, superClass, currentClass, index);
            index ++;
        }
    }

    public void verifyListDeclFieldPass3(DecacCompiler compiler, EnvironmentExp envExp, ClassDefinition currentClass) throws ContextualError {
        assert(getList() != null);
        for (AbstractDeclField field: getList()){
            currentClass.incNumberOfFields();
            field.verifyDeclFieldPass3(compiler, envExp, currentClass);
        }
    }


    
}
