package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.log4j.Logger;

/**
 *
 * @author gl22
 * @date 01/01/2020
 */
public class Not extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("start Not::verifyExpr");
        assert(getOperand() != null);
        
        
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!type.isBoolean()) {
            throw new ContextualError(getOperand() + " doit être de type `boolean`", 
                    getOperand().getLocation());
        }

        setType(compiler.getTypeFromString(DecacCompiler.BOOLEAN));
        LOG.debug("stop Not::verifyExpr");
        return getType();
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
        getOperand().codeGenBoolExpr(compiler, !condToranch, label);
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        //inutile
    }
}
