package fr.ensimag.deca.context;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import java.io.IOException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author abelc
 */
public class TestContextCustom {
    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            Logger.getRootLogger().setLevel(Level.DEBUG);
            DecaLexer lex = new DecaLexer(CharStreams.fromFileName(args[0]));
            CommonTokenStream tokens = new CommonTokenStream(lex);
            DecaParser parser = new DecaParser(tokens);
            DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
            parser.setDecacCompiler(compiler);
            AbstractProgram prog = parser.parseProgramAndManageErrors(System.err);
            if (prog == null) {
                System.exit(1);
                return; // Unreachable, but silents a warning.
            }
            try {
                prog.verifyProgram(compiler);
            } catch (LocationException e) {
                e.display(System.err);
                System.exit(1);
            }
            prog.prettyPrint(System.out);
        }
    }
}
