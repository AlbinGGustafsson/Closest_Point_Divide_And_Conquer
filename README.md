Min implementation till algrotimen som löser "Closest point problem".
Använder sig utav "divide and conquer" för att hitta det närmsta paret.

Den börjar med att sortera arrayen den tar in på dess punkters X-koordinater.
Den skapar även en ArrayList med samma punkter och den sorteras på dess Y-koordinater.
Sedan anropas en rekursiv metod dels båda samlingarna men även 2 heltal som kommer agera start och slutindex på "x-arrayen".
Algoritmen tar fram en mittpunkt på x axeln.

Algoritmen skapar 2 listor som kommer innehålla punkter sorterat på deras Y-koordinater.
Den inskickade "Y-listan" loopas igenom och punkter till vänster om mittpunkten hamnar i den första och de andra hamnar i den andra.

Algoritmen tar fram det minsta paret på vardera sida om denna mittpunkt med två rekursiva anrop.
När denna "halva" är <= 3 så når den ett "basecase", då hittar den det minsta paret genom att jämföra alla med alla

Det finns dock en möjlighet att de 2 närmsta punkterna i en halva korsar dess mittlinje så man måste även ta hänsyn till dessa.
Om jag kallar avståndet mellan de närmsta punkterna som ligger i någon av halvorna d.
För att ett par som "korsar" dess mittlinje ska ha mindre avstånd så måste det ligga inom d steg från denna mittlinje.
De punkter som uppfyller det steget läggs i en separat lista, "strip".


Eftersom vi har skickat med en lista sorterad på Y så kan man använda den och stoppa en sökning från en
punkt om dess y värde och punkten den jämförs meds y värde skiljer sig mer än d.
Detta gör att antalet punkter som måste jämföras i "strippen" går ner mycket. (det är varför detta kan göras med O(N))

Designval:
Jag har valt att ett par representeras av en klass Pair.
När ett nytt minsta par hittas så sparas punkterna och dess distans i ett Pair objekt.
Jag har gjort detta för att det blir väldigt tydligt vad som jämförs (par av punkter) och vad man vill söka efter. (ett par av punkter)
Pair klassen har en metod som gör det enkelt att omvandla den till en Point Array som testfallen frågar efter.

Man hade kunnat sortera strip listan på dess punkters Y-kordinater i varje rekursivt anrop. Det hade dock bidragit till en sämre tidskomplexitet.
Dock hade det enligt mig gjort algoritmen enklare att implementera och följa. Istället har jag valt att en extra lista vars punkter är sorterade på
 dess Y skickas med och delas upp i varje rekursivt anrop.

Jag har valt att dessa listor ska vara just listor istället för arrayer
för att slippa hålla koll på dess storlekar och index när man adderar punkter till den.
