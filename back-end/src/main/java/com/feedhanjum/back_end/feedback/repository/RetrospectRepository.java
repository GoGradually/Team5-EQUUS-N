package com.feedhanjum.back_end.feedback.repository;


import com.feedhanjum.back_end.feedback.domain.Retrospect;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.team.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {

    Page<Retrospect> findByWriterAndTeam(Member writer, Team team, Pageable pageable);

    Page<Retrospect> findByWriter(Member writer, Pageable pageable);
}
