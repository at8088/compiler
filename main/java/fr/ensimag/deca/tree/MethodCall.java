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
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;
import java.io.PrintStream;

/**
 *
 * @author ricletm
 */
public class MethodCall extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    private AbstractExpr expr;
    private AbstractIdentifier ident;
    private ListExpr param;

    public MethodCall(AbstractExpr expr, AbstractIdentifier ident, ListExpr param) {
        this.expr = expr;
        this.ident = ident;
        this.param = param;
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        assert(expr != null);
        assert(ident!= null);
        assert(param != null);
        LOG.debug("MethodCall::verifyMethodIdent start");

        Type typeExpr = expr.verifyExpr(compiler, localEnv, currentClass);
        EnvironmentExp envExpr = compiler.getClassDefFromString(typeExpr.toString()).getMembers();
        setType(typeExpr);

        ident.verifyMethodIdent(compiler, envExpr);

        LOG.debug("MethodCall::verifyMethodIdent stop");
        return getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        expr.decompile(s);
        s.print(".");
        ident.decompile(s);
        s.print("(");
        param.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, false);
        param.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
        ident.iter(f);
        param.iter(f);
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        compiler.incParamCpt(param.size());
        compiler.addInstruction(new ADDSP(param.size() + 1));
        this.expr.codeGenExprRight(compiler);
        int reg = compiler.getNextFreeReg(this);
        compiler.addInstruction(new LOAD(this.expr.getAddrResultat() , Register.getR(reg)));
        compiler.addInstruction(new STORE(Register.getR(reg) , new RegisterOffset(0,Register.SP)));
        compiler.freeReg(reg);
        int paramCpt = 1;
        for(AbstractExpr arg : this.param.getList()){
            arg.codeGenExpr(compiler);
            compiler.addInstruction(new STORE((GPRegister)arg.getAddrResultat() , new RegisterOffset(-paramCpt , Register.SP)));
            paramCpt ++ ;
        }
        int reg2 = compiler.getNextFreeReg(this);
        compiler.addInstruction(new LOAD( new RegisterOffset(0,Register.SP) , Register.getR(reg2)));
        compiler.addInstruction(new CMP(new NullOperand() , Register.getR(reg2)));
        compiler.addInstruction(new BEQ(new Label("dereferencement_null")));
        compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.getR(reg2)) , Register.getR(reg2)));
        int methodIndex = this.ident.getMethodDefinition().getIndex();
        compiler.addInstruction(new BSR(new RegisterOffset(methodIndex,Register.getR(reg2))));
        compiler.incBsrNbr();
        compiler.addInstruction(new SUBSP(param.size() + 1));
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToBranch, Label label) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
