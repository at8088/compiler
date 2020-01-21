package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.SUB;


/**
 * @author gl22
 * @date 01/01/2020
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }
    
    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        super.codeGenExpr(compiler);
        compiler.addInstruction(new SUB(getRightOperand().getAddrResultat(),
                (GPRegister)getLeftOperand().getAddrResultat()));
        //on libère pas le reg du leftoperand car il contient le resultat
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
