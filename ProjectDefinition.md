# 1. Problemdefinition

Es soll eine KI entwickelt werden, die auf Basis einer Eingabe (Wort) die passendsten Ergebnisse (Fragen) in einer Datenbank oder per JSON-Request (Ankidroid/AnkiConnect) findet.

Damit das KI-Prinzip besser genutzt werden kann, sollen nicht nur einfache `contains()`-Funktionen verwendet werden, sondern auch:

- Berücksichtigung von Synonymen
- Korrektur fehlerhafter Eingaben
- weitere Parameter (z. B. Länge der Antwort = Komplexität der Frage)

---

# 2. Strukturierung des Vorgehens

Jeder Schritt der Entwicklung soll verständlich dokumentiert werden, sodass der Prozess für spätere Leser nachvollziehbar bleibt.

Die KI soll bewusst **ohne den Einsatz kompletter KI-Bibliotheken** entwickelt werden. Dadurch soll ein tiefes Verständnis für Funktionsweise, Datenverarbeitung und Algorithmik entstehen.

---

# 3. Planung des Datensatzes

Als Datenbasis soll die DB von Ankidroid dienen. Als Testdaten werden u. a. für den StraightForwardAlgorithm 
konkrete Testdaten in einer csv verwendet. 

---

# 4. Strukturierung des Projekts

### StraightForwardAlgorithm
Ein rein programmatisch gelöster Ansatz des Problems — zunächst begrenzt auf Funktionalität ähnlich `contains()` und `equalsIgnoreCase()`.