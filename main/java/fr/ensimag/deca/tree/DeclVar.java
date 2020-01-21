package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;
import org.apache.log4j.Logger;
import org.apache.commons.lang.Validate;

/**
 * @author gl22
 * @date 01/01/2020
 */
public class DeclVar extends AbstractDeclVar {
    final private static Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    final private AbstractIdentifier type;

    public AbstractInitialization getInitialization() {
        return initialization;
    }

    public AbstractIdentifier getType() {
        return type;
    }

    public AbstractIdentifier getVarName() {
        return varName;
    }
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, 
            AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }
    
    /**
     * Code la règle 3.17 *decl_var*
     * env_exp_sup = localEnv.parent
     * env_exp = localEnv.current
     * @param compiler
     * @param localEnv
     * @param currentClass
     * @throws ContextualError 
     */
    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        assert(type != null);
        assert(initialization != null);
        
        // On véfifie que type != void
        Type localType = type.verifyType(compiler);
        
        if (localType == null) {
            throw new DecacInternalError("Pointeur null non attendu");
        } else if (localType.isVoid() || localType.isString()) {
            throw new ContextualError(
                    varName.toString() + " ne peut pas être `" + localType + "`", 
                    type.getLocation());
        }
        
        // Pour les object on stack les env
        //EnvironmentExp stackedEnv =
          //      EnvironmentExp.stack(localEnv, localEnv.getParent());
        
        initialization.verifyInitialization(compiler, localType, localEnv, 
                currentClass);
        
        try {
            LOG.trace(varName.getName());
            localEnv.declare(varName.getName(), new VariableDefinition(localType,
                    varName.getLocation()));
        } catch (EnvironmentExp.DoubleDefException ex) {
            throw new ContextualError(  " La variable " + varName + 
                    " est déjà définie.",varName.getLocation());
        }
        varName.setDefinition(localEnv.get(varName.getName()));
        varName.setType(localType);

    }

    protected EnvironmentExp verifyDeclVarPass3(DecacCompiler compiler, EnvironmentExp envExpSup,
                                      EnvironmentExp envExp, ClassDefinition currentClass)
            throws ContextualError {

        assert(type != null);
        assert(initialization != null);
        assert(varName != null);

        Type returnType = type.verifyType(compiler);
        //on vérifie que type != void
        if (returnType == null) {
            throw new DecacInternalError("Pointeur null non attendu");
        } else if (returnType.isVoid()) {
            throw new ContextualError(
                    varName.toString() + " ne peut pas être `void`",
                    type.getLocation());
        }

        EnvironmentExp stackedEnv = EnvironmentExp.stack(envExp, envExpSup);
        initialization.verifyInitialization(compiler, returnType,
                stackedEnv, currentClass);

        try {
            envExp.declare(varName.getName(), new VariableDefinition(returnType,
                    varName.getLocation()));
        } catch (EnvironmentExp.DoubleDefException ex) {
            throw new ContextualError(  " La variable " + varName +
                    " est déjà définie.",varName.getLocation());
        }
        varName.setDefinition(envExp.get(varName.getName()));
        varName.setType(returnType);

        return envExp;
    }

    @Override
    protected void codeGenDeclVarMethodBody(DecacCompiler compiler, String className, String methodName) {
        codeGenDeclVar(compiler, true);
    }

    public void codeGenDeclVar(DecacCompiler compiler , boolean isLocal){
        int offset = 0;
        RegisterOffset regWithOffset = null;
        if(isLocal){
            offset = compiler.getLocalVarNbr();
            regWithOffset = new RegisterOffset(offset + 1,
                    Register.LB);
            compiler.incLocalVarNbr(1);
        }else{
            offset = compiler.getDeclVarNbr();
            regWithOffset = new RegisterOffset(offset + 1,
                    Register.GB);
            compiler.incDeclVarNbr();
        }
        this.getVarName().getVariableDefinition().setOperand(regWithOffset);
        AbstractInitialization declInit = this.getInitialization();
        if (declInit instanceof Initialization) {
            ((Initialization) declInit).getExpression().codeGenExpr(compiler);
            compiler.addInstruction(new STORE((Register) ((Initialization) declInit).getExpression().getAddrResultat(), regWithOffset));
            compiler.freeReg(((Initialization) declInit).getExpression().getRegIndex());
        }
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        // Au cas où qu'il n'y ai pas d'initialisation (NoInitialization)
        if (initialization instanceof Initialization) {
            s.print(" = ");
            initialization.decompile(s);
        }
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
