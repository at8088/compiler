package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Full if/else if/else statement.
 *
 * @author gl22
 * @date 01/01/2020
 */
public class IfThenElse extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;
    /**
     * Représente la liste des else if du bloc
     */
    private ListInst elseIf;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
        this.elseIf = new ListInst();
    }

    /**
     * Verify the rule 3.22
     * @param compiler contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass
     *          corresponds to the "class" attribute (null in the main bloc).
     * @param returnType
     * @throws ContextualError
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        assert(condition != null);
        assert(thenBranch != null);
        assert(elseBranch != null);

        LOG.debug("start IfThenElse::verifyInst");

        condition.verifyCondition(compiler, localEnv, currentClass);
        thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseIf.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        LOG.debug("stop IfThenElse::verifyInst");
    }

    @Override
    protected void codeGenIfThenElse(DecacCompiler compiler, int indice, boolean isMainBlock) {
        // Incrémente le nombre de block conditionnel
        int currentBlock;
        if (!isMainBlock) {
            currentBlock = compiler.getCondBlocksNbr() - 1;
        } else {
            currentBlock = compiler.getCondBlocksNbr();
        }
        if (isMainBlock) {
            compiler.incCondBlocksNbr();
        }

        Label sinon = new Label("E_Sinon." + currentBlock + indice);
        Label fin = new Label("E_Fin." + currentBlock);
        this.condition.codeGenBoolExpr(compiler, false, sinon);
        indice ++;

        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(fin));
        compiler.addLabel(sinon);

        for (AbstractInst inst: elseIf.getList()) {
            inst.codeGenIfThenElse(compiler, indice, false);
            indice ++;
        }

        elseBranch.codeGenListInst(compiler);
        if (isMainBlock) {
            compiler.addLabel(fin);
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.codeGenIfThenElse(compiler, 0, true);
    }



    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();

        for (AbstractInst inst: elseIf.getList()) {
            s.print("} else ");
            inst.decompile(s);
        }

        if (!elseBranch.isEmpty()) {
            s.println("} else {");
            s.indent();
            elseBranch.decompile(s);
            s.unindent();
            s.println("}");
        }
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);

        for (AbstractInst inst: elseIf.getList()) {
            inst.iter(f);
        }

        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseIf.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }

    /**
     * Ajoute un else if à notre bloc ifthenelse
     * Le champs else doit toujours rester vide
     * @param condition
     * @param thenBranch
     */
    public void addElseIf(AbstractExpr condition, ListInst thenBranch, Location location) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(location);

        IfThenElse elseIfElement = new IfThenElse(condition, thenBranch, new ListInst());
        elseIfElement.setLocation(location);
        elseIf.add(elseIfElement);
    }
}
