package com.mycompany.jacartavisa.Bean;

import com.mycompany.jacartavisa.business.SessionManager;
import com.mycompany.jacartavisa.business.UtilisateurEntrepriseBean;
import com.mycompany.jacartavisa.entities.Utilisateur;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;


@Named(value = "profilBean")
@RequestScoped
public class ProfilBean {

    private String username;
    private String email;
    private String description;
    private String newDescription;
    private String newPassword;
    private String confirmPassword;

// Getters et Setters pour newPassword et confirmPassword


    @Inject 
    private UtilisateurEntrepriseBean utilisateurEntrepriseBean;
    
    @Inject 
    private SessionManager sessionManager;
    // Constructeur par défaut

   @PostConstruct
   public void init() {
       Utilisateur utilisateur = recupererSessionUser();
       if (utilisateur != null) {
           this.username = utilisateur.getUsername();
           this.email = recupererEmailUser(); // Si nécessaire
           this.description = utilisateur.getDescription(); // Mettez à jour la description ici
       }
   }
   

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }
    
  
    public void modifierProfil(){
            try{ 
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("modifier.xhtml");
            }catch (IOException e){
                
                e.printStackTrace();
        }
    }
    
    public  void deconnexion() {
        
         sessionManager.invalidateSession();
       }
    public  String recupererUsername() {
        
         Utilisateur utilisateur = recupererSessionUser();
         return utilisateur != null ? utilisateur.getUsername() : "Pas d'utilisateur";
       }
    public  String recupererEmailUser() {
        
         String email_session = sessionManager.getValueFromSession("user");
         return email_session != null ? email_session : "Pas d'utilisateur";
       }
    
    public String recupererDescriptionUser() {
         Utilisateur utilisateur = recupererSessionUser();
         return utilisateur != null ? utilisateur.getDescription() : "Pas d'utilisateur";
    }
    
    public Utilisateur recupererSessionUser() {
        
         String email = recupererEmailUser();
         System.out.println("Email de l'utilisateur connecté" + email);
         
         if (email == null || email.equals("Pas d'info sur le user")){
             return null;
         }
         
         Utilisateur utilisateur = utilisateurEntrepriseBean.trouverUtilisateurParEmail(email);
         
         if(utilisateur != null){
         System.out.println("Utilisateur " + utilisateur.getUsername());
         }else{
             System.out.println("Pas d'utilisateur trouvé avec ce mail" );
         }
         
         return utilisateur;
       }
    
    public String modifierProfil(String newDescription, String newPassword, String confirmPassword) {
        FacesContext context = FacesContext.getCurrentInstance();
     // Récupérer l'utilisateur connecté via la session
        Utilisateur utilisateur = recupererSessionUser();
        if (utilisateur == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Session expirée. Veuillez vous reconnecter.", ""));
            return "login?faces-redirect=true"; } // Vérifier si un nouveau mot de passe est fourni
        
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les mots de passe ne correspondent pas.", ""));
            return null; } } // Modifier les informations de l'utilisateur
        utilisateurEntrepriseBean.modifierUtilisateur(utilisateur.getId(), newDescription, newPassword); // Mettre à jour la session avec la nouvelle description si elle a changé
        if (newDescription != null && !newDescription.trim().isEmpty()) {
           sessionManager.createSession("user", newDescription);
        } context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modifications enregistrées avec succès.", ""));
           return "profil?faces-redirect=true"; 
}
}   
    

