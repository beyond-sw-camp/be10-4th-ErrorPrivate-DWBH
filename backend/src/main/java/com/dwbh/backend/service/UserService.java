package com.dwbh.backend.service;

import com.dwbh.backend.dto.UserDetailResponse;
import com.dwbh.backend.dto.user.UserModifyResponse;
import com.dwbh.backend.entity.User;
import com.dwbh.backend.repository.user.UserRepository;
import com.dwbh.backend.dto.CreateUserRequest;
import com.dwbh.backend.repository.user.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomUserRepository userRepositoryImp;
    private final UserRepository userRepository;

    // 회원 등록
    @Transactional
    public void createUser(CreateUserRequest newUser) {
        User user = modelMapper.map(newUser, User.class);
        user.encryptPassword(passwordEncoder.encode(newUser.getUserPassword()));
        userRepository.save(user);
    }

    // user_seq 등록 여부 확인
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User loginUser = userRepository.findByUserEmail(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId));

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(loginUser.getUserEmail(), loginUser.getUserPassword(), grantedAuthorities);
    }

    // user_email 로 user_seq 검색
    public Long getUserSeq(String Email) {
        User user = userRepository.findByUserEmail(Email).orElseThrow();
        return user.getUserSeq();
    }

    // 회원 상세 조회
    public UserDetailResponse getUserDetail(Long userSeq) {

        return userRepositoryImp.findUserDetailResponse(userSeq);
    }

    // 회원 정보 수정 조회
    public UserModifyResponse getUserModify(Long userSeq) {

        return modelMapper.map(userRepository.findByUserSeq(userSeq), UserModifyResponse.class);
    }
}