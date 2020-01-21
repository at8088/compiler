/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.syntax.AbstractDecaLexer;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Obsolète, à utiliser avec précaution
 * Cette classe génére un code indépendamment des étapes A et B
 * pour l'utiliser il faut changer la visibilité de la méthode setType de
 * AbstractExpr en public.
 * @author tahiria
 */
public class ManualCodeGenTest {
    public static void main(String args[]) throws IOException{
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        ListExpr lExpr = new ListExpr();
        lExpr.add(new StringLiteral("Testing "));
        float f = (float)666.999;
        FloatLiteral flo = new FloatLiteral(f);
        BooleanLiteral b = new BooleanLiteral(true);
        SymbolTable table = new SymbolTable();
        flo.setType(new FloatType(table.create(""+f)));
        b.setType(new BooleanType(table.create(" true")));
        lExpr.add(flo);
        lExpr.add(b);
        ListInst lInst = new ListInst();
        AbstractInst prnt = new Println(false, lExpr);
        lInst.add(prnt);
        ListDeclVar lDecVar = new ListDeclVar();
        Identifier type = new Identifier(table.create("int"));
        Identifier varName = new Identifier(table.create("x"));
        IntLiteral entier1 = new IntLiteral(58);
        IntLiteral entier2 = new IntLiteral(100);
        IntLiteral entier3 = new IntLiteral(77);
        AbstractExpr plus1 = new Plus(entier1,entier2);
        AbstractExpr plus2 = new Plus(entier1,plus1);
        type.setType(new IntType(table.create("69")));
        Initialization init = new Initialization(plus2);
        lDecVar.add(new DeclVar(type,varName,init));
        ListDeclClass lDeclClass = new ListDeclClass();
        AbstractMain main = new Main(lDecVar, lInst);
        Program prog = new Program(lDeclClass,main );
        prog.codeGenProgram(compiler);
        System.out.println(compiler.getProgram().display());
    }
}
