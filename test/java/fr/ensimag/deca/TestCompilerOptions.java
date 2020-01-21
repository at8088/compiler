/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mortbay.log.Log;

/**
 * Tests de bonne reconnaissance des options et des arguments 
 * @author ricletm
 * (Test uniquement la class CompilerOptions)
 */
public class TestCompilerOptions {
    @Test
    public void testIsArg() {
        CompilerOptions compilerTest = new CompilerOptions();
        String[] args = {"-b", "-p", "-n"};
        Iterable<String> list = Arrays.asList(args);
        Iterator<String> arguments = list.iterator();
        String arg1 = "-p";
        String arg2 = "-v";
        assertTrue(compilerTest.isArg(arguments, arg1));
        assertFalse(compilerTest.isArg(arguments, arg2));
    }
    
    @Test
    public void testPattern() {
        String file = "hello.deca";
        String random = "hellodeca";
        assertTrue(Pattern.matches(".*\\.deca", file));
        assertFalse(Pattern.matches(".*\\.deca", random));
    }
    
    @Test
    public void testParseArgBanner() throws CLIException {
        // decac -b
        CompilerOptions compilerOptions1 = new CompilerOptions();
        String[] args1 = {"-b"};
        compilerOptions1.parseArgs(args1);
        assertTrue(compilerOptions1.getPrintBanner());
        assertFalse(compilerOptions1.getParallel());
        assertFalse(compilerOptions1.getAllowWarnings());
        assertEquals(compilerOptions1.getDebug(), compilerOptions1.QUIET);
        assertTrue(compilerOptions1.getSourceFiles().isEmpty());
        assertEquals(compilerOptions1.getWhereStop(), 0);
        assertFalse(compilerOptions1.getNoCheck());
        assertEquals(compilerOptions1.getNbRegisters(), 0);
        
        // decac -b -p
        CompilerOptions compilerOptions2 = new CompilerOptions();
        boolean hasCatchExcep = false;
        String[] args2 = {"-b", "-p"};
        try {
            compilerOptions2.parseArgs(args2);
        } catch (CLIException e) {
            // Verification atributs du CompilerOption
            assertFalse(compilerOptions2.getPrintBanner());
            assertFalse(compilerOptions2.getParallel());
            assertFalse(compilerOptions2.getAllowWarnings());
            assertEquals(compilerOptions2.getDebug(), compilerOptions2.QUIET);
            assertTrue(compilerOptions2.getSourceFiles().isEmpty());
            assertEquals(compilerOptions2.getWhereStop(), 0);
            assertFalse(compilerOptions2.getNoCheck());
            assertEquals(compilerOptions2.getNbRegisters(), 0);
            // Verification qu'on récupère bien l'exception
            hasCatchExcep = true;
        }
        assertTrue(hasCatchExcep);
    }
    
    @Test
    public void testParseArgParseVerif() throws CLIException {
        // decac -p -v (impossible)
        CompilerOptions compilerOptions1 = new CompilerOptions();
        String[] args1 = {"-p", "-v"};
        boolean hasCatchExcep = false;
        try {
            compilerOptions1.parseArgs(args1);
        } catch (CLIException e) {
            assertFalse(compilerOptions1.getPrintBanner());
            assertFalse(compilerOptions1.getParallel());
            assertFalse(compilerOptions1.getAllowWarnings());
            assertEquals(compilerOptions1.getDebug(), compilerOptions1.QUIET);
            assertTrue(compilerOptions1.getSourceFiles().isEmpty());
            assertEquals(compilerOptions1.getWhereStop(), 0);
            assertFalse(compilerOptions1.getNoCheck());
            assertEquals(compilerOptions1.getNbRegisters(), 0);
            hasCatchExcep = true;
        }
        assertTrue(hasCatchExcep);
        
        // decac -p
        CompilerOptions compilerOptions2 = new CompilerOptions();
        String[] args2 = {"-p"};
        compilerOptions2.parseArgs(args2);
        assertFalse(compilerOptions2.getPrintBanner());
        assertFalse(compilerOptions2.getParallel());
        assertFalse(compilerOptions2.getAllowWarnings());
        assertEquals(compilerOptions2.getDebug(), compilerOptions2.QUIET);
        assertTrue(compilerOptions2.getSourceFiles().isEmpty());
        assertEquals(compilerOptions2.getWhereStop(), 1);
        assertFalse(compilerOptions2.getNoCheck());
        assertEquals(compilerOptions2.getNbRegisters(), 0);
        
        // decac -v
        CompilerOptions compilerOptions3 = new CompilerOptions();
        String[] args3 = {"-v"};
        compilerOptions3.parseArgs(args3);
        assertFalse(compilerOptions3.getPrintBanner());
        assertFalse(compilerOptions3.getParallel());
        assertFalse(compilerOptions3.getAllowWarnings());
        assertEquals(compilerOptions3.getDebug(), compilerOptions3.QUIET);
        assertTrue(compilerOptions3.getSourceFiles().isEmpty());
        assertEquals(compilerOptions3.getWhereStop(), 2);
        assertFalse(compilerOptions3.getNoCheck());
        assertEquals(compilerOptions3.getNbRegisters(), 0);
    }
    
