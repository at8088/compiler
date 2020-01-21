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
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author ricletm
 */
public class MethodAsmBody extends AbstractMethodBody {
    private StringLiteral string;

    public MethodAsmBody(StringLiteral string) {
        this.string = string;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("asm (");
        string.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        string.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        string.iter(f);
    }

    @Override
    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp EnvExpParam, ClassDefinition currentClass, Type type) throws ContextualError {

    }
}
