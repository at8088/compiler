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
public abstract class AbstractOpBool extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("start AbstractOpCmp::verifyExpr");
        LOG.debug("location : " + getLocation());

        
        assert(getLeftOperand() != null);
        assert(getRightOperand() != null);
        // On est bien dans une opération boolean
        assert((getOperatorName() == "==" || getOperatorName() == "!=" || 
                getOperatorName() == "&&" || getOperatorName() == "||"));
           
        Type type1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);        
        Type type2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        
        // boolean obligatoire en cas de comparaison
        if (!type1.isBoolean()) {
            throw new ContextualError(getLeftOperand() + " doit être un boolean", 
                    getLeftOperand().getLocation());
        } else if (!type2.isBoolean()) {
            throw new ContextualError(getRightOperand() + " doit être un boolean", 
                    getRightOperand().getLocation());
        } else {
            /***  TODO Comparaison des classes (page 8 poly papier)  ***/
        }
        
        setType(compiler.getTypeFromString(DecacCompiler.BOOLEAN));

        LOG.debug("start AbstractOpCmp::verifyExpr");
        return getType();
    }

    
}
