package com.feedhanjum.back_end.auth.repository;

import com.feedhanjum.back_end.auth.domain.MemberDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberDetailsRepository extends JpaRepository<MemberDetails, Long> {
    Optional<MemberDetails> findByEmail(String email);
}
