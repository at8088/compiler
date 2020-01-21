#! /bin/sh

decac=../../main/bin/decac 

"$decac" ../deca/codegen/valid/hello.deca

if $(diff ../deca/codegen/valid/hello.ass ../deca/codegen/valid/provided/hello.ass) 
then
    echo les fichiers .ass sont identiques.
else
    echo les fichiers .ass sont différents.
fi
rm ../deca/codegen/valid/hello.ass
echo le fichier .ass généré est supprimé.