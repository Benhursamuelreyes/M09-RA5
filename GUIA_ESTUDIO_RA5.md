# Criptografía desde cero
### Guía de estudio del RA5 — Seguridad y criptografía: proteger mensajes con claves

---

## Cómo usar esta guía

Esta guía avanza como avanzaría un problema real:

1. **Primero el problema**: alguien quiere enviar algo y alguien más quiere leerlo sin permiso.
2. **Después las herramientas**: qué familias de algoritmos existen y qué garantiza cada una.
3. **Luego la evolución**: de los trucos con letras a los algoritmos que usan tu wifi o tu VPN.
4. **Por último, la clave pública**: cómo dos personas se comunican en secreto sin haber compartido nunca una clave.

**Convención de fuentes**

- Todo el texto sin marca es **contenido procedente del documento**, es decir, de los apuntes de clase: definiciones, datos, clasificaciones, comandos y ejemplos de código, reformulados y reorganizados.
- Los bloques marcados así son **información adicional** externa, añadida solo para que se entienda mejor:

> **Información adicional:** ejemplo de bloque externo.


---

## Mapa general: todo en una página

```
                    +-----------------------------------------------+
                    |  OBJETIVO: proteger información que viaja o   |
                    |  se almacena en un medio inseguro             |
                    +-----------------------+-----------------------+
                                            |
     ¿Qué garantizar?  confidencialidad · integridad · autenticación · no repudio
                                            |
         +----------------------------------+----------------------------------+
         v                                  v                                  v
  CIFRADO SIMÉTRICO                  CIFRADO ASIMÉTRICO                 FUNCIÓN HASH
  1 clave compartida                 2 claves (pública/privada)         sin vuelta atrás
  (reversible)                       (reversible solo con la privada)   (NO reversible)

  - clásicos: RotX, César,           - RSA                              - SHA2, SHA3
    monoalfabético, polialfabético,  - ECC                              - KDF
    transposición
  - modernos: DES, 3DES, RC4, AES
```

Al terminar la guía deberías poder explicar cada rama de este mapa.

---

# BLOQUE 1 — El problema

## 1.1 Tres personajes y un canal

Imagina que **Laia** quiere mandarle a **Marc** el código de la alarma del local. El mensaje pasa por Internet, un canal que **no controlan**. En ese canal puede haber una tercera persona, **Eva**, que intercepta todo lo que circula.

La **criptografía** es el conjunto de técnicas que permite que Laia envíe el mensaje a Marc de forma que, **aunque Eva lo intercepte, no pueda interpretarlo**.

Fíjate en el matiz: la criptografía **no impide que Eva capture el mensaje**. Impide que lo **entienda**.

## 1.2 El vocabulario mínimo

| Término | Qué es | En el ejemplo |
|---|---|---|
| **Texto plano / texto en claro** | El mensaje original, legible | `CODIGO 4471` |
| **Cifrar** | Transformar el texto plano en algo ilegible | Lo hace Laia |
| **Texto cifrado / criptograma** | El resultado ilegible | Lo que ve Eva |
| **Descifrar** | Recuperar el texto plano a partir del criptograma | Lo hace Marc |
| **Clave** | Parámetro que decide *qué transformación concreta* se aplica | El secreto que hay que proteger |

Un sistema de cifrado está bien definido cuando tiene **dos funciones**:

```
                 clave                                  clave
                   │                                      │
                   ▼                                      ▼
 texto plano ──► [CIFRAR] ──► criptograma ──(canal)──► [DESCIFRAR] ──► texto plano
```

**La idea más importante del bloque:** el algoritmo es como la *marca de la cerradura* y la clave es *la llave concreta*. Dos personas pueden tener la misma marca de cerradura y aun así no abrirse la puerta la una a la otra. Con el mismo algoritmo y otra clave, el resultado es completamente distinto.

> **Información adicional — Principio de Kerckhoffs:** en criptografía se asume que el atacante **conoce el algoritmo**. Toda la seguridad debe depender de la clave. Por eso los algoritmos modernos (AES, RSA…) son públicos y nadie lo considera un problema.

## 1.3 ¿Qué queremos garantizar exactamente?

"Seguridad" es demasiado vago. El documento lo concreta en **cuatro garantías**. Para recordarlas, piensa en la pregunta que responde cada una:

