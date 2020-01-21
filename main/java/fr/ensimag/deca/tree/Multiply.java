package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.MUL;



/**
 * @author gl22
 * @date 01/01/2020
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        super.codeGenExpr(compiler); 
        compiler.addInstruction(new MUL(getRightOperand()
            .getAddrResultat(),(GPRegister)getLeftOperand().getAddrResultat()));
        // on libère pas le reg du leftoperand car il contient le resultat
        if (getRightOperand().getRegIndex() != -1) {
            compiler.freeReg(getRightOperand().getRegIndex());
        }
        compiler.addArithOverFlowCheck();
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        codeGenExpr(compiler);
    }
    
    
}
