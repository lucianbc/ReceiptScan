# Tehnologii

Un scop secundar al acesui proiect este explorarea unor metode moderne pentru dezvoltarea aplicațiilor Android. Menținerea unor reguli și structuri clare în organizarea codului aplicației aduce o serie de beneficii, printre care reducerea numărului de bug-uri și ușurința în menținerea și extinderea aplicației pe termen lung și este crucială pentru succesul proiectelor de dimensiuni medii și mari sau la care lucrează mai multe persoane. Dezavantajul acestora este timpul ce trebuie investit la începutul proiectului și o continuă disciplină și atenție din partea programatorilor. 

## Motivație Android

Decizia de a dezvolta această aplicație pentru platforma Android este susținută de motive atât tehnologice, cât și de oportunitate. Conform StatCounter, în Iulie 2019, sistemul de operare Android deținea 76.08% cotă de piață la nivel global [@StatsWorldwide], 73.71% la nivelul Europei [@StatsEurope] și 81.3% la nivelul României [@StatsRomania]. Aceste cifre justifică prioritizarea platformei Android în dezvoltarea unei aplicații mobile. 

Din punct de vedere tehnologic, opțiunile pentru dezvoltarea unei aplicații mobile în 2019 sunt *Android Nativ*, *iOS Nativ* sau *cross-platform*, folosind una dintre cele câteva soluții populare pentru dezvoltare cross-platform (printre care *React Native*, *Flutter* sau *Native Script*). Nevoile tehnice ale acestei aplicații presupun integrarea unei soluții OCR, iar procesarea să se facă pe dispozitiv. Implementarea unei astfel de soluții într-un framework cross-platform nu este o sarcină trivială datorită lipsei de suport și documentație. Alegând între *Android Nativ* și *iOS Nativ*, dezvoltarea pentru Android se poate face de pe orice sistem de operare major, pe când dezvoltarea pentru iOS necesită sistemul de operare macOs. 

Analizând cele două motive de mai sus, am ales *Android Nativ* ca platformă de dezvoltare datorită popularității sistemului de operare, a stabilității ecosistemului de dezvoltare și a suportului și a documentației extensive disponibile. Pentru o viitoare migrare către iOS, framework-ul *Flutter* este considerat ca fiind o soluție viabilă.

## Tehnologii utilizate

Dezvoltarea pe platforma *Android Nativ* oferă acces la întreg ecosistemul *JVM*. Acest lucru a permis utilizarea a mai multor librării care nu au fost dezvoltate special pentru Android. În continuare vor fi prezentate tehnologiile folosite în dezvoltarea aplicației și motivația din spatele lor.

### Kotlin

Dezvoltarea aplicațiilor Android nu mai înseamnă doar *Java*. *Kotlin* este un limbaj de programare ce rezolvă multe dintre problemele din Java și care, începând din 2017 este suportat în mod oficial de către *Google* ca limbaj de dezvoltare pentru Android, iar din 2019, considerat limbaj preferat pentru Android. Aceasta înseamnă că noile funcționalități ale SDK-ului Android vor fi dezvoltate și oferite cu prioritate către Kotlin. 

Principalele caracteristici ale acestui limbaj sunt sistemul de tipuri superior, ce suportă inferența tipurilor, existența tipurilor de date care nu pot fi nule (*null safety*), lipsa excepțiilor *verificate* (*checked exceptions*) și diferențierea clară și ușoară între variabile și constante (prin cuvintele cheie `var` și `val`). Codul scris în Kotlin este de cele mai multe ori mai scurt, mai concis, mai sigur și mai ușor de înțeles decât cel scris în Java.

### RxJava

*Programare reactivă* [@ReactiveProgramming] este o paradigmă concentrată în jurul reacționării la modificări în starea unui obiect și a devenit populară în ultimii ani, atât pentru dezvoltarea aplicațiilor grafice, cât și pentru aplicațiile de server care procesează fluxuri de date. Avantajele acesteia sunt facilitarea procesării pe mai multe *thread-uri* și abstractizarea componentelor aplicației (*separation of concerns*).

RxJava implementează o serie de abstractizări ce extind ideea de *Observer*[@ObseverPattern] și operatori asupra acestor abstractizări pentru a executa computații asupra valorilor reprezentate. Această aplicație folosește RxJava pentru a reprezenta fiecare operație sau unitate computațională și pentru a orchestra aceste computații pe diferite thread-uri, cu scopul de a nu bloca interfața grafică. De exemplu, surprinderea și extragerea informațiilor dintr-o poză este reprezentată folosint abstractizarea `Single` și este executată pe un thread secundar, în timp ce thread-ul principal afișează un mesaj și răspunde acțiunilor utilizatorului,

### Android Architecture Components

*Architecture Components* este o colecție de librării dezvoltată de Google cu scopul de a oferi uneltele necesare pentru a dezvolta aplicații robuste și testabile. Această aplicație folosește:

* **ViewModel**: gestionează datele aferente unui ecran sau a unei colecții de ecrane într-o manieră care ține cont de ciclul de viață al componentelor vizuale (*Activities*, *Fragments*). Folosite pentru a împărtăși date comune între componente vizuale și pentru a nu pierde datele în timpul schimbării configurației, cum ar fi rotirea ecranului.
* **LiveData**: expune date către componentele vizuale în mod reactiv. Această librărie se aseamănă cu RxJava, fără complexitatea aferentă. În schimb, este dependentă de ciclul de viață al componentelor vizuale, ceea ce evită problemele de tipul *memory leak*.
* **DataBinding**: este o metodă prin care datele din *ViewModel* pot fi observate în fișierele de *layout* XML.
* **Room**: este o librărie ce facilitează accesul la baza de date *sqlite* disponibilă pe dispozitiv. Se integrează cu *LiveData* și *RxJava* pentru a oferi actualizări datelor interogate.
* **WorkManager**: programează și execută activități de *background* sub anumite constrângeri, care să fie executate într-un mod eficient din punct de vedere al bateriei. Folosit pentru a colecta bonurile în cloud.

### Firebase ML Vision

Google oferă o serie de servicii de machine learning pentru dezvoltatorii de aplicații prin intermediul *Firebase ML Kit*. Unul dintre aceste servicii este *Firebase ML Vision*, ce conține și un modul OCR. Procesarea se poate face atât local, cât și în cloud pentru o performanță sporită. Opțiunea de procesare în cloud este supusă unor tarife, dar procesarea locală este gratuită și oferă o performanță suficient de bună pentru scopul acestei aplicații. Acest serviciu a fost ales în urma unei comparații ce va fi detaliată într-un capitol ulterior.

### Firebase Cloud Services

Firebase este o suită de servicii cloud oferită de Google dezvoltatorilor de aplicații mobile și web. Prin folosirea unor servicii cloud este eliminată complexitatea asociată dezvoltării și întreținerii unui serviciu back-end. Dintre serviciile Firebase, această aplicație utilizează:

* **Firestore**: o bază de date noSql ce stochează documente (obiecte JSON). Este folosită pentru funcționalitatea de colectare a bonurilor;
* **Cloud Storage**: un sistem de foldere și fișiere. Folosit pentru a stoca imaginile asociate bonurilor și fișierele pentru export;
* **Cloud Functions**: este un serviciu computațional ce este declanșat de diferite evenimente și rulează un mediu NodeJS ce execută un anumit program. Este folosit pentru a arhiva fișierele exportate de aplicație și pentru a trimite o notificare cu link-ul de descărcare.

