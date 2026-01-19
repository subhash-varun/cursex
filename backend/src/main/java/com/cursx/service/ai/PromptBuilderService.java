package com.cursx.service.ai;

import com.cursx.dto.GenerateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PromptBuilderService {

    public String buildPrompt(GenerateRequest request) {
        StringBuilder prompt = new StringBuilder();

        // Base instruction
        prompt.append("You are CurseX, an AI-powered creative writing assistant that generates viral, witty, and engaging short-form content for social media.\n\n");

        // Mode-specific instruction
        if (request.getMode() == GenerateRequest.Mode.TOPIC) {
            prompt.append("Generate creative social media content based on the following topic: \"")
                 .append(request.getInput())
                 .append("\"\n\n");
        } else {
            prompt.append("Rewrite and enhance the following draft into creative social media content: \"")
                 .append(request.getInput())
                 .append("\"\n\n");
        }

        // Format instruction
        prompt.append("Format: ").append(getFormatDescription(request.getFormat())).append("\n");

        // Tone instruction
        prompt.append("Tone: ").append(getToneDescription(request.getTone())).append("\n");

        // Chaos level instruction
        prompt.append("Chaos Level: ").append(request.getChaos())
              .append("/100 (0 = safe and normal, 100 = unhinged and chaotic)\n");

        // Platform instruction
        prompt.append("Platform: ").append(getPlatformDescription(request.getPlatform())).append("\n\n");

        // Final instruction
        prompt.append("Generate 3 different variations of this content. Make it engaging, viral-worthy, and true to the specified parameters.");

        log.debug("Generated prompt: {}", prompt.toString());
        return prompt.toString();
    }

    private String getFormatDescription(GenerateRequest.Format format) {
        return switch (format) {
            case ONE_LINER -> "A single punchy sentence or short phrase";
            case THREAD -> "A Twitter thread (3-5 connected tweets)";
            case HOT_TAKE -> "A controversial, opinionated statement";
            case LIST -> "A numbered or bulleted list format";
            case SATIRICAL_POST -> "Satirical or humorous take on current events";
            case BRAND_ANNOUNCEMENT -> "Professional brand/product announcement";
            case META_POST -> "Self-referential content about social media or content creation";
            case MEME_CAPTION -> "Caption for a meme or image";
        };
    }

    private String getToneDescription(GenerateRequest.Tone tone) {
        return switch (tone) {
            case SARCASTIC -> "Witty sarcasm and irony";
            case PROFESSIONAL -> "Polished and business-appropriate";
            case CHAOTIC -> "Unpredictable and wild";
            case INSPIRATIONAL -> "Motivational and uplifting";
            case AGGRESSIVE -> "Bold and confrontational";
            case FUNNY -> "Light-hearted humor";
            case DARK_HUMOR -> "Edgy, dark comedy";
            case NEUTRAL -> "Balanced and straightforward";
        };
    }

    private String getPlatformDescription(GenerateRequest.Platform platform) {
        return switch (platform) {
            case TWITTER -> "Twitter/X (concise, hashtags, trending topics)";
            case LINKEDIN -> "LinkedIn (professional networking, B2B focus)";
            case INSTAGRAM -> "Instagram (visual, casual, emoji-friendly)";
            case THREADS -> "Threads (conversational, community-focused)";
            case REDDIT -> "Reddit (discussion-oriented, subreddit-specific)";
        };
    }
}
