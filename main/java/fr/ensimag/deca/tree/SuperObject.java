package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Created by tahiria on 1/20/20.
 */
public class SuperObject extends DeclClass{

    public SuperObject(AbstractIdentifier ident, AbstractIdentifier extend, ListDeclField fields, ListDeclMethod methods) {
           //"Object"  null   null   null
        super(ident, extend, fields, methods);
        this.tableEtiquettes = new Label[1];
        this.tableEtiquettes[0] = new Label("code.Object.equals");
    }

    @Override
    public void codeGenMethodTable(DecacCompiler compiler){
        compiler.addComment("Code de la table des méthodes de Object");
        // on n'utlise que R0 pour construire la table des méthodes
        RegisterOffset gbWithOffset = new RegisterOffset(1,
                Register.GB);
        compiler.addInstruction(new LOAD(new NullOperand() , Register.R0));
        compiler.addInstruction(new STORE(Register.R0, gbWithOffset));
        compiler.addInstruction(new LOAD(new LabelOperand(this.tableEtiquettes[0]) , Register.R0));
        gbWithOffset = new RegisterOffset(2, Register.GB);
        compiler.addInstruction(new STORE(Register.R0 ,gbWithOffset ));
        compiler.incIndiceDansPile(2);
    }

    @Override
    public void codeGenMethods(DecacCompiler compiler){
        compiler.addLabel(this.tableEtiquettes[0]);
    }
}
