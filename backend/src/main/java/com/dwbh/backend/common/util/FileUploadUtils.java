package com.dwbh.backend.common.util;

import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUploadUtils {

    public static String saveFile(String uploadDir, MultipartFile multipartFile) {

        try(InputStream inputStream = multipartFile.getInputStream()) {

            Path uploadPath = Paths.get(uploadDir);

            /* 업로드 경로가 존재하지 않을 시 경로 먼저 생성 */
            if(!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);

            /* 파일명 생성 */
            String replaceFileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

            /* 파일 저장 */
            Path filePath = uploadPath.resolve(replaceFileName);
//            System.out.println("filePath: " + filePath.getRoot());
//            System.out.println("filePath: " + filePath.getParent());

            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            return replaceFileName;

        } catch (IOException e) {
            throw new  CustomException(ErrorCodeType.FILE_UPLOAD_FAIL);
        }
    }

    public static void deleteFile(String uploadDir, String fileName) {

        try {
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(fileName);
            Files.delete(filePath);
        } catch (IOException e) {
            throw new CustomException(ErrorCodeType.FILE_UPLOAD_FAIL);
        }

    }

    /*
     * 파일의 실제 MIME 타입을 가져오는 메서드
     */
    public static String getMimeType(MultipartFile multipartFile) {
        String mimeType = multipartFile.getContentType();

        // MIME 타입이 multipart/form-data로 반환될 경우 기본 MIME 타입 설정
        if (mimeType == null || mimeType.equals("multipart/form-data")) {
            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename()).toLowerCase();

            switch (extension) {
                case "jpg":
                case "jpeg":
                case "png":
                case "gif":
                case "ai":
                case "psd":
                case "tiff":
                case "tif":
                case "svg":
                case "bmp":
                case "eps":
                case "raw":
                    mimeType = "image/" + extension;
                    break;
                case "mp4":
                case "avi":
                case "mov":
                    mimeType = "video/" + extension;
                    break;
                default:
                    mimeType = "application/octet-stream"; // 기본 타입
            }
        }

        return mimeType;
    }

    /*
     * 파일이 이미지인지 확인
     */
    public static boolean isImageFile(MultipartFile file) {
        String mimeType = file.getContentType();
        return mimeType != null && (mimeType.startsWith("image/"));
    }

}
