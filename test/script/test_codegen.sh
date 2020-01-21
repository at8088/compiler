#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2020

# Encore un test simpliste. On compile un fichier (cond0.deca), on
# lance ima dessus, et on compare le résultat avec la valeur attendue.

# Ce genre d'approche est bien sûr généralisable, en conservant le
# résultat attendu dans un fichier pour chaque fichier source.
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

 decac ./src/test/deca/codegen/valid/infeq_float_int.deca || exit 1
 if [ ! -f ./src/test/deca/codegen/valid/infeq_float_int.ass ]; then
     echo "Fichier add_float.ass non généré."
     exit 1
 fi
 # On code en dur la valeur attendue.
 attendu="true"
 echo "$attendu"
 resultat=$(ima ./src/test/deca/codegen/valid/infeq_float_int.ass) || exit 1
 echo "$resultat"
 rm -f ./src/test/deca/codegen/valid/infeq_float_int.ass



 if [ "$resultat" = "$attendu" ]; then
     echo "Tout va bien"
 else
     echo "Résultat inattendu de ima:"
     echo "$resultat"
     exit 1
 fi