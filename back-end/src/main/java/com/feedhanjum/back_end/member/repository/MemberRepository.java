package com.feedhanjum.back_end.member.repository;

import com.feedhanjum.back_end.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
