package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Single precision, floating-point literal
 *
 * @author gl22
 * @date 01/01/2020
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;

    public FloatLiteral(float value) {
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(compiler.getTypeFromString(DecacCompiler.FLOAT));
        return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        getLOG().debug("FLOAT");
        s.print(value);
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
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
    public String toString() {
        return "`" + String.valueOf(getValue()) + "`";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        int reg = compiler.getNextFreeReg(this);
        compiler.incNbrRegs();
        float value = getValue();
        setAddrResultat(Register.getR(reg), reg);
        compiler.addInstruction(new LOAD(new ImmediateFloat(value), Register.getR(reg)));
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
        //inutile
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        setAddrResultat(new ImmediateFloat(this.value), -1);
    }
    
    

}
