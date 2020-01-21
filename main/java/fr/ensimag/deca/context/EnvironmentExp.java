package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Main;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import fr.ensimag.deca.tree.Or;
import org.apache.log4j.Logger;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl22
 * @date 01/01/2020
 */
public class EnvironmentExp {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    private EnvironmentExp parent;
    private HashMap <Symbol ,ExpDefinition> current ;
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parent = parentEnvironment;
        this.current = new HashMap<>(); 
    }


    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     * 
     * @param key
     *            Symbol ton find
     * 
     * @return
     *            Definition associated with the Symbol in this environment
     */
    public ExpDefinition get(Symbol key) {
        LOG.trace("key == " + current.get(key));
        if(current.containsKey(key)){
            return current.get(key);
        }else if(parent != null && parent.getCurrent().containsKey(key)) {
            return parent.getCurrent().get(key);
        }else{
            return null;
        }
    }

    public boolean containsKey(Symbol key) {
        return current.containsKey(key);
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        // Nom de symbole unique car défini ainsi dans la table des symboles
        // Seulement si le symbole est dans l'env courant, sinon il cache les
        // Définitions précédentes
        
        if (current.containsKey(name)) {
            throw new DoubleDefException();
        } else {
            current.put(name, def);
        }
    }
    
    public HashMap<Symbol ,ExpDefinition> getCurrent(){
        return current;
    }

    public EnvironmentExp getParent() {
        return parent;
    }
    
    public void setParent(EnvironmentExp parent) {
        this.parent = parent;
    }
    
    /**
     * Empile l'environnement env1 et env2 selon la définition du poly
     * L'opération est notée: env1 / env2
     * @param env1
     * @param env2
     * @return 
     */
    public static EnvironmentExp stack(EnvironmentExp env1, EnvironmentExp env2) {
        // Si env1 est null on créer un nouvel environment vide avec les même 
        EnvironmentExp newEnv = new EnvironmentExp(null);
        if (env1 == null) {
            return newEnv;
        }
        // On créer un nouvel envrironnement avec le même parent
        newEnv.setParent(env1.parent);
        // ajout de env1 au sommet du nouvel env
        for (SymbolTable.Symbol s : env1.current.keySet()) {
            try {
                newEnv.declare(s, newEnv.get(s));
            } catch (DoubleDefException ex) {
                /* On ne fait rien */
            }
        }
        
        if (env2 != null) {
            // ajout de env2 au sommet du nouvel env
            for (SymbolTable.Symbol s : env2.current.keySet()) {
                try {
                    newEnv.declare(s, newEnv.get(s));
                } catch (DoubleDefException ex) {
                    /* On ne fait rien */
                }
            }
        }
        return newEnv;
    }

    @Override
    public String toString() {
        String str = "";
        for (SymbolTable.Symbol s : current.keySet()) {
            str += s + " -> " + current.get(s) + "\n";
        }
        
        return str;
    }
}
