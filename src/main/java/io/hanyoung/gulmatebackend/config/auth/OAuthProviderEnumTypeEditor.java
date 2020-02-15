package io.hanyoung.gulmatebackend.config.auth;

import io.hanyoung.gulmatebackend.web.question.dto.QuestionCategory;

import java.beans.PropertyEditorSupport;

public class OAuthProviderEnumTypeEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        OAuthProvider provider = (OAuthProvider) getValue();
        return provider.name();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        OAuthProvider[] enumTypes = OAuthProvider.values();
        for(OAuthProvider enumType : enumTypes){
            if(enumType.toString().equals(text) || enumType.name().equals(text)){
                setValue(enumType);
                return;
            }
        }

        throw new IllegalArgumentException("No Such OAuthProvider Type: " + text);
    }
}
