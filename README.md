![alt text](https://i.imgur.com/sLHGY3G.png "Svinicola")

Svinicola p un sito web stile e-commerce per la vendita di vini.

E' strutturato in modo responsive ed usa una architettura MVC scritta in Java attraverso Tomcat 9.0.

Usa AJAX per cercare nel catalogo in modo immediato e un JSON per tenere conto del carello.

Usa la libreria JSON-Java per e la libreria MysqlConnectorJ per connettersi al database.

E' impostato per il database in root con password admin, per cambiarlo, guarda dentro la webapp/META-INF/context.xml.

web.xml poco toccato: tutte le servlet sono state aggiunte tramite annotazioni.

# Problemi noti:
- Le JSP che dovrebbero essere più "private", sopratutto quelle dell'admin collegate a servlet doPost sono accessibili scrivendo l'indirizzo giusto.
- Il root di tutto il sito sembra essere il nome originale del progetto, ProgettoPetrossi; almeno quando lo apro con Eclipse dal portatile.
