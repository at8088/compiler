/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author ricletm
 */
public class NoExtends extends Identifier {
    
    /**
     * Empty constructor for no extends class
     * By default Object is father
     */
    public NoExtends() {
        this(new SymbolTable().create(DecacCompiler.OBJECT));
    }

    public NoExtends(SymbolTable.Symbol name) {
        super(name);
    }
  

    @Override
    public ClassDefinition verifyClass(DecacCompiler compiler) throws ContextualError {

        setType(compiler.getTypeFromString(DecacCompiler.OBJECT));
        setDefinition(compiler.getClassDefFromString(DecacCompiler.OBJECT));

        return compiler.getClassDefFromString(DecacCompiler.OBJECT);
    }
}