| Garantía | Pregunta que responde | Qué evita | Ejemplo cotidiano |
|---|---|---|---|
| **Confidencialidad** | ¿Quién puede *leerlo*? | Que alguien no autorizado lea el mensaje | Solo Marc puede ver el código de la alarma |
| **Integridad** | ¿Ha llegado *intacto*? | Que alguien lo modifique por el camino | Eva no puede cambiar `4471` por `0000` |
| **Autenticación** | ¿*Quién* lo envía de verdad? | Suplantaciones: garantiza origen, autoría y propiedad | Marc sabe que el mensaje es realmente de Laia |
| **No repudio** | ¿Puede el autor *negarlo* después? | Que quien envió o hizo algo diga "yo no fui" | Laia no puede negar haber dado esa orden de transferencia |

> **Error frecuente:** confundir *autenticación* con *no repudio*. La autenticación protege al **receptor** ("sé quién me escribe"). El no repudio protege frente al **emisor** ("no puedes desdecirte"). Están relacionadas, pero no son lo mismo.

## 1.4 ¿Dónde se aplica todo esto?

El documento cita cuatro grandes campos de aplicación. Cada uno combina varias garantías:

| Campo | En qué consiste |
|---|---|
| **Identificación y autenticación** | Comprobar la identidad de una persona o validar el acceso a un servidor |
| **Certificación** | Agentes de confianza dan fe de la identidad de agentes desconocidos (por ejemplo, usuarios reales) |
| **Seguridad de las comunicaciones** | Crear canales seguros para aplicaciones que funcionan sobre redes que no lo son |
| **Comercio electrónico** | Canales seguros y mecanismos de identificación para que empresas y usuarios tengan garantías de que sus operaciones no serán espiadas ni modificadas |

**Comprobación rápida 1.** En una compra online, ¿qué garantía se ve afectada si alguien cambia el importe durante la transmisión? ¿Y si lo lee pero no lo cambia?
**Respuesta:** Si lo cambia, se rompe la **integridad**. Si solo lo lee, se rompe la **confidencialidad**.

---

# BLOQUE 2 — Las tres familias de algoritmos

Antes de estudiar algoritmos concretos, conviene tener un **criterio para clasificarlos**. Basta con dos preguntas:

```
                    ¿Se puede recuperar el original?
                         /                \
                       NO                  SÍ
                       /                     \
             HASH (unidireccional)     ¿La clave de cifrar es la misma
                                         que la de descifrar?
                                           /            \
                                         SÍ              NO
                                         /                 \
                                   SIMÉTRICO           ASIMÉTRICO
```

| Familia | Regla de oro | Ejemplos del documento |
|---|---|---|
| **Simétrico** | Clave de cifrado **=** clave de descifrado | RotX, polialfabético, AES… |
| **Asimétrico** | Clave de cifrado **≠** clave de descifrado | RSA, ECC |
| **Unidireccional (hash)** | Transforma los datos en un **hash** y no hay vuelta atrás | SHA2, SHA3, KDF… |

**Analogías para fijarlo:**

- **Simétrico** = una caja fuerte con **una sola llave copiada**. Quien tenga una copia puede cerrar y abrir.
- **Asimétrico** = un **buzón**. Cualquiera puede echar cartas por la ranura (clave pública), pero solo el dueño tiene la llave para abrirlo (clave privada).
- **Hash** = una **trituradora que da siempre el mismo confeti** para el mismo papel. Puedes comparar confetis, pero no reconstruir el papel.

Ahora estudiaremos cada familia por separado, empezando por la más antigua.

---

# BLOQUE 3 — Cifrado simétrico: de los trucos con letras a AES

## 3.1 Las características que definen la familia

- Usa la **misma clave** para cifrar y para descifrar.
- Es **reversible**: si aplicas los pasos de cifrado **en orden inverso**, descifras.
- Incluye **todos los sistemas clásicos** y **muchos modernos**.
- Hay dos modos de trabajar:
  - **De bloque**: cifra trozos de **tamaño fijo** cada vez.
  - **De flujo**: cifra **byte a byte** a medida que llega la información.

## 3.2 Las tres "operaciones atómicas"

