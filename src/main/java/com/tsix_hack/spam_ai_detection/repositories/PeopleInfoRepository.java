package com.tsix_hack.spam_ai_detection.repositories;

import com.tsix_hack.spam_ai_detection.entities.PeopleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PeopleInfoRepository extends JpaRepository<PeopleInfo, UUID> {

}
