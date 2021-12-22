package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.exception.MechanicWithThatEmailExists;
import com.mechanicfinder.mechanicfindersystem.model.*;
import com.mechanicfinder.mechanicfindersystem.repository.MechanicRepository;
import com.mechanicfinder.mechanicfindersystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MechanicServiceImpl implements MechanicService{
    private final PasswordEncoder passwordEncoder;
    private final MechanicRepository mechanicRepository;
    private final AppUserService appUserService;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public List<Mechanic> findAllMechanics() {
        return mechanicRepository.findAll();
    }

    @Override
    @Transactional
    public Mechanic  registerMechanic(Mechanic mechanic,
                                 MultipartFile profileImage,
                                 MultipartFile qualificationsDocument) throws IOException, MechanicWithThatEmailExists {

        //get image and document names
        String profileImageName = StringUtils
                .cleanPath(profileImage.getOriginalFilename());
        String qualificationDocumentName = StringUtils
                .cleanPath(qualificationsDocument.getOriginalFilename());

        //set the name of image and document
        mechanic.setProfileImage(profileImageName);
        mechanic.setQualification(qualificationDocumentName);

        //register the mechanic
        Mechanic mechanicByEmail = mechanicRepository.findMechanicByEmail(mechanic.getEmail());
        if (mechanicByEmail != mechanic){
            mechanic.setCreatedAt(LocalDate.now());
            mechanic.setAvailability(Availability.AVAILABLE);
            mechanic.setApplicationStatus(ApplicationStatus.PENDING);
            Mechanic registeredMechanic = mechanicRepository.save(mechanic);

//            AppUser appUser = new AppUser(registeredMechanic.getUsername(),
//                    passwordEncoder.encode(registeredMechanic.getPassword()));
//
//            Role role_mechanic = roleRepository.findRoleByRoleName("ROLE_MECHANIC");
//            appUser.getRoles().add(role_mechanic);
//
//            appUserService.createCredentials(appUser);
//
//            registeredMechanic.setAppUser(appUser);
//
//            mechanicRepository.save(registeredMechanic);


            //upload image
            uploadImageOrDocument(registeredMechanic,
                    profileImage,
                    profileImageName,
                    "./mechanic-images/");

            //upload document
            uploadImageOrDocument(registeredMechanic,
                    qualificationsDocument,
                    qualificationDocumentName,
                    "./mechanic-qualifications/");

            return registeredMechanic;
        }else {
            throw new MechanicWithThatEmailExists("mechanic with that email id exists");
        }


    }

    @Override
    public Mechanic findMechanicByEmail(String email) {
        return mechanicRepository.findMechanicByEmail(email);
    }

    @Override
    public Mechanic findMechanicById(Long id) {
        return mechanicRepository.findMechanicById(id);
    }

    @Override
    public Mechanic findMechanicByFirstName(String firstName) {
        return mechanicRepository.findMechanicByFirstName(firstName);
    }

    @Override
    public Mechanic saveMechanic(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }

    @Override
    public Mechanic approveMechanic(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }

    @Override
    public Mechanic declineMechanic(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }

    @Override
    @Transactional
    public Mechanic updateMechanic(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }

    @Override
    public Mechanic findMechanicByPhoneNumber(String email) {
        return mechanicRepository.findMechanicByPhoneNumber(email);
    }

    @Override
    public void deleteMechanic(Mechanic mechanic) {
        mechanicRepository.delete(mechanic);
    }

    private void uploadImageOrDocument(Mechanic registeredMechanic,
                                       MultipartFile multipartFile,
                                       String fileName,
                                       String fileLocation) throws IOException {
        String uploadDir = StringUtils.cleanPath(fileLocation)+ registeredMechanic.getId();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        InputStream inputStream = multipartFile.getInputStream();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

}
