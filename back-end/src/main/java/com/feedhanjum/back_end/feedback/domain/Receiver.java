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
public class Receiver {
    private Long id;

    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "backgroundColor", column = @Column(name = "receiver_background_color")),
            @AttributeOverride(name = "image", column = @Column(name = "receiver_image"))
    })
    private ProfileImage profileImage;

    public static Receiver of(Member receiver) {
        return new Receiver(receiver.getId(), receiver.getName(), receiver.getProfileImage());
    }


}
