#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2020

# Test minimaliste de la vérification contextuelle.
# Le principe et les limitations sont les mêmes que pour basic-synt.sh

#DECAC_HOME=$(cd "$(dirname "$0")"/../../../ && pwd)
#CP_FILE="$DECAC_HOME"/target/generated-sources/classpath.txt

#CP="$DECAC_HOME"/target/generated-classes/cobertura:"$DECAC_HOME"/target/test-classes/:"$DECAC_HOME"/target/classes/:$(cat "$CP_FILE")

#javac -s . -cp "$CP" ../java/fr/ensimag/deca/context/TestContextCustom.java
#echo "POOF"
#run=`java -enableassertions fr.ensimag.deca.context.TestContextCustom "$@"`

cd "$(dirname "$0")"/../../.. || exit 1
PATH=./src/test/script/launchers:"$PATH"
error=0
error_files=""
nb_test=0
nb_error=0
# Function for invalid tests
test_synt_invalide () {
    # $1 = premier argument.
    ./src/main/bin/decac -v "$1" 1>log.txt 2>err.txt # code de retour dans $?
    if [[ $? -ne 0 ]]    #"ContextualError"
    then
        printf "."
    else
        printf "!"
        error_files+="ERROR $1\n"
        error=1
        nb_error=$((nb_error+1))
    fi
    nb_test=$((nb_test+1))
}

# Function for valid tests
test_synt_valid () {
    # $1 = premier argument.
    ./src/main/bin/decac -v "$1" 1>log.txt 2>err.txt # code de retour dans $?
    if [[ $? -eq 0 ]]    #"ContextualError"
    then
        printf "."
    else
        printf "!"
        error_files+="ERROR $1\n"
        error=1
        nb_error=$((nb_error+1))
    fi
    nb_test=$((nb_test+1))
}

#### End of Functions

# Invalid Tests
for cas_de_test_invalide in src/test/deca/context/invalid/*.deca
do
    test_synt_invalide "$cas_de_test_invalide"
done

for cas_de_test_invalide in src/test/deca/context/invalid/provided/*.deca
do
    test_synt_invalide "$cas_de_test_invalide"
done

# Valid Tests
for cas_de_test_valide in src/test/deca/context/valid/*.deca
do
    test_synt_valid "$cas_de_test_valide"
done

for cas_de_test_valide in src/test/deca/context/valid/provided/*.deca
do
    test_synt_valid "$cas_de_test_valide"
done

echo
printf "${error_files}\n"
echo "#test: $nb_test ; #error: $nb_error"

rm log.txt err.txt

exit $error