    @Test
    public void testNoCheck() throws CLIException {
        CompilerOptions compilerOptions = new CompilerOptions();
        String[] args = {"-n"};
        compilerOptions.parseArgs(args);
        assertFalse(compilerOptions.getPrintBanner());
        assertFalse(compilerOptions.getParallel());
        assertFalse(compilerOptions.getAllowWarnings());
        assertEquals(compilerOptions.getDebug(), compilerOptions.QUIET);
        assertTrue(compilerOptions.getSourceFiles().isEmpty());
        assertEquals(compilerOptions.getWhereStop(), 0);
        assertTrue(compilerOptions.getNoCheck());
        assertEquals(compilerOptions.getNbRegisters(), 0);
    }
    
    @Test
    public void testRegister() throws CLIException {
        // decac -r 5 : 5 registres
        CompilerOptions compilerOptions1 = new CompilerOptions();
        String[] args1 = {"-r", "5"};
        compilerOptions1.parseArgs(args1);
        assertFalse(compilerOptions1.getPrintBanner());
        assertFalse(compilerOptions1.getParallel());
        assertFalse(compilerOptions1.getAllowWarnings());
        assertEquals(compilerOptions1.getDebug(), compilerOptions1.QUIET);
        assertTrue(compilerOptions1.getSourceFiles().isEmpty());
        assertEquals(compilerOptions1.getWhereStop(), 0);
        assertFalse(compilerOptions1.getNoCheck());
        assertEquals(compilerOptions1.getNbRegisters(), 5);
        
        // decac -r : must failed
        CompilerOptions compilerOptions2 = new CompilerOptions();
        String[] args2 = {"-r"};
        boolean hasCatchExcep = false;
        try {
            compilerOptions2.parseArgs(args2);
        } catch (CLIException e) {
            assertFalse(compilerOptions2.getPrintBanner());
            assertFalse(compilerOptions2.getParallel());
            assertFalse(compilerOptions2.getAllowWarnings());
            assertEquals(compilerOptions2.getDebug(), compilerOptions2.QUIET);
            assertTrue(compilerOptions2.getSourceFiles().isEmpty());
            assertEquals(compilerOptions2.getWhereStop(), 0);
            assertFalse(compilerOptions2.getNoCheck());
            assertEquals(compilerOptions2.getNbRegisters(), 0);
            hasCatchExcep = true;
        }
        assertTrue(hasCatchExcep);
        
        // decac -r 2 : must failed
        CompilerOptions compilerOptions3 = new CompilerOptions();
        String[] args3 = {"-r", "2"};
        hasCatchExcep = false;
        try {
            compilerOptions3.parseArgs(args3);
        } catch (CLIException e) {
            assertFalse(compilerOptions3.getPrintBanner());
            assertFalse(compilerOptions3.getParallel());
            assertFalse(compilerOptions3.getAllowWarnings());
            assertEquals(compilerOptions3.getDebug(), compilerOptions3.QUIET);
            assertTrue(compilerOptions3.getSourceFiles().isEmpty());
            assertEquals(compilerOptions3.getWhereStop(), 0);
            assertFalse(compilerOptions3.getNoCheck());
            assertEquals(compilerOptions3.getNbRegisters(), 0);
            hasCatchExcep = true;
        }
        assertTrue(hasCatchExcep);
        
        // decac -r 17 : must failed
        CompilerOptions compilerOptions4 = new CompilerOptions();
        String[] args4 = {"-r", "2"};
        hasCatchExcep = false;
        try {
            compilerOptions4.parseArgs(args4);
        } catch (CLIException e) {
            assertFalse(compilerOptions4.getPrintBanner());
            assertFalse(compilerOptions4.getParallel());
            assertFalse(compilerOptions4.getAllowWarnings());
            assertEquals(compilerOptions4.getDebug(), compilerOptions4.QUIET);
            assertTrue(compilerOptions4.getSourceFiles().isEmpty());
            assertEquals(compilerOptions4.getWhereStop(), 0);
            assertFalse(compilerOptions4.getNoCheck());
            assertEquals(compilerOptions4.getNbRegisters(), 0);
            hasCatchExcep = true;
        }
        assertTrue(hasCatchExcep);
    }
    
