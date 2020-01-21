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
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricletm
 */
public class DeclMethod extends AbstractDeclMethod {
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private ListDeclParam parameters;
    private AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam parameters, AbstractMethodBody body) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    protected int verifyDeclMethod(DecacCompiler compiler,
                                    ClassDefinition currentClass, int index)
            throws ContextualError {
        assert(type != null);
        assert(name != null);
        assert(parameters != null);
        assert(body != null);
        int methodIndex;
        Type returnType = type.verifyType(compiler);
        Signature returnSignature = parameters.verifyListParam(compiler);

        if (currentClass.getSuperClass().getMembers().containsKey(name.getName())) {
            methodIndex = currentClass.getSuperClass().getMembers().get(name.getName()).asMethodDefinition(
                    name + " n'est pas une méthode", getLocation()).getIndex();
        } else {
            methodIndex = index;
            index++;
        }

        MethodDefinition methodDef = new MethodDefinition(returnType, getLocation(), returnSignature, methodIndex);
        name.setDefinition(methodDef);
        methodDef.setLabel(new Label("code."+currentClass.getType().getName().getName()+"."+name.toString()));
        try {
            currentClass.getMembers().declare(name.getName(), methodDef);
        } catch (EnvironmentExp.DoubleDefException ex) {
            throw new ContextualError("Méthode `" + name.getName() + 
                    "` déjà définie", getLocation());
        }
        LOG.debug(currentClass.getType().getName() + "::" + name.getName());
        return index;
    }

    @Override
    protected void verifyDeclMethodPass3(DecacCompiler compiler, EnvironmentExp envExp,
                                    ClassDefinition currentClass)
            throws ContextualError {
        assert(type != null);
        assert(name != null);
        assert(parameters != null);
        assert(body != null);

        Type returnType = type.verifyType(compiler);

        EnvironmentExp returnEnvParam = parameters.verifyListParamPass3(compiler);

        body.verifyMethodBody(compiler, envExp, returnEnvParam, currentClass, returnType);
    }

    public AbstractIdentifier getName() {
        return name;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
        s.print("(");
        parameters.decompile(s);
        s.println(") {");
        s.indent();
        body.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        parameters.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        parameters.iter(f);
        body.iter(f);
    }

    public void codeGenMethod(DecacCompiler compiler, String className) {
        compiler.addLabel(new Label("code." + className + "." + this.name.toString()));
        compiler.getProgram().addFirst(new TSTO( compiler.getNbrRegs() + compiler.getLocalVarNbr() +
                compiler.getIndiceDansPile() + 2*compiler.getBsrNbr()));
        //DecacCompiler methodProg = new DecacCompiler(null,null);
        compiler.getProgram().addFirst(new BOV(new Label("pile_pleine")));
        for(int i = 2 ; i < compiler.getUsedReg().length ; i++){
            compiler.addInstruction(new PUSH(Register.getR(i)));
//            compiler.decSavedRegsCpt();
        }
        ((MethodBody)body).codeGenMethodBody(compiler, className, this.toString());
        compiler.addLabel(new Label("fin."+className+"."+this.name.toString()));
        for(int i = compiler.getUsedReg().length -1 ; i >= 2  ; i--){
            compiler.addInstruction(new POP(Register.getR(i)));
//            program.incSavedRegsCpt();
        }
        compiler.addInstruction(new RTS());

    }

    @Override
    public String toString(){
        return name.toString();
    }
}
