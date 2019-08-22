# Specificațiile aplicației

În cadrul dezvoltării unui produs software, definirea specificațiilor are un rol crucial în succesul proiectului. Această etapă poate implica studii laborioase de piață, iar specificațiile pot trece prin mai multe etape, de la discuții cu utilizatorii până la documente formale ce ajung la programatori. În cazul de față, specificațiile sunt dictate de nevoile personale și pot fi formulate într-un limbaj mai apropiat de cel tehnic, gata de implementare.

Soecificațiile sunt definite în jurul noțiunii de *usecase* (caz de utilizare), așa cum acestea sunt definite în lucrarea *Structuring use cases with goals* [@cockburn1997structuring] și inspirate din exemplul prezentat în *The Pragmatic Programmer* [@Hunt:2000:PPJ:320326]. Așadar, **scopul** acestor *usecases* este de a defini specificațiile, **conținutul** este proza consistentă (descriere în cuvinte astfel încat să nu apară contradicții), **pluralitatea** este de unul sau mai multe scenarii per *usecase*, iar **structura** este semi-formală.

## Înțelegerea imaginilor

* **Scop**: Capturarea unei imagini și extragerea informațiilor relevante din aceasta;
* **Condiție de succes**: prezența unei înregistrări în baza de date ce modelează bonul fiscal în starea de draft;
* **Condiții de eșec**: imaginea nu poate fi capturată; modulul OCR nu funcționează; o imagine este deja în curs de procesare;

### Principalul scenariu

1. Utilizatorul capturează o imagine;
2. Modulul OCR este apelat; Textul și chenarele aferente sunt extrase;
3. Rezultatul OCR este procesat pentru a obține conținutul bonului;
4. Bonul fiscal este salvat în stadiu de draft pentru a fi editat; (Use Case 2)

### Variații

* Imaginea poate fi capturată utilizând camera telefonului sau importată din galerie;

### Extensii

1. Pentru a ajuta utilizatorul atunci când folosește camera, procesarea imaginilor venite de la cameră se face continuu, la o rată maximă configurabilă;
2. Nu pot fi procesate mai multe imagini în același timp. Starea ultimei procesări este accesibilă permanent. Dacă se primește o cerere de procesare înainte ca ultima să se fi încheiat este semnalată o eroare.
