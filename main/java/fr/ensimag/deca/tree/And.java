package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;


/**
 *
 * @author gl22
 * @date 01/01/2020
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    
    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        super.codeGenExpr(compiler);
        
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
        if(condToranch){
            //les expressions booléenne dont la condition du branchement est vraie
            // n'apparaissent que dans les while
            Label andLabel = new Label("E_And."+compiler.getCondAndNbr());
            getLeftOperand().codeGenBoolExpr(compiler, false, andLabel);
            getRightOperand().codeGenBoolExpr(compiler, true, label);
            compiler.addLabel(andLabel);
            compiler.incCondAndNbr();


        }else{
            getLeftOperand().codeGenBoolExpr(compiler, condToranch, label);
            getRightOperand().codeGenBoolExpr(compiler, condToranch, label);
        }
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
