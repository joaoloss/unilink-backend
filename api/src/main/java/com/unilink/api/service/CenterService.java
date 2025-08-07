package com.unilink.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.unilink.api.DTO.CenterRequestDTO;
import com.unilink.api.exceptions.NotFoundException;
import com.unilink.api.model.Center;
import com.unilink.api.repository.CenterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CenterService {
    private final CenterRepository centerRepository;

    public Center getCenterById(UUID id) {
        return this.centerRepository.findById(id).orElseThrow(() -> new NotFoundException("Center not found with id: " + id));
    }

    public Center createCenter(CenterRequestDTO centerRequest) {
        Center newCenter = new Center();
        System.out.println("Creating center with request: " + centerRequest);

        if(centerRequest.name() != null) newCenter.setName(centerRequest.name());
        if(centerRequest.centerUrl() != null) newCenter.setCenterUrl(centerRequest.centerUrl());

        return this.centerRepository.save(newCenter);
    }

    public Center updateCenter(UUID id, CenterRequestDTO updatedCenter) {
        Center existingCenter = this.getCenterById(id);

        if(updatedCenter.name() != null) existingCenter.setName(updatedCenter.name());
        if(updatedCenter.centerUrl() != null) existingCenter.setCenterUrl(updatedCenter.centerUrl());

        return this.centerRepository.save(existingCenter);
    }

    public void deleteCenter(UUID id) {
        Center center = this.getCenterById(id);
        this.centerRepository.delete(center);
    }

    public List<Center> getAllCenters() {
        return this.centerRepository.findAll();
    }
}
