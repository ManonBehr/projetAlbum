package fr.uga.miashs.album.service;

import fr.uga.miashs.album.model.Picture;

public class PictureService extends JpaService<Long, Picture>{

	public void createP(Picture p) throws ServiceException {
		p.setPictAlbum(getEm().merge(p.getPictAlbum()));
		super.create(p);
	}

}
