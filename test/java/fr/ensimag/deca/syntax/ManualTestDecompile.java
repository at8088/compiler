/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.syntax;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractProgram;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 *
 * @author ricletm
 */
public class ManualTestDecompile {
    public static void main(String[] args) throws IOException {
        AbstractProgram prog = getProgram(args);
        if (prog == null) {
            System.exit(1);
        } else {
            // Create files where to put the results of decompile();
            FileOutputStream test1 = new FileOutputStream(new File("src/test/deca/syntax/valid/resultsDecompile/test1.deca"));
            FileOutputStream test2 = new FileOutputStream(new File("src/test/deca/syntax/valid/resultsDecompile/test2.deca"));

            // Write the result of the first decompile() in a file
            test1.write(prog.decompile().getBytes());
            
            // Decompile the test1.deca file and write the result in the second file
            String[] newArgs = {"src/test/deca/syntax/valid/resultsDecompile/test1.deca"};
            AbstractProgram prog2 = getProgram(newArgs);
            test2.write(prog2.decompile().getBytes());
            
//            // Pour juste decompiler et afficher sur terminal :
//            System.out.println(prog.decompile());
        }
    }
    
    public static AbstractProgram getProgram(String[] args) throws IOException {
        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        File file = null;
        if (lex.getSourceName() != null) {
            file = new File(lex.getSourceName());
        }
        final DecacCompiler decacCompiler = new DecacCompiler(new CompilerOptions(), file);
        parser.setDecacCompiler(decacCompiler);
        AbstractProgram prog = parser.parseProgramAndManageErrors(System.err);
        return prog;
    }
}
