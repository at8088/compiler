package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl22
 * @date 01/01/2020
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        assert(getLeftOperand() != null);
        assert(getRightOperand() != null);
        
        Type type1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    
        if (!type1.isInt()) {
            throw new ContextualError("L'opération modulo nécessite un `int`", 
                    getLeftOperand().getLocation());
        } else if (!type2.isInt()) {
            throw new ContextualError("L'opération modulo nécessite un `int`", 
                    getRightOperand().getLocation());
        }
        
        setType(compiler.getTypeFromString(DecacCompiler.INT));
        return getType();
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        super.codeGenExpr(compiler);
        compiler.addInstruction(new REM(((AbstractBinaryExpr) this)
                .getRightOperand().getAddrResultat(),
                (GPRegister)getLeftOperand().getAddrResultat()));
        // on libère pas le reg du leftoperand car il contient le resultat
        if (getRightOperand().getRegIndex() != -1) {
            compiler.freeReg(getRightOperand().getRegIndex());
        }
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
