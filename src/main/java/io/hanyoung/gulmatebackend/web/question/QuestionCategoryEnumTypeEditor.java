package io.hanyoung.gulmatebackend.web.question;

import io.hanyoung.gulmatebackend.web.question.dto.QuestionCategory;

import java.beans.PropertyEditorSupport;

public class QuestionCategoryEnumTypeEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        QuestionCategory questionCategory = (QuestionCategory) getValue();
        return questionCategory.name();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        QuestionCategory[] enumTypes = QuestionCategory.values();
        for(QuestionCategory enumType : enumTypes){
            if(enumType.toString().equals(text) || enumType.name().equals(text)){
                setValue(enumType);
                return;
            }
        }

        throw new IllegalArgumentException("No Such QuestionCategory Type: " + text);
    }
}
