package com.feedhanjum.back_end.team.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(1)
@RestControllerAdvice
public class TeamMemberControllerAdvice {
    @ExceptionHandler(TeamMembershipNotFoundException.class)
    public ResponseEntity<String> teamMembershipNotFoundException(TeamMembershipNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamLeaderMustExistException.class)
    public ResponseEntity<String> teamLeaderMustExistException(TeamLeaderMustExistException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TeamJoinTokenNotValidException.class)
    public ResponseEntity<String> teamJoinTokenNotValidException(TeamJoinTokenNotValidException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamEndedException.class)
    public ResponseEntity<String> teamEndedException(TeamEndedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.GONE);
    }
}