Todos los cifrados clásicos son combinaciones de tres manipulaciones de las letras del texto en claro. Entenderlas bien sirve también para los modernos, porque **AES las sigue usando**, aunque sobre bits en lugar de letras (lo veremos en 3.8).

| Operación | Qué hace con cada letra | ¿Cambia *qué* letras aparecen? | ¿Cambia *dónde* están? |
|---|---|---|---|
| **Desplazamiento** | La cambia por la que está a una **distancia fija** en el alfabeto | Sí | No |
| **Sustitución** | La cambia por otra letra o símbolo según una **tabla** (alfabeto permutado) | Sí | No |
| **Transposición** | **No la cambia**: solo la **mueve de sitio** | No | Sí |

> Truco para recordarlo: desplazar y sustituir cambian el **disfraz** de cada letra. Transponer cambia el **asiento** de cada letra.

Nota: el desplazamiento es en realidad un caso particular de sustitución, uno en el que la "tabla" es simplemente el alfabeto corrido. Por eso el documento usa el cifrado César como ejemplo de "cifrado por sustitución".

## 3.3 Desplazamiento: RotX, Rot13 y César

**Cómo funciona.** Cada letra se sustituye por la que está **X posiciones más adelante** en el alfabeto. Al llegar a la Z se vuelve a empezar por la A, de forma circular.

- **RotX** (también **RorX**): X es la **clave**, el número secreto.
- **Rot13** (también **Ror13**): caso concreto con X = 13.
- **Cifrado César**: caso histórico con X = 3 (Rot3).

**Ejemplo nuevo (Rot5):**

```
Alfabeto:      A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
Desplazado +5: F G H I J K L M N O P Q R S T U V W X Y Z A B C D E

ATAQUE  ──Rot5──►  FYFVZJ
FYFVZJ  ──Rot-5──► ATAQUE   (descifrar = desplazar hacia atrás)
```

**Curiosidad de Rot13:** como el alfabeto latino básico tiene 26 letras, desplazar 13 dos veces es dar la vuelta completa. Por eso **cifrar y descifrar son la misma operación**: `HOLA → UBYN → HOLA`.

**Debilidad: la fuerza bruta.**

> **Información adicional (deducción):** con 26 letras solo hay 25 desplazamientos útiles. Un atacante prueba las 25 claves y lee cuál da un texto con sentido. Le cuesta segundos, incluso a mano.

## 3.4 Sustitución monoalfabética

**Cómo funciona.** Se crea un **alfabeto permutado**, es decir, desordenado, y cada letra del original se cambia por la que ocupa su misma posición en ese alfabeto. La permutación entera actúa como clave.

```
Original:  a b c d e f g h i j k l m n o p q r s t u v w x y z
Permutado: d i n h o s c t j u g p a r v m w x k e q y z f l b
```

**Ejemplo nuevo:** `secreto` → `konxoev`

| s | e | c | r | e | t | o |
|---|---|---|---|---|---|---|
| k | o | n | x | o | e | v |

Hay dos variantes:

- **Monoalfabética**: cada letra se sustituye **siempre** por la misma letra del alfabeto permutado. Fíjate en que las dos `e` de "secreto" se convierten las dos en `o`.
- **Simbólica**: en lugar de letras se usan **símbolos** (iconos, signos…).

**Debilidad: el ataque estadístico.** Ese "las dos `e` dan `o`" es precisamente el problema. Las frecuencias de las letras del idioma **se conservan disfrazadas**.

> **Información adicional:** en castellano, la E y la A son las letras más frecuentes. Si en un criptograma largo la `o` es, con diferencia, la letra que más se repite, probablemente sea la `e`. Tirando de ese hilo se reconstruye la tabla. La fuerza bruta no sirve aquí (hay 26! ≈ 4·10²⁶ permutaciones posibles), pero la estadística sí.

## 3.5 Sustitución polialfabética (o múltiple)

**La idea que lo cambia todo:** si el problema de la monoalfabética es que una letra siempre da el mismo resultado, se usa **un alfabeto permutado diferente para cada posición** del mensaje.

