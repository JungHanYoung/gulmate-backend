package io.hanyoung.gulmatebackend.web.family;

import io.hanyoung.gulmatebackend.domain.family.FamilyType;

import java.beans.PropertyEditorSupport;

public class FamilyTypeEnumEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        FamilyType value = (FamilyType) getValue();
        return value.name();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        FamilyType[] familyTypes = FamilyType.values();
        for(FamilyType type : familyTypes) {
            if(type.toString().equals(text.toUpperCase()) || type.name().equals(text.toUpperCase())) {
                setValue(type);
                return;
            }
        }

        throw new IllegalArgumentException("No Such Family Type: " + text);

    }
}
