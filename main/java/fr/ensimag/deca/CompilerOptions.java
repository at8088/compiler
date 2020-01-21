package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl22
 * @date 01/01/2020
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }
    
    public static final int ALL = 0;
    public static final int PARSE = 1;
    public static final int VERIF = 2;
    public int getWhereStop() {
        return whereStop;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }
    
    public boolean getNoCheck() {
        return noCheck;
    }
    
    public int getNbRegisters() {
        return nbRegisters;
    }
    
    public boolean getAllowWarnings() {
        return allowWarnings;
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private int whereStop = 0;
    private boolean noCheck = false;
    private int nbRegisters = 0; // 0 means no registers limit
    private boolean allowWarnings = false;
    
    public boolean isArg(Iterator<String> args, String arg) {
        while (args.hasNext()) {
            String a = args.next();
            if (a.equals(arg)) {
                return true;
            }
        }
        return false;
    }
    
    public void parseArgs(String[] args) throws CLIException {
        Iterable<String> list = Arrays.asList(args);
        Iterator<String> arguments = list.iterator();
        int cptDebug = 0;
        
        if (args.length == 1 && args[0].equals("-b")) { // decac -b
            this.printBanner = true;
        } else if (args.length > 1 && this.isArg(list.iterator(), "-b")) {
            throw new CLIException("arg -b must be used alone");
        } else { // Else we analized args
            while (arguments.hasNext()) {
                String arg = arguments.next();
                // Options parse
                if (arg.equals("-p")) {
                    if (this.isArg(list.iterator(), "-v")) { 
                             throw new CLIException("args -v et -p can't "
                                                    + "be used at same time");
                    }
                    this.whereStop = PARSE;
                // Option verification
                } else if (arg.equals("-v")) {
                    if (this.isArg(list.iterator(), "-p")) {
                             throw new CLIException("args -v et -p can't be "
                                                    + "used at same time");
                    }
                    this.whereStop = VERIF;
                // Option no Check
                } else if (arg.equals("-n")) {
                    this.noCheck = true;
                // Option registers
                } else if (arg.equals("-r")) {
                    if (arguments.hasNext()) {
                        arg = arguments.next();
                        try {
                            int i = Integer.parseInt(arg);
                        } catch (NumberFormatException e) {
                            throw new CLIException("Must give an int after arg -r");
                        }
                        if (Integer.parseInt(arg) >= 4 && 
                                Integer.parseInt(arg) <= 16) {
                            this.nbRegisters = Integer.parseInt(arg);
                        } else {
                            throw new CLIException("option -r X : X must be "
                                                + "included between 4 and 16");
                        }
                    } else {
                        throw new CLIException("must put a number of register "
                                                + "after option -r");
                    }
                // Option Debug
                } else if (arg.equals("-d")) {
                    if (cptDebug!=3) {
                        cptDebug++;
                        // Necessite pas que les -d soient à coté.
                    }
                // Option Parallel
                } else if (arg.equals("-P")) {
                    this.parallel = true;
                // Option AllowWarnings
                } else if (arg.equals("-w")) {
                    this.allowWarnings = true;   
                // Recognize files
                } else if (Pattern.matches(".*\\.deca", arg)) { 
                    // Verify if file not written multiple times in the command
                    File file = new File(arg);
                    if (!this.sourceFiles.contains(file)) {
                        this.sourceFiles.add(file);
                    }
                // Mistake in the arguments given
                } else {
                    throw new CLIException(arg + "not an option or a file name");
                    }   
                }
                this.debug = cptDebug; // We have count how many -d have been
                                       // given, now we put the right debug mode

                if (this.parallel && this.sourceFiles.size() <= 1) {
                    // It's useless to activate option parallel
                    this.parallel = false;
                }
        }
        
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
            case QUIET: break; // keep default
            case INFO:
                logger.setLevel(Level.INFO); break;
            case DEBUG:
                logger.setLevel(Level.DEBUG); break;
            case TRACE:
                logger.setLevel(Level.TRACE); break;
            default:
                logger.setLevel(Level.ALL); break;
        }
    }

    protected void displayUsage() {
        System.out.println("\nHow to use me : --------------------------------------------------------------\n");
        System.out.println("-b       (banner)        :      affiche une bannière indiquant le nom de l’équipe");
        System.out.println("-p       (parser)        :      arrête decac après l’étape de construction de" );
        System.out.println("                                l’arbre, et affiche la décompilation de ce dernier" );
        System.out.println("                                (i.e. s’il n’y a qu’un fichier source à" );
        System.out.println("                                compiler, la sortie doit être un programme" );
        System.out.println("                                deca syntaxiquement correct)");
        
        System.out.println("-v       (verification)  :      arrête decac après l’étape de vérifications" );
        System.out.println("                                (ne produit aucune sortie en l’absence d’erreur)");
        System.out.println("-n       (no check)      :      supprime les tests de débordement à l’exécution" );
        System.out.println("                                    - débordement arithmétique" );
        System.out.println("                                    - débordement mémoire" );
        System.out.println("                                    - déréférencement de null");
        System.out.println("-r X     (registers)     :      limite les registres banalisés disponibles à" );
        System.out.println("                                R0 ... R{X-1}, avec 4 <= X <= 16");
        System.out.println("-d       (debug)         :      active les traces de debug. Répéter" );
        System.out.println("                                l’option plusieurs fois pour avoir plus de" );
        System.out.println("                                traces.");
        System.out.println("-P       (parallel)      :      s’il y a plusieurs fichiers sources," );
        System.out.println("                                lance la compilation des fichiers en" );
        System.out.println("                                parallèle (pour accélérer la compilation)\n");
        System.out.println("------------------------------------------------------------------------------");
    }
}