```
Original:      a b c d e f g h i j k l m n o p q r s t u v w x y z
Alfabeto 1:    d i n h o s c t j u g p a r v m w x k e q y z f l b   ← letra 1 del mensaje
Alfabeto 2:    a r v m c t j u g p w x k e q y z f l b d i n h o s   ← letra 2 del mensaje
   …                                                                     …
Alfabeto n:    …                                                     ← letra n del mensaje
```

**Ejemplo nuevo:** el mensaje `ee`.

- La primera `e` usa el alfabeto 1 → `o`
- La segunda `e` usa el alfabeto 2 → `c`
- Resultado: `oc`. **La misma letra produce dos letras distintas**, y la estadística simple deja de funcionar.

**Seguridad:** relativamente fuerte, pero **no suficiente para uso comercial o militar**.

## 3.6 Transposición

**Cómo funciona.** Las letras **no cambian**: solo se **reordenan** según una regla. Un método típico es escribir el mensaje **por filas** en una cuadrícula y leerlo **por columnas**.

**Ejemplo nuevo:** `HUID AL BOSQUE` en una cuadrícula de 4 columnas (`_` = espacio o relleno).

```
Escribir por filas →     Leer por columnas ↓

  H  U  I  D             col.1: H _ B U
  _  A  L  _             col.2: U A O E
  B  O  S  Q             col.3: I L S _
  U  E  _  _             col.4: D _ Q _

Criptograma: H_BU UAOE ILS_ D_Q_
```

> **Información adicional:** en este método, la clave sería el número de columnas (y, en variantes más elaboradas, el orden en que se leen).

**Seguridad según el documento:**

- **Débil ante el ataque estadístico**: las letras son exactamente las originales, así que sus frecuencias delatan el idioma.
- **Más robusta ante la fuerza bruta cuanto más largo es el mensaje**: el número de reordenaciones posibles crece muchísimo con la longitud.

## 3.7 Los clásicos en la historia

Hasta la llegada de los ordenadores se usaron variantes y combinaciones de estas técnicas. El documento destaca:

| Sistema | Contexto |
|---|---|
| **Escítala** | Antigua Grecia |
| **Cifrado César** | Es un Rot3 |
| **Vigenère** *(en los apuntes aparece escrito "Vignere")* | — |
| **Enigma** | Segunda Guerra Mundial |

> **Información adicional:** la escítala era un bastón alrededor del cual se enrollaba una tira de cuero. Es una **transposición**: el texto solo se leía bien enrollándolo en un bastón del mismo grosor. Vigenère es un cifrado **polialfabético** que usa una palabra clave para escoger el desplazamiento de cada letra. Enigma era una máquina electromecánica de **sustitución polialfabética** con rotores.

**Conclusión:** los métodos clásicos **han quedado desfasados** frente a los modernos.

## 3.8 El salto a los sistemas modernos

Se llama **cifrado moderno** al que se aplica **con ayuda de ordenadores**. Dos diferencias clave respecto a los clásicos:

1. Ya no trabaja con **letras**, sino con el **código binario** que representa el texto o los datos.
2. Se basa en **transformaciones matemáticas** sobre esos bits, y por eso es **mucho más seguro**.

Los cuatro algoritmos simétricos modernos del documento, comparados:

| | **DES** | **Triple-DES (3DES)** | **RC4** | **AES** |
|---|---|---|---|---|
| **Nombre** | Data Encryption Standard | — | — | Advanced Encryption Standard |
| **Origen** | NSA | NSA | Rivest, 1987 (propietario) | NIST, para sustituir a 3DES |
| **Cómo funciona** | Bloques de **64 bits**, clave de **56 bits**, permutaciones de bits y operaciones **XOR** | DES tres veces: **cifra con K₁, descifra con K₂ y vuelve a cifrar con K₃** | Clave que **se va modificando en cada aplicación** | Divide el mensaje en bloques, añade la clave y aplica sustitución → desplazamiento → permutación → vuelve a cifrar; el ciclo **se repite según los bits del algoritmo: 128, 192 o 256** |
| **Seguridad** | Roto por fuerza bruta en **3 días (1999)** | Más robusto que DES | Más robusto que DES, pero **ya obsoleto** | **Estándar de seguridad actual** |
| **Uso actual** | Aún en **cajeros automáticos**; en desuso en general | Declarado **obsoleto por el NIST en 2023**; lo usaban MS Office y Firefox | Obsoleto; base del wifi **WEP** y del **SSL primitivo** | Wifi **WPA2**, **VPN**, **gestores de contraseñas**… de uso generalizado |

