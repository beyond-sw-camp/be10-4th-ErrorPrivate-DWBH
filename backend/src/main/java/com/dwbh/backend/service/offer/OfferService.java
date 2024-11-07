package com.dwbh.backend.service.offer;

import com.dwbh.backend.common.util.FileUploadUtils;
import com.dwbh.backend.dto.offer.CreateOrUpdateOfferRequest;
import com.dwbh.backend.dto.offer.OfferDTO;
import com.dwbh.backend.entity.*;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.counselor_hire.CounselorRepository;
import com.dwbh.backend.repository.file.FileRepository;
import com.dwbh.backend.repository.offer.OfferRepository;
import com.dwbh.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

    private final OfferRepository offerRepository;
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CounselorRepository hireRepository;
    private static final String UPLOAD_DIR = "uploads"; // 파일 저장 디렉토리

    @Transactional
    public OfferDTO  createOffer(Long hireSeq, CreateOrUpdateOfferRequest request, MultipartFile file) {
        log.info("--------------댓글작성 서비스 진입----------------");

        // 1. 필요한 연관 관계 데이터 가져오기(추후 연관관계 기능 구현시 수정예정)
        User user = userRepository.findById(request.getUserSeq())
                .orElseThrow(() ->  new CustomException(ErrorCodeType.USER_NOT_FOUND));
        CounselorHire hire = hireRepository.findById(hireSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.POST_NOT_FOUND));


        // 2. DTO를 Entity로 매핑
        CounselOffer offer = modelMapper.map(request, CounselOffer.class);
//        offer.putHireSeq(2L); // 임의로 설정
//        offer.putUserSeq(request.getUserSeq()); // 임의 설정
        offer.putUserSeq(user);
        offer.putHireSeq(hire);

        // 3. 파일이 포함된 경우 파일 먼저 저장
        // 3-1. 이미지 파일 여부 확인
        if (file != null && !FileUploadUtils.isImageFile(file)) {
            throw new CustomException(ErrorCodeType.INVALID_FILE_TYPE);
        }

        // 3-2. 파일 저장
        if (file != null) {
            String savedFileName = FileUploadUtils.saveFile(UPLOAD_DIR, file);

            // MIME 타입 추출
            String mimeType = FileUploadUtils.getMimeType(file);

            // 파일 엔티티 생성 및 설정
            File fileEntity = new File(savedFileName, mimeType, UPLOAD_DIR + "/" + savedFileName, file.getContentType());
            fileEntity = fileRepository.save(fileEntity);

            // CounselOfferFile 엔티티 생성 및 설정
            CounselOfferFile offerFile = new CounselOfferFile(fileEntity, offer);
            offer.inputOfferFile(offerFile);
        }

        // 4. 엔티티 저장
        CounselOffer savedOffer = offerRepository.save(offer);

        // 5. Entity를 Response DTO로 매핑
        OfferDTO response = modelMapper.map(savedOffer, OfferDTO.class);

        return response;
    }

    @Transactional
    public OfferDTO updateOffer(Long hireSeq, Long offerSeq, CreateOrUpdateOfferRequest request, MultipartFile newFile) {
        log.info("--------------댓글수정 서비스 진입----------------");

        // 1. 댓글 엔티티 조회(수정하려는 댓글 찾기)
        User user = userRepository.findById(request.getUserSeq())
                .orElseThrow(() ->  new CustomException(ErrorCodeType.USER_NOT_FOUND));
        // 탈퇴된 회원인지 확인
        if (!"activate".equals(user.getUserStatus())) {
            // 탈퇴된 회원일 경우 수정 불가 예외 발생
            throw new CustomException(ErrorCodeType.INACTIVATE_USER);
        }

        CounselorHire hire = hireRepository.findById(hireSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.POST_NOT_FOUND));

        CounselOffer offer = offerRepository.findById(offerSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.COMMENT_NOT_FOUND));
        // 수정 권한 검증(시큐리티 구현 후 추가 예정)
//        if (!CustomUserUtils.getCurrentUserSeq().equals(offer.getUserSeq())) {
//            throw new CustomException(ErrorCodeType.SECURITY_ACCESS_ERROR);
//        }
        long requestUserSeq  = request.getUserSeq();
        log.info("-----------request의 userSeq: {}", requestUserSeq);
        log.info("-----------db 저장 userSeq: {}", offer.getUser().getUserSeq());
        if (requestUserSeq != offer.getUser().getUserSeq()) {
            throw new CustomException(ErrorCodeType.SECURITY_ACCESS_ERROR);
        }

        // 이미 삭제된 게시글의 댓글이거나 이미 삭제된 댓글의 경우
        if((offer.getDelDate() != null) || (offer.getHire().getDelDate() != null)) {
            throw new CustomException(ErrorCodeType.COMMENT_NOT_FOUND);
        }

        // 2. 댓글 내용 및 비밀 여부 수정
        offer.updateContent(request.getOfferContent(), request.getOfferPrivateYn());

        // 3-1. 이미지 파일 여부 확인
        if (newFile != null && !FileUploadUtils.isImageFile(newFile)) {
            throw new CustomException(ErrorCodeType.INVALID_FILE_TYPE);
        }

        // 3-2. 기존 파일 삭제 및 새 파일 추가
        if (newFile != null) {
            // 기존 파일이 있는 경우 소프트 딜리트 처리
            if (offer.getOfferFile() != null) {
                fileRepository.softDeleteById(offer.getOfferFile().getFile().getFileSeq(),  LocalDateTime.now(ZoneId.of("Asia/Seoul")));
                // 관계를 끊어주기 위해 offerFile을 null로 설정
                offer.inputOfferFile(null);
            }

            // 새 파일 저장
            String savedFileName = FileUploadUtils.saveFile(UPLOAD_DIR, newFile);
            String mimeType = FileUploadUtils.getMimeType(newFile);
            File fileEntity = new File(savedFileName, mimeType, UPLOAD_DIR + "/" + savedFileName, newFile.getContentType());
            fileEntity = fileRepository.save(fileEntity);

            // 새 파일을 CounselOfferFile 엔티티에 생성
            CounselOfferFile offerFile = new CounselOfferFile(fileEntity, offer);
            offer.inputOfferFile(offerFile);

        }

        // 4. 엔티티를 DTO로 변환하여 반환
        return modelMapper.map(offer, OfferDTO.class);
    }
}
