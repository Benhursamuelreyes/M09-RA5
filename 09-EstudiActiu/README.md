# Entorno de estudio activo — Criptografía en Java

Un entorno para **aprender haciendo**: en lugar de releer la teoría, implementas
cada concepto de cifrado en Java y un corrector te dice al instante, en verde o
rojo, si lo has entendido. Cubre los conceptos del RA5: desplazamiento,
sustitución, transposición, fuerza bruta, hash y la taxonomía
simétrico/asimétrico/hash.

## Qué es el "estudio activo" y cómo lo aplica este entorno

El estudio activo consiste en **recuperar y producir** lo que sabes, no en
reconocerlo de forma pasiva. Aquí se aplica con cinco palancas:

| Palanca del método | Cómo se materializa en el entorno |
|---|---|
| **Recuerdo activo** (active recall) | Cada método está vacío: lo escribes tú de memoria a partir de una pista |
| **Feedback inmediato** | El corrector marca ✓/✗ al momento y te da la primera pista del nivel |
| **Práctica por niveles** | 6 niveles de dificultad creciente; avanzas cuando el anterior está verde |
| **Intercalado** (interleaving) | Mezclas familias (clásico, hash, taxonomía) en una sola sesión |
| **Autoexplicación** | Las preguntas de reflexión te obligan a decir *por qué*, no solo *cómo* |

## Cómo se usa

Necesitas Java (JDK 17 o superior; el repositorio usa Java 21).

1. Abre el archivo de ejercicios:

   ```
   src/estudi/Exercicis.java
   ```

2. Busca el primer bloque `// TODO`. Sustituye la línea
   `throw new UnsupportedOperationException(...)` por tu implementación,
   siguiendo la **pista** del comentario.

3. Ejecuta el corrector:

   ```bash
   ./estudia.sh
   ```

   Verás algo así:

   ```
   NIVELL 1 — Desplaçament (César / RotX)
     ✓ xifraCesar("ABC", 3) = "DEF"
     ✗ xifraCesar("XYZ", 3) = "ABC"  (dona la volta)
         pista: El % 26 fa que després de la Z es torni a la A.
   ...
   Resultat: 6/17 proves superades
     [███████░░░░░░░░░░░░░]
   ```

4. Repite: implementa el siguiente ✗, vuelve a ejecutar, hasta tener **17/17**.

### Comparar con la solución

Solo cuando lo hayas intentado de verdad:

```bash
./estudia.sh --solucio
```

Ejecuta la implementación de referencia (`solucio/estudi/Exercicis.java`) y la verás
en verde. Úsala para contrastar tu enfoque con uno correcto, no para copiar antes
de pensar.

## Los 6 niveles

| Nivel | Concepto | Qué implementas |
|---|---|---|
| 1 | Desplazamiento (César / RotX) | `xifraCesar`, `desxifraCesar` |
| 2 | Fuerza bruta | `forcaBruta` (los 26 candidatos) |
| 3 | Sustitución monoalfabética | `xifraMonoalfabetic` |
| 4 | Transposición | `xifraTransposicio` (filas por columnas) |
| 5 | Hash unidireccional | `sha256` (determinista + avalancha) |
| 6 | Taxonomía | `classifica` (simétrico/asimétrico/hash) |

## Preguntas de autoexplicación

Respóndelas en voz alta o por escrito **después** de superar cada nivel. Si no
sabes contestarlas, repasa la guía `GUIA_ESTUDIO_RA5.md` aunque el test esté verde:

1. **Nivel 1.** ¿Por qué `desxifraCesar(x, d)` equivale a `xifraCesar(x, 26 - d)`?
2. **Nivel 2.** Si el alfabeto tuviera 100 símbolos, ¿cuántos candidatos probaría la
   fuerza bruta? ¿Sigue siendo un ataque viable? ¿Por qué el César es débil?
3. **Nivel 3.** El test comprueba que `EE` se cifra como dos letras iguales.
   ¿Qué debilidad del cifrado monoalfabético demuestra eso?
4. **Nivel 4.** La transposición no cambia las letras, solo su orden.
   ¿Frente a qué ataque es débil y frente a cuál es más robusta?
5. **Nivel 5.** ¿Por qué un hash sirve para guardar contraseñas si no se puede
   descifrar? ¿Qué aporta la *sal*?
6. **Nivel 6.** Explica en una frase la regla que distingue cada familia.

## Estructura

```
09-EstudiActiu/
├── README.md                     esta guía
├── estudia.sh                    compila y ejecuta el corrector
├── src/estudi/
│   ├── Exercicis.java            ← AQUÍ trabajas tú (tiene los TODO)
│   └── Corrector.java            motor de tests (no hace falta tocarlo)
└── solucio/estudi/
    ├── Exercicis.java            solución de referencia
    └── Corrector.java            copia del corrector para la solución
```

> Relación con el resto del repositorio: estos ejercicios usan el alfabeto
> simplificado A–Z para centrarse en la idea de cada cifrado. Las versiones
> "de producción", con el alfabeto catalán completo y el patrón Factory, están
> en `01-Rot13`, `03-Monoalfabetic`, `06-Factory`, etc. Cuando tengas los 6
> niveles en verde, abre esas carpetas y reconoce en ellas lo que has implementado.
