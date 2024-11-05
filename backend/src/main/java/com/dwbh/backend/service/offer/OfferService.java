package com.dwbh.backend.service.offer;

import com.dwbh.backend.common.util.FileUploadUtils;
import com.dwbh.backend.dto.offer.CreateOfferRequest;
import com.dwbh.backend.dto.offer.OfferDTO;
import com.dwbh.backend.entity.CounselOffer;
import com.dwbh.backend.entity.CounselOfferFile;
import com.dwbh.backend.entity.CounselorHire;
import com.dwbh.backend.entity.File;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.offer.OfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
//    private final UserRepository userRepository;
//    private final HireRepository hireRepository;
    private static final String UPLOAD_DIR = "/uploads"; // 파일 저장 디렉토리

//    public void createOffer(Long postSeq, CreateOfferRequest createOfferRequest, MultipartFile file) {
    public OfferDTO  createOffer(Long postSeq, CreateOfferRequest createOfferRequest, MultipartFile file) {
        log.info("--------------댓글작성 서비스 진입----------------");

        // 1. 필요한 연관 관계 데이터 가져오기(추후 연관관계 기능 구현시 수정예정)
//        User user = userRepository.findById(createOfferRequest.getUserSeq())
//                .orElseThrow(() ->  new CustomException(ErrorCodeType.USER_NOT_FOUND));
//        CounselorHire hire = hireRepository.findById(hireSeq())
//                .orElseThrow(() -> new CustomException(ErrorCodeType.POST_NOT_FOUND));


        // 2. DTO를 Entity로 매핑
        CounselOffer offer = modelMapper.map(createOfferRequest, CounselOffer.class);
//        offer.putUserSeq(user);
//        offer.putHireSeq(hire);

        // 2. 파일이 포함된 경우 파일 처리
        if (file != null) {
            // FileUploadUtils를 사용하여 파일 저장
            String savedFileName = FileUploadUtils.saveFile(UPLOAD_DIR, file);

            // File 엔티티 생성 및 설정
            File fileEntity = new File(savedFileName, file.getContentType(), UPLOAD_DIR + "/" + savedFileName, file.getContentType());
            CounselOfferFile offerFile = new CounselOfferFile(fileEntity, offer);
            offer.inputOfferFile(offerFile);
        }

        // 4. 엔티티 저장
        CounselOffer savedOffer = offerRepository.save(offer);

        // 5. Entity를 Response DTO로 매핑
        OfferDTO response = modelMapper.map(savedOffer, OfferDTO.class);

        return response;
    }

}
