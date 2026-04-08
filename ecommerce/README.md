# E-Commerce

Implementazione di un sistema di E-Commerce caratterizzata dalle seguenti entità:
- Il CLIENTE ovvero colui che inserisce gli ordini a sistema. È caratterizzato dai seguenti attributi:
  - Nome 
  - Cognome
  - Data di nascita
  - Codice fiscale 
  - Email
- Il PRODOTTO ovvero un articolo ordinabile dal cliente tramite il sistema. Un prodotto è
  caratterizzato da:
  - Codice
  - Nome
  - Stock (ovvero la quantità di prodotti disponibili nel sistema)
- L'ORDINE rappresenta la richiesta da parte di un cliente di uno o più prodotti, ciascuno secondo
  una quantità.

## Servizi

Il sistema gestisce i seguenti servizi:
- Gestire i clienti (aggiungerli e visualizzarli)
- Gestire i prodotti (inserirli, consultarli e tenere traccia dello stock disponibile)
- Gestire gli ordini (creare ordini con uno o più prodotti e visualizzarli)
- Controllare la disponibilità (impedisce di ordinare più prodotti di quelli disponibili)
- Aggiornare automaticamente lo stock (quando viene effettuato un ordine)
- Esporre tutte le funzionalità tramite API REST
- Gestire grandi quantità di dati (tramite paginazione delle liste)
- Supportare operazioni contemporanee (evita errori quando più utenti ordinano insieme)
- Salvare i dati in un database
- Gestire lo stato degli ordini (es. ordinato → consegnato)
- Cancellare ordini (solo se non sono già stati consegnati)

Le funzionalità sono state esposte tramite un'API che segua il paradigma REST

## Start

### Command Line

Posizionarsi nella cartella del progetto

- export JAVA_HOME="PATH" (se non settata, il path è quello della variabile d'ambiente o da IntelliJ > Projects Structure > Jdks 
- ./mvnw.cmd clean package
- ./mvnw spring-boot:run

### IDE

Aprire il progetto con un IDE (Per questo progetto è stato usato IntelliJ)

- ECommerceApplication -> Start Application

