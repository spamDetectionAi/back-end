// PeoplePreferenceRepository.java
package com.tsix_hack.spam_ai_detection.repositories;

import com.tsix_hack.spam_ai_detection.entities.preference.PeoplePreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeoplePreferenceRepository extends JpaRepository<PeoplePreference, Long> {
}
