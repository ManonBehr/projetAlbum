package fr.uga.miashs.album.model;
//besoin de rajouter hashcode, equals
import java.net.URI;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.CascadeType;
import javax.validation.constraints.NotNull;

import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;

@Entity
public class Picture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	@ManyToOne
	private Album album;
	
	@NotNull
	private String title;
	
	@NotNull
	private URI uri;
	//fait le lien avec le web semantique
	
	@NotNull
	private String localfile;
	
	public Picture(){
	}
	

	public Picture(String titre, URI chemin, String cheminLocal) {
		title = titre;
		uri = chemin;
		localfile = cheminLocal;
	}
	
	public String getLocalfile() {
		return localfile;
	}
	
	public Album getPictAlbum(){
		return album;
	}
	
	public void setPictAlbum(Album album){
		this.album = album;
	}
	
	public void setPath(String p){
		localfile=p;
	}
	
	
}
