package com.loch.meetingplanner.domain.sample;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Sample {

  @Id
  private Long id;

  private String hello;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getHello() {
    return hello;
  }

  public void setHello(String hello) {
    this.hello = hello;
  }
}
