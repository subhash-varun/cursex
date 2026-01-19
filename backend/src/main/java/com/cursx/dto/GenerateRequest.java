package com.cursx.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerateRequest {

    @NotBlank(message = "Input is required")
    private String input;

    @NotNull(message = "Mode is required")
    private Mode mode;

    private Format format = Format.ONE_LINER;

    private Tone tone = Tone.SARCASTIC;

    private Integer chaos = 50;

    private Platform platform = Platform.TWITTER;

    public enum Mode {
        TOPIC, DRAFT
    }

    public enum Format {
        ONE_LINER, THREAD, HOT_TAKE, LIST, SATIRICAL_POST, BRAND_ANNOUNCEMENT, META_POST, MEME_CAPTION
    }

    public enum Tone {
        SARCASTIC, PROFESSIONAL, CHAOTIC, INSPIRATIONAL, AGGRESSIVE, FUNNY, DARK_HUMOR, NEUTRAL
    }

    public enum Platform {
        TWITTER, LINKEDIN, INSTAGRAM, THREADS, REDDIT
    }
}
