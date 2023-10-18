package it.dedagroup.project_cea.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dedagroup.project_cea.dto.request.TechnicianRequest;
import it.dedagroup.project_cea.exception.model.UserNotFoundException;
import it.dedagroup.project_cea.model.Technician;
import it.dedagroup.project_cea.repository.TechnicianRepository;
import it.dedagroup.project_cea.service.def.TechnicianServiceDef;

@Service
public class TechnicianServiceImpl implements TechnicianServiceDef{
	
	@Autowired
	TechnicianRepository techRepo;
	
	@Override
	public void save(Technician t) {
		techRepo.save(t);
		
	}

	@Override
	public Technician update(Technician t) {
		Technician tech = techRepo.findById(t.getId()).orElse(null);
		t.setName(t.getName());
		t.setSurname(t.getSurname());
		t.setUsername(t.getUsername());
		t.setPassword(t.getPassword());
		t.setRole(t.getRole());
		techRepo.save(tech);
		return tech;
	}

	@Override
	public Technician findByIntervention(long idIntervention) {
		techRepo.findByInterventions_Id(idIntervention).orElseThrow(()->new UserNotFoundException("Nessun tecnico trovato con ID intervento: " + idIntervention));
		return null;
	}

	@Override
	public Technician findById(long idTechnician) {
		techRepo.findById(idTechnician).orElseThrow(()->new UserNotFoundException("Nessun tecnico trovato con ID utente: " + idTechnician));
		return null;
	}

	@Override
	public Technician findByUsername(String username) {
		techRepo.findByUsername(username).orElseThrow(()->new UserNotFoundException("Nessun tecnico trovato con username: " +username));
		return null;
	}

	@Override
	public List<Technician> findAll() {
		return techRepo.findAll();
	}

	@Override
	public List<Technician> findFree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(long id) {
		techRepo.deleteById(id);
	}

	@Override
	public void removeTechncianByUsername(String username) {
		Technician tech = findByUsername(username);
		techRepo.delete(tech);
		
	}


}
