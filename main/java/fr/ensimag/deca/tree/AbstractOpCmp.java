package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

/**
 *
 * @author gl22
 * @date 01/01/2020
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    
    /**
     * Ce verify implémente la règle 3.33 avec une opération de compararaison
     * Nous respectons les règles et contraintes de la page 8 poly papier
     * @param compiler
     * @param localEnv
     * @param currentClass
     * @return
     * @throws ContextualError 
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("start AbstractOpCmp::verifyExpr");
        LOG.debug("location : " + getLocation());
        assert(getLeftOperand() != null);
        assert(getRightOperand() != null);
        // On est bien dans une opération de comparaison
        assert(getOperatorName().equals("==") || getOperatorName().equals("!=") ||
                getOperatorName().equals("<=") || getOperatorName().equals(">=") ||
                getOperatorName().equals(">") || getOperatorName().equals("<"));
           
        Type typeRight = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeLeft = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        // Convertion implicite
        if (typeRight.isInt() && typeLeft.isFloat()) {
            // On change rightOperand en Float via conv float
            setRightOperand(new ConvFloat(getRightOperand()));
            getRightOperand().setType(compiler.getTypeFromString(DecacCompiler.FLOAT));
            getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            typeRight = compiler.getTypeFromString(DecacCompiler.FLOAT);
        } else if (typeRight.isFloat() && typeLeft.isInt()) {
            // On change leftOperand en Float via conv float
            setLeftOperand(new ConvFloat(getLeftOperand()));
            getLeftOperand().setType(compiler.getTypeFromString(DecacCompiler.FLOAT));
            getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            typeLeft = compiler.getTypeFromString(DecacCompiler.FLOAT);
        }
        
        // (type1, type2) not in dom(type_arith_op)
        // type1 n'est pas un float ET n'est pas un int => On lève une exceptionn
        // Opérateur autre que equal et diff nécessite float et int
        if (!getOperatorName().equals("==") && !getOperatorName().equals("!=")) {
            if (!typeRight.isFloat() && !typeRight.isInt()) {
                throw new ContextualError(getLeftOperand() + " doit être un `int` ou un `float`",
                        getLeftOperand().getLocation());
            } else if (!typeLeft.isFloat() && !typeLeft.isInt()) {
                throw new ContextualError(getRightOperand() + " doit être un `int` ou un `float`"
                        , getRightOperand().getLocation());
            }
        } else {
            if (typeRight.isBoolean() && !typeLeft.isBoolean()) {
                throw new ContextualError(getRightOperand() + " doit être un `boolean`"
                        , getRightOperand().getLocation());
            } else if (typeRight.isInt() && !typeLeft.isInt()) {
                throw new ContextualError(getRightOperand() + " doit être un `int`"
                        , getRightOperand().getLocation());
            } else if (typeRight.isFloat() && !typeLeft.isFloat()) {
                throw new ContextualError(getRightOperand() + " doit être un `float`"
                        , getRightOperand().getLocation());
            }
        }

        setType(compiler.getTypeFromString(DecacCompiler.BOOLEAN));

        LOG.debug("start AbstractOpCmp::verifyExpr");
        return getType();
    }


}
