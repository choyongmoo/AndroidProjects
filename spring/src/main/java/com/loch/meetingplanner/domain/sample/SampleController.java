package com.loch.meetingplanner.domain.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.domain.sample.dto.SampleDto;

@RestController
public class SampleController {

  private final SampleService sampleService;

  public SampleController(SampleService sampleService) {
    this.sampleService = sampleService;
  }

  @GetMapping("/api/sample")
  public SampleDto getHello() {
    return sampleService.getMessage();
  }
}
