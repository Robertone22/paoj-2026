# ArenaBid

## Despre proiect

ArenaBid este o aplicatie de consola scrisa in Java, care simuleaza o platforma de licitatii pentru obiecte sportive de colectie.

Ideea proiectului este urmatoarea: un seller poate adauga un obiect sportiv rar sau valoros, apoi poate crea o licitatie pentru acel obiect. Ceilalti utilizatori, adica bidderii, pot plasa oferte pe licitatiile deschise. La final, licitatia este inchisa, iar sistemul stabileste automat castigatorul pe baza celui mai mare bid.

Pe langa partea principala de licitatii, proiectul include si un sistem de giveaway. Daca o licitatie este premium, castigatorul ei este adaugat automat in giveaway-ul curent. In momentul in care se strang cel putin 10 participanti, giveaway-ul poate fi pornit, se extrage aleator un castigator si un premiu, iar apoi lista participantilor este resetata pentru runda urmatoare.

Aplicatia este realizata pe baza principiilor OOP si foloseste mostenire, clase abstracte, clase imutabile, exceptii custom, colectii si servicii singleton.

---

## Actiuni / interogari posibile in sistem

1. Adaugarea unui seller nou
2. Adaugarea unui bidder nou
3. Afisarea tuturor utilizatorilor
4. Stergerea unui utilizator dupa id
5. Cautarea unui utilizator dupa id
6. Cautarea utilizatorilor dupa nume
7. Adaugarea unui obiect sportiv de licitatie
8. Afisarea tuturor obiectelor sportive
9. Afisarea doar a obiectelor premium
10. Cautarea unui obiect sportiv dupa id
11. Crearea unei licitatii
12. Afisarea tuturor licitatiilor
13. Afisarea licitatiilor deschise
14. Cautarea unei licitatii dupa id
15. Plasarea unui bid pe o licitatie
16. Afisarea istoricului bid-urilor pentru o licitatie
17. Inchiderea unei licitatii si stabilirea castigatorului
18. Afisarea participantilor inscrisi in giveaway
19. Pornirea giveaway-ului daca sunt minimum 10 participanti, extragerea castigatorului si resetarea rundei

---

## Obiecte din domeniu

1. User
2. Participant
3. Seller
4. Bidder
5. AuctionItem
6. AuthenticityCertificate
7. Auction
8. Bid
9. Giveaway
10. GiveawayPrize
11. GiveawayWinner

---

## Cum este organizat proiectul

Proiectul este impartit in mai multe pachete, astfel incat codul sa fie mai clar si mai usor de urmarit:

- `model` - contine clasele de baza ale aplicatiei si enum-urile
- `service` - contine logica principala a aplicatiei, implementata prin servicii singleton
- `exception` - contine exceptiile custom
- `Main` - contine meniul interactiv din consola si punctul de pornire al aplicatiei

---

## Cateva detalii de implementare

- pentru utilizatori exista o ierarhie de mostenire de forma `User -> Participant -> Seller / Bidder`
- `User` este o clasa abstracta
- `AuthenticityCertificate` este o clasa imutabila
- serviciile principale sunt:
  - `UserService`
  - `AuctionService`
  - `GiveawayService`
- aplicatia foloseste colectii de tip `List` si `Map`
- bid-urile pot fi afisate sortat dupa valoare
- meniul este interactiv si este realizat cu `Scanner`

---

## Observatie

Aplicatia este gandita astfel incat structura din etapa 1 sa poata fi extinsa usor in etapa 2, unde se vor adauga persistenta in baza de date, JDBC, audit si tranzactii.