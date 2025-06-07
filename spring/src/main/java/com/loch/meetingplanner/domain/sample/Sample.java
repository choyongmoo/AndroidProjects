package com.loch.meetingplanner.domain.sample;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Sample {

  @Id
  private Long id;

  private String message;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
