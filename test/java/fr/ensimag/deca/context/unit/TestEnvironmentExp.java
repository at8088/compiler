/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.context.unit;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mortbay.log.Log;

/**
 * Unit tests for EnvironmentExp class
 * 
 * @author ricletm
 */
public class TestEnvironmentExp {
    // A mettre en mode Mock comme les autres tests ?
    @Test
    public void testEnv() throws DoubleDefException {
        SymbolTable t = new SymbolTable();
        Symbol s1 = t.create("s1");
        
        EnvironmentExp e = new EnvironmentExp(null);
        
        Symbol t1 = t.create("t1");
        IntType type = new IntType(t1); // Creation d'un type int pour le test
        Location loc = new Location(1, 1, "test"); // Location totalement random pour le test
        ParamDefinition def = new ParamDefinition(type, loc); // Au pif, on dit qu'on considère un paramètre
        
        e.declare(s1, def);
        
        assertEquals(e.get(s1), def);
        assertTrue(e.getCurrent().containsKey(s1));
        assertTrue(e.getCurrent().containsValue(def));
        
        try {
            e.declare(s1, def); //verifie que soulève bien une DoubleDefException
            System.out.println("popopop");
            assert(false);
        } catch(DoubleDefException doubleDef) {
            
        }
    }
    
    @Test
    public void testType() {
        SymbolTable t = new SymbolTable();
        Symbol bool = t.create("bool");
        Symbol booltest = t.create("booltest");
        Symbol i = t.create("int");
        
        Type tbool = new BooleanType(bool);
        Type tboolref = new BooleanType(booltest);
        Type tint = new IntType(i);
        
        assertTrue(tbool.sameType(tboolref));
        assertFalse(tbool.sameType(tint));
    }
    
    @Test
    public void testEnvType() {
        // Test super shlag
        HashMap<String, TypeDefinition> env_types = new HashMap<String, TypeDefinition>();
        SymbolTable t = new SymbolTable();
        env_types.put("void", 
                    new TypeDefinition(new VoidType(t.create("void")), null));
        env_types.put("boolean",
                    new TypeDefinition(new BooleanType(t.create("boolean")), null));
        env_types.put("float",
                    new TypeDefinition(new FloatType(t.create("float")), null));
        env_types.put("int",
                    new TypeDefinition(new IntType(t.create("int")), null));
        assertEquals(env_types.get("void").getNature(), "type");
        assertEquals(env_types.get("boolean").getNature(), "type");
        assertEquals(env_types.get("float").getNature(), "type");
        assertEquals(env_types.get("int").getNature(), "type");
        assertTrue(env_types.get("void").getType().isVoid());
        assertTrue(env_types.get("boolean").getType().isBoolean());
        assertTrue(env_types.get("float").getType().isFloat());
        assertTrue(env_types.get("int").getType().isInt());
    }
}
