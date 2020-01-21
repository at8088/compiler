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

//vérifier que la lexicographie est correcte
//les noms des tests sont du type testType1Type2, par exemple quand le lexer lit
//un int puis un float  et vérifie qu'on a bien testé ces deux types
/**
 *
 * @author koumbac
 */
public class TestLexComplex {
    
     /**
     *
     * @param lexer
     * @return une liste de String correspondant aux tokens donnés par le lexer
     * donné en paramètre
     */
    public ArrayList<String> TokensToStrings(DecaLexer lexer)   {

        List<? extends Token> tokens = lexer.getAllTokens();

        ArrayList<String> tokensString = new ArrayList<>();
        for (Token element:tokens)    {
            tokensString.add(element.getText());
        }

        return tokensString;
    }
    
    // **** TESTS VALIDES INT **** // 
    
      @Test
    public void testInt() throws IOException  {
   
        String tokenExpected = "1";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("1");

        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
    
    @Test
    public void testIntFloat() throws IOException  {
   
        String tokenExpected = "1 1.2f";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 2, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("1");
        tokensAttendus.add("1.2f");

        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
      @Test
    public void testIntInt() throws IOException  {
   
        String tokenExpected = "1 3";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 2, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("1");
        tokensAttendus.add("3");


        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
      @Test
    public void testLongInt() throws IOException  {
   
        String tokenExpected = "143";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("143");


        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
    // **** FIN TESTS INT **** // 
    
    
    // **** TEST INCLUDE VALIDE **** // 
    
    @Test
    public void testInclude() throws IOException  {
   
        String tokenExpected = "#include \"TestLexer.java\"";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("#include \"TestLexer.java\"");


        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
    // **** FIN TEST INCLUDE VALIDE **** // 
    
    // **** TESTS FLOAT VALIDES **** // 
    
    @Test
    public void testFloatDecExp() throws IOException  {
   
        String tokenExpected = "1.9e+4f";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("1.9e+4f");


        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
     @Test
    public void testFloatDec() throws IOException  {
   
        String tokenExpected = "12.94F";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("12.94F");


        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
     @Test
    public void testFloaTHex() throws IOException  {
   
        String tokenExpected = "0x5.78aP-4f";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("0x5.78aP-4f");


        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
    // **** FIN TESTS FLOAT VALIDES **** // 
    
    
    
    // **** TESTS IDENTIFIERS VALIDES **** // 
    
    @Test
    public void testIdent() throws IOException  {
   
        String tokenExpected = "h$12_54z$";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("h$12_54z$");


        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
    @Test
    public void testIdentUnderscoreDoll() throws IOException  {
   
        String tokenExpected = "__$$";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("__$$");


        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
    // **** FIN TESTS IDENTIFIERS VALIDES **** // 
    
    // **** TESTS FLOAT VALIDES **** // 
    /* Test valide si maj du parseur */
    
    @Test
    public void testFloat() throws IOException  {
   
        String tokenExpected = "1.3";
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensAttendus = new ArrayList<String>();
        tokensAttendus.add("1.3");


        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensAttendus);
    }
    
    
    // *** FIN TESTS FLOAT **** // 
    
    // **** TESTS STRING VALIDES **** // 
    
    
    
    @Test
    public void testString() throws IOException  {
        String tokenExpected = "\"chat\""; //token rentré à la main
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensObtenus = new ArrayList<String>();
        tokensObtenus.add("\"chat\"");

        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensObtenus);
    } 
    
    @Test
    public void testStringSlash() throws IOException  {
        String tokenExpected = "\"\\\"\""; //token à partir du quel on synthétise les lex
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());
        // construction liste attendue
        System.out.println("print slash slash");
        ArrayList<String> tokensObtenus = new ArrayList<String>();
        tokensObtenus.add("\"\\\"\"");

        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensObtenus);
    } 
    
    @Test
    public void testMultilineString() throws IOException  {
        String tokenExpected = "\"ch\nat\""; //token rentré à la main
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensObtenus = new ArrayList<String>();
        tokensObtenus.add("\"ch\nat\"");

        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensObtenus);
    }
    
    /* PB TEST CRASH ; ABANDON
    
    @Test
    public void testStringTripleSlashQuote() throws IOException  {
        String tokenExpected = "\"cha\"t\""; 
        //leve une exception car entier non codable sur 32 bits signés
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());
        // construction liste attendue
        System.out.println("print slash slash");
        ArrayList<String> tokensObtenus = new ArrayList<String>();
        tokensObtenus.add("\"cha\\\"t");

        // check que les tokens sont ceux attendus
        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                        tokensList, tokensObtenus);    }
     */
    
    
    
    
    @Test
    public void testPrintOpar() throws IOException  {
        String tokenExpected = "print("; //token rentré
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 2, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensObtenus = new ArrayList<String>();
        tokensObtenus.add("print");
        tokensObtenus.add("(");

        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                                // check que les tokens sont ceux attendus
tokensList, tokensObtenus);
    } 
    
    @Test
    public void testPrintCpar() throws IOException  {
        String tokenExpected = "print)"; //token rentré
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertEquals("la taille est fausse pour "+tokenExpected , 2, tokensList.size());


        // construction liste attendue
        ArrayList<String> tokensObtenus = new ArrayList<String>();
        tokensObtenus.add("print");
        tokensObtenus.add(")"); 

        assertEquals("le token n'est pas celui attendu pour "+tokenExpected,
                    // check que les tokens sont ceux attendus
                    tokensList, tokensObtenus);
    } 
      
    // **** TEST INT **** // 
    
    /* @Test
    public void testIntLimitSupError() throws IOException  {
        String tokenExpected = "4294967297";
        //leve une exception car entier non codable sur 32 bits signés
        Logger.getRootLogger().setLevel(Level.DEBUG);
        try {
            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
        } catch(DecaRecognitionException te) {
            assertTrue(true);
            return ;
        }
        assertTrue(false);
    }*/

    
    @Test
    public void testIntNullError() throws IOException  {
        String tokenExpected = "00"; 
        //leve une exception car deux 0 à la suite
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertNotEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());
    }
    
        // **** FIN TESTS D'ERREURS INT **** //
  
    // **** TEST ERREUR IDENTIFIER **** //
//    @Test
//    public void testIdentDigitError() throws IOException  {
//        String tokenExpected = "7$rij";
//        //leve une exception car entier non codable sur 32 bits signés
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
    
//    @Test
//    public void testIdentDotError() throws IOException  {
//        String tokenExpected = "$r1.ij";
//        //leve une exception car entier non codable sur 32 bits signés
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
    
//    @Test
//    public void testIdentDashError() throws IOException  {
//        String tokenExpected = "$ri-j";
//        //leve une exception car entier non codable sur 32 bits signés
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
//    @Test
//    public void testIdentHopperError() throws IOException  {
//        String tokenExpected = "$ri&j";
//        //leve une exception car entier non codable sur 32 bits signés
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }

    
        // **** FIN TESTS D'ERREURS IDENTIFIER **** //

    
        // **** TESTS D'ERREUR STRING **** //
    
//    @Test
//    public void testStringOquoteError() throws IOException  {
//        String tokenExpected = "\"chat";
//        //leve une exception car ne contient pas le cquote
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
//    @Test
//    public void testStringCquoteError() throws IOException  {
//        String tokenExpected = "chat\"";
//        //leve une exception car ne contient pas le oquote
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
//    @Test
//    public void testStringQuoteError() throws IOException  {
//        String tokenExpected = "'chat'";
//        //leve une exception car entier non codable sur 32 bits signés
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }

//    @Test
//    public void testStringSlashError() throws IOException  {
//        String tokenExpected = "\"ch\\at\"";
//        //leve une exception car entier non codable sur 32 bits signés
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
//    @Test
//    public void testStringTripleSlashError() throws IOException  {
//        String tokenExpected = "\"cha\\\\\\t\"";
//        //leve une exception car entier non codable sur 32 bits signés
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
//    @Test
//    public void testString4SlashError() throws IOException  {
//        String tokenExpected = "\"cha\\\t\"";
//        //leve une exception car entier non codable sur 32 bits signés
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
    // **** FIN TESTS ERREUR STRING **** // 
    
    // *** TESTS ERREUR MULTILINE STRING **** //
    
//    @Test
//    public void testStringEOLError() throws IOException  {
//        String tokenExpected = "\n\"chat\"";
//        //leve une exception car caractère end of line au début du string
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
    // **** FIN TEST STRING MULTILINE **** // 
    
    
    // **** FLOAT TESTS D'ERREUR - FLOATHEX **** //
    
//    @Test
//    public void testFloatHexWithout0xError() throws IOException  {
//        String tokenExpected = "\"05f.36ep+5f\"";
//        //leve une exception car pas de 0x au début du float
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
//    @Test
//    public void testFloatHexWithoutxError() throws IOException  {
//        String tokenExpected = "\"005f.36ep+5f\"";
//        //leve une exception car pas de x au début du float
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
    @Test
    public void testFloatHexWithout0Error() throws IOException  {
        String tokenExpected = "x05f.36ep+5f";
        //leve une exception car pas de 0 au début du 0x du float
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertNotEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());
    }
    
