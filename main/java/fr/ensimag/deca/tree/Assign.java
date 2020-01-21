package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl22
 * @date 01/01/2020
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        assert(getLeftOperand() != null);
        assert(getRightOperand() != null);
        
        Type type = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        getRightOperand().verifyRValue(compiler, localEnv, currentClass, type);
        setType(type);
        return type;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        getRightOperand().codeGenExpr(compiler);
        setAddrResultat(getLeftOperand()
                            .getAddrResultat(),getLeftOperand().getRegIndex());
        compiler.addInstruction(new STORE((GPRegister)
                            getRightOperand().getAddrResultat()
                            ,((Identifier)getLeftOperand()).
                                    getExpDefinition().getOperand()));
        compiler.addArithOverFlowCheck();
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
        //inutile
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        codeGenExpr(compiler);
    }
    
    
}
