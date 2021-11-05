package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.repository.MechanicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MechanicServiceImpl implements MechanicService{

    private final MechanicRepository mechanicRepository;

    @Override
    @Transactional
    public List<Mechanic> findAllMechanics() {
        return mechanicRepository.findAll();
    }
}
