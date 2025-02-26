/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jacartavisa.Bean;

import com.mycompany.jacartavisa.business.SessionManager;
import com.mycompany.jacartavisa.business.UtilisateurEntrepriseBean;
import com.mycompany.jacartavisa.entities.Utilisateur;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Orman
 */
@Named(value="utilisateurBean" )
@RequestScoped
public class UtilisateurBean {
    
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit avoir entre 3 et"
            + " 50 caractères")
    private String username;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
      @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
        message = "Le mot de passe doit contenir au moins une majuscule, un chiffre et un caractère spécial"
    )
    private String password;
    
     @NotBlank(message = "Veuillez confirmer votre mot de passe")
    private String confirmPassword;
     
    private String description;

    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
      @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
        message = "Le mot de passe doit contenir au moins une majuscule, un chiffre et un caractère spécial"
    )
    private String newPassword;
    
    private String newdescription;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewdescription() {
        return newdescription;
    }

    public void setNewdescription(String newdescription) {
        this.newdescription = newdescription;
    }
    
    
    @Inject
    private UtilisateurEntrepriseBean utilisateurEntrepriseBean;
    
    @Inject 
    private SessionManager sessionManager;
    
    public void ajouterUtilisateur() {
        FacesContext context = FacesContext.getCurrentInstance();
        // Vérifier si les mots de passe correspondent
        if (!password.equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les mots de passe ne correspondent pas", null));
            return;
        }
        
        if (utilisateurEntrepriseBean.trouverUtilisateurParUserName(username)== null &&
                utilisateurEntrepriseBean.trouverUtilisateurParEmail(email) == null){
            
            utilisateurEntrepriseBean.ajouterUtilisateurEntreprise(username, email, password, description);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Utilisateur ajouté avec succès", null));
             System.out.println("Utilisateur ajouté : " + username + " - " + email);
        }else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " « Ce nom d'utilisateur et cette adresse existent déjà. »", null));
             System.out.println("Utilisateur ajouté : " + username + " - " + email);
        }

       
        // Réinitialisation des champs
        username = "";
        email = "";
        password = "";
        description = "";
    }
    
    public String modifierE() {
        FacesContext context = FacesContext.getCurrentInstance(); // Vérification si l'utilisateur est connecté
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session == null) { 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Session expirée, veuillez vous reconnecter.", ""));
            return "login?faces-redirect=true";
        }
        String email = (String) session.getAttribute("email");
        Utilisateur utilisateur = utilisateurEntrepriseBean.trouverUtilisateurParEmail(email);
        if (utilisateur == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Utilisateur introuvable", ""));
                return null;
        } // Vérifier si les mots de passe correspondent
        if (!newPassword.equals(confirmPassword)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les mots de passe ne correspondent pas", ""));
                return null;
        } // Modifier les informations
        utilisateurEntrepriseBean.modifierUtilisateur(utilisateur.getId(), newdescription, newPassword); // Mettre à jour la session 
        session.setAttribute("description", newdescription);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modifications enregistrées avec succès", ""));
        return "profil?faces-redirect=true";
    }
    
        public String modifier() {
            FacesContext context = FacesContext.getCurrentInstance(); // Vérifier si l'utilisateur est connecté en session 
            String email = sessionManager.getValueFromSession("email");

            System.out.println("Email récupéré de la session : " + email);
  

            if (email == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Session expirée, veuillez vous reconnecter.", ""));
                return "login?faces-redirect=true";
            } else {
            System.out.println("Recherche de l'utilisateur avec l'email : " + email);
            }
            Utilisateur utilisateur = utilisateurEntrepriseBean.trouverUtilisateurParEmail(email);
            if (utilisateur == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Utilisateur introuvable", ""));
                return null;
            }   
            // Vérifier si les mots de passe correspondent (si un nouveau mot de passe est fourni)
            if (newPassword != null && !newPassword.isEmpty()) {
                if (!newPassword.equals(confirmPassword)) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les mots de passe ne correspondent pas", ""));
                    return null; }
            } // Modifier les informations
            utilisateurEntrepriseBean.modifierUtilisateur(utilisateur.getId(), newdescription, newPassword); // Mettre à jour la session avec la nouvelle description si elle a changé
            if (newdescription != null && !newdescription.trim().isEmpty()) {
                sessionManager.createSession("description", newdescription); 
            } 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modifications enregistrées avec succès", "")); 
        return "profil?faces-redirect=true"; } 
}
