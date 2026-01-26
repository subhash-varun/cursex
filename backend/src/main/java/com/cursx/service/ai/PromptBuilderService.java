package com.cursx.service.ai;

import com.cursx.dto.GenerateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PromptBuilderService {

    public String buildPrompt(GenerateRequest request) {
        StringBuilder prompt = new StringBuilder();

        // Persona + Core Instruction
        prompt.append("""
            You are an unhinged, chronically-online Gen-Z shitpost generator.
            Your job is to create absurd, ironic, low-effort–looking but high-impact shitposts that feel like they belong on Instagram reels, Twitter/X, or Discord meme servers.

            Tone & Style Rules:
            - Use Gen-Z slang naturally (bro, fr, ong, no cap, mid, based, delulu, it’s giving, slay, cooked, core, NPC, side quest, main character).
            - Be ironic, unserious, chaotic, and self-aware.
            - Mix existential dread, pop culture, internet culture, and random absurdity.
            - Keep it short, punchy, and screenshot-able.
            - Capitalization and grammar can be intentionally wrong for comedic effect.
            - Emojis are allowed but don’t overuse (💀😭🗿🤡✨🔥).

            Humor Guidelines:
            - Dry humor, brainrot humor, or anti-jokes encouraged.
            - Use exaggerated emotions and overreactions.
            - Contrast deep thoughts with dumb delivery.
            - Make it feel like a 2am tweet or a cursed Discord message.
            """);

        // Mode-specific instruction
        if (request.getMode() == GenerateRequest.Mode.TOPIC) {
            prompt.append("\nTopic / Context:\n")
                  .append("\"").append(request.getInput()).append("\"\n");
        } else {
            prompt.append("\nRewrite and transform the following into Gen-Z shitpost content:\n")
                  .append("\"").append(request.getInput()).append("\"\n");
        }

        // Format instruction
        prompt.append("\nOutput Format Rules:\n")
              .append("- ").append(getFormatDescription(request.getFormat())).append("\n")
              .append("- Each post should be 1–3 lines max.\n")
              .append("- Keep it short, punchy, and meme-ready.\n");

        // Tone instruction
        prompt.append("\nDesired Vibe:\n")
              .append(getToneDescription(request.getTone())).append("\n");

        // Chaos level instruction (mapped to behavior)
        int chaos = request.getChaos();
        prompt.append("\nChaos Level: ").append(chaos).append("/100\n");

        if (chaos >= 80) {
            prompt.append("""
                - Make it extremely unhinged, surreal, and chaotic.
                - Add fake-deep or existential energy.
                - Include at least one unexpected metaphor or random comparison.
                """);
        } else if (chaos >= 50) {
            prompt.append("""
                - Make it noticeably chaotic and ironic.
                - Add mild absurdity and exaggerated emotions.
                """);
        } else if (chaos >= 20) {
            prompt.append("""
                - Keep it playful, ironic, and meme-like.
                - Light chaos, mostly relatable humor.
                """);
        } else {
            prompt.append("""
                - Keep it safe, relatable, and mildly funny.
                - Minimal absurdity.
                """);
        }

        // Platform instruction
        prompt.append("\nTarget Platform:\n")
              .append(getPlatformDescription(request.getPlatform())).append("\n");

        // Final instruction
        prompt.append("""
            
            Final Instructions:
            - Return 1–3 shitpost captions or meme texts.
            - No explanations.
            - No disclaimers.
            - No hashtags unless they are ironic or cursed.
            - Just output the post text only.
            """);

        log.debug("Generated Gen-Z shitpost prompt: {}", prompt.toString());
        return prompt.toString();
    }

    private String getFormatDescription(GenerateRequest.Format format) {
        return switch (format) {
            case ONE_LINER -> "Generate a single punchy Gen-Z one-liner shitpost.";
            case THREAD -> "Generate a short unhinged Twitter/X-style thread (3–5 micro-posts).";
            case HOT_TAKE -> "Generate a fake-confident, ironic hot take.";
            case LIST -> "Generate a cursed or brainrot-style list.";
            case SATIRICAL_POST -> "Generate a satirical Gen-Z shitpost.";
            case BRAND_ANNOUNCEMENT -> "Generate a fake-ironic Gen-Z brand-style announcement.";
            case META_POST -> "Generate a self-aware post about being online or posting content.";
            case MEME_CAPTION -> "Generate a meme caption that feels screenshot-able.";
        };
    }

    private String getToneDescription(GenerateRequest.Tone tone) {
        return switch (tone) {
            case SARCASTIC -> "Heavy sarcasm, irony, and dry humor.";
            case PROFESSIONAL -> "Ironically professional but still meme-coded.";
            case CHAOTIC -> "Unhinged, surreal, and brainrot-coded.";
            case INSPIRATIONAL -> "Fake-deep, ironic motivation-core energy.";
            case AGGRESSIVE -> "Unnecessarily intense, overconfident, and dramatic.";
            case FUNNY -> "Playful, ironic, and meme-first humor.";
            case DARK_HUMOR -> "Existential, cursed, and slightly concerning humor.";
            case NEUTRAL -> "Relatable, ironic, and lightly unserious.";
        };
    }

    private String getPlatformDescription(GenerateRequest.Platform platform) {
        return switch (platform) {
            case TWITTER -> "Twitter/X (short, cursed tweets, main character energy).";
            case LINKEDIN -> "LinkedIn but ironic and unprofessional on purpose.";
            case INSTAGRAM -> "Instagram reels or meme-page caption energy.";
            case THREADS -> "Casual, chaotic, chronically-online Threads vibe.";
            case REDDIT -> "Reddit shitpost or cursed comment energy.";
        };
    }
}
