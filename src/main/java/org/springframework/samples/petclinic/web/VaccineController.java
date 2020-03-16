	package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vaccine;
import org.springframework.samples.petclinic.service.VaccineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VaccineController {
	
	@Autowired
	private VaccineService vaccineService;
	
	@GetMapping("/vaccine")
	public String listadoVaccine(ModelMap modelMap) {
		String vista="vaccine/vaccineList";
		
		List<Vaccine> vaccine=vaccineService.findAll();
		modelMap.addAttribute("vaccine",vaccine);
		
		return vista;
	}
	
	
	@GetMapping("/vaccine/{vaccineId}")
	public String ShowVaccineDetail(@PathVariable("vaccineId")  int vaccineId, Map<String,Object>  model ) {
		Vaccine v = vaccineService.findById(vaccineId);
		model.put("vaccine",v);
		return "vaccine/vaccineDetails";
	}
	
	@GetMapping(value = "/vaccine/new")
	public String initCreationVaccineForm(Map<String, Object> model) {
		Vaccine vaccine = new Vaccine();
		model.put("vaccine", vaccine);
		return "vaccine/vaccineCreate";
	}

	@PostMapping(value = "/vaccine/new")
	public String processVaccineForm(@Valid Vaccine vaccine, BindingResult result) {
		if (result.hasErrors()) {
			return "vaccine/vaccineCreate";
		}
		else {
		
			this.vaccineService.saveVaccine(vaccine);
			
			return "redirect:/vaccine/" + vaccine.getId();
		}
	}

	


}