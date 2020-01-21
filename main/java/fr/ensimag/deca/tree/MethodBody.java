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
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;

/**
 *
 * @author ricletm
 */
public class MethodBody extends AbstractMethodBody {
    private ListDeclVar var;
    private ListInst inst;

    public MethodBody(ListDeclVar var, ListInst inst) {
        this.var = var;
        this.inst = inst;
    }

    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExp,
                                 EnvironmentExp EnvExpParam, ClassDefinition currentClass,
                                 Type type) throws ContextualError{

        //assert à faire ???
        assert(var != null);
        assert(inst != null);
        var.verifyListDeclVariablePass3(compiler, envExp, EnvExpParam, currentClass);
        inst.verifyListInst(compiler, EnvironmentExp.stack(envExp, EnvExpParam), currentClass, type);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        var.decompile(s);
        inst.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        var.prettyPrint(s, prefix, false);
        inst.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        var.iter(f);
        inst.iter(f);
    }

    public void codeGenMethodBody(DecacCompiler program , String className , String methodName) {
        for (AbstractDeclVar declVar : var.getList()) {
            declVar.codeGenDeclVarMethodBody(program, className, methodName);
        }

        for (AbstractInst instruction : inst.getList()) {
            instruction.codeGenInstMethodBody(program, className, methodName);
        }
        //this.var.codeGenListDeclVar(program , true);
        //this.inst.codeGenListInst(program);
//        program.addLabel(new Label("fin."+className+"."+methodName));
//        for(int i = program.getNbrRegs() ; i > 0 ; i--){
//            program.addInstruction(new POP(Register.getR(i+1)));
//            program.incSavedRegsCpt();
//        }
//        program.addInstruction(new RTS());
//        for(int i = program.getNbrRegs() ; i > 0 ; i--){
//            program.getProgram().addFirst(new PUSH(Register.getR(i+1)));
//            program.decSavedRegsCpt();
//        }
       // program.getProgram().addFirst(new BOV(new Label("pile_pleine")));
//        program.getProgram().addFirst(new TSTO( program.getNbrRegs() + program.getLocalVarNbr() +
//                program.getIndiceDansPile() + 2*program.getBsrNbr()));

    }
    
}
