package com.infinity.fashionity.members.service;

import com.infinity.fashionity.members.dto.LoginDTO;
import com.infinity.fashionity.members.dto.SaveDTO;
import com.infinity.fashionity.security.oauth.dto.AuthUserInfo;
import com.infinity.fashionity.security.oauth.dto.OAuthUserInfo;

public interface MemberService {

    LoginDTO.Response login(LoginDTO.Request dto);
    AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo);
    SaveDTO.Response register(SaveDTO.Request dto);
    boolean isIdValidate(String id);
    boolean isNicknameValidate(String nickname);
    boolean isEmailValidate(String email);
}
