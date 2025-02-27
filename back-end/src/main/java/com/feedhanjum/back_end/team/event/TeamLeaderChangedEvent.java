package com.feedhanjum.back_end.team.event;

public record TeamLeaderChangedEvent(Long teamId, Long newLeaderId) {
}
