package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.CMP;


/**
 * Operator "x >= y"
 * 
 * @author gl22
 * @date 01/01/2020
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
        getLeftOperand().codeGenExpr(compiler);
        getRightOperand().codeGenExprRight(compiler);
        compiler.addInstruction(new CMP(getRightOperand().getAddrResultat() ,
                (GPRegister) getLeftOperand().getAddrResultat()));
        if(condToranch){
            compiler.addInstruction(new BGE(label));
        }else{
            compiler.addInstruction(new BLT(label));
        }
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        //inutile
    }

}
