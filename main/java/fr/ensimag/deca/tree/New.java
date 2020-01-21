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
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

/**
 *
 * @author ricletm
 */
public class New extends AbstractExpr {
    private AbstractIdentifier ident;

    public New(AbstractIdentifier ident) {
        this.ident = ident;
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        assert(ident != null);

        Type type = ident.verifyType(compiler);

        if (!type.isClass()) {
            throw new ContextualError("`" + ident.toString() +
                    "` n'est pas une class", ident.getLocation());
        }

        setType(type);

        return getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        ident.decompile(s);
        s.print("()");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        ident.iter(f);
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        codeGenExpr(compiler);
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        int reg = compiler.getNextFreeReg(this);
        compiler.incNbrRegs();
        this.setAddrResultat(Register.getR(reg) , reg);
        int nbrCases = ident.getClassDefinition().getNumberOfFields();
        compiler.addInstruction(new NEW(nbrCases+1 , Register.getR(reg)));
        compiler.addInstruction(new BOV(new Label("tas_plein")));
        compiler.addInstruction(new LEA(compiler.getTableAddrClasses().get(ident.toString()) , Register.R0));
        compiler.addInstruction(new STORE(Register.R0 , new RegisterOffset(0,Register.getR(reg))));
        compiler.addInstruction(new PUSH(Register.getR(reg)));
        compiler.incSavedRegsCpt();
        compiler.addInstruction(new BSR(new Label("init."+this.ident.toString())));
        compiler.addInstruction(new POP(Register.getR(reg)));
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToBranch, Label label) {
        //inutile
    }
    
}
