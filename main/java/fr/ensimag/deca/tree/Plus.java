package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADD;


/**
 * @author gl22
 * @date 01/01/2020
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        super.codeGenExpr(compiler);
        compiler.addInstruction(new ADD(getRightOperand().getAddrResultat(),
                (GPRegister)getLeftOperand().getAddrResultat()));
        //on libère pas le reg du leftoperand car il contient le resultat
        if (getRightOperand().getRegIndex() != -1) {
            compiler.freeReg(getRightOperand().getRegIndex());
        }   
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
        // cette fonction ne sera jamais appeller sur un objet de cette classe.
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        codeGenExpr(compiler);
    }
}
