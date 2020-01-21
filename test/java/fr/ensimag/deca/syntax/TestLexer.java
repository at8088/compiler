/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.syntax;


import java.io.IOException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import java.io.BufferedReader;
import org.antlr.v4.runtime.CommonTokenStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.mortbay.log.Log;
import static org.junit.Assert.*;

// pourquoi on declare pas decaLexer ? je ne le trouve pas
// il n'est pas declare non plus dans ManualTestlex.java


/**
 *premier test du lexer sur hello.deca
 * on teste si la liste de token est celle voulue
 * 
 * @author abelc
 */
public class TestLexer {  
    
    @Test
    public void testObrace() throws IOException  {
        String token = "{";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(token));
        
        //le lex est non nul
        assertNotNull("le resultat du lexer est nul pour "+token,lex);
        
        //les token sont des token & bonne taille
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+token , 1, tokens.size());
        
        //chaque token non nul et egal à ce qui est voulu
        assertTrue("le token n'est pas celui attendu pour "+token , 
                tokens.get(0).getText().equals(token));
    } 
  
    
    @Test
    public void testCbrace() throws IOException  {
        String token = "}";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(token));
        
        assertNotNull("le resultat du lexer est nul",lex);
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+token , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+token , 
                tokens.get(0).getText().equals(token));
    } 
    
     @Test
    public void testSemi() throws IOException  {
        String t = ";";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul",lex);
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testComma() throws IOException  {
        String t = ",";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testEquals() throws IOException  {
        String t = "=";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul",lex);  

        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testOparent() throws IOException  {
        String t = "(";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul",lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testCparent() throws IOException  {
        String t = ")";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul",lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }
    
    @Test
    public void testOr() throws IOException  {
        String t = "||";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul",lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }
    
    @Test
    public void testAnd() throws IOException  {
        String t = "&&";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul",lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }
    
    @Test
    public void testEqEq() throws IOException  {
        String t = "==";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul",lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }    

    @Test
    public void testNEq() throws IOException  {
        String t = "!=";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul",lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }
    
    @Test
    public void testLEq() throws IOException  {
        String t = "<=";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul",lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }
 
    @Test
    public void testGEq() throws IOException  {
        String t = ">=";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul",lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }
    
    @Test
    public void testGT() throws IOException  {
        String t = ">";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }  
    
    @Test
    public void testLT() throws IOException  {
        String t = "<";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }
    
    @Test
    public void testPlus() throws IOException  {
        String t = "+";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }
    
    @Test
    public void testMinus() throws IOException  {
        String t = "-";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t, lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }   
 
    @Test
    public void testTimes() throws IOException  {
        String t = "*";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testSlash() throws IOException  {
        String t = "/";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testPercent() throws IOException  {
        String t = "%";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testExclam() throws IOException  {
        String t = "!";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testDot() throws IOException  {
        String t = ".";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testPrint() throws IOException  {
        String t = "print";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testPrintln() throws IOException  {
        String t = "println";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testPrintlnx() throws IOException  {
        String t = "printlnx";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
 
    @Test
    public void testPrintx() throws IOException  {
        String t = "printx";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testWhile() throws IOException  {
        String t = "while";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }
    
    @Test
    public void testReturn() throws IOException  {
        String t = "return";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testIf() throws IOException  {
        String t = "if";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testElse() throws IOException  {
        String t = "else";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testInstanceof() throws IOException  {
        String t = "instanceof";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
  
    @Test
    public void testReadInt() throws IOException  {
        String t = "readInt";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testReadFloat() throws IOException  {
        String t = "readFloat";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testNew() throws IOException  {
        String t = "new";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testTrue() throws IOException  {
        String t = "true";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testFalse() throws IOException  {
        String t = "false";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pour "+t,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testThis() throws IOException  {
        String t = "this";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    
    @Test
    public void testNull() throws IOException  {
        String t = "null";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testExtends() throws IOException  {
        String t = "extends";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testClass() throws IOException  {
        String t = "class";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testProtected() throws IOException  {
        String t = "protected";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testAsm() throws IOException  {
        String t = "asm";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }

    @Test
    public void testDigit() throws IOException  {
        String t = "1";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testLetter() throws IOException  {
        String t = "a";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
 
    @Test
    public void testIdent1() throws IOException  {
        String t = "chat";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
     @Test
    public void testIdent2() throws IOException  {
        String t = "$chat";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testIdent3() throws IOException  {
        String t = "_chat";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testIdent4() throws IOException  {
        String t = "chat_roux";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testIdent5() throws IOException  {
        String t = "chat1";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testIdent6() throws IOException  {
        String t = "chat$roux";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testInt1() throws IOException  {
        String t = "1230";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testInt2() throws IOException  {
        String t = "0";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testInt3() throws IOException  {
        String t = "9";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testNum1() throws IOException  {
        String t = "1.1";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testNum2() throws IOException  {
        String t = "10.32";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testFloatDec1() throws IOException  {
        String t = "1.1f";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testFloatDec2() throws IOException  {
        String t = "22.35F";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testFloatDec3() throws IOException  {
        String t = "1.1e+1f";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testFloatDec4() throws IOException  {
        String t = "1.1e+1F";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testFloat4() throws IOException  {
        String t = "0x12F.2p+5";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testFloat5() throws IOException  {
        String t = "0X12.eP+8F";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testString1() throws IOException  {
        String t = "\"je suis une string \"";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    }

    @Test
    public void testString3() throws IOException  {
        String t = "\"je suis \\\\\\\\ \"";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 
    
    @Test
    public void testMultiLineString() throws IOException  {
        String t = "\" je suis \n une \r string\"";
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));
        
        assertNotNull("le resultat du lexer est nul pout"+t ,lex);  
        
        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour "+t , 1, tokens.size());
        
        assertTrue("le token n'est pas celui attendu pour "+t , 
                tokens.get(0).getText().equals(t));
    } 

    @Test
    public void testInclude() throws IOException {
        String t = "#include \"fime.name\"";
        System.out.println(t);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(t));

        assertNotNull("le resultat du lexer est nul pout" + t, lex);

        List<? extends Token> tokens = lex.getAllTokens();
        assertEquals("la taille est fausse pour " + t, 1, tokens.size());

        assertTrue("le token n'est pas celui attendu pour " + t,
                tokens.get(0).getText().equals(t));
    }
}
