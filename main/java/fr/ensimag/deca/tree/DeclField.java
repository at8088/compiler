/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

/**
 *
 * @author ricletm
 */
public class DeclField extends AbstractDeclField {
    private Visibility visibility;
    private AbstractIdentifier type;
    private AbstractIdentifier field;
    private AbstractInitialization initialization;

    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier field, AbstractInitialization initialization) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(field);
        Validate.notNull(initialization);
        this.visibility = visibility;
        this.type = type;
        this.field = field;
        this.initialization = initialization;
    }

    /**
     * Règle 2.5
     * @param compiler
     * @param superClass
     * @param currentClass
     * @throws ContextualError
     */
    @Override
    protected void verifyDeclField(DecacCompiler compiler, 
            ClassDefinition superClass, ClassDefinition currentClass, int index)
            throws ContextualError {
        assert(visibility != null);
        assert(type != null);
        assert(field != null);
        assert(initialization != null);
        
        Type localType = type.verifyType(compiler);
        
        if (localType.isVoid()) {
            throw new ContextualError("Le type de l'attribut ne peut ête void", 
                    type.getLocation());
        } else if (superClass != null 
                && superClass.getMembers().getCurrent().containsKey(field.getName())
                && !superClass.getMembers().getCurrent().get(field.getName()).isField()) {
            throw new ContextualError("L'attribut `" + field.getName() + "` est "
                    + "déjà utilisé par une méthode de la classe mère", getLocation());
        }

        initialization.verifyInitialization(compiler, localType, null, currentClass);
        
        FieldDefinition localField = new FieldDefinition(localType, 
                getLocation(), visibility, currentClass, index);

        field.setDefinition(localField);

        try {
            currentClass.getMembers().declare(field.getName(), localField);
        } catch (EnvironmentExp.DoubleDefException ex) {
            throw new ContextualError("Attribut `" + field.getName() +
                    "` déjà définie", field.getLocation());
        }
        LOG.debug(currentClass.getType().getName() + "->" + field.getName());
    }

    public void verifyDeclFieldPass3(DecacCompiler compiler, EnvironmentExp envExp,
                                     ClassDefinition currentClass) throws ContextualError{
        assert(visibility != null);
        assert(type != null);
        assert(field != null);
        assert(initialization != null);

        Type localType = type.verifyType(compiler);
        initialization.verifyInitialization(compiler, localType, envExp, currentClass);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (visibility.equals(Visibility.PROTECTED)) {
            s.print("protected ");
        }
        type.decompile(s);
        s.print(" ");
        field.decompile(s);
        if (!(initialization instanceof NoInitialization)) {
            s.print(" = ");
            initialization.decompile(s);
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //visibility.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //visibility.iter(f);
        type.iter(f);
        field.iter(f);
        initialization.iter(f);
    }

    public void codeGenField(DecacCompiler compiler) {
        if (initialization instanceof NoInitialization) {
            codeGenInitZero(compiler);
        }else{
            codeGenInitExplicite(compiler);
        }
    }

    public AbstractIdentifier getField() {
        return field;
    }

    public void codeGenInitZero(DecacCompiler compiler){
        if (this.type.getType().isInt() || type.getType().isBoolean()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(0) , Register.R0));
        } else if (type.getType().isFloat()) {
            compiler.addInstruction(new LOAD(new ImmediateFloat(0) , Register.R0));
        }else if(type.getType().isClass()){
            compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        }
        compiler.addInstruction(new STORE(Register.R0 , new RegisterOffset( this.getField().getFieldDefinition().getIndex(),Register.R1)));
    }

    public void codeGenInitExplicite(DecacCompiler compiler) {
        if (! (initialization instanceof NoInitialization)) {
            Initialization init = (Initialization)initialization;
            if (this.type.getType().isInt() || this.type.getType().isBoolean()) {
                compiler.addInstruction(new LOAD(new ImmediateInteger(((IntLiteral)init.getExpression()).getValue()), Register.R0));
            } else if (this.type.getType().isFloat()) {
                compiler.addInstruction(new LOAD(new ImmediateFloat(((FloatLiteral)init.getExpression()).getValue()) , Register.R0));
            }else if(this.type.getType().isClass()){
                compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
            }
            compiler.addInstruction(new STORE(Register.R0 , new RegisterOffset( this.getField().getFieldDefinition().getIndex(),Register.R1)));
        }
    }
}
