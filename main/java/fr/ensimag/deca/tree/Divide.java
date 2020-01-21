package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;


/**
 *
 * @author gl22
 * @date 01/01/2020
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        super.codeGenExpr(compiler); 
        if(getRightOperand().getType().toString().equalsIgnoreCase("float")
                && getLeftOperand().getType().toString().equalsIgnoreCase("float")) {
            compiler.addInstruction(new DIV(getRightOperand().getAddrResultat(), 
                    (GPRegister)getLeftOperand().getAddrResultat()));
        } else {
            compiler.addInstruction(new QUO(getRightOperand().getAddrResultat(),
                    (GPRegister)getLeftOperand().getAddrResultat()));
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
