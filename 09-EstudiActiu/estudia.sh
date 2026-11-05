#!/usr/bin/env bash
# Llançador de l'entorn d'estudi actiu de criptografia.
#   ./estudia.sh            -> corregeix la TEVA feina  (src/estudi/Exercicis.java)
#   ./estudia.sh --solucio  -> executa la solució de referència (tot verd)
set -e
cd "$(dirname "$0")"
SRC="src"
if [ "$1" == "--solucio" ] || [ "$1" == "-s" ]; then
    SRC="solucio"
    echo ">> Executant la SOLUCIÓ de referència"
fi
OUT="build/$(basename "$SRC")"
rm -rf "$OUT"; mkdir -p "$OUT"
# Compila sense el soroll de JAVA_TOOL_OPTIONS
if ! javac -d "$OUT" "$SRC"/estudi/*.java 2>build/err.log; then
    echo "!! No compila encara. Errors:"
    grep -v "Picked up JAVA_TOOL_OPTIONS" build/err.log || cat build/err.log
    exit 1
fi
java -Dstdout.encoding=UTF-8 -Dfile.encoding=UTF-8 -cp "$OUT" estudi.Corrector