    @Test
    public void testFloatHexx0Error() throws IOException  {
        String tokenExpected = "x005f.36ep+5f";
        //leve une exception car pas de 0x au début du float
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertNotEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());
    }
    
    @Test
    public void testFloatHexPError() throws IOException  {
        String tokenExpected = "0x05f.36ee+5f";
        //leve une exception car e de exponentielle au lieu du p de la puissance
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertNotEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());
    }
    
//    @Test
//    public void testFloatHexCParError() throws IOException  {
//        String tokenExpected = "\"0x05f.36ep+(5f)\"";
//        //leve une exception car parentheses autour de l'argument de puissance
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
//    @Test
//    public void testFloatHexNoNumError() throws IOException  {
//        String tokenExpected = "\"0x05f.36ep+f\"";
//        //leve une exception car pas de num autour de l'argument de puissance
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
    
//    @Test
//    public void testFloatHexTwofError() throws IOException  {
//        String tokenExpected = "\"0x05f.36epff\"";
//        //leve une exception car pas de num autour de l'argument de puissance
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
        // **** FIN TESTS D'ERREUR FLOATHEX **** // 
    
    
        // **** FLOAT TESTS D'ERREUR - FLOATDEC **** //

    @Test
    public void testFloatDecCommaError() throws IOException  {
        String tokenExpected = "1,2"; 
        //leve une exception car contient une virgule
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertNotEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());
    }
    
