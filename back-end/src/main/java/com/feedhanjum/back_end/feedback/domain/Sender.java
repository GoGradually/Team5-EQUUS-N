package com.feedhanjum.back_end.feedback.domain;


import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Sender {
    private Long id;

    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "backgroundColor", column = @Column(name = "sender_background_color")),
            @AttributeOverride(name = "image", column = @Column(name = "sender_image"))
    })
    private ProfileImage profileImage;

    public static Sender of(Member sender) {
        return new Sender(sender.getId(), sender.getName(), sender.getProfileImage());
    }
}
