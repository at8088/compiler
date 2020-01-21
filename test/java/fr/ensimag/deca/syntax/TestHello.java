/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.syntax;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.antlr.v4.runtime.CommonTokenStream;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tree.AbstractProgram;



/**
 * teste le hello.deca
 * 
 * @author koumbac
 */
public class TestHello {
    

    @Test
    public void testHello() throws IOException  {
//        System.out.println(1);
//        String pathName = "../../../../deca/syntax/valid/provided/hello.deca";
//        System.out.println(1);
//        File source = new File(pathName);
//        System.out.println(1);
//        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), source);
//        /**
//        // construction de l'abre
//        StringLiteral string = new StringLiteral("Hello");
//
//        ListExpr arg = new ListExpr();
//        arg.add(string);
//        Println print = new Println(false, arg);
//
//        ListInst insts = new ListInst();
//        insts.add(print);
//
//        ListDeclVar declVar = new ListDeclVar(); // pas de constructeur direct
//        Main main = new Main(declVar, insts);
//
//        ListDeclClass listDecl = new ListDeclClass(); // pas de contructeur direct
//        Program tree = new Program(listDecl, main);
//        **/
//
//        // lancement de la compilation de hello.deca
//        System.out.println(1);
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        System.out.println(1);
//        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(new String[]{pathName});
//        System.out.println(1);
//        CommonTokenStream tokens = new CommonTokenStream(lex);
//        System.out.println(1);
//        DecaParser parser = new DecaParser(tokens);
//        System.out.println(1);
//        parser.setDecacCompiler(compiler);
//        System.out.println(1);
//        AbstractProgram prog = parser.parseProgramAndManageErrors(System.err);
//
//        if (prog == null) {  // cas ou tree nul
//            System.exit(1);
//            return; // Unreachable, but silents a warning.
//        }
//        try {
//            prog.verifyProgram(compiler);
//        } catch (LocationException e) {
//            e.display(System.err);
//            System.exit(1);
//        }
//
//        prog.decompile(); // a voir ce qu'on doit faire demain
//
        
        
        
        
    }

    
    
    
}
