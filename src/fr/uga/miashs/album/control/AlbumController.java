package fr.uga.miashs.album.control;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.*;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.annotation.PostConstruct;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.AlbumService;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

@ManagedBean
@Named
@SessionScoped
public class AlbumController implements Serializable {

	@Inject
	private AppUserSession appUserSession;
	
	@Inject
	private AlbumService albumService;
	@Inject
	private PictureService pictureService;
	
	private Album album;
	private Album currentAlbum;
	private String albId;
	
	public String getAlbumName(){
		return currentAlbum.getTitle();
	}
	
	public String getAlbId() {
		return albId;
	}
	public void setAlbId(String albId) {
		this.albId = albId;
	}
	private UploadedFile file;
	
	public UploadedFile getFile(){
		return file;
	}
	public void setFile(UploadedFile photo){
		this.file = photo;
	}
	
	public Album getAlbum() {
		if (album==null) {
			album = new Album(appUserSession.getConnectedUser());
		}
		return album;
	}
	
	
	public String createAlbum() {
		try {
			albumService.create(album);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return Pages.list_album;
	}
	
	public void deleteAlbum(Album alb){
		System.out.println("id de l'album à supprimer : " + alb.getId());
		albumService.delete(alb);
	}
	
	public void createPicture(Picture picture, Album alb){
		try {
			pictureService.createP(picture, alb);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public void setCurrentAlbum(Album alb) {
		currentAlbum = alb;
		System.out.println("alboum courant : " + currentAlbum.getId());
	}
	
	
	
	public void upload(FileUploadEvent event) throws IOException, URISyntaxException {
		String file_name = event.getFile().getFileName();
		
		String s = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/photos/");
		Path f = Paths.get(s, file_name);
		OutputStream out = null;
		InputStream in = null;
		try {
			 out = Files.newOutputStream(f);
			 in = event.getFile().getInputstream();
			 byte[] buf = new byte[512];
			 int nb = 0;
			 while ((nb = in.read(buf)) != -1)
				 out.write(buf, 0, nb);
			 
		} catch (NoSuchFileException e) {
			System.err.println("le fichier " + file_name + " n'existe pas");
		}
		finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
		String urlStr = "http://semanticweb.org/masterDCISS/projetAlbum#"+file_name;
		URL url = new URL(urlStr);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		Picture pict = new Picture("default_title", uri, f.toString(), file_name);
		System.out.println(currentAlbum.getTitle() + "titre");
		currentAlbum.addPicture(pict);
		createPicture(pict, currentAlbum);
		 	
	}
		
	public List<Album> getListAlbumOwnedByCurrentUser() {
		try {
			return albumService.listAlbumOwnedBy(appUserSession.getConnectedUser());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<String> getListPictureOwnedByCurrentAlbum() {
		try {
			long id = Long.parseLong(albId);
			List<Picture> allPictures = pictureService.listPictureOwnedBy(id);
			//System.out.println("id de l'alb courant" + alb.getId());
			List<String> pictName = new ArrayList<String>();
			for(int i=0; i<allPictures.size(); i++){
				pictName.add(allPictures.get(i).getFileName());
				System.out.println("photos de l'album courant : " + pictName.get(i));
			}
			return pictName;
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
