	package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Insurance;
import org.springframework.samples.petclinic.model.InsuranceBase;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vaccine;
import org.springframework.samples.petclinic.service.InsuranceBaseService;
import org.springframework.samples.petclinic.service.InsuranceService;
import org.springframework.samples.petclinic.service.VaccineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class VaccineController {
	
	@Autowired
	private VaccineService vaccineService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceBaseService insuranceBaseService;
	
	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	@InitBinder("vaccine")
	public void initPetBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new VaccineValidator());
	}
	
	@GetMapping("/vaccine")
	public String listadoVaccine(ModelMap modelMap) {
		String vista="vaccine/vaccineList";		
		List<Vaccine>vaccine=vaccineService.findAll();	
		List<Vaccine> vaccines = new ArrayList<Vaccine>();
		for (Vaccine v: vaccine) {
			if(v.getExpirated().equals(false)) {
				vaccines.add(v);
			}
		}
		modelMap.addAttribute("vaccine",vaccines);
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
		List<PetType> types = vaccineService.findPetTypes();
		model.put("vaccine", vaccine);
		model.put("types",types);
		return "vaccine/vaccineCreate";
	}

	@PostMapping(value = "/vaccine/new")
	public String processVaccineForm(@Valid Vaccine vaccine, BindingResult result, Map<String, Object> model) {
		if (result.hasErrors()) {
			List<PetType> types = vaccineService.findPetTypes();
			model.put("types",types);
			return "vaccine/vaccineCreate";
		}
		else {
		
			this.vaccineService.saveVaccine(vaccine);
			
			return "redirect:/vaccine/" + vaccine.getId();
		}
	}

	@GetMapping(value = "/vaccine/expirated")
	public String showExpiratedVaccine(ModelMap modelMap) {
		String vista="vaccine/vaccineExpirated";
		
		List<Vaccine> vaccine=vaccineService.findAllExpirated();
		modelMap.addAttribute("vaccine",vaccine);
		
		return vista;
	}
	
	@GetMapping(value = "/vaccine/stock")
	public String showLowStockVaccine(ModelMap modelMap) {
		String vista="vaccine/vaccineStock";
		
		List<Vaccine> vaccine=vaccineService.findVaccinesWithLowStock();
		modelMap.addAttribute("vaccine",vaccine);
		return vista;
	}
	@GetMapping(value= "/vaccine/{vaccineId}/delete")
    public String delete(@PathVariable("vaccineId") int vaccineId, ModelMap model) {
       Collection<Insurance> insurances = this.insuranceService.findInsurances();
       List<InsuranceBase> insurancesBase= this.insuranceBaseService.findInsurancesBases();
       Vaccine vaccine = this.vaccineService.findById(vaccineId);
       for(Insurance i: insurances) {
    	   if(i.getVaccines().contains(vaccine)) {
    		   i.getVaccines().remove(vaccine);    		 
    	   }
       }
       for(InsuranceBase i: insurancesBase) {
    	   if(i.getVaccines().contains(vaccine)) {
    		   i.getVaccines().remove(vaccine);   		      		   
    	   }
       }
       this.vaccineService.deleteVaccine(vaccine);
      
       return "redirect:/vaccine";
    }

}
