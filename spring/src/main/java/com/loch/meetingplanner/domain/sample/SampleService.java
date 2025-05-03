package com.loch.meetingplanner.domain.sample;

import org.springframework.stereotype.Service;

import com.loch.meetingplanner.domain.sample.dto.SampleDto;

@Service
public class SampleService {

  private final SampleRepository sampleRepository;

  public SampleService(SampleRepository sampleRepository) {
    this.sampleRepository = sampleRepository;
  }

  public SampleDto getMessage() {
    Sample sample = sampleRepository.findById(1L)
        .orElseThrow(() -> new IllegalArgumentException("데이터가 없습니다"));
    return new SampleDto(sample.getMessage());
  }
}