Los cuatro son **simétricos**: las transformaciones aplicadas en orden inverso descifran.

**Fíjate en la conexión con el apartado 3.2:** AES combina **sustitución + desplazamiento + permutación**, las mismas operaciones atómicas de los clásicos. La diferencia es que se aplican sobre bits, con una clave enorme y repetidas muchas veces.

> **Información adicional — matices históricos y técnicos:**
> - DES fue **diseñado por IBM** y adoptado como estándar en los años 70, **con participación de la NSA** en su revisión. Los apuntes lo atribuyen directamente a la NSA.
> - En AES, 128/192/256 es el **tamaño de la clave**, y de él depende el número de rondas (10, 12 o 14). El bloque es siempre de 128 bits.
> - DES y AES son cifrados **de bloque**. RC4 es un cifrado **de flujo** (de ahí que su clave "vaya cambiando" con cada byte). Así se conecta esta tabla con la distinción bloque/flujo del apartado 3.1.

### Línea temporal de la familia simétrica

```
Antigüedad ── César, escítala ──► … ── Vigenère ──► s. XX ── Enigma (2ª GM)
                                                              │
         ┌──────────────── llegada de los ordenadores ────────┘
         ▼
        DES ──(roto en 1999)──► 3DES ──(obsoleto NIST 2023)──► AES (estándar actual)
        RC4 (1987) ──► WEP / SSL primitivo ──► obsoleto
```

---

# BLOQUE 4 — Cifrado asimétrico (de clave pública)

## 4.1 Por qué hace falta

> **Información adicional — el problema que resuelve:** en el cifrado simétrico, Laia y Marc necesitan **la misma clave**. Pero ¿cómo se la hace llegar Laia a Marc sin que Eva la intercepte? Si tuvieran un canal seguro para enviar la clave, ya podrían usarlo para el mensaje. Este es el **problema de la distribución de claves**, y el cifrado asimétrico lo resuelve.

## 4.2 Cómo funciona

- También se llama **cifrado de clave pública**.
- **Cada participante genera dos claves** que van emparejadas:
  - una **pública**, que **se reparte** a quien sea y **sirve para cifrar**;
  - una **privada**, que es **secreta** y **sirve para descifrar**.
- Internamente se basa en **números primos**.
- Es **uno de los sistemas más seguros conocidos hasta ahora**.
- Ejemplos: **RSA** y **ECC**.

## 4.3 El flujo paso a paso

Laia quiere enviar un mensaje confidencial a Marc:

```
   LAIA                                                     MARC
    |                                                        |
 1. | tiene el mensaje en claro                              |
 2. | lo CIFRA con la CLAVE PUBLICA DE MARC                  |
 3. | ------------- lo envía por Internet ------------------>|
    |       (Eva lo ve, pero solo ve el texto cifrado)       | 4. lo recibe
    |                                                        | 5. lo DESCIFRA con
    |                                                        |    SU CLAVE PRIVADA
```

**La regla que hay que interiorizar:** se cifra con la clave pública **del destinatario**, porque solo el destinatario tiene la privada que hace pareja con ella.

> **Error frecuente:** pensar que Laia cifra con *su propia* clave pública o con *su* privada. Si Laia cifrara con su propia clave pública, solo **ella** podría descifrarlo: Marc no podría leerlo.

> **Información adicional:** usar las claves en sentido contrario (cifrar con la *privada* del emisor) sirve para la **firma digital**. Cualquiera puede verificarla con la clave pública del emisor, y eso aporta **autenticación y no repudio** (las garantías del apartado 1.3). En la práctica (HTTPS, VPN), el asimétrico se usa para **acordar una clave simétrica** de forma segura y después se cifran los datos con AES, que es mucho más rápido.
---

# BLOQUE 5 — Función hash (cifrado unidireccional)

Las dos familias anteriores son **reversibles**: lo que se cifra se puede descifrar. La función hash es la tercera familia del mapa y rompe esa regla: **va solo en una dirección**.

## 5.1 Qué hace

