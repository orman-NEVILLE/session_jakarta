import com.mycompany.jacartavisa.business.LieuEntrepriseBean;
import com.mycompany.jacartavisa.entities.Lieu;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "lieuBean")
@RequestScoped
public class LieuBean implements Serializable {

    private int idLieuSelectionne; // Stocke l'ID du lieu en cours de modification
    private String nom;
    private String description;
    private double longitude;
    private double latitude;

    @Inject
    private LieuEntrepriseBean lieuEntrepriseBean;

    public List<Lieu> getLieux() {
        return lieuEntrepriseBean.listerTousLesLieux();
    }

    public void ajouterLieu() {
        if (nom != null && !nom.isEmpty() && description != null && !description.isEmpty()) {
            lieuEntrepriseBean.ajouterLieuEntreprise(nom, description, latitude, longitude);
            resetForm();
        }
    }

    public void supprimerLieu(int id) {
        lieuEntrepriseBean.supprimerLieu(id);
    }

    public void selectionnerLieuPourModification(Lieu lieu) {
        if (lieu != null) {
            this.idLieuSelectionne = lieu.getId();
            this.nom = lieu.getNom();
            this.description = lieu.getDescription();
            this.latitude = lieu.getLatitude();
            this.longitude = lieu.getLongitude();
        }
    }

public void modifierLieu() {
    if (idLieuSelectionne != 0) {
        Lieu lieu = lieuEntrepriseBean.trouverLieuParId(idLieuSelectionne);
        if (lieu != null) {
            lieu.setNom(nom);
            lieu.setDescription(description);
            lieu.setLatitude(latitude);
            lieu.setLongitude(longitude);
            lieuEntrepriseBean.modifierLieu(lieu);
            resetForm();
        }
    }
}


    private void resetForm() {
        this.idLieuSelectionne = 0;
        this.nom = "";
        this.description = "";
        this.latitude = 0;
        this.longitude = 0;
    }

    // Getters et Setters
    public int getIdLieuSelectionne() { return idLieuSelectionne; }
    public void setIdLieuSelectionne(int id) { this.idLieuSelectionne = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
}
