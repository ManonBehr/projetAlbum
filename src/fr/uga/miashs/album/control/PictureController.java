package fr.uga.miashs.album.control;

import java.net.URI;
import java.nio.file.Path;

import javax.inject.Inject;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.AlbumService;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;

public class PictureController {

	@Inject
	private AppUserSession appUserSession;

	
	
	private Album album;
	
	private Picture picture;
	
	public PictureController() {
	}
	

	
	public Picture getPicture() {
		if (album==null) {
			album = new Album(appUserSession.getConnectedUser());
		}
		if(picture==null){
			picture = new Picture();
			picture.setPictAlbum(album);
		}
		return picture;
	}
	

}
