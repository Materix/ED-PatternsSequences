zmienna liczba transakcji           1000 - 10000, co 1000
stosunek wzorców do transakcji      1:10
stosunek elementów do transakcji    1:100

średni support                      0.75
średnia długość transakcji          10
średnia długość wzorca              4
liczba itemów                       100

POWERSHELL

for ($i = 1000; $i -le 10000; $i += 1000) {
    .\SyntheticDataGenerator.exe lit -fname $i -ntrans $i -nitems 100 -npats ($i / 10) -randseed "-42"
     java -cp ..\..\..\repository\src\Utils\bin pl.edu.agh.ed.utils.TransactionsTranslator "$i.transactions" "..\..\..\repository\data\transactions\transactionsCount\$i.txt"
}