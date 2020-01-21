package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import org.apache.log4j.Logger;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl22
 * @date 01/01/2020
 */
public class ConvFloat extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("start ConvFloat::verifyExpr");
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        assert(getOperand() != null);
        if (type.isInt()) {
            setType(compiler.getTypeFromString(DecacCompiler.FLOAT));
        } else {
            throw new ContextualError("Impossible de convertir implicitement un `"
                    + type.toString() + "` en `float`", getLocation());
        }
        LOG.debug("stop ConvFloat::verifyExpr");
        return getType();
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        getOperand().codeGenInst(compiler);
        setAddrResultat(getOperand().getAddrResultat(),getOperand().getRegIndex());
        if( ! getOperand().getType().isFloat()){
            compiler.addInstruction(new FLOAT(getOperand().getAddrResultat(),
                (GPRegister)getAddrResultat()));
            compiler.addArithOverFlowCheck();
        }
        
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
