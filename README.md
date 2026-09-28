TP 2 : Approfondissement de JPA, Hibernate et Validation de Données
Ce second TP fait suite à l'initiation à JPA et Hibernate. L'objectif était de structurer une application Java plus réaliste en mettant en place une architecture en couches (avec des services génériques), de manipuler plusieurs entités métier (Utilisateur et Salle), d'ajouter la validation de données avec Hibernate Validator, d'écrire des requêtes JPQL sur mesure et d'automatiser la validation via des tests unitaires JUnit.

1. Ce que nous avons appris et mis en place
Contrairement au premier TP où l'ensemble du code était concentré dans une seule classe, nous avons adopté ici une organisation modulaire et réutilisable :

Architecture générique avec Services CRUD :

Définition d'une interface générique CrudService<T, ID> spécifiant les opérations de base (save, findById, findAll, update, delete).

Implémentation d'une classe abstraite AbstractCrudService<T, ID> factorisant la gestion de l'EntityManager et des transactions pour éviter les doublons de code.

Création de services spécialisés (UtilisateurService et SalleService) étendant cette base pour y ajouter des requêtes spécifiques.

Validation des modèles (Bean Validation) :

Intégration de Hibernate Validator pour contrôler l'intégrité des données directement au niveau des entités (@NotNull, @Size, contraintes sur la capacité et le format des emails).

Requêtes JPQL dynamiques :

Recherche ciblée d'utilisateurs par adresse email (SELECT u FROM Utilisateur u WHERE u.email = :email).

Filtrage des salles selon leur disponibilité (WHERE s.disponible = :disponible) ou leur capacité minimale (WHERE s.capacite >= :capacite).

Tests automatisés avec JUnit :

Écriture de tests unitaires pour valider chaque opération CRUD (UtilisateurServiceTest, SalleServiceTest).

Vérification rigoureuse des états avec les assertions JUnit (assertNotNull, assertEquals, assertFalse).

2. Technologies utilisées
Langage : Java (JDK 17 ou supérieur)

Build tool : Maven

JPA API : javax.persistence-api (2.2)

ORM : Hibernate Core (5.6.5.Final)

Validation : Hibernate Validator (6.2.0.Final) + Jakarta Validation API

Base de données : H2 Database (2.1.214) exécutée en mémoire (mem)

Logging : SLF4J (Simple)

Tests : JUnit (4.13.2)

3. Structure du projet
Plaintext
TP 2 hib/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/example/
    │   │       ├── model/
    │   │       │   ├── Utilisateur.java     # Entité Utilisateur avec contrainte unique sur l'email
    │   │       │   └── Salle.java           # Entité Salle (nom, capacité, étage, disponibilité)
    │   │       ├── service/
    │   │       │   ├── CrudService.java         # Interface CRUD générique
    │   │       │   ├── AbstractCrudService.java # Gestion commune de l'EntityManager et des transactions
    │   │       │   ├── UtilisateurService.java  # Requêtes spécifiques Utilisateur (findByEmail)
    │   │       │   └── SalleService.java        # Requêtes spécifiques Salle (filtrage disponibilité & capacité)
    │   │       └── App.java                 # Démonstration console de l'ensemble du cycle de vie
    │   └── resources/
    │       └── META-INF/
    │           └── persistence.xml          # Unité de persistance "gestion-salles"
    └── test/
        └── java/
            └── com/example/service/
                ├── UtilisateurServiceTest.java  # Tests unitaires JUnit pour Utilisateur
                └── SalleServiceTest.java        # Tests unitaires JUnit pour Salle
4. Points techniques importants
Configuration de l'Unité de Persistance (persistence.xml)
L'unité porte le nom gestion-salles :

Mode transactionnel : RESOURCE_LOCAL (nous pilotons nous-mêmes l'ouverture et la fermeture des transactions via em.getTransaction().begin() et commit()).

Politique DDL : hibernate.hbm2ddl.auto = create-drop, ce qui permet d'avoir une base propre et recréée à chaque lancement puis nettoyée à la fermeture.

Dialecte : org.hibernate.dialect.H2Dialect avec affichage et formatage des requêtes SQL dans la console.

Gestion rigoureuse des transactions
Toutes les opérations d'écriture (persist, merge, remove) sont obligatoirement encapsulées dans un bloc transactionnel. En cas d'exception, un rollback() est systématiquement prévu pour laisser la base dans un état cohérent.



https://github.com/user-attachments/assets/e9dde63c-afb2-4dd6-a40a-da6abe49420e



