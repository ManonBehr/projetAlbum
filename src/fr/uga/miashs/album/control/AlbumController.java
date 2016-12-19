package fr.uga.miashs.album.control;

import java.io.*;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.enterprise.context.*;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import fr.uga.miashs.album.model.Album;
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
	public void createPicture(Picture picture){
		try {
			pictureService.createP(picture);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public void setCurrentAlbum(Album alb) {
		currentAlbum = alb;
	}
	
//	public void upload(FileUploadEvent event) throws IOException {
//		String file_name = event.getFile().getFileName();
//		Path f = Paths.get("/home/manon/Bureau/workspace/ProjetAlbum2016/photos/", file_name);
//		OutputStream out = null;
//		InputStream in = null;
//		try {
//			 out = Files.newOutputStream(f);
//			 in = event.getFile().getInputstream();
//			 byte[] buf = new byte[512];
//			 int nb = 0;
//			 while ((nb = in.read(buf)) != -1)
//				 out.write(buf, 0, nb);
//			 
//		} catch (NoSuchFileException e) {
//			System.err.println("le fichier " + file_name + " n'existe pas");
//		}
//		finally {
//			if (in != null)
//				in.close();
//			if (out != null)
//				out.close();
//		}	
//		currentAlbum.addPicture(f.toString());
//		 	
//	}
	
	public void upload() throws IOException {
		if(file != null){
			String file_name = file.getFileName();
			Path f = Paths.get("/home/manon/Bureau/workspace/ProjetAlbum2016/photos/", file_name);
			OutputStream out = null;
			InputStream in = null;
			try {
				 out = Files.newOutputStream(f);
				 in = file.getInputstream();
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
			Picture pict = new Picture();
			pict.setPath(f.toString());
			pict.setPictAlbum(currentAlbum);
			currentAlbum.addPicture(pict);
			this.createPicture(pict);
		} 	
	}
	
	public List<Album> getListAlbumOwnedByCurrentUser() {
		try {
			return albumService.listAlbumOwnedBy(appUserSession.getConnectedUser());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
