# Specificațiile aplicației

În cadrul dezvoltării unui produs software, definirea specificațiilor are un rol crucial în succesul proiectului. Această etapă poate implica studii laborioase de piață, iar specificațiile pot trece prin mai multe etape, de la discuții cu utilizatorii până la documente formale ce ajung la programatori. În cazul de față, specificațiile sunt dictate de nevoile personale și pot fi formulate într-un limbaj mai apropiat de cel tehnic, gata de implementare.

Soecificațiile sunt definite în jurul noțiunii de *usecase* (caz de utilizare), așa cum acestea sunt definite în lucrarea *Structuring use cases with goals* [@cockburn1997structuring] și inspirate din exemplul prezentat în *The Pragmatic Programmer* [@Hunt:2000:PPJ:320326]. Așadar, **scopul** acestor *usecases* este de a defini specificațiile, **conținutul** este proza consistentă (descriere în cuvinte astfel încat să nu apară contradicții), **pluralitatea** este de unul sau mai multe scenarii per *usecase*, iar **structura** este semi-formală.

## Înțelegerea imaginilor

* **Scop**: Capturarea unei imagini și extragerea informațiilor relevante din aceasta;
* **Condiție de succes**: Prezența unei înregistrări în baza de date ce modelează bonul fiscal în starea de draft;
* **Condiții de eșec**: Imaginea nu poate fi capturată; modulul OCR nu funcționează; o imagine este deja în curs de procesare;
* **Precondiții**: Valorile predefinite pentru categorie și monedă;

### Mențiuni

Informațiile relevante de extras dintr-o imagine sunt:

* numele comerciantului;
* data tranzacției;
* suma totală;
* moneda;
* categoria tranzacției;
* produse:
    * numele produsului;
    * prețul aferent;
* elementele OCR:
    * coordonatele casetelor ce înconjoară o unitate de text;
    * textul aferent;

Imaginile se salvează astfel încât să nu fie accesibile din galerie.

### Principalul scenariu

1. Utilizatorul capturează o imagine;
2. Modulul OCR este apelat; Textul și chenarele aferente sunt extrase;
3. Rezultatul OCR este procesat pentru a obține conținutul bonului;
4. Bonul fiscal este salvat în stadiu de draft pentru a fi editat; (Editare draft)

### Variații

* Imaginea poate fi capturată utilizând camera telefonului sau importată din galerie;
* Moneda și categoria pot avea valori prestabilite, ce se stabilesc din setări; (Editare setări)

### Extensii

* Pentru a ajuta utilizatorul atunci când folosește camera, procesarea imaginilor venite de la cameră se face continuu, la o rată maximă configurabilă;
* Nu pot fi procesate mai multe imagini în același timp. Starea ultimei procesări este accesibilă permanent. Dacă se primește o cerere de procesare înainte ca ultima să se fi încheiat este semnalată o eroare.

## Editere draft

* **Scop**: Validarea informațiilor extrase din imagine de către utilizator;
* **Condiție de succes**: Modificările făcute de utilizator se reflectă în baza de date; Bonul este validat și marcat ca final;
* **Condiții de eșec**: Modificările nu pot fi persistate; Modificările sunt invalide;

### Mențiuni

Algoritmul de validare poate fi subiectul unor modificări ulterioare și trebuie să fie ușor de modificat. 

Validarea considerată la momentul scrierii presupune ca niciun câmp să nu fie null sau fără conținut.

### Principalul scenariu

1. Utilizatorul accesează un bon;
2. Utilizatorul modifică câmpurile dorite;
3. Utilizatorul cere validarea bonului; Validarea se efectuează cu succes;
4. Bonul este scos din lista *drafts* și pus în lista bonurilor validate;

### Variații

* Utilizatorul poate modifica valori, dar fără a valida bonul;
* Utilizatorul poate valida bonul, ceea ce îl scoate din lista de *drafts* și îl pune în lista de bonuri valide;
* Accesarea unui bon se face fie prin alegerea acestuia din listă, fie în urma scanării unei imagini; (Înțelegerea imaginilor)

### Extensii

* Utilizatorul poate vedea imaginea capturată, cu și fără elementele OCR;
* Utilizatorul poate vedea toate bonurile din lista *drafts* și poate naviga către unul din ele;

## Gestionare setări

* **Scop**: Modificarea și accesarea unor valori folosite în diferite puncte ale aplicației;
* **Scenariu de succes**: Modificările făcute de utilizator sunt persistate și pot fi accesate;
* **Scenarii de eșec**: Modificările nu pot fi persistate; Valorile nu pot fi accesate;

Setările considerate sunt:

* Valoarea predefinită pentru categorie;
* Valoarea predefinită pentru monedă;
* Activarea sau dezactivarea colectării anonime de date;

### Principalul scenariu

1. Utilizatorul accesează setările
2. Utilizatorul modifică valoarea unei setări;
3. Noua valoare este persitată și accesibilă;

## Colectare bonuri fiscale

* **Scop**: Utilizatorul salvează un bon, acesta este sincronizat in cloud numai dacă utilizatorul permite colectarea de date;
* **Condiție de succes**: Bonul este trimis cu succes către server;
* **Condiții de eșec**: Colectarea este permisă, utilizatorul salvează un bon, acesta nu este sincronizat în cloud; Datele nu pot fi accesate la momentul sincronizării;
* **Precondiții**: Colectarea este permisă sau nu

### Mențiuni

Acțiunea de sincronizare se face în background, fără ca atenția utilizatorului să fie atrasă. Sincronizarea se face numai pe conexiune Wi-Fi și poate fi amânată până când conexiunea este disponibilă.

Se sincronizează toate informațiile aferente bonului, inclusiv imaginea și elementele OCR.

### Prinipalul scenariu

1. Utilizatorul finalizează salvarea unui bon cu succes;
2. În consecința acțiunii de salvare, precondiția este interogată;
3. Dacă este perisă colectarea, bonul este sincronizat în cloud;

## Export

* **Scop**: Accesarea datelor în afara aplicației și a dispozitivului;
* **Condiție de succes**: Utilizatorul selectează formatul, conținutul și perioada pentru export și primește un link la care poate accesa datele;
* **Condiție de eșec**: Nu există date înregistrate în perioada selectată; Datele nu sunt trimise cu succes; Utilizatorul nu primește link-ul aferent;

### Mențiuni

Pentru a consuma cât mai puține resurse (timp, baterie), exportul se face cu minim de procesare pe dispozitiv;

Datele salvate pe cloud au o dată de expirare, după care sunt șterse;

Odată ce datele sunt încărcate și procesate în cloud, aplicația primește o notificare ce conține link-ul de descărcare;

Datele pot fi descărcate într-o arhivă zip;

### Variații

* Pentru a oferi maximum de flexibilitate utilizatorilor, datele pot fi accesate in format JSON sau CSV și pot conține fie doar text, fie text și imagini;

### Extensii

* În cazul lipsei de conectivitate, acțiunea de export este programată pentru o dată ulterioară, odată ce telefonul are conexiune;
* Toate sesiunile de export sunt înregistrare într-o listă și sunt eliminate odată ce datele aferente sunt șterse din cloud;
