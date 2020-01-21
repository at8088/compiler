package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;

/**
 *
 * @author gl22
 * @date 01/01/2020
 */
public class ReadInt extends AbstractReadExpr {

    /**
     * Immplements 3.35 rule
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return
     * @throws ContextualError
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.getTypeFromString(DecacCompiler.INT));
        return getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
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
    protected void codeGenExpr(DecacCompiler compiler) {
        if( compiler.getUsedReg()[1] ){
            int reg = compiler.getNextFreeReg(this);
            compiler.incNbrRegs();
            compiler.getExprAllocR1().setAddrResultat(Register.getR(reg), reg);
            compiler.addInstruction(new LOAD(Register.getR(1) , Register.getR(reg)));
        }
        setAddrResultat(Register.getR(1), 1);
        compiler.setExprAllocR1(this);
        compiler.getUsedReg()[1] = true;
        compiler.addInstruction(new RINT());
        compiler.addInvalideInputCheck();
        
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
    // cette fonction ne sera jamais appeller sur un objet de cette classe.   
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        codeGenExpr(compiler);
        //setAddrResultat(Register.getR(1), 1);
        //compiler.addInstruction(new RINT());
    }
    

}
