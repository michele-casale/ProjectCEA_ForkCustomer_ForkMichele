package it.dedagroup.project_cea.service.def;

import java.util.List;

import it.dedagroup.project_cea.dto.request.TechnicianRequest;
import it.dedagroup.project_cea.model.Technician;

public interface TechnicianServiceDef {
	
	void save(Technician t);
	Technician update(Technician t);
	
//	boolean removeTechnician (TechnicianRequest request);
	void remove(long id);
	void removeTechncianByUsername(String username);
	
	Technician findById(long idTechnician);
	Technician findByUsername(String username);
	List<Technician> findAll();
	List<Technician> findFree();
	Technician findByIntervention(long idIntervention);
}
