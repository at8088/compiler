package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl22
 * @date 01/01/2020
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
//            System.out.println(" GL22\n"
//                    +           "  ____ _     ____  ____  \n" +
//                               " / ___| |   |___ \\|___ \\\n" +
//                               "| |  _| |     __) | __) |\n" +
//                               "| |_| | |___ / __/ / __/\n" +
//                              " \\____|_____|_____|_____|");
            System.out.println("GL22");
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
            System.out.println("Options available : "
                + "[[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...]"
                + " | [-b]");
            System.out.println("Don't forget to put at least one file !");
            System.exit(0);
        }
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            // throw new UnsupportedOperationException("Parallel build not yet implemented");
            // TODO : version shlag ci dessous
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
