package com.tsix_hack.spam_ai_detection.controller;

import com.tsix_hack.spam_ai_detection.entities.PeopleInfo;
import com.tsix_hack.spam_ai_detection.repositories.PeopleInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/people/info")
@AllArgsConstructor
public class PeopleInfoController {
    private PeopleInfoRepository peopleInfoRepository;

    @PostMapping("/save")
    public ResponseEntity<UUID> save(@RequestBody PeopleInfo peopleInfo) {
        UUID ID = peopleInfoRepository.save(peopleInfo).getId() ;
        return ResponseEntity.status(HttpStatus.CREATED).body(ID) ;
    }

    @GetMapping("/find/{id}")
    public Optional<PeopleInfo> findById(@PathVariable UUID id) {
        UUID me = new UUID(3 , 5) ;
        return peopleInfoRepository.findById(id);
    }
}
