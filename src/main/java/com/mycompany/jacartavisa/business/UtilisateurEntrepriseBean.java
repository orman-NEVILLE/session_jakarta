package com.mycompany.jacartavisa.business;

import com.mycompany.jacartavisa.entities.Utilisateur;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class UtilisateurEntrepriseBean {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void ajouterUtilisateurEntreprise(String username, String email, String password, String description) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Utilisateur utilisateur = new Utilisateur(username, email, hashedPassword, description);
        em.persist(utilisateur);
    }

    public List<Utilisateur> listerTousLesUtilisateurs() {
        return em.createQuery("SELECT u FROM Utilisateur u", Utilisateur.class).getResultList();
    }

    @Transactional
    public void supprimerUtilisateur(Long id) {
        Utilisateur utilisateur = em.find(Utilisateur.class, id);
        if (utilisateur != null) {
            em.remove(utilisateur);
        }
    }

    public Utilisateur trouverUtilisateurParId(Long id) {
        return em.find(Utilisateur.class, id);
    }

    public Utilisateur authentifier(String email, String password) {
        Utilisateur user = trouverUtilisateurParEmail(email);
        if (user != null && verifierMotDePasse(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public Utilisateur trouverUtilisateurParEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Utilisateur trouverUtilisateurParUserName(String username) {
        try {
            return em.createQuery("SELECT u FROM Utilisateur u WHERE u.username = :username", Utilisateur.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // Méthode pour vérifier un mot de passe
    public boolean verifierMotDePasse(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    // Nouvelle méthode pour modifier la description et le mot de passe d'un utilisateur
    @Transactional
    public void modifierUtilisateur(Long id, String newDescription, String newPassword) { 
        Utilisateur utilisateur = em.find(Utilisateur.class, id);
      if (utilisateur != null) { 
        if (newDescription != null && !newDescription.trim().isEmpty()){
            utilisateur.setDescription(newDescription);
        }
        if (newPassword != null && !newPassword.trim().isEmpty()){
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
         utilisateur.setPassword(hashedPassword); }
         em.merge(utilisateur); // Mettre à jour l'utilisateur en base de données
       }
    }
   }

