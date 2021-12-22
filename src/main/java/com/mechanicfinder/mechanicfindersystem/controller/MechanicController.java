package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.exception.DeleteMechanicException;
import com.mechanicfinder.mechanicfindersystem.exception.MechanicWithThatEmailExists;
import com.mechanicfinder.mechanicfindersystem.model.*;
import com.mechanicfinder.mechanicfindersystem.repository.MechanicRepository;
import com.mechanicfinder.mechanicfindersystem.repository.RoleRepository;
import com.mechanicfinder.mechanicfindersystem.service.*;
import lombok.AllArgsConstructor;
import org.bouncycastle.math.raw.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/mechanic")
@Controller
@AllArgsConstructor
public class MechanicController {
    private final Logger logger = LoggerFactory.getLogger(MechanicController.class);
    private final MechanicService mechanicService;
    private final AppointmentService appointmentService;
    private final RoleRepository roleRepository;
    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;
    private final MechanicRepository mechanicRepository;
    private final CustomerService customerService;
    private final TaskService taskService;

    @GetMapping("/all")
    public List<Mechanic> findAllMechanic(){
        return mechanicService.findAllMechanics();
    }

    @GetMapping("/register")
    public String mechanicRegForm(Model model){
        model.addAttribute("mechanic",new Mechanic());
        return "mechanic-views/mechanic-reg-form";
    }

    @PostMapping("/process-mechanic-reg-form")
    public String processMechanicRegForm(@Valid @ModelAttribute("mechanic") Mechanic mechanic,
                                         BindingResult bindingResult,
                                         @RequestParam("image") MultipartFile profileImage,
                                         @RequestParam("pdf") MultipartFile qualification,
                                         Model model,
                                         RedirectAttributes redirectAttributes) throws IOException, MechanicWithThatEmailExists {

        if (bindingResult.hasErrors() || profileImage.isEmpty() || qualification.isEmpty()){
            model.addAttribute("file","a file is missing");
            return "mechanic-views/mechanic-reg-form";
        }else {
            Mechanic byEmail = mechanicService.findMechanicByEmail(mechanic.getEmail());
            Mechanic byPhoneNumber = mechanicService.findMechanicByPhoneNumber(mechanic.getPhoneNumber());
            Customer byEmail1 = customerService.findCustomerByEmail(mechanic.getEmail());
            Customer phoneNumber = customerService.findCustomerByPhoneNumber(mechanic.getPhoneNumber());
            if ((byEmail != null) || (byPhoneNumber != null) || (byEmail1 != null) || (phoneNumber != null)){
                throw new MechanicWithThatEmailExists("Check your email and phone number");
            }else {
                Mechanic registeredMechanic =
                        mechanicService.registerMechanic(mechanic, profileImage, qualification);

                redirectAttributes.addFlashAttribute("message",
                        "Your Application has been received! \n You will receive an email if approved.");
            }

            return "redirect:/api/mechanic/register";
        }

    }

    @GetMapping("/add-mechanic-info/{id}")
    public String AddMechanicDetails(@PathVariable("id") Long id, Model model){
        logger.info("The retrieved id ========================>"+id);
       model.addAttribute("mechanic", mechanicService.findMechanicById(id));
       return "mechanic-views/add-mechanic-info";
    }

    @PostMapping("/process-add-mechanic-info")
    @Transactional
    public String processAddMechanicInfo(@Valid @ModelAttribute("mechanic") Mechanic mechanic,
                                         BindingResult bindingResult){
        if (bindingResult.hasErrors() || !mechanic.getPhoneNumber().startsWith("07")){
            return "mechanic-views/add-mechanic-info";
        }else {

            AppUser appUser = new AppUser(mechanic.getUsername(),
                    passwordEncoder.encode(mechanic.getPassword()));

            Role role = roleRepository.findRoleByRoleName("ROLE_MECHANIC");
            appUser.getRoles().add(role);

            AppUser user = appUserService.createCredentials(appUser);

            mechanic.setAppUser(user);

            Mechanic updateMechanic = mechanicRepository.save(mechanic);

            return "redirect:/api/mechanic/"+updateMechanic.getId();

        }
    }

    @GetMapping("/{id}")
    public String mechanicProfile(@PathVariable("id") Long id, Model model){
        model.addAttribute("mechanic",
                mechanicService.findMechanicById(id));
        return "mechanic-views/mechanic-viewport";
    }

    @GetMapping("/approve/{id}")
    public String approveAppointment(@PathVariable("id") Long id){
        Appointment appointmentById = appointmentService.findAppointmentById(id);
        appointmentById.setAppointmentStatus(AppointmentStatus.APPROVED);
        appointmentService.updateAppointmentStatus(appointmentById);
        return "redirect:/api/mechanic/"+appointmentById
                .getMechanic()
                .getId();
    }

    @GetMapping("/decline/{id}")
    public String declineAppointment(@PathVariable("id")Long id){
        Appointment appointmentById = appointmentService.findAppointmentById(id);
        appointmentById.setAppointmentStatus(AppointmentStatus.DECLINED);
        appointmentService.updateAppointmentStatus(appointmentById);
        return "mechanic-views/mechanic-viewport";
    }

    @GetMapping("/edit-mechanic/{id}")
    public String editMechanic(@PathVariable("id") Long id, Model model){
        model.addAttribute("mechanic",mechanicService.findMechanicById(id));
        model.addAttribute("appUser",mechanicService.findMechanicById(id).getAppUser());
        return "mechanic-views/edit-mechanic";
    }

    @PostMapping("/process-update-mechanic/{id}")
    public String processUpdateMechanic(@Valid @ModelAttribute("mechanic") Mechanic mechanic,
                                        BindingResult bindingResult,
                                        @PathVariable("id") Long id,
                                        Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("mechanic",mechanicService.findMechanicById(mechanic.getId()));
            model.addAttribute("appUser",mechanicService.findMechanicById(mechanic.getId()).getAppUser());
            return "mechanic-views/edit-mechanic";
        }else {

            Mechanic updateMechanic = mechanicService.updateMechanic(mechanic);
            AppUser user = appUserService.findAppUserById(id);
            updateMechanic.setAppUser(user);
            Mechanic saveMechanic = mechanicService.saveMechanic(updateMechanic);

            return "redirect:/api/mechanic/"+saveMechanic.getId();
        }

    }

    @GetMapping("/delete-mechanic/{id}")
    public String deleteMechanic(@PathVariable("id") Long id) throws DeleteMechanicException {
        Mechanic mechanic = mechanicService.findMechanicById(id);
        List<Appointment> pendingAndApprovedAppointments = mechanic.getAppointments()
                .stream()
                .filter(appointment -> appointment
                        .getAppointmentStatus().equals("APPROVED"))
                .collect(Collectors.toList());
        if (! pendingAndApprovedAppointments.isEmpty()){
            throw new DeleteMechanicException("Check pending and approved appointments before deleting account");
        }else {
            mechanicService.deleteMechanic(mechanic);
            return "redirect:/logout";
        }
    }
}
