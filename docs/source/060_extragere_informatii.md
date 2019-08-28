## Înțelegerea conținutului bonurilor fiscale

Înțelegerea automată a informațiilor din imagini cu bonuri fiscare reprezintă pricipala provocare a acestui proiect. Informațiile de interes considerate sunt *numele comerciantului, data tranzacției, totalul cheltuit și lista de produse și prețuri asociate*. Această provocare se desparte în mod natural în două sarcini:

* detectarea textului din imagini;
* extragerea de informații relevante din acest text;

Există câteva lucrări în comunitatea științifică axate pe analizarea și extragerea informațiilor din bonuri fiscale. Lucrarea [@DL_receipt_understanding] prezintă o metodă bazată pe Deep Learning, iar [@Receipts2Go] și [@Heuristics_extract] abordează problema prin prisma unor reguli euristice de extragere. În continuare sunt prezentare abordările mele pentru extragerea informațiilor de interes.

### Detectarea textului din imagini

Soluția open soruce standard pentru probleme de OCR este , dezvoltat în prezent de Google și disponibil sub licența Apache 2.0. Tesseract poate fi integrat intr-o aplicație Android, deci este un candidat pentru a rezolva problema de OCR.

În contextul aplicațiilor mobile, atât Android cât și iOS, Google pune la dispoziție SDK-ul Firebase ML Kit care conține și o librărie pentru OCR, cu suport pentru procesare atât local, pe dispozitiv, cât și în cloud. Acest SDK nu este open source și este supus politicii de monetizare stabilită de Google, dar rularea algoritmilor pe dispozitiv este gratuită.

Comparând cele două soluții, am observat că Tesseract necesită o amplă preprocesare a imaginilor, iar performanța acestuia nu este una foarte bună. În schimb, ML Kit oferă o performanță superioară, fără a necesita preprocesarea imaginii.

De exemplu:

\begin{figure}[!Ht]
  \includegraphics[width=\linewidth,angle=-90,origin=c,height=2cm]{./source/figures/receipt.jpg}
  \caption{A really Awesome Image}\label{fig:awesome_image1}
\end{figure}