    @Test
    public void testDebug() throws CLIException {
        // decac -d : niveau INFO
        CompilerOptions compilerOptions1 = new CompilerOptions();
        String[] args1 = {"-d"};
        compilerOptions1.parseArgs(args1);
        assertFalse(compilerOptions1.getPrintBanner());
        assertFalse(compilerOptions1.getParallel());
        assertFalse(compilerOptions1.getAllowWarnings());
        assertEquals(compilerOptions1.getDebug(), compilerOptions1.INFO);
        assertTrue(compilerOptions1.getSourceFiles().isEmpty());
        assertEquals(compilerOptions1.getWhereStop(), 0);
        assertFalse(compilerOptions1.getNoCheck());
        assertEquals(compilerOptions1.getNbRegisters(), 0);
        
        // decac -d -d : niveau DEBUG
        CompilerOptions compilerOptions2 = new CompilerOptions();
        String[] args2 = {"-d", "-d"};
        compilerOptions2.parseArgs(args2);
        assertFalse(compilerOptions2.getPrintBanner());
        assertFalse(compilerOptions2.getParallel());
        assertFalse(compilerOptions2.getAllowWarnings());
        assertEquals(compilerOptions2.getDebug(), compilerOptions2.DEBUG);
        assertTrue(compilerOptions2.getSourceFiles().isEmpty());
        assertEquals(compilerOptions2.getWhereStop(), 0);
        assertFalse(compilerOptions2.getNoCheck());
        assertEquals(compilerOptions2.getNbRegisters(), 0);
        
        // decac -d -d -d : niveau TRACE
        CompilerOptions compilerOptions3 = new CompilerOptions();
        String[] args3 = {"-d", "-d", "-d"};
        compilerOptions3.parseArgs(args3);
        assertFalse(compilerOptions3.getPrintBanner());
        assertFalse(compilerOptions3.getParallel());
        assertFalse(compilerOptions3.getAllowWarnings());
        assertEquals(compilerOptions3.getDebug(), compilerOptions3.TRACE);
        assertTrue(compilerOptions3.getSourceFiles().isEmpty());
        assertEquals(compilerOptions3.getWhereStop(), 0);
        assertFalse(compilerOptions3.getNoCheck());
        assertEquals(compilerOptions3.getNbRegisters(), 0);
        
        // decac -d -d -d -d : niveau TRACE
        CompilerOptions compilerOptions4 = new CompilerOptions();
        String[] args4 = {"-d", "-d", "-d", "-d"};
        compilerOptions4.parseArgs(args4);
        assertFalse(compilerOptions4.getPrintBanner());
        assertFalse(compilerOptions4.getParallel());
        assertFalse(compilerOptions4.getAllowWarnings());
        assertEquals(compilerOptions4.getDebug(), compilerOptions4.TRACE);
        assertTrue(compilerOptions4.getSourceFiles().isEmpty());
        assertEquals(compilerOptions4.getWhereStop(), 0);
        assertFalse(compilerOptions4.getNoCheck());
        assertEquals(compilerOptions4.getNbRegisters(), 0);
    }
    
