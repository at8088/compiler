#! /bin/sh

cd "$(dirname "$0")"/../../.. || exit 1
# Script ayant pour objectif de se lancer avec la commande mvn test
#echo "Tests Lexicaux"
#./basic-lex.sh

#echo "Tests Syntaxiques"
#./basic-synt.sh

echo "=====Tests  contextuels"
./src/test/script/basic-context_test_auto.sh

echo "=====Tests  codegen"
./src/test/script/basic_codegen_test_auto.sh