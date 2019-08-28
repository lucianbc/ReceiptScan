# Detalii și implementare

Secțiunile precedente au prezentat specificațiile aplicației, arhitectura și principalele tehnologii care facilitează implementarea. În continuare vor fi discutate detaliile fiecărei funcționalități și modul în care acestea se reflectă în cod.

## Înțelegerea imaginilor

Așa cum am precizat mai devreme, înțelegerea conținutului bonurilor fiscale din imagini reprezintă principala provocare a acestui proiect. În mod natural, această funcționalitate se desparte în două sarcini: 

* Recunoașterea textului;
* Extragerea informațiilor din textul neprocesat;

Metode mai bune pentru a rezolva această provocare pot ține cont de imagini și pentru a doua sarcină și pot folosi metode mai avansate, de *machine learning*, odată cu colectarea a mai multor date. De aceea am implementat funcționalitatea de colectare de date, care va fi discutată într-o secțiune viitoare. Aceste opțiuni sunt subiectul unor cercetări viitoare.

### Recunoașterea textului

O necesitate pentru rezolvarea acestei sarcini este accea ca procesarea să se facă pe dispozitiv. Astfel, datele utilizatorului nu părăsesc dispozitivul decât cu acordul său. 

O soluție open source populară pentru rezolvarea problemelor OCR este *Tesseract* [@Tesseract]. Pentru dezvoltatorii de aplicații mobile, Google oferă librăria *Firebase Vision*, cu suport gratuit pentru OCR pe dispozitiv. Comparația dintre cele două soluții a fost făcută astfel:

* Firebase Vision a fost rulat folosind un test de instrumentare, întrucât această librărie nu poate rula decât pe un dispozitiv mobil;
* Tesseract a fost rulat pe un computerul personal, folosind python;
* Imaginea a fost preprocesată doar pentru Tesseract, întrucât această librărie nu oferă o performanță satisfăcătoare pe imagini neprocesate;
* Preprocesarea a constat în aplicarea unui algoritm care să elimine fundalul, să transforme imaginea în alb-negur și să uniformizeze luminozitatea.
* Asupra ambelor rezultate a fost aplicat un algoritm care să grupeze chenarele de text pe linii.

Anexa 1 cuprinde cele două script-uri folosite pentru comparație. Rezultele obținute sunt prezentate în figura ... .
<!-- TODO: Adaugă figura și anexa -->

De menționat este și efortul necesar pentru a integra Tesseract într-o aplicație mobilă. În același timp, Firebase Vision este disponibilă ca o dependință *gradle*. 

Performanța superioară a Firebase Vision ar fi suficientă pentru a alege această librărie. La aceasta se adaugă și ușurința integrării și lipsa necesității de preprocesare. Dezavantajul major al acestei librării este integrarea unui serviciu extern, care nu este open soruce în codul aplicației, dar acesta nu este unul foarte mare pentru versiunea curentă a aplicației. Așădar, pentru sarcina de OCR am ales soluția Firebase Vision.

### Extragerea informațiilor din text

Procesarea textului rezultat în urma procesului de OCR se face pe baza unor reguli observate în majoritatea bonurilor fiscale. Firebase Vision returnează textul și chenarele de text, grupate blocuri, linii și elemente, în funcție de coordonatele geometrice din imagine. Această organizare pe blocuri nu este de folos în procesarea de față, dar organizarea pe linii este, din moment ce informația de pe bonurile fiscale este așezată in format cheie-valoare, pe linii. De aceea, prima etapă în extragerea informațiilor este renunțarea la structura de blocuri și organizarea în linii raportate la întregul document. Această etapă se face după algoritmul:

1. Extrage liniile din blocuri;
2. Sortează liniile de sus în jos, în funcție de punctul lor de mijloc; Consideră liniile ca fiind elementele OCR
3. Grupează elementele OCR după distanța relativă dintre punctele lor de mijloc: elementele la o distanță mai mică de jumătate din media înălțimii tuturor elementelor se află în același grup.  

