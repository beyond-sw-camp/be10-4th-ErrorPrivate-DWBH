package com.dwbh.backend.service.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.CounselorDTO;
import com.dwbh.backend.entity.CounselorHire;
import com.dwbh.backend.entity.User;
import com.dwbh.backend.repository.counselor_hire.CounselorRepository;
import com.dwbh.backend.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

        // 게시글 등록
        @Transactional
        public void savePost(CounselorDTO savePostReqDTO) {
            Long loginUserId = 1L;

            User foundUser = userRepository.findById(loginUserId)
                    .orElseThrow(() -> new IllegalArgumentException("회원이 아닙니다."));

            CounselorHire saveCounselor = modelMapper.map(savePostReqDTO, CounselorHire.class);
            saveCounselor.updateUser(foundUser);
            counselorRepository.save(saveCounselor);
        }

        // 모든 게시글 조회
        public List<CounselorDTO> findAllPosts() {
            List<CounselorHire> counselorHires = counselorRepository.findAll();

            return counselorHires.stream()
                    .map(counselor -> modelMapper.map(counselor, CounselorDTO.class))
                    .collect(Collectors.toList());
        }

        //게시글 수정
        @Transactional
        public void updatePost(Long id, CounselorDTO updatePostReqDTO) {
            CounselorHire counselorHire = counselorRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

            counselorHire.updateCounselor(updatePostReqDTO.getHireTitle(), updatePostReqDTO.getHireContent(), updatePostReqDTO.getHireGender());
        }

        //게시글 삭제
        @Transactional
        public void deletePost(Long id) {
            CounselorHire counselorHire = counselorRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
            counselorRepository.delete(counselorHire);
        }
    }

