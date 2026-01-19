package com.cursx.controller;

import com.cursx.dto.GenerateRequest;
import com.cursx.dto.GenerateResponse;
import com.cursx.service.generation.GenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Content Generation", description = "AI-powered content generation APIs")
public class GenerateController {

    private final GenerationService generationService;

    @PostMapping("/generate")
    @Operation(summary = "Generate creative social media content")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<GenerateResponse> generateContent(@Valid @RequestBody GenerateRequest request) {
        GenerateResponse response = generationService.generateContent(request);
        return ResponseEntity.ok(response);
    }
}
