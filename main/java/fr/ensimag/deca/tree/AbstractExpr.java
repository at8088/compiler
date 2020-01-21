package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl22
 * @date 01/01/2020
 */
public abstract class AbstractExpr extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
  
    private int indexIfReg = -1;
    private DVal addrResultat = null;  // si l'expr est un identificateur
    public void setAddrResultat(DVal reg , int indexReg) {
        if(reg instanceof GPRegister){
           this.indexIfReg = indexReg;
        }
        this.addrResultat = reg;
    }

    public DVal getAddrResultat() {
        return addrResultat;
    }
    
    public int getRegIndex(){
        return this.indexIfReg;
    }


    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error ie the 3.20, 3.32, 3.34 rules
     *
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * Check rule 3.28
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     * @throws fr.ensimag.deca.context.ContextualError
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
        
        Type exprType = verifyExpr(compiler, localEnv, currentClass);
        
        // Si on a une class, l'expression doit être nulle ou du même type 
        // que la classe
        if (expectedType.isClass() 
                && !(exprType.isNull() || exprType.sameType(expectedType))) {
            throw new ContextualError("Doit être null ou de type `" 
                    + expectedType, getLocation());
        } else if (!expectedType.isClass() && !exprType.sameType(expectedType)) {
            throw new ContextualError("le résultat de l'expression n'est pas de "
                    + "type: " + expectedType, getLocation());
        } 
        
        if (!exprType.isNull()) {
            setType(expectedType);
        } else {
            setType(compiler.getTypeFromString(DecacCompiler.NULL));
        }
        
        return this;
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("start verifyInst");

        verifyExpr(compiler, localEnv, currentClass);

        LOG.debug("stop verifyInst");
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean as explained in the 3.29 rule
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = verifyExpr(compiler, localEnv, currentClass);
        
        assert(type != null);
        
        if (!type.isBoolean()) {
            throw new ContextualError("Une condition doit renvoyer un type "
                    + "`boolean`", getLocation());
        }
        setType(compiler.getTypeFromString(DecacCompiler.BOOLEAN));
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler , boolean hex) {
        codeGenExprRight(compiler);
        assert(this.type != null);
        if(this.type.isInt()){
            if(this.getAddrResultat() != Register.R1){
                compiler.addInstruction(new LOAD(this.getAddrResultat(), Register.R1));
            }
            compiler.addInstruction( new WINT());
        }else if(this.type.isFloat()){
            if(this.getAddrResultat() != Register.R1){
                compiler.addInstruction(new LOAD(this.getAddrResultat(), Register.R1));
            }
            if(hex){
                compiler.addInstruction(new WFLOATX());
            }else{
                compiler.addInstruction(new WFLOAT());
            }
        }else {
            compiler.addInstruction(new WSTR(new ImmediateString(this.type.getName().toString())));
        }
    }
    
    protected abstract void codeGenExprRight(DecacCompiler compiler);
    
    protected abstract void codeGenExpr(DecacCompiler compiler);
    
    /**
     * @param compiler 
     */

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenExpr(compiler);
    }
    
    public abstract void codeGenBoolExpr(DecacCompiler compiler , boolean condToBranch
                    , Label label);

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
    }

    @Override
    public void decompile(PrintStream s) {
        getLOG().error(this.getClass());
        getLOG().debug("ABST EXPR");
    }
    
    

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
