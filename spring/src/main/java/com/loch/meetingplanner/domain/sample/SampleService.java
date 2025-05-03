package com.loch.meetingplanner.domain.sample;

import org.springframework.stereotype.Service;

import com.loch.meetingplanner.domain.sample.dto.SampleDto;
import com.loch.meetingplanner.exception.SampleException;

@Service
public class SampleService {

  private final SampleRepository sampleRepository;

  public SampleService(SampleRepository sampleRepository) {
    this.sampleRepository = sampleRepository;
  }

  public SampleDto getMessage() {
    Sample sample = sampleRepository.findById(1L)
        .orElseThrow(() -> new SampleException("Good Bye!"));
    return new SampleDto(sample.getMessage());
  }

}
