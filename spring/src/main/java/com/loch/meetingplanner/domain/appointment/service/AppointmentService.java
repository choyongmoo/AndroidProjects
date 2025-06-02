package com.loch.meetingplanner.domain.appointment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.loch.meetingplanner.domain.appointment.dto.AppointmentRequest;
import com.loch.meetingplanner.domain.appointment.dto.AppointmentResponse;
import com.loch.meetingplanner.domain.appointment.model.Appointment;
import com.loch.meetingplanner.domain.appointment.model.ArrivalLog;
import com.loch.meetingplanner.domain.appointment.repository.AppointmentRepository;
import com.loch.meetingplanner.domain.appointment.repository.ArrivalLogRepository;
import com.loch.meetingplanner.domain.group.model.Group;
import com.loch.meetingplanner.domain.group.model.Place;
import com.loch.meetingplanner.domain.group.repository.GroupMemberRepository;
import com.loch.meetingplanner.domain.group.repository.GroupPlaceRepository;
import com.loch.meetingplanner.domain.group.repository.GroupRepository;
import com.loch.meetingplanner.domain.group.repository.PlaceRepository;
import com.loch.meetingplanner.domain.user.model.User;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PlaceRepository placeRepository;
    private final GroupPlaceRepository groupPlaceRepository;
    private final ArrivalLogRepository arrivalLogRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
            GroupRepository groupRepository,
            GroupMemberRepository groupMemberRepository,
            PlaceRepository placeRepository,
            GroupPlaceRepository groupPlaceRepository,
            ArrivalLogRepository arrivalLogRepository) {
        this.appointmentRepository = appointmentRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.placeRepository = placeRepository;
        this.groupPlaceRepository = groupPlaceRepository;
        this.arrivalLogRepository = arrivalLogRepository;
    }

    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AppointmentResponse getAppointment(Long id, User user) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (!isGroupMember(user, appointment.getGroup())) {
            throw new AccessDeniedException("User is not a member of the group");
        }

        return toResponse(appointment);
    }

    public AppointmentResponse createAppointment(AppointmentRequest request, User user) {
        Group group = groupRepository.findById(Long.parseLong(request.groupId()))
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        if (!isGroupMember(user, group)) {
            throw new AccessDeniedException("User is not a member of the group");
        }

        Place place = placeRepository.findById(Long.parseLong(request.placeId()))
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));

        if (!groupPlaceRepository.existsByGroupAndPlace(group, place)) {
            throw new IllegalArgumentException("Place is not registered in the group");
        }

        Appointment appointment = new Appointment();
        appointment.setTitle(request.title());
        appointment.setPenalty(request.penalty());
        appointment.setGroup(group);
        appointment.setPlace(place);
        appointment.setTime(request.time());
        appointment.setCreatedBy(user);
        appointment.setCreatedAt(LocalDateTime.now());

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    public void updateAppointment(Long id, AppointmentRequest request, User user) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (!isGroupMember(user, appointment.getGroup())) {
            throw new AccessDeniedException("User is not a member of the group");
        }

        Place place = placeRepository.findById(Long.parseLong(request.placeId()))
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));

        if (!groupPlaceRepository.existsByGroupAndPlace(appointment.getGroup(), place)) {
            throw new IllegalArgumentException("Place is not registered in the group");
        }

        appointment.setTitle(request.title());
        appointment.setPenalty(request.penalty());
        appointment.setPlace(place);
        appointment.setTime(request.time());

        appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id, User user) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (!isGroupMember(user, appointment.getGroup())) {
            throw new AccessDeniedException("User is not a member of the group");
        }

        appointmentRepository.delete(appointment);
    }

    public List<ArrivalLog> getArrivalLogs(Long appointmentId, User user) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        if (!isGroupMember(user, appointment.getGroup())) {
            throw new AccessDeniedException("User is not a member of the group");
        }
        return arrivalLogRepository.findByAppointment(appointment);
    }

    public void recordArrival(Long appointmentId, User user) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (!isGroupMember(user, appointment.getGroup())) {
            throw new AccessDeniedException("User is not a member of the group");
        }

        if (arrivalLogRepository.existsByAppointmentAndUser(appointment, user)) {
            throw new IllegalStateException("User already recorded arrival for this appointment");
        }

        LocalDateTime now = LocalDateTime.now();
        boolean isLate = now.isAfter(appointment.getTime());

        ArrivalLog log = new ArrivalLog();
        log.setAppointment(appointment);
        log.setUser(user);
        log.setArrivalTime(now);
        log.setIsLate(isLate);

        arrivalLogRepository.save(log);
    }

    private boolean isGroupMember(User user, Group group) {
        return groupMemberRepository.existsByGroupAndUser(group, user);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getGroup().getId().toString(),
                appointment.getPlace().getId().toString(),
                appointment.getTime(),
                appointment.getCreatedBy().getId().toString(),
                appointment.getCreatedAt());
    }

    //그룹 약속 목록 조회
    public List<AppointmentResponse> getAppointmentsByGroupId(Long groupId) {
        return appointmentRepository.findByGroupId(groupId).stream()
            .map(appointment -> new AppointmentResponse(
                appointment.getId(),
                String.valueOf(appointment.getGroup().getId()),
                String.valueOf(appointment.getPlace().getId()),
                appointment.getTime(),
                String.valueOf(appointment.getCreatedBy().getId()),
                appointment.getCreatedAt()
            ))
            .toList();
    }

}
