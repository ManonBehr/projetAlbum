package fr.uga.miashs.album.control;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@SessionScoped
public class PictureAnnotationController implements Serializable {

	private String propriete;
	private String valeurPropriete;
	
	private String requete;
	
	public void testReq(){
		System.out.println("test req : " + this.requete);
	}
	
	public String getRequete() {
		System.out.println("requete numero : " + requete);
		return requete;
	}

	public void setRequete(String requete) {
		this.requete = requete;
		System.out.println("ap maj requete numero : " + requete);
	}

	public String getPropriete() {
		System.out.println("propriété : " + propriete);
		return propriete;
	}

	public void setPropriete(String propriete) {
		this.propriete = propriete;
	}

	public String getValeurPropriete() {
		System.out.println("valeur de la propriété : " + valeurPropriete);
		return valeurPropriete;
	}

	public void setValeurPropriete(String valeurPropriete) {
		this.valeurPropriete = valeurPropriete;
	}
}
