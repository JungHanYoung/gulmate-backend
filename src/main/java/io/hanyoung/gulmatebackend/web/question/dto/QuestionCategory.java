package io.hanyoung.gulmatebackend.web.question.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum QuestionCategory {

    CSE("cse"),ECE("ece");

    private String text;

    QuestionCategory(String text) {
        this.text = text;
    }

    @JsonCreator
    public static QuestionCategory fromText(String text) {
        for (QuestionCategory category : values()) {
            if (category.text.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type " + text + ", Allowed values are " + Arrays.toString(values()));
    }

}
