/**
 * Paka satur klases, kas apraksta MLVV struktūru un ir pamatā atvasinātas no
 * lv.ailab.dict.struct klasēm. Atvasināšana parasti tiek veikta, vai nu lai
 * pievienotu jaunus datu laukus, vai arī lai būtu kur likt MLVV-doc analīzes
 * metodes un tās nemētātos apkārt.
 *
 * Nosaukumu veidošanas regularitātes:
 * * apstrādes metodes, kas dotās simbolu virknes atdala kādu gabalu un parsē
 *   tikai to, sauc par extractSomething, bet metodes, kas sagaida, ka izgūstamā
 *   elementa atdalīšana jau ir veikta iepriekš, un parsē doto simbolu virkni
 *   visu, sauc par parseSomething.
 * Izveidots 2017-01-09.
 * @author Lauma
 */
package lv.ailab.dict.mlvv.analyzer.struct;