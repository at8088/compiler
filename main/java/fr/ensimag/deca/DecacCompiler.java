package fr.ensimag.deca;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;



/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl22
 * @date 01/01/2020
 */
public class DecacCompiler {
    /**
     * COMPILER PROPERS FIELDS
     */
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
        /** Options du compiler */
    private final CompilerOptions compilerOptions;
        /** Fichier à compiler */
    private final File source;  
        /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");
    
    /**
     * ATTRIBUTS POUR ETAPE B
    */
        /** For management of environmentTypes */
    private final HashMap<String, TypeDefinition> envTypes = new HashMap<>();
        /** Symbol Table for creating types in envTypes */
    private static final SymbolTable symbolTable = new SymbolTable();
    
    /**
     * Attributs de classe définissant les types prédéfinis
     */
    public static final String INT = "int";    
    public static final String FLOAT = "float";
    public static final String BOOLEAN = "boolean";
    public static final String VOID = "void";
    public static final String STRING = "string";
    public static final String OBJECT = "Object";
    public static final String NULL = "null";

    /**
     * ATTRIBUTS POUR ETAPE C
     */
    
    /** Pour l'empilement (TSTO) */
            /** Nbr de registres sauvegardés au début du bloc */
    private int savedRegsCpt = 0 ;
        /** Nbr variables globales et empilement dans la pile */
    private int declVarNbr;
        /** Nbr de variables locales */
    private int localVarNbr = 0;
        /** Nbr de paramètre des méthodes appellées */
    private int paramCpt = 0;
        /** Nbr d'instructions BSR */
    private int bsrNbr = 0 ;
    
    /** Gestion de la table des methodes */
        /** Stockage des adresses (RegisterOffset) de chaque vTable d'une classe
        * "String" */
    private HashMap<String, RegisterOffset> tableAddrClasses = new HashMap<>();
        /** Compteur Indice du prochaine empilement dans la vTable */
    private int indiceDansPile = 1;
        /** Compteur des débuts de chaque vTable */
    private int cptVTable = 1;
        /** Nbr de registres pour la sauvegarde des paramètre d'une méthode
         * (MethodBody) */
    private int nbrRegs = 0;
    
    /** Gestion des registres */
        /** pour savoir quels registres sont alloués */
    private boolean[] usedReg ;
        /** pour savoir quels registres ont été sauvegardé dans la pile */
    private boolean[] regsSavedPushedInStack;
    
        /** La super classe Object de DECA */
    public static SuperObject superObjet = new SuperObject(
            new Identifier(DecacCompiler.symbolTable.create("Object")),
                            null,null,null);
    
        /** Compteur nbr de Or */
    private int condOrNbr;
        /** Compteur nbr de And */
    private int condAndNbr;
        /** Pour readInt et readFloat */
    private AbstractExpr exprAllocR1;
        /** pour stocker le nombre d'instructions conditionnelles (if et while)
        * pour générer les étiquettes */
    private int condBlocksNbr;

    public int getParamCpt() {
        return paramCpt;
    }

    public int getBsrNbr() {
        return bsrNbr;
    }

    public void incBsrNbr() {
        this.bsrNbr ++;
    }

    public void incParamCpt(int incr) {
        this.paramCpt += incr;
    }

    public HashMap<String, RegisterOffset> getTableAddrClasses() {
        return tableAddrClasses;
    }

    public int getLocalVarNbr() {
        return localVarNbr;
    }

    public int getNbrRegs() {
        return nbrRegs;
    }

    public void incNbrRegs() {
        this.nbrRegs ++;
    }

    public void incLocalVarNbr(int incr) {
        this.localVarNbr += incr;
    }

    public int getIndiceDansPile() {
        return indiceDansPile;
    }

    public void setCptVTable(int cptVTable) {
        this.cptVTable = cptVTable;
    }

    public void incIndiceDansPile(int incr) {
        this.indiceDansPile += incr;
    }

