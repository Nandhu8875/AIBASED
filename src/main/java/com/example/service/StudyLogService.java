package com.example.service;

import com.example.dto.StudyLogRequest;
import com.example.dto.StudyLogResponse;
import com.example.entity.StudyLog;
import com.example.entity.User;
import com.example.exception.UserNotFoundException;
import com.example.repository.StudyLogRepository;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;
    private final UserRepository userRepository;

    // Constructor injection
    public StudyLogService(StudyLogRepository studyLogRepository,
            UserRepository userRepository) {
        this.studyLogRepository = studyLogRepository;
        this.userRepository = userRepository;
    }

    /**
     * Adds a new study log entry for the user identified by telegramId.
     */
    @Transactional
    public StudyLogResponse addStudyLog(Long telegramId, StudyLogRequest request) {
        String telegramIdStr = String.valueOf(telegramId);

        User user = userRepository.findByTelegramId(telegramIdStr)
                .orElseThrow(() -> new UserNotFoundException(telegramId));

        StudyLog studyLog = new StudyLog();
        studyLog.setUser(user);
        studyLog.setSubject(request.getSubject());
        studyLog.setHours(request.getHours());
        studyLog.setDate(request.getDate());

        StudyLog saved = studyLogRepository.save(studyLog);
        return mapToResponse(saved);
    }

    /**
     * Returns all study logs for the user identified by telegramId.
     */
    @Transactional(readOnly = true)
    public List<StudyLogResponse> getStudyLogsByUser(Long telegramId) {
        String telegramIdStr = String.valueOf(telegramId);

        User user = userRepository.findByTelegramId(telegramIdStr)
                .orElseThrow(() -> new UserNotFoundException(telegramId));

        return studyLogRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ─── Mapper ────────────────────────────────────────────────────────────
    private StudyLogResponse mapToResponse(StudyLog log) {
        return new StudyLogResponse(
                log.getId(),
                log.getSubject(),
                log.getHours(),
                log.getDate(),
                log.getUser().getId(),
                log.getUser().getName());
    }
}
