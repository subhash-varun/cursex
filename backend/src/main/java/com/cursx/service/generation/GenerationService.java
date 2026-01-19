package com.cursx.service.generation;

import com.cursx.dto.GenerateRequest;
import com.cursx.dto.GenerateResponse;
import com.cursx.service.ai.AIService;
import com.cursx.service.ai.PromptBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerationService {

    private final AIService aiService;
    private final PromptBuilderService promptBuilder;

    public GenerateResponse generateContent(GenerateRequest request) {
        log.info("Generating content for request: mode={}, format={}, tone={}, chaos={}, platform={}",
                request.getMode(), request.getFormat(), request.getTone(), request.getChaos(), request.getPlatform());

        String prompt = promptBuilder.buildPrompt(request);
        String aiResponse = aiService.generateContent(prompt);

        // Parse the AI response into individual outputs
        List<String> outputs = parseAIResponse(aiResponse);

        log.info("Generated {} content variations", outputs.size());
        return new GenerateResponse(outputs);
    }

    private List<String> parseAIResponse(String aiResponse) {
        // Split by numbered lines (1., 2., 3., etc.)
        String[] lines = aiResponse.split("\\n");
        List<String> outputs = Arrays.stream(lines)
                .filter(line -> line.matches("^\\d+\\..*")) // Lines starting with numbers
                .map(line -> line.replaceFirst("^\\d+\\.", "").trim()) // Remove numbering
                .filter(line -> !line.isEmpty())
                .toList();

        // If no numbered responses found, split by double newlines or return as single response
        if (outputs.isEmpty()) {
            outputs = Arrays.stream(aiResponse.split("\\n\\n"))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .toList();
        }

        // Ensure we have at least one output
        if (outputs.isEmpty()) {
            outputs = List.of(aiResponse.trim());
        }

        return outputs;
    }
}