    /*public SuperObject getSuperObjet() {
        return superObjet;
    }*/

    public int getCptVTable() {
        return cptVTable;
    }

    public boolean[] getUsedReg() {
        return usedReg;
    }

    public boolean[] getRegsSavedPushedInStack() {
        return regsSavedPushedInStack;
    }

    public void setExprAllocR1(AbstractExpr exprAllocR1) {
        this.exprAllocR1 = exprAllocR1;
    }

    public int getCondBlocksNbr() {
        return condBlocksNbr;
    }

    public int getCondOrNbr() {
        return condOrNbr;
    }
    
    public int getDeclVarNbr() {
        return declVarNbr;
    }
    
    public void incDeclVarNbr(){
        this.declVarNbr++;
    }

    public void incOrNbr(){
        this.condOrNbr++;
    }

    public AbstractExpr getExprAllocR1() {
        return exprAllocR1;
    }
    
    public void addArithOverFlowCheck(){
        if( !getCompilerOptions().getNoCheck() ){
            this.addInstruction(new BOV(new Label("debordement_arith")));
        }
    }
    
    public void addInvalideInputCheck(){
        this.addInstruction(new BOV(new Label("entree_invalide")));
    }
    public int getNextFreeReg(AbstractExpr expr)  throws DecacInternalError{
        // pour le langage complet i est init à 2
        // R0 et R1 pour les appels de fct éventuels
        for(int i = 2 ; i < usedReg.length ; i++){
            if(! usedReg[i]){
                if(i == 1) setExprAllocR1(expr);
                usedReg[i] = true;
                return i;
            }
        }
        for(int i = 2 ; i < this.regsSavedPushedInStack.length ; i++){
            if(!regsSavedPushedInStack[i]){
                if(i == 1) setExprAllocR1(expr);
                this.savedRegsCpt++;
                regsSavedPushedInStack[i]=true;
                this.addInstruction(new PUSH(Register.getR(i)));
                return i;
            }
        }
        throw new DecacInternalError("Tout les registres sont utilisés.");
    }

    public int getSavedRegsCpt() {
        return savedRegsCpt;
    }

    public void incSavedRegsCpt() {
        this.savedRegsCpt ++;
    }

    public void decSavedRegsCpt() {
        this.savedRegsCpt --;
    }

    public void freeReg(int regIndex){
        if(regsSavedPushedInStack[regIndex]){
            regsSavedPushedInStack[regIndex] = false;
            this.savedRegsCpt--;
            this.addInstruction(new POP(Register.getR(regIndex)));
        }else{
            this.usedReg[regIndex] = false;
        }
    }
 
    public void incCondBlocksNbr(){
        this.condBlocksNbr++;
    }

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.condBlocksNbr = 0;
        this.condOrNbr = 0;
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.declVarNbr = 0;
        // Au cas où on a pas de compilerOptions, si c'est le cas on n'initialise pas les usedReg etc
        // Nécessaire pour les tests
        if(this.compilerOptions != null) {
            if (this.compilerOptions.getNbRegisters() == 0) {
                this.usedReg = new boolean[16];
                this.regsSavedPushedInStack = new boolean[16];
            } else {
                this.regsSavedPushedInStack = new boolean[this.compilerOptions.getNbRegisters()];
                this.usedReg = new boolean[this.compilerOptions.getNbRegisters()];
            }
            for (int i = 0; i < this.usedReg.length; i++) {
                this.usedReg[i] = false;
                this.regsSavedPushedInStack[i] = false;
            }
        }

        this.envTypes.put(DecacCompiler.VOID, new TypeDefinition(
                new VoidType(symbolTable.create(DecacCompiler.VOID)), 
                Location.BUILTIN));
        
        this.envTypes.put(DecacCompiler.BOOLEAN, new TypeDefinition(
                new BooleanType(symbolTable.create(DecacCompiler.BOOLEAN)), 
                Location.BUILTIN));
        