Implementarea acestui algoritm se găsește în Anexa 2.

Având textul din imagine organizat în grupuri de cuvinte apropiate (vechile linii returnate de Firebase Vision) și linii raportate la întreaga imagine, ordonate de sus în jos, informațiile relevante sunt extrase după următoarele reguli:

* **Numele comerciantului**: 
    1. Extrage prima linie. Dacă aceasta este formată dintr-o singură literă, continuă extragerea. Această regulă este motivată de faptul că multe bonuri pot conține la început un logo ce poate fi confundat cu o literă.
    2. Dacă linia curentă are înălțimea peste media tuturor liniilor, atunci verifică următoarea linie. Dacă și aceasta are înălțimea peste medie și mai puțin de 3 cuvinte, consideră numele comerciantului ca fiind concatenarea celor două linii. În caz contrar, consideră numele comerciantului ca fiind textul liniei curente.
* **Data achiziției**: aplică o serie de expresii regulate pentru a parsa date din întregul text. Dacă sunt găsite mai multe date, alege data cea mai apropiată de data curentă. Dacă nu este găsită nicio dată, consideră data curentă.
* **Produse și preț total**: Acestea sunt procesate parcurgând liniile de sus în jos și alcătuind o listă obiecte de tip cheie-valoare. Cheile sunt nume de produse sau cuvinte cheie care să marcheze prețul total, iar valorile sunt prețuri, numere fracționare. Produsele și prețurile aferente sunt considerate toate obiectele care sunt întâlnite deasupra primului obiect ce marchează totalul.
* **Categoria și moneda**: aceste valori sunt citite din setările predefinite și pot fi modificate de utilizator.

Implementarea detaliată a algoritmului de extragere a informațiilor este prezentat în Anexa 3.

### Integrarea în aplicație

![Ecranul de Scanare \label{scanner}](source/figures/Scanner.png){ height=50% }

Figura \ref{scanner} prezintă ecranul de scanare, care este interfața cu utilizatorul a algoritmului de extragere a informațiilor. Acesta permite utilizatorului să obțină o imagine a bonului fiscal folosind camera telefonului sau importând-o din galerie. De asemenea, ecranul permite utilizatorului activarea sau dezactivarea blitz-ului. Odată ce utilizatorul atinge ecranul sau importa o imagine din galerie, este afișat un ecran de incărcare, în timp ce procesarea se face în background. La finalul procesării, utilizatorul este redirecționat către un ecran de unde poate face modificări asupra datelor extrase.

La nivelul domeniului, algoritmul de OCR este ascuns sub interfața `Scannable`, care este implementată la nivelul infrastructurii. Aceasta expune două metode, `ocrElements()` și `image()`, ce furnizează elementele textuale și imaginea sub abstractizarea `Observable` din RxJava.

\lstinputlisting[style=javaCodeStyle, caption=Interfețele Scannable și ExtractUseCase]{source/code/ScannableExtractUseCase.kt}

`ExtractUseCase` modelează și orchestrează funcționalitățile aferente ecranului de scanare: 

* Valoarea `preview` expune un flux de elemente OCR care să fie afișate pe ecran, deasupra camerei, pentru a ajuta utilizatorul în capturarea imaginii. 
* Funcția `fetchPreview` permite livrarea unui nou cadru surprins de cameră, care să fie procesat asincron, iar rezultatul să fie livrat către `preview`.
* funcția `extract` declanșează procesarea imaginii bonului și salvarea informațiilor în baza de date, returnând id-ul entității salvate.
* valoarea `state` marchează daca o imagine este procesată pentru extragerea unui bon sau nu, sau dacă a fost întâmpinată o eroare.

Procesarea unei imagini durează în funcție de performanțele telefonului, timp de câteva secunde. Părăsirea ecranului de scanare este permisă în acest timp deoarece obiectul `ExtractUseCase` nu este distrus odată cu obiectul vizual, ceea ce nu întrerupe procesarea.

## Gestionare Drafturi

