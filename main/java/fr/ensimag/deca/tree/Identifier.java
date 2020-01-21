package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import java.io.PrintStream;
import java.lang.invoke.MethodType;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca Identifier
 *
 * @author gl22
 * @date 01/01/2020
 */
public class Identifier extends AbstractIdentifier {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call "
                            + "getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call "
                            + "getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call "
                            + "getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call "
                            + "getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call "
                            + "getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        assert(localEnv != null);
        LOG.debug("start Identifier::verifyExpr");
        LOG.debug("envirronement local : \n" + localEnv);
        if (localEnv.get(name) == null) {
            throw new ContextualError("`" + name + "` n'est pas définis", getLocation());
        }
        setType(localEnv.get(name).getType());
        setDefinition(localEnv.get(name));
        LOG.debug("stop Identifier::verifyExpr");
        return localEnv.get(name).getType();
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     * @return 
     * @throws fr.ensimag.deca.context.ContextualError
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        assert(name != null);

        if (compiler.getEnvTypes().containsKey(name.getName())) {
            setType(compiler.getTypeFromString(name.getName()));
            setDefinition(compiler.getEnvTypes().get(name.getName()));
        } else {
            throw new ContextualError("Type inconnu:" + name, getLocation());
        }
        
        return getType();
    }
    
    /**
     * Implémente le mot clef extends
     * @param compiler contains "env_types" attribute
     * @return 
     * @throws fr.ensimag.deca.context.ContextualError
     */
    @Override
    public ClassDefinition verifyClass(DecacCompiler compiler) throws ContextualError {
        assert(name != null);

        // Test si la classe en question est déjà définie
        if (!compiler.getEnvTypes().containsKey(name.getName())) {
            throw new ContextualError("La classe `" + name + 
                    "` n'est pas définie", getLocation());
        }
        
        // Test si le type associé à la définition est bien une classe
        if (!compiler.getClassDefFromString(name.getName()).isClass()) {
            throw new ContextualError("`" + name.getName() + 
                    "` n'est pas une classe valide", getLocation());
        }

        setType(compiler.getTypeFromString(name.getName()));
        setDefinition(compiler.getClassDefFromString(name.getName()));

        return getClassDefinition();
    }
    
    
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getName().toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }    

    @Override
    protected void codeGenExpr(DecacCompiler compiler) {
        int reg = compiler.getNextFreeReg(this);
        compiler.incNbrRegs();
        setAddrResultat(Register.getR(reg), reg);
        compiler.addInstruction(new LOAD(getExpDefinition().getOperand()
                , Register.getR(reg)));
    }

    @Override
    public void codeGenBoolExpr(DecacCompiler compiler, boolean condToranch, Label label) {
        codeGenExpr(compiler);
        compiler.addInstruction(new CMP(new ImmediateInteger(0) , 
                (GPRegister) getAddrResultat()));
        if(condToranch){
            compiler.addInstruction(new BNE(label));
        }else{
            compiler.addInstruction(new BEQ(label));
        }
    }

    @Override
    protected void codeGenExprRight(DecacCompiler compiler) {
        setAddrResultat(this.getExpDefinition().getOperand(), -1);
    }

    @Override
    public MethodDefinition verifyMethodIdent(DecacCompiler compiler, EnvironmentExp localEnv) {
        assert(name != null);
        LOG.debug("Identifier::verifyMethodIdent start");

        MethodDefinition methodDef = (MethodDefinition) localEnv.get(name);
        setDefinition(methodDef);
        setType(methodDef.getType());

        LOG.debug("Identifier::verifyMethodIdent stop");
        return getMethodDefinition();
    }
}
