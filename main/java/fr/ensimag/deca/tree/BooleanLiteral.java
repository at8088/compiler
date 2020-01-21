package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import java.io.PrintStream;

/**
 *
 * @author gl22
 * @date 01/01/2020
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(compiler.getTypeFromString(DecacCompiler.BOOLEAN));
        return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        int reg = compiler.getNextFreeReg(this);
        compiler.incNbrRegs();
        int entierImageBool = getValue() ? 1 : 0;
        setAddrResultat(Register.getR(reg), reg);
        compiler.addInstruction(new LOAD(new ImmediateInteger(entierImageBool), Register.getR(reg)));
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
        if(value && condToranch){
            compiler.addInstruction(new BRA(label));
        }else if(!value && !condToranch){
            compiler.addInstruction(new BRA(label));
        }
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        int entierImageBool = getValue() ? 1 : 0;
        setAddrResultat(new ImmediateInteger(entierImageBool),-1);
    }

}