        this.envTypes.put(DecacCompiler.FLOAT, new TypeDefinition(
                new FloatType(symbolTable.create(DecacCompiler.FLOAT)), 
                Location.BUILTIN));
        
        this.envTypes.put(DecacCompiler.INT, new TypeDefinition(
                new IntType(symbolTable.create(DecacCompiler.INT)), 
                Location.BUILTIN));
        
        this.envTypes.put(DecacCompiler.STRING, new TypeDefinition(
                new StringType(symbolTable.create(DecacCompiler.STRING)), 
                Location.BUILTIN));
        
        this.envTypes.put(DecacCompiler.NULL, new TypeDefinition(
                new NullType(symbolTable.create(DecacCompiler.NULL)), 
                Location.BUILTIN));
        
        this.envTypes.put(DecacCompiler.OBJECT, new ClassDefinition(
                new ClassType(symbolTable.create(DecacCompiler.OBJECT), Location.BUILTIN, null), 
                Location.BUILTIN, null));
    }

    /**
     * Source file associated with this compiler instance.
     * @return File source
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     * @return Compiler options
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     * @param line 
     *          line to add
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     * @param comment
     *          comment to add
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     * @param label
     *          label to add
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     * @param instruction
     *          instruction to add
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     * @param instruction
     *          instruction to add
     * @param comment
     *          comment to add
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     * @return affichage du programme
     */
    public String displayIMAProgram() {
        return program.display();
    }

    public IMAProgram getProgram() {
        return program;
    }

    public HashMap<String, TypeDefinition> getEnvTypes() {
        return envTypes;
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = sourceFile.replace(".deca", ".ass");

        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        // Etape A
        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());
        
        if (this.compilerOptions.getWhereStop() == CompilerOptions.PARSE) {
            System.out.println(prog.decompile());
        } else {

            // Etape B
            prog.verifyProgram(this);
            assert(prog.checkAllDecorations());
            
            if (this.compilerOptions.getWhereStop() != CompilerOptions.VERIF) {
                addComment("start main program");
                prog.codeGenProgram(this);
                addComment("end main program");
                LOG.debug("Generated assembly code:" + nl + program.display());
                LOG.info("Output file assembly file is: " + destName);

                FileOutputStream fstream = null;
                try {
                    fstream = new FileOutputStream(destName);
                } catch (FileNotFoundException e) {
                    throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
                }

                LOG.info("Writing assembler file ...");

                program.display(new PrintStream(fstream));
                LOG.info("Compilation of " + sourceName + " successful.");
            } else {
                System.out.println(prog.prettyPrint());
            }
        }
        return false;        
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws DecacFatalError When a compilation error (incorrect program)
     * occurs.
     * @throws DecacInternalError When
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }
    
    /**
     * Permet de simplifier la récupération d'un type
     * @param type type
     * @return type
     */
    public Type getTypeFromString(String type) {
        if (envTypes.get(type) == null) {
            throw new InternalError("Le type `" + type + "` n'existe pas");
        }
        return envTypes.get(type).getType();
    }
    
    public ClassDefinition getClassDefFromString(String type) {
        if (envTypes.get(type) == null) {
            throw new InternalError("Le type `" + type + "` n'existe pas");
        } else if (!envTypes.get(type).isClass()) {
            throw new InternalError("la classe `" + type + "` n'existe pas");
        }
        return (ClassDefinition) envTypes.get(type);
    }
    
    /**
     * associe le symbole au nom d'une classe et identifieur permet de trouver 
     * la classe mère etc
     * @param symbol
     * @param location
     * @param superClass
     */
    public void declareClass(SymbolTable.Symbol symbol, Location location, 
            ClassDefinition superClass) {
        envTypes.put(symbol.getName(), 
                new ClassType(symbol, location, superClass).getDefinition());
    }

    public int getCondAndNbr() {
        return condAndNbr;
    }

    public void incCondAndNbr() {
        condAndNbr++;
    }
}
