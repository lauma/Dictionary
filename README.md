Tezaurs.lv datu apstrādes rīki
================================

Projektam pievienots arī rekomendējoša rakstua IntelliJ IDEA projekts. Kompilēšana un pakošana - build.xml

Sapakotajā rezultātā iekļautie apstrādes skripti:

1. `1-TezaursDocChecker.bat` - Tēzaura sākotnējo MS Word failu (.doc, katra rindkopa ir šķirklis) formāta loģiskās pārbaudes.
2. `2-TezaursDoc2Dic.bat` - Tēzaura pārveidošana no MS Word dokumentiem (.doc) uz teksta dokumentiem (.dic, šķirkļus atdala tukša rinda, katra netukša rinda sākas ar vienu divburtu marķieri un nesatur citus marķierus).
3. `3-TezaursDic2Xml.bat` - Tēzaura pārveidošana no teksta failiem (dic.) uz XML formātu.
4. `4-TezaursXml2Json.bat` - Tēzaura XMLa faila analīze, struktūras padziļināšana un gramatiku bagātināšana, rezultātu izdrukājot JSON vai XML formā.
5. `MlvvTxt2Xml.bat` - MLVV apstrādes un izgūšanas skripts (jālieto kopā ar VBA skriptu `src\MlvvMarkjeetaajs.bas`).
6. `TezaursDoc2Txt.bat` - palīgskripts, kas Tēzaura .doc failu saturu apvieno vienā kopējai meklēšanai ērtākā tekstafailā.