Una función hash toma unos datos de cualquier tamaño (un texto, un archivo, una contraseña) y los transforma en una cadena llamada **hash** o **resumen**. La característica que lo cambia todo:

```
  texto plano ──► [FUNCIÓN HASH] ──► hash
       │                             #b!c1d&"(#df#!sk84
       └───────────◄ ✗ ─────────────┘
          No se conoce forma de volver atrás
```

No hay "clave de descifrado" porque **no existe la operación inversa**. Del hash no se puede reconstruir el dato original.

## 5.2 Las propiedades que lo definen

| Propiedad | Qué significa |
|---|---|
| **Unidireccional** | Es imposible deshacer el cifrado y recuperar el dato original |
| **Determinista** | El mismo dato de entrada produce **siempre** el mismo hash |
| **Tamaño fijo** | En la mayoría de algoritmos, el hash siempre mide lo mismo, entre más larga o corta que sea la entrada |
| **Bajas colisiones** | Es muy difícil encontrar dos datos distintos que den el mismo hash (o uno parecido) |
| **Efecto avalancha** | Un cambio mínimo en los datos de entrada produce un cambio **enorme** en el hash |

**Ejemplo nuevo del efecto avalancha** (SHA-256, verificado). Fíjate en que cambiar una sola letra o añadir un punto da un hash completamente distinto, no parecido:

```
hola   →  b221d9dbb083a7f33428d7c2a3c3198ae925614d70210e28716ccaa7cd4ddb79
Hola   →  e633f4fc79badea1dc5db970cf397c8248bac47cc3acf9915ba60b5d76b0e88f
hola.  →  3b952738efe124f6b34181cb9e608432741ab05df195462dc98b26f50f9ef217
```

Ejemplos de algoritmos del documento: **SHA2**, **SHA3** y **KDF**.

## 5.3 Para qué sirve si no se puede descifrar

Justo porque es determinista pero irreversible, el hash es ideal para **guardar contraseñas**. En lugar de almacenar tu contraseña, el sistema guarda su hash:

1. Cuando te registras, el sistema calcula el hash de tu contraseña y guarda **solo el hash**.
2. Cuando inicias sesión, calcula el hash de lo que escribes y lo **compara** con el guardado.
3. Si coinciden, eres tú. Y si alguien roba la base de datos, se lleva hashes, no contraseñas.

Esta es la pieza que conecta con el último bloque: **auditar contraseñas es, en la práctica, atacar hashes**.

> **Información adicional — sal (salt):** si dos usuarios tienen la misma contraseña, al ser el hash determinista obtienen el **mismo hash**, y eso se nota. Para evitarlo se añade a cada contraseña un valor aleatorio llamado **sal** antes de calcular el hash. Un hash **sin sal** es más débil justamente por esto. KDF (función de derivación de clave) va un paso más allá: repite el hash **miles de veces** a propósito para que calcularlo sea lento y atacarlo salga caro.

---

# BLOQUE 6 — Auditoría de contraseñas

## 6.1 La idea: ponerse en el lugar del atacante

**Auditar** contraseñas significa intentar romperlas uno mismo, de forma controlada, para descubrir cuáles son débiles **antes de que lo haga un atacante real**. La fortaleza de una contraseña se mide por el **tiempo que tardan las herramientas de auditoría en romperla**.

```
   Muy débil   →  segundos a minutos   (ej. 5-7 caracteres)
   Débil       →  horas a días         (ej. 8-10 caracteres)
   Buena       →  meses a años         (ej. 11-13 caracteres)
   Fuerte      →  siglos               (16+ caracteres)
```

La regla práctica: **cuantos más caracteres, exponencialmente más tiempo** para romperla.

## 6.2 Herramientas del documento

| Herramienta | Ámbito |
|---|---|
| **John The Ripper** | Universal |
| **Specops** | Auditoría de Active Directory |
| **RainbowCrack** | Windows |
| **Cain and Abel** | Windows |
| **Wfuzz** | Análisis web |
| **THC Hydra** | Web |
| **Ncrack** | Varios |

## 6.3 John the Ripper en detalle

Es la herramienta universal de la lista. Dos ideas clave:

- Trabaja **sobre hashes**: no ataca la contraseña directamente, sino el hash que la representa.
- Es capaz de **convertir casi cualquier cifrado a un formato compatible** y después atacarlo.

