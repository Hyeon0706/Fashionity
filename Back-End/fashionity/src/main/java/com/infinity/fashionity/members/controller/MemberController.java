package com.infinity.fashionity.members.controller;

import com.infinity.fashionity.members.dto.MemberFollowDTO;
import com.infinity.fashionity.members.dto.ProfileDTO;
import com.infinity.fashionity.members.dto.ProfilePostDTO;
import com.infinity.fashionity.members.service.MemberService;
import com.infinity.fashionity.security.dto.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{nickname}")
    public ResponseEntity<ProfileDTO.Response> getMemberProfile(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname
    ) {
        ProfileDTO.Response response = memberService.getMemberProfile(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{nickname}/posts")
    public ResponseEntity<ProfilePostDTO.Response> getMemberProfilePost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname
    ) {
        ProfilePostDTO.Response response = memberService.getMemberProfilePost(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{nickname}/liked")
    public ResponseEntity<ProfilePostDTO.Response> getMemberProfileLikedPost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname
    ) {
        ProfilePostDTO.Response response = memberService.getMemberProfileLikedPost(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{nickname}/hidden")
    public ResponseEntity<ProfilePostDTO.Response> getMemberProfileHiddenPost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname
    ) {
        ProfilePostDTO.Response response = memberService.getMemberProfileHiddenPost(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/edit")
    public ResponseEntity<ProfileDTO.Response> editMyProfile(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody ProfileDTO.Request profile
    ) {
        ProfileDTO.Response response = memberService.editMemberProfile(auth.getSeq(), profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/edit/pw")
    public ResponseEntity<ProfileDTO.PwResponse> editMyPassword(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody ProfileDTO.PwRequest data
    ) {
        ProfileDTO.PwResponse response = memberService.editMyPassword(auth.getSeq(), data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{nickname}/followings")
    public ResponseEntity<MemberFollowDTO.FollowingResponse> getFollowings(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname
    ) {
        MemberFollowDTO.FollowingResponse response = memberService.getFollowings(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{nickname}/followers")
    public ResponseEntity<MemberFollowDTO.FollowerResponse> getFollowers(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname
    ) {
        MemberFollowDTO.FollowerResponse response = memberService.getFollowers(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}