![Ecranul de listare a drafturilor \label{draftList}](source/figures/DraftsList.png){ height=50% }

![Ecranul de editare draft \label{draftScreen}](source/figures/DraftScreen.png){ height=50% }

Întrucât extragerea informațiilor nu este un proces robust, datele extrase trebuie validate de utilizator. Odată ce imaginile sunt procesate, datele extrase sunt salvate în baza de date, sub categoria *drafts*. În acest moment, bonurile sunt editabile. Figura \ref{draftList} prezintă ecranul unde utilizatorul vede toate drafturile, iar figura \ref{draftScreen} ilustrează ecranul de editare.

Asupra unui draft, utilizatorul are la dispoziție următoarele opțiuni:

* modificarea categoriei, prin apăsarea pe ilustrația corespunzătoare;
* modificarea numelui comerciantului;
* modificarea datei, prin folosirea unui *date picker*;
* modificarea prețului total și a monedei;
* modificarea numelui sau prețului unui produs;
* ștergerea unui produs, prin gestul de *swipe*;
* adăugarea unui produs, prin apăsarea butonului de adăugare;
* ștergerea, validarea și vizualizarea imaginii aferente prin butoanele din bara de opțiuni;

Validarea unui draft mută acel bon în lista de bonuri validate și poate fi văzut în ecranul *Receipts*. Totodată, validarea declanșează și trimiterea bonului împreună cu imaginea aferentă către cloud în cazul în care utilizatorul permite colectarea datelor. Această acțiune nu este una de importanță majoră pentru utilizator și este executată în background.

### Implementare

La nivelul modelului, aceste opțiuni sunt reprezentate prin interfața `DraftsUseCase`. Atât funcționalitatea de listare, cât și cea de editare se folosesc de funcționalitatea *Room* prin care atunci când apare o modificare la nivelul bazei de date, o nouă valoare este emisă pentru interogările deja executate. Astfel, este ușoară o implementare reactivă pentru aceste funcționalități.

\lstinputlisting[style=javaCodeStyle, caption=Interfața Drafts Use Case]{source/code/DraftsUseCase.kt}

Editarea câmpurilor text se face în manieră *on the fly*, ceea ce înseamnă că nu este necesar un ecran separat drept formular și apăsarea unui buton de persistare a modificărilor. Aceasta se realizează înregistrând un *callback* pe câmpurile de text, care apelează funcția de update. Această abordare ridică problema unor fluxuri de date mult prea rapide. De aceea, asupra fluxului de modificări este aplicat operatorul RxJava `throttleLast`. Figura \ref{throttle}, din documentația RxJava [@ThrottleLast] ilustrează modul în care acest operator funcționează.

![Ilustrație throttleLast \label{throttle}](source/figures/throttleLast.png)

În continuare este prezentată utilizarea operatorului `throttleLast`, împreună cu un exemplu de utilizare. Funcția `throttled` primește argumentele pentru aplicarea operatorului și o funcție și returnează o nouă funcție care are aceeași signatură, același comportament, dar executată la o rată de timp specificată. În acest mod sunt exemplificate funcțiile de ordin înalt și abilitatea de a reprezenta funcțiile ca valori în limbajul Kotlin.

\lstinputlisting[style=javaCodeStyle, caption=Funcții throttled]{source/code/ThrottleImplementation.kt}


<!-- TODO: Implementarea colectării -->

## Setări

![Ecranul de setări \label{settingsScreen}](source/figures/SettingsScreen.png)

Ecranul de setări controlează valorile predefinite utilizate în extragerea datelor despre bonuri și indicatorul care permite sau nu colectarea datelor. Modificarea acestor valori nu este inclusă explicit în domeniul aplicației, de aceea această funcționalitate este implementată numai la nivelul prezentării și infrastructurii. Interfețele definite de model, `CollectingOption` și `ReceiptDefaults` sunt implementate de clasa `PreferencesDao`, care este folosită pentru a accesa mediul de stocare `SharedPreferences`.



