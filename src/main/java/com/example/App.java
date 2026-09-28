package com.example;

import com.example.model.Salle;
import com.example.model.Utilisateur;
import com.example.service.SalleService;
import com.example.service.UtilisateurService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class App {
    public static void main(String[] args) {
        // 1. Initialisation de l'EntityManagerFactory selon le persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion-salles");

        // 2. Instanciation des services
        UtilisateurService utilisateurService = new UtilisateurService(emf);
        SalleService salleService = new SalleService(emf);

        try {
            // Test des opérations CRUD pour Utilisateur
            System.out.println("\n==========================================");
            System.out.println("         TEST CRUD UTILISATEUR            ");
            System.out.println("==========================================");
            testCrudUtilisateur(utilisateurService);

            // Test des opérations CRUD pour Salle
            System.out.println("\n==========================================");
            System.out.println("            TEST CRUD SALLE               ");
            System.out.println("==========================================");
            testCrudSalle(salleService);

        } finally {
            // 3. Fermeture propre de la fabrique et destruction de la base H2 en mémoire
            emf.close();
        }
    }

    private static void testCrudUtilisateur(UtilisateurService service) {
        // --- CREATE ---
        System.out.println("\n--- [CREATE] Ajout d'utilisateurs ---");
        Utilisateur u1 = new Utilisateur("Dupont", "Jean", "jean.dupont@example.com");
        u1.setDateNaissance(LocalDate.of(1985, 5, 15));
        u1.setTelephone("+33612345678");

        Utilisateur u2 = new Utilisateur("Martin", "Sophie", "sophie.martin@example.com");
        u2.setDateNaissance(LocalDate.of(1990, 10, 20));
        u2.setTelephone("+33687654321");

        service.save(u1);
        service.save(u2);

        // --- READ ---
        System.out.println("\n--- [READ] Tous les utilisateurs ---");
        List<Utilisateur> utilisateurs = service.findAll();
        utilisateurs.forEach(System.out::println);

        System.out.println("\n--- [READ] Recherche par ID (1) ---");
        Optional<Utilisateur> utilisateurOpt = service.findById(1L);
        utilisateurOpt.ifPresent(System.out::println);

        System.out.println("\n--- [READ] Recherche JPQL par Email ---");
        Optional<Utilisateur> utilisateurParEmail = service.findByEmail("sophie.martin@example.com");
        utilisateurParEmail.ifPresent(System.out::println);

        // --- UPDATE ---
        System.out.println("\n--- [UPDATE] Modification du numéro de téléphone ---");
        utilisateurOpt.ifPresent(utilisateur -> {
            utilisateur.setTelephone("+33699887766");
            service.update(utilisateur);
            System.out.println("Utilisateur après modification : " + utilisateur);
        });

        // --- DELETE ---
        System.out.println("\n--- [DELETE] Suppression par ID (2) ---");
        service.deleteById(2L);
        System.out.println("Utilisateur ID=2 supprimé.");

        System.out.println("\n--- [READ] Liste finale après suppression ---");
        service.findAll().forEach(System.out::println);
    }

    private static void testCrudSalle(SalleService service) {
        // --- CREATE ---
        System.out.println("\n--- [CREATE] Ajout de salles ---");
        Salle s1 = new Salle("Salle A101", 30);
        s1.setDescription("Salle de réunion équipée d'un projecteur");
        s1.setEtage(1);

        Salle s2 = new Salle("Amphithéâtre B201", 150);
        s2.setDescription("Grand amphithéâtre pour conférences");
        s2.setEtage(2);

        Salle s3 = new Salle("Salle C305", 10);
        s3.setDescription("Petite salle pour entretiens");
        s3.setEtage(3);
        s3.setDisponible(false);

        service.save(s1);
        service.save(s2);
        service.save(s3);

        // --- READ ---
        System.out.println("\n--- [READ] Toutes les salles ---");
        List<Salle> salles = service.findAll();
        salles.forEach(System.out::println);

        System.out.println("\n--- [READ] Recherche salle par ID (2) ---");
        Optional<Salle> salleOpt = service.findById(2L);
        salleOpt.ifPresent(System.out::println);

        System.out.println("\n--- [READ] Salles disponibles uniquement ---");
        List<Salle> sallesDisponibles = service.findByDisponible(true);
        sallesDisponibles.forEach(System.out::println);

        System.out.println("\n--- [READ] Salles de capacité >= 50 ---");
        List<Salle> sallesGrandes = service.findByCapaciteMinimum(50);
        sallesGrandes.forEach(System.out::println);

        // --- UPDATE ---
        System.out.println("\n--- [UPDATE] Extension capacité amphithéâtre à 200 ---");
        salleOpt.ifPresent(salle -> {
            salle.setCapacite(200);
            service.update(salle);
            System.out.println("Salle après mise à jour : " + salle);
        });

        // --- DELETE ---
        System.out.println("\n--- [DELETE] Suppression salle ID (3) ---");
        service.deleteById(3L);
        System.out.println("Salle ID=3 supprimée.");

        System.out.println("\n--- [READ] Liste finale des salles ---");
        service.findAll().forEach(System.out::println);
    }
}