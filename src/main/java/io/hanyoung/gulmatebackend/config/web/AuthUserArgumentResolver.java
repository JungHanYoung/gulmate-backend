package io.hanyoung.gulmatebackend.config.web;

import io.hanyoung.gulmatebackend.config.auth.OAuthAuthenticationToken;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

@RequiredArgsConstructor
@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AccountRepository accountRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isAuthUserAnnotation = parameter.getParameterAnnotation(AuthUser.class) != null;
        boolean isAccountClass = Account.class.equals(parameter.getParameterType());
        return isAuthUserAnnotation && isAccountClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        OAuthAuthenticationToken authentication = (OAuthAuthenticationToken) webRequest.getUserPrincipal();
        Account account = accountRepository.findById(authentication.getPrincipal())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user error"));


        return account;
    }
}
