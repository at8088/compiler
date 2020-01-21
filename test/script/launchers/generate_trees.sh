#! /bin/sh

filepath=../../../test/deca/syntax/valid/written/
destpath=../../../test/deca/syntax/valid/expectedtrees/
for file in $(find $filepath -type f -printf "%f\n")
do
    if ./test_synt "$filepath""$file" > "$destpath""$file".tree
    then
        echo "$file".tree généré
    fi
done
