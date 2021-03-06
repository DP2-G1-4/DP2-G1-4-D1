package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnnouncementService {

	private AnnouncementRepository annrepository;

	@Autowired
	public AnnouncementService(AnnouncementRepository annrepository) {
		this.annrepository = annrepository;
	}
	
	@Transactional(readOnly = true)	
	public Collection<Announcement> findAnnouncements() {
		return annrepository.findAll();
	}
	
	public Collection<Announcement> findAnnouncementsByTag(String tag){
		return annrepository.findByTag(tag);
	}
	
	public Announcement findAnnouncementById(int id) {
		
		return annrepository.findById(id);
	}
	
	public Collection<Announcement> findOldAnnouncements(){
		return annrepository.findOld();
	}
	
	public void saveAnnouncement(@Valid Announcement announcement){
		 annrepository.save(announcement);
		
	}
	
	public Vet findVetByUser(String user){
		return annrepository.findVetByUserId(user);
	}	

	
}
