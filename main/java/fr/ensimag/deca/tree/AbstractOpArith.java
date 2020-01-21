package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import org.apache.log4j.Logger;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl22
 * @date 01/01/2020
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    
    /**
     * renvoie le type du resultat de l'operation arithmetique
     * Le type renvoyé est définis à la page 74 de la documentation
     */
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        assert(getLeftOperand() != null);
        assert(getRightOperand() != null);
        LOG.debug("start AbstractOpArith::verifyExpr");
        Type typeLeft = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeRight = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        
        // Les opération arithmétiques sont définis
        // seulement pour float et int
        if (!typeRight.isFloat() && !typeRight.isInt()) {
            throw new ContextualError(getLeftOperand() + " doit être un " +
                    "`float` ou un `int`", getLeftOperand().getLocation());
        } else if (!typeLeft.isFloat() && !typeLeft.isInt()) {
            throw new ContextualError(getRightOperand() + " doit être un " +
                    "float` ou un `int`", getRightOperand().getLocation());
        }

        if (typeRight.isInt() && typeLeft.isFloat()) {
            // On change rightOperand en Float via conv float
            setRightOperand(new ConvFloat(getRightOperand()));
            getRightOperand().setType(compiler.getTypeFromString(DecacCompiler.FLOAT));
            setType(typeLeft);
        } else if (typeRight.isFloat() && typeLeft.isInt()) {
            // On change leftOperand en Float via conv float
            setLeftOperand(new ConvFloat(getLeftOperand()));
            getLeftOperand().setType(compiler.getTypeFromString(DecacCompiler.FLOAT));
            setType(typeRight);
        } else {
            // Les types sont identiques donc le type de retour est celui de typeLeft ou typeRight
            setType(typeLeft);
        }
        LOG.debug("stop AbstractOpArith::verifyExpr");
        return getType();
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        getLeftOperand().codeGenExpr(compiler);
        getRightOperand().codeGenExprRight(compiler);
        setAddrResultat(getLeftOperand()
                            .getAddrResultat(),getLeftOperand().getRegIndex());
    }
        
}
