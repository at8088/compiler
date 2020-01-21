package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;


/**
 *
 * @author gl22
 * @date 01/01/2020
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
        if(condToranch){
            this.getLeftOperand().codeGenBoolExpr(compiler, true, label);
            this.getRightOperand().codeGenBoolExpr(compiler, true, label);
        }else{
            Label orLabel = new Label("E_Or." + compiler.getCondOrNbr());
            compiler.incCondBlocksNbr();
            getLeftOperand().codeGenBoolExpr(compiler, true, orLabel);
            getRightOperand().codeGenBoolExpr(compiler, false, label);
            compiler.addLabel(orLabel);
            compiler.incOrNbr();
        }
    }


    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        //inutile
    }


}
