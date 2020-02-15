package io.hanyoung.gulmatebackend.config.web;

import io.hanyoung.gulmatebackend.web.question.dto.QuestionCategory;

import java.beans.PropertyEditorSupport;

public class QuestionCategoryConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(QuestionCategory.fromText(text));
    }
}
