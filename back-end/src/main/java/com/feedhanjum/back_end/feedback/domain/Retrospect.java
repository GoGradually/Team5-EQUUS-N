package com.feedhanjum.back_end.feedback.domain;

import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Retrospect {
    public static final int MAX_TITLE_LENGTH = 50;
    public static final int MAX_CONTENT_BYTE = 400;
    @Id
    @Column(name = "retrospect_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "team_id")),
            @AttributeOverride(name = "name", column = @Column(name = "team_name")),
    })
    private AssociatedTeam team;

    public Retrospect(String title, String content, Member writer, Team team) {
        this.title = title;
        this.content = content;
        this.team = AssociatedTeam.of(team);
        this.writer = writer;
        this.createdAt = LocalDateTime.now();
    }

}
