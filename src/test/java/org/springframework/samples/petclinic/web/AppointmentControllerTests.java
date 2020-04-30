package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Insurance;
import org.springframework.samples.petclinic.model.InsuranceBase;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Shift;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.model.VaccinationSchedule;
import org.springframework.samples.petclinic.model.Vaccine;
import org.springframework.samples.petclinic.model.VetSchedule;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.InsuranceBaseService;
import org.springframework.samples.petclinic.service.InsuranceService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ShiftService;
import org.springframework.samples.petclinic.service.TreatmentService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VaccineService;
import org.springframework.samples.petclinic.service.VetScheduleService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=AppointmentController.class,includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class AppointmentControllerTests {
	
	private static final int TEST_APPOINTMENT_ID = 3;
	private static final int TEST_VET_SCHEDULE_ID = 1;
	private static final int TEST_VACCINE_ID = 10;
	private static final int TEST_TREATMENT_ID = 13;
	private static final int TEST_VACCINE_BASE_ID = 21;
	private static final int TEST_TREATMENT_BASE_ID = 32;
	private static final int TEST_SHIFT_ID = 25;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_INSURANCE_ID = 4;
	private static final int TEST_INSURANCE_BASE_ID = 5;
	
	@Autowired
	private AppointmentController appointmentController;

	@MockBean
	private AppointmentService appointmentService;
	
	@MockBean
	private VetScheduleService vetScheduleService;
	
	@MockBean
	private ShiftService shiftService;
	
	@MockBean
    private PetService petService;
	
	@MockBean
    private VaccineService vaccineService;
	
	@MockBean
    private TreatmentService treatmentService;
	
	@MockBean
    private InsuranceBaseService insuranceBaseService;
	
	@MockBean
    private InsuranceService insuranceService;
	
	@MockBean
	private VetService vetService;
	
    @MockBean
	private UserService userService;
        
    @MockBean
    private AuthoritiesService authoritiesService; 
	
	@Autowired
	private MockMvc mockMvc;
	
	private VetSchedule horario;
	private Appointment appointment;
	private Shift shift;
	private Vaccine vaccine;
	private Treatment treatment;
	private Vaccine vaccine2;
	private Treatment treatment2;
	private Pet pet;
	private VaccinationSchedule vaSchedule;
	private InsuranceBase insuranceBase;
	private Insurance insurance;
	
	
	@BeforeEach
	void setup() {
		
		vaccine = new Vaccine();
	    PetType human = new PetType();
	    human.setId(5);
	    human.setName("human");
	    vaccine.setId(TEST_VACCINE_ID);
	    vaccine.setInformation("Vacuna del coronavirus en pruebas, testeado en monos");
		vaccine.setExpiration(LocalDate.of(2021, Month.APRIL, 3));
	    vaccine.setName("Vacuna contra el coronavirus");
	    vaccine.setPetType(human);
	    vaccine.setPrice(75.0);
	    vaccine.setProvider("China");
	    vaccine.setSideEffects("Puede provocar crisis nerviosas");
	    vaccine.setStock(235);

	    treatment = new Treatment();
	    PetType raton = new PetType();
	    raton.setId(6);
	    raton.setName("raton");
	    treatment.setId(TEST_TREATMENT_ID);
	    treatment.setPetType(raton);
	    treatment.setPrice(25.0);
	    treatment.setType("Tratamiento contra el aburrimiento");
	    treatment.setDescription("Para que no te arranques los pelos este mes");
	    
	    vaccine2 = new Vaccine();
        PetType koala = new PetType();
        koala.setId(12);
        koala.setName("koala");
        vaccine2.setId(TEST_VACCINE_BASE_ID);
        vaccine2.setInformation("Vacuna contra la explotacion, testeado en Chao");
        vaccine2.setExpiration(LocalDate.of(2021, 5, 9));
        vaccine2.setName("Vacuna contra la explotacion");
        vaccine2.setPetType(koala);
        vaccine2.setPrice(32.3);
        vaccine2.setProvider("Madagascar");
        vaccine2.setSideEffects("Puede volverte mas tonto");
        vaccine2.setStock(2);
        
        
        treatment2 = new Treatment();
        PetType agaporni = new PetType();
        agaporni.setId(11);
        agaporni.setName("agaporni");
        treatment2.setId(TEST_TREATMENT_BASE_ID);
        treatment2.setPetType(agaporni);
        treatment2.setPrice(43.4);
        treatment2.setType("Tratamiento inutil");
        treatment2.setDescription("No sabia que poner");

	    Set<Vaccine> vacunas = new HashSet<Vaccine>();
		vacunas.add(vaccine);
		Set<Treatment> tratamientos = new HashSet<Treatment>();
		tratamientos.add(treatment);
		Set<Vaccine> vacunas2 = new HashSet<Vaccine>();
		vacunas.add(vaccine2);
		Set<Treatment> tratamientos2 = new HashSet<Treatment>();
		tratamientos.add(treatment2);
		LocalDate date1 = LocalDate.of(2020, Month.JULY, 10);
		List<LocalDate> dates = new ArrayList<>();
		dates.add(date1);
		
		vaSchedule = new VaccinationSchedule();
		vaSchedule.setDates(dates);
		
		insuranceBase = new InsuranceBase();
		PetType mascota = new PetType();
		mascota.setId(8);
		mascota.setName("mascota");
		insuranceBase.setId(TEST_INSURANCE_BASE_ID);
		insuranceBase.setName("Seguro para ganar dinero");
		insuranceBase.setPetType(mascota);
		insuranceBase.setVaccines(vacunas2);
		insuranceBase.setTreatments(tratamientos2);
		insuranceBase.setConditions("Ser rico");	
		
		insurance = new Insurance();
		insurance.setId(TEST_INSURANCE_ID);
		insurance.setInsuranceDate(LocalDate.of(2020, 4 ,3));
		insurance.setInsuranceBase(insuranceBase);
		insurance.setTreatments(tratamientos);
		insurance.setVaccines(vacunas);
		
		pet = new Pet();
		PetType rata = new PetType();
		rata.setId(7);
		rata.setName("rata");
		pet.setBirthDate(LocalDate.of(2015, Month.APRIL, 14));
		pet.setId(TEST_PET_ID);
		pet.setName("Anton");
		pet.setType(rata);
		pet.setTreatment(treatment);
		pet.setSchedule(vaSchedule);
		pet.setInsurance(insurance);
		
		appointment = new Appointment();
		appointment.setId(TEST_APPOINTMENT_ID);
		appointment.setAppointmentDate(LocalDate.of(2020, Month.AUGUST, 3));
		appointment.setAppointmentTime(LocalTime.of(9, 30, 00));
		appointment.setAttended(false);
		appointment.setTreatment(treatment);
		appointment.setVaccine(vaccine);
		appointment.setBilling(100.0);
		appointment.setObservations("Mascota herida");
		appointment.setPet(pet);
		appointment.setReason("Curar la pata");
		
		shift = new Shift();
		shift.setId(TEST_SHIFT_ID);
		shift.setShiftDate(LocalTime.of(9, 30, 00));
	
		Set<Appointment> appointments = new HashSet<Appointment>();
		appointments.add(appointment);
		Set<Shift> shifts = new HashSet<Shift>();
		shifts.add(shift);						
			
		horario = new VetSchedule();
		horario.setId(TEST_VET_SCHEDULE_ID);
		horario.setAppointments(appointments);
		horario.setShifts(shifts);
		

    	given(vetScheduleService.findById(TEST_VET_SCHEDULE_ID)).willReturn(horario);
    	given(insuranceBaseService.findInsuranceBaseById(TEST_INSURANCE_BASE_ID)).willReturn(insuranceBase);
    	given(appointmentService.findAppointmentById(TEST_APPOINTMENT_ID)).willReturn(appointment);
    	given(shiftService.findById(TEST_SHIFT_ID)).willReturn(shift);
    	given(vaccineService.findById(TEST_VACCINE_ID)).willReturn(vaccine);
    	given(treatmentService.findById(TEST_TREATMENT_ID)).willReturn(treatment);
    	given(vaccineService.findById(TEST_VACCINE_BASE_ID)).willReturn(vaccine2);
    	given(treatmentService.findById(TEST_TREATMENT_BASE_ID)).willReturn(treatment2);
    	given(petService.findPetById(TEST_PET_ID)).willReturn(pet);
    	given(insuranceService.findInsuranceById(TEST_INSURANCE_ID)).willReturn(insurance);
    	
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
    @Test
    void testInitAppointmentCreationForm() throws Exception {
	mockMvc.perform(get("appointment/new/{petId}", TEST_PET_ID)).andExpect(status().isOk())
	.andExpect(model().attributeExists("vaccines"))
			.andExpect(view().name("appointments/createAppointment"));
}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowAppintmentsDetails() throws Exception {	    	
	 	mockMvc.perform(get("/appointment/{appointementId}", TEST_APPOINTMENT_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("appointment"))
				.andExpect(view().name("appointments/appointmentDetails"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormSuccess() throws Exception {
		Set<Vaccine> vaccines = new HashSet<Vaccine>();
		vaccines.add(vaccine);
		Set<Treatment> treatments = new HashSet<Treatment>();
		treatments.add(treatment);
		List<LocalTime> times = new ArrayList<LocalTime>();
		times.add(LocalTime.of(9, 30, 00));
		mockMvc.perform(post("appointment/new/{petId}", TEST_PET_ID).with(csrf())
				
						.param("pet", pet.toString())
						.param("appointment",appointment.toString())
						.param("times", times.toString())
						.param("vaccines", vaccines.toString())
						.param("treatments", treatments.toString()))
	
						
			.andExpect(status().isOk());
}


}