    @Test
    public void testParallel() throws CLIException {
        // decac -P
        CompilerOptions compilerOptions1 = new CompilerOptions();
        String[] args1 = {"-P"};
        compilerOptions1.parseArgs(args1);
        assertFalse(compilerOptions1.getPrintBanner());
        assertFalse(compilerOptions1.getParallel());
        assertFalse(compilerOptions1.getAllowWarnings());
        assertEquals(compilerOptions1.getDebug(), compilerOptions1.QUIET);
        assertTrue(compilerOptions1.getSourceFiles().isEmpty());
        assertEquals(compilerOptions1.getWhereStop(), 0);
        assertFalse(compilerOptions1.getNoCheck());
        assertEquals(compilerOptions1.getNbRegisters(), 0);
        
        // decac -P fichier_test1.deca fichier_test2.deca
        CompilerOptions compilerOptions2 = new CompilerOptions();
        String[] args2 = {"-P", "fichier_test1.deca", "fichier_test2.deca"};
        compilerOptions2.parseArgs(args2);
        assertFalse(compilerOptions2.getPrintBanner());
        assertTrue(compilerOptions2.getParallel());
        assertFalse(compilerOptions2.getAllowWarnings());
        assertEquals(compilerOptions2.getDebug(), compilerOptions2.QUIET);
        assertEquals(compilerOptions2.getSourceFiles().size(), 2);
        assertEquals(compilerOptions2.getWhereStop(), 0);
        assertFalse(compilerOptions2.getNoCheck());
        assertEquals(compilerOptions2.getNbRegisters(), 0);
        
        // decac -P fichier_test.deca fichier_test.deca
        CompilerOptions compilerOptions3 = new CompilerOptions();
        String[] args3 = {"-P", "fichier_test.deca", "fichier_test.deca"};
        compilerOptions3.parseArgs(args3);
        assertFalse(compilerOptions3.getPrintBanner());
        assertFalse(compilerOptions3.getParallel());
        assertFalse(compilerOptions3.getAllowWarnings());
        assertEquals(compilerOptions3.getDebug(), compilerOptions3.QUIET);
        assertEquals(compilerOptions3.getSourceFiles().size(), 1);
        assertEquals(compilerOptions3.getWhereStop(), 0);
        assertFalse(compilerOptions3.getNoCheck());
        assertEquals(compilerOptions3.getNbRegisters(), 0);
    }
    
    @Test
    public void testAllowWarnings() throws CLIException {
        CompilerOptions compilerOptions = new CompilerOptions();
        String[] args = {"-w"};
        compilerOptions.parseArgs(args);
        assertFalse(compilerOptions.getPrintBanner());
        assertFalse(compilerOptions.getParallel());
        assertTrue(compilerOptions.getAllowWarnings());
        assertEquals(compilerOptions.getDebug(), compilerOptions.QUIET);
        assertTrue(compilerOptions.getSourceFiles().isEmpty());
        assertEquals(compilerOptions.getWhereStop(), 0);
        assertFalse(compilerOptions.getNoCheck());
        assertEquals(compilerOptions.getNbRegisters(), 0);
    }
    
    @Test
    public void testFiles() throws CLIException {
        // decac fichier_test.deca
        CompilerOptions compilerOptions1 = new CompilerOptions();
        String[] args1 = {"fichier_test.deca"};
        compilerOptions1.parseArgs(args1);
        assertFalse(compilerOptions1.getPrintBanner());
        assertFalse(compilerOptions1.getParallel());
        assertFalse(compilerOptions1.getAllowWarnings());
        assertEquals(compilerOptions1.getDebug(), compilerOptions1.QUIET);
        assertEquals(compilerOptions1.getWhereStop(), 0);
        assertFalse(compilerOptions1.getNoCheck());
        assertEquals(compilerOptions1.getNbRegisters(), 0);
        assertEquals(compilerOptions1.getSourceFiles().get(0), 
                new File("fichier_test.deca"));
        assertEquals(compilerOptions1.getSourceFiles().size(), 1);
        
        // decac fichier_test1.deca fichier_test2.deca
        CompilerOptions compilerOptions2 = new CompilerOptions();
        String[] args2 = {"fichier1_test.deca", "fichier2_test.deca"};
        compilerOptions2.parseArgs(args2);
        assertFalse(compilerOptions2.getPrintBanner());
        assertFalse(compilerOptions2.getParallel());
        assertFalse(compilerOptions2.getAllowWarnings());
        assertEquals(compilerOptions2.getDebug(), compilerOptions2.QUIET);
        assertEquals(compilerOptions2.getWhereStop(), 0);
        assertFalse(compilerOptions2.getNoCheck());
        assertEquals(compilerOptions2.getNbRegisters(), 0);
        assertEquals(compilerOptions2.getSourceFiles().get(0), 
                new File("fichier1_test.deca"));
        assertEquals(compilerOptions2.getSourceFiles().get(1), 
                new File("fichier2_test.deca"));
        assertEquals(compilerOptions2.getSourceFiles().size(), 2);
    }
}
