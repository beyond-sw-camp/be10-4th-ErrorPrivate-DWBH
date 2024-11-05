package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)  // 엔티티 생성, 삭제 시점 체크를 위해 필요한 리스너
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "file_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "file_mod_date"))
})
@SQLDelete(sql = "UPDATE tb_file SET file_del_date = NOW() WHERE file_seq = ?")
public class File extends BaseDateEntity {

    @Id
    @Column(name = "file_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileSeq;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_content_type", nullable = false)
    private String fileContentType;

    @Column(name = "file_del_date")
    private LocalDateTime delDate;

    public File(String fileName, String fileType, String filePath, String fileContentType) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileContentType = fileContentType;
        // regDate 자동생성
    }

}