**Los tres modos de ataque**, de más listo a más bruto:

| Modo | Qué prueba |
|---|---|
| **Single** | Claves evidentes: el propio nombre de usuario, contraseña vacía, etc. |
| **Wordlist** | Una **lista de palabras** (diccionario); trae una por defecto |
| **Brute force** | Prueba **todas** las combinaciones posibles, de forma exhaustiva |

**Sintaxis básica:**

```
john arxiuhash                             Ataque por defecto
john --format=NT arxiuhash_win             Indicando el tipo de hash
john --format=XX --session=nom arxiuhash   Guardando la sesión
john --show --format=XX arxiuhash          Mostrar lo encontrado
john --restore=nom                         Reanudar la sesión 'nom'
```

**Uso avanzado:** los scripts `*2john.py` / `*2john.pl` convierten un archivo (un ZIP, un 7z, un PDF…) al formato que John entiende; después se aplica la sintaxis básica.

> **Información adicional — versiones:** la versión de código abierto es **John the Ripper Jumbo** (Windows desde Openwall, Linux desde GitHub). Existe también **John the Ripper Pro**, de pago, en la web de Openwall.

## 6.4 Caso práctico: resultados de la Act09

Esta es la síntesis de la actividad de auditoría realizada, atacando tres tipos de hash con John the Ripper. La diferencia de velocidad entre ellos es la lección principal del bloque:

| Caso / fichero | Tipo de cifrado | Contraseñas/segundo (media) | Contraseña encontrada | Tiempo total |
|---|---|---|---|---|
| `10-secret.7z` | 7z (SHA-256 + AES-256, 524288 iter.) | ~142 p/s | `secret` | ~1 s (3,83 s con arranque) |
| `10-dificil.7z` | 7z (SHA-256 + AES-256, 524288 iter.) | ~151 p/s | `dificil01` | ~19 s (22,39 s con arranque) |
| `10-vbox.txt` (vboxuser) | NT (MD4, sin sal) | ~71,6 millones c/s | `changeme` | <1 s |

**Lo que revela la tabla:** el 7z prueba solo ~150 contraseñas por segundo, mientras que el hash NT de Windows prueba **millones** por segundo. Misma herramienta, una diferencia de velocidad de medio millón de veces, y la causa está en el cifrado, no en la contraseña.

## 6.5 Las conclusiones que hay que saber defender

**¿Por qué el 7z es el cifrado más seguro de los probados?**
Porque aplica **524.288 iteraciones** (key stretching) en cada intento. Esa lentitud deliberada deja la velocidad en ~150 p/s y hace que la fuerza bruta y el diccionario salgan carísimos en tiempo.

**¿Cuál es el más débil?**
El **LM de Windows** (basado en DES): parte la contraseña en bloques de 7 caracteres, la pasa a mayúsculas y **no usa ni sal ni iteraciones**. Muy cerca está el **NT (MD4)**: tampoco tiene sal ni iteraciones, por eso corría a millones de intentos por segundo. La falta de sal hace que contraseñas iguales den hashes idénticos (de ahí que varias cuentas vacías compartan el mismo hash `31D6CFE0…`).

**¿Por qué se eligen contraseñas débiles en la práctica?**
Porque con un cifrado lento como el del 7z, una contraseña larga y aleatoria tardaría **años** en caer: la práctica no terminaría nunca. Se eligen contraseñas débiles para que el ejercicio sea demostrable en segundos. La lección: **si la contraseña es larga y el cifrado es lento, el ataque es inviable**.

**¿John the Ripper es solo para atacantes?**
No: es sobre todo una herramienta de **administradores de sistemas**. Sirve para auditar la robustez de las contraseñas de la propia organización (detectar cuentas con claves débiles, vacías o por defecto, como el `changeme`) y forzar su cambio antes de que lo aproveche un atacante. **La herramienta es neutra**; lo que distingue el uso legítimo del malicioso es el permiso y la intención.

---

## En una frase

Proteger información es elegir, según lo que quieras garantizar, entre **una clave compartida** (simétrico), **dos claves emparejadas** (asimétrico) o **ningún camino de vuelta** (hash); y auditar contraseñas no es más que **medir cuánto resiste un hash** antes de rendirse.
