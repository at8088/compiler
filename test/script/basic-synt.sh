    #! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2020

# Test minimaliste de la syntaxe.
# On lance test_synt sur un fichier valide, et les tests invalides.

# dans le cas du fichier valide, on teste seulement qu'il n'y a pas eu
# d'erreur. Il faudrait tester que l'arbre donné est bien le bon. Par
# exemple, en stoquant la valeur attendue quelque part, et en
# utilisant la commande unix "diff".
#
# Il faudrait aussi lancer ces tests sur tous les fichiers deca
# automatiquement. Un exemple d'automatisation est donné avec une
# boucle for sur les tests invalides, il faut aller encore plus loin.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Fonction for invalid tests
test_synt_invalide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "."
    else
        echo "! ERROR $1."
        # exit 1
    fi
}   

# Fonction for valid tests
test_synt_valid () {
    if test_synt $1 2>&1 | \
    grep -q -e ':[0-9][0-9]*:'
then
    echo "Echec inattendu pour test_synt sur $1"
    # exit 1
else
    test_decompile $1
    if diff src/test/deca/syntax/valid/resultsDecompile/test1.deca \
        src/test/deca/syntax/valid/resultsDecompile/test2.deca | \
        grep -q -e '.*'
    then
        echo "! ERROR $1"
    else
        echo "."
    fi
fi
}

for cas_de_test_invalide in src/test/deca/syntax/invalid/*.deca
do
    test_synt_invalide "$cas_de_test_invalide"
done

#for cas_de_test_valide in src/test/deca/syntax/valid/*.deca
#do
#    test_synt_valid "$cas_de_test_valide"
#done