//     @Test
//    public void testNumDotError() throws IOException  {
//        String tokenExpected = "2.";
//        //leve une exception car contient une virgule
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
    
//      @Test
//    public void testDecExpNoNumError() throws IOException  {
//        String tokenExpected = "2.3fe+";
//        //leve une exception car pas d'argument à la fonction exponentielle
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
//
//      @Test
//    public void testDecExpWrongSignError() throws IOException  {
//        String tokenExpected = "2.3fe*2";
//        //leve une exception car mauvais signe dans l'exponentielle
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
       
    @Test
    public void testFloatOParError() throws IOException  {
        String tokenExpected = "2.3fe(+2"; 
        //leve une exception car contient une parenthèse ouvrante
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));

        assertNotNull("le resultat du lexer est nul pour"+tokenExpected ,lex);

        //création de la liste de string correspondant aux tokens donnés par le lexer
        ArrayList<String> tokensList = this.TokensToStrings(lex);
        // check de la taille
        assertNotEquals("la taille est fausse pour "+tokenExpected , 1, tokensList.size());
    }

//        @Test
//    public void testFloatExpWrongLetterError() throws IOException  {
//        String tokenExpected = "2.3g";
//        //leve une exception car contient une parenthèse ouvrante
//        Logger.getRootLogger().setLevel(Level.DEBUG);
//        try {
//            DecaLexer lex = new DecaLexer(CharStreams.fromString(tokenExpected));
//        } catch(DecaRecognitionException te) {
//            assertTrue(true);
//            return ;
//        }
//        assertTrue(false);
//    }
    
        // **** FIN TESTS D'ERREUR FLOATDEC **** // 
    
    // **** FIN TESTS D'ERREUR FLOAT **** // 
    
}
