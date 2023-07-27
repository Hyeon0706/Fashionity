package com.infinity.fashionity.members.service;

import com.infinity.fashionity.global.utils.HashUtil;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.members.data.MemberMaxLength;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.data.SNSType;
import com.infinity.fashionity.members.dto.LoginDTO;
import com.infinity.fashionity.members.dto.SaveDTO;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.exception.AlreadyExistException;
import com.infinity.fashionity.members.exception.IdOrPasswordNotMatchedException;
import com.infinity.fashionity.members.exception.MemberNotFoundException;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.security.oauth.dto.AuthUserInfo;
import com.infinity.fashionity.security.oauth.dto.OAuthUserInfo;
import com.infinity.fashionity.security.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.infinity.fashionity.global.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    private final String ID_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{5," + MemberMaxLength.ID + "}$";

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final String PW_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{8,16}$";

    // TODO: 정규식 넣어서 아이디, 비번, 닉네임, 패스워드 유효성 검사 진행
    // 그에 맞는 테스트 코드 작성
    /**
     * 일반 로그인 전용
     * 아이디가 없으면 MemberNotFoundException
     * 로그인 정보가 틀리면 IdOrPasswordNotMatchedException
     * @param dto
     * @return 유저 seq + 유저 role 담은 accessToken 반환
     */
    @Override
    public LoginDTO.Response login(LoginDTO.Request dto) {
        MemberEntity member = memberRepository.findById(dto.getId())
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

        if (!member.getId().equals(dto.getId()) || !passwordEncoder.matches(dto.getPassword(),member.getPassword()))
            throw new IdOrPasswordNotMatchedException(CREDENTIAL_NOT_MATCHED);

        return LoginDTO.Response.builder()
                .accessToken(jwtProvider.createAccessToken(member.getSeq(), member.getMemberRoles()))
                .build();
    }

    /**
     * 소셜 로그인 전용 로그인 및 회원가입
     * 이미 가입된 유저라면 DB 에 있는 유저 반환
     * 새로 가입하는 유저라면 (  ) 어떻게 할지 생각
     * @param oauthUserInfo
     * @return
     */
    @Override
    public AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo) {

        // 유저가 존재하는지 확인
        MemberEntity member = memberRepository.findByEmailWithRole(oauthUserInfo.getEmail());

        if (member == null) {
            // TODO: 회원가입 로직
            MemberEntity newMember = MemberEntity.builder()
                    .id(HashUtil.makeHashId())
                    .password(passwordEncoder.encode("password12345"))
                    .nickname(oAuthNameConstraints(oauthUserInfo.getNickname()))
                    .email(oauthUserInfo.getEmail())
                    .profileUrl(oauthUserInfo.getProfileImgUrl())
                    .sns(true)
                    .snsType(SNSType.GOOGLE)
                    .build();

            MemberRoleEntity memberRole = MemberRoleEntity.builder()
                    .memberRole(MemberRole.USER)
                    .member(newMember)
                    .build();

            newMember.getMemberRoles().add(memberRole);

            member = memberRepository.save(newMember);
        }

        List<MemberRoleEntity> memberRoles = member.getMemberRoles();
        return new AuthUserInfo(member.getSeq(), member.getEmail(), memberRoles);
    }

    private void registerDtoValidation(SaveDTO.Request dto) {
        if (!Pattern.matches(ID_REGEX, dto.getId()))
            throw new
    }

    /**
     * 일반 로그인 전용 회원가입
     * 처음 가입하는 멤버는 role: USER 로 통일
     * @param dto
     * @return
     */
    @Override
    public SaveDTO.Response register(SaveDTO.Request dto) {

        // 이메일, 아이디, 닉네임 중복검사
        Optional<MemberEntity> byId = memberRepository.findById(dto.getId());
        if (byId.isPresent())
            throw new AlreadyExistException(EXIST_MEMBER_ID);

        Optional<MemberEntity> byEmail = memberRepository.findByEmail(dto.getEmail());
        if (byEmail.isPresent())
            throw new AlreadyExistException(EXIST_MEMBER_EMAIL);

        Optional<MemberEntity> byNickname = memberRepository.findByNickname(dto.getNickname());
        if (byNickname.isPresent())
            throw new AlreadyExistException(EXIST_MEMBER_NICKNAME);

        MemberEntity member = MemberEntity.builder()
                .id(dto.getId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .sns(dto.getSns())
                .build();

        MemberRoleEntity memberRole = MemberRoleEntity.builder()
                .memberRole(MemberRole.USER)
                .member(member)
                .build();

        member.getMemberRoles().add(memberRole);

        MemberEntity savedMember = memberRepository.save(member);

        return SaveDTO.Response.builder()
                .id(savedMember.getId())
                .nickname(savedMember.getNickname())
                .build();
    }

    /**
     * 소셜로그인으로 받아온 닉네임 길이 13자 이상일 때 13자로 제한
     * @param nickname 소셜 로그인으로 받아온 이름
     * @return nickname 제약 조건에 만족한 이름
     */
    private String oAuthNameConstraints(String nickname) {
        if (nickname.length() > 13) {
            nickname = nickname.trim().substring(0, 13);
        }
        return nickname;
    }
}
