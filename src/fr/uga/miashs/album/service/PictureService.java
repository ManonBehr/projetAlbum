package fr.uga.miashs.album.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.Picture;

public class PictureService extends JpaService<Long, Picture>{
	EntityManager em = getEm();
	EntityTransaction t = em.getTransaction();
	
	public void createP(Picture p, Album a) throws ServiceException {
		t.begin();
		em.merge(p);
		t.commit();
		updatePicture(p, a);

	}
	
	public void updatePicture(Picture p, Album a) {
		Album alb = getAlbumById(a.getId());
		t.begin();
		p.setPictAlbum(alb);
		t.commit();
	}
	
	public Album getAlbumById(long albumId){
		Query query = em.createQuery("SELECT a FROM Album a WHERE a.id="+albumId);
		return (Album) query.getSingleResult();
	}

}
