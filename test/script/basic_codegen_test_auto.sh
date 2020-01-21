#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2020

# Encore un test simpliste. On compile un fichier (cond0.deca), on
# lance ima dessus, et on compare le résultat avec la valeur attendue.

# Ce genre d'approche est bien sûr généralisable, en conservant le
# résultat attendu dans un fichier pour chaque fichier source.
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

is_error=0
error_files=""
nb_test=0
nb_error=0

for cas_de_test in src/test/deca/codegen/valid/*.deca
do
    expected="$(grep -oP '(?<=//RESULT:).*' $cas_de_test)"
    namefile="$(basename $cas_de_test .deca)"
    ./src/main/bin/decac "$cas_de_test" || exit 1
    # Compilation
    src/main/bin/decac $cas_de_test
    
    assfile="src/test/deca/codegen/valid/${namefile}.ass"

    if [[ ! -f "$assfile" ]]
    then
        printf !
        error_files+="ERROR COMPILATION $cas_de_test\n"
        is_error=1
        nb_error=$((nb_error+1))
    else
        resultat="$(ima $assfile)"
        [[ "$expected" == "$resultat" ]]
        if [[ $? -ne 0 ]]
        then
            printf !
            is_error=1
            error_files+="ERROR DIFF $cas_de_test"
            nb_error=$((nb_error+1))
        else
            printf .
        fi
        rm $assfile
    fi
    nb_test=$((nb_test+1))
done

echo
printf "${error_files}\n"
echo "#test: $nb_test ; #error: $nb_error"

exit $error