package fr.uga.miashs.album.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;


import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;

public class PictureService extends JpaService<Long, Picture>{
	EntityManager em = getEm();
	
	public void createP(Picture p, Album a) throws ServiceException {
		EntityTransaction t = em.getTransaction();
		t.begin();
		em.merge(p);
		t.commit();
		List<Picture> picturesIdToDelete = getPictureNull();
		updatePicture(p, a);
		for(int i=0; i<picturesIdToDelete.size(); i++)
			deletePicture(picturesIdToDelete.get(i).getId());
	}
	
	public void updatePicture(Picture p, Album a) {
		EntityTransaction t = em.getTransaction();
		Album alb = getAlbumById(a.getId());
		t.begin();
		p.setPictAlbum(alb);
		t.commit();
	}
	
	public void deletePicture(long id){
		EntityTransaction t = em.getTransaction();
		t.begin();
		Query query = em.createQuery("SELECT p FROM Picture p WHERE p.id="+id);
		Picture p = (Picture) query.getSingleResult();
		em.remove(p);
		t.commit();
	}
	
	public List<Picture> getPictureNull(){
		// get List of pictures to delete
		List<Picture> picturesNull = new ArrayList<Picture>();
		Query query = em.createQuery("SELECT p FROM Picture p WHERE p.album IS NULL");
		picturesNull = query.getResultList();
		return picturesNull;
	}
	
	public Album getAlbumById(long albumId){
		Query query = em.createQuery("SELECT a FROM Album a WHERE a.id="+albumId);
		return (Album) query.getSingleResult();
	}
	
	public List<Picture> listPictureOwnedBy(Album a) throws ServiceException {
		//request is defined in class Picture with an annotation
		Query query = getEm().createNamedQuery("Picture.findAllPicture");
		query.setParameter("album", getEm().merge(a));
		return query.getResultList();
	}

}
