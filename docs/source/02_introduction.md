# Introducere

Această lucrare prezintă \AppName, o aplicație mobilă de scanare a bonurilor fiscale și vizualizare a cheltuielilor, disponibilă pentru platforma *Android*.

## Motivație

Urmărirea și analizarea cheltuielilor este o sarcină importantă pentru alcătuirea unui buget personal și pentru o organizare mai bună a activităților financiare. Popularizarea în ultimii ani a plăților electronice și cu cardul a facilitat apariția a tot mai multe astfel de servicii automate oferite de bănci. Majoritatea băncilor oferă astăzi o aplicație mobilă cu funcționalitatea de a urmări și clasifica tranzacțiile clienților.

Cea mai mare problemă pe care aceste servicii o întâmpină este lipsa unor date mai bogate, fără de care valoarea pe care o pot aduce este limitată. Într-adevăr, soluțiile existente valorifică un set limitat de date disponibile tranzacțiilor, printre care numele comerciantului și suma totală. Fără a avea acces la conținutul tranzacției, serviciile existente nu pot oferi o listă comprehensivă cu toate achizițiile utilizatorului.

O altă problemă a serviciilor oferite de bănci pentru urmărirea cheltuielilor este disponibilitatea datelor. Utilizatorii nu au decât un acces limitat la datele ce le aparțin. Aceste date sunt disponibile fie doar în aplicația băncii, fie pot fi exportate în formate ce nu pot fi valorificate mai departe, cum ar fi documente PDF. În acest caz, o întrebare simplă, cum ar fi *Cât am cheltuit în această lună pe pâine?* devine greu de răspuns.

Având în vedere dezavantajele soluțiilor curente, am dezvoltat \AppName, o soluție completă de gestionare a tranzacțiilor personale. Aceasta permite scanarea bonurilor fiscale, înțelegerea automată a acestora, prezentarea grafică și stocarea acestora într-o bază de date locală și exportul acestora în cloud, de unde pot fi descărcate pentru o analiză mai amănunțită utilizând uneltele utilizatorului.

## Avantaje și obstacole

Am gândit \AppName{} în jurul ideii democratizării informațiilor financiare. Utilizarea bonurilor fiscale ca date de intrare pentru aplicație aduce avantajul de a nu depinde de modul în care a fost făcută achiziția. Majoritatea utilizatorilor au mai multe carduri, dar folosesc și bani lichizi pentru unele achiziții. Soluția propusă adună toate aceste tranzacții într-un singur loc.

Un alt avantaj este flexibilitatea datelor. Soluția propusă oferă o vizualizare rapidă a tranzacțiilor în aplicație, asemenea altor soluții, dar oferă și exportul datelor, pentru ca acestea să fie folosite pentru analize detaliate în programe cum ar fi *Excel*.

Principalul obstacol al acestei aplicații este înțelegerea automată a bonurilor fiscale. Am împărțit această sarcină în două probleme: *OCR* și extragerea informațiilor relevante din textul oferit de OCR. Deși problema recunoașterii caracterelor este considerată rezolvată în condiții ideale, aceasta pune în continuare probleme sub condiții imperfecte. În cadrul acestei aplicații, condițiile pentru OCR sunt date de hartie de proastă calitate, posibil mototolită, cerneală ștearsă și poze care pot fi de proastă calitate și pun probleme soluțiilor existente. Extragerea informațiilor din textul bonurilor fiscale este și ea o problemă dificilă deoarece bonurile nu respectă un format standard, iar rezultatul OCR-ului poate să nu fie perfect. Abordarea acestor probleme va fi prezentată într-un capitol ulterior.

## Lucrări asemănătoare

Analizarea și procesarea bonurilor fiscale pe baza imaginilor obținute folosind camera foto a telefoanelor a stârnit un interes moderat în comunitatea științifică și tehnică.

* [@Receipts2Go]
* [@DL_receipt_understanding]
* [@Heuristics_extract]

## Obiectivele lucrării

Așa cum am arătat în secțunile precedente, problema aleasă este una complexă, pentru care nu există o soluție standard. De asemenea, o abordare nouă are potențialul de a aduce valoare unei categorii de utilizatori. Așadar, această lucrare își propune:

* Dezvoltarea unui algoritm de procesare și analizare a bonurilor fiscale sub formă de imagini obținute cu un telefon mobil;
* Distribuirea acestui algoritm sub forma unei aplicații mobile, dezvoltată în manieră Open Source și publicată pe GitHub;
* Construirea unei baze de date cu imagini și informațiile aferente ce poate fi folosită în viitor pentru îmbunătățirea algoritmului.

## Structura lucrării

Lorem Ipsum
