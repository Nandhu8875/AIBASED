package com.example.controller;

import com.example.dto.StudyLogRequest;
import com.example.dto.StudyLogResponse;
import com.example.service.StudyLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study")
@Tag(name = "Study Logs", description = "Add and retrieve study log entries. Requires JWT authentication.")
@SecurityRequirement(name = "BearerAuth")
public class StudyLogController {

    private final StudyLogService studyLogService;

    public StudyLogController(StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    @Operation(summary = "Add a study log", description = "Saves a new study log entry for the user identified by their Telegram ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Study log created successfully", content = @Content(schema = @Schema(implementation = StudyLogResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found with given Telegram ID", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token missing or invalid", content = @Content)
    })
    @PostMapping("/add/{telegramId}")
    public ResponseEntity<StudyLogResponse> addStudyLog(
            @Parameter(description = "Telegram ID of the user", example = "123456789") @PathVariable Long telegramId,
            @Valid @RequestBody StudyLogRequest request) {
        StudyLogResponse response = studyLogService.addStudyLog(telegramId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all study logs", description = "Returns all study log entries for the user identified by their Telegram ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of study logs returned", content = @Content(schema = @Schema(implementation = StudyLogResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token missing or invalid", content = @Content)
    })
    @GetMapping("/{telegramId}")
    public ResponseEntity<List<StudyLogResponse>> getStudyLogs(
            @Parameter(description = "Telegram ID of the user", example = "123456789") @PathVariable Long telegramId) {
        List<StudyLogResponse> logs = studyLogService.getStudyLogsByUser(telegramId);
        return ResponseEntity.ok(logs);
    }
}
