package com.dwbh.backend.repository.file;

import com.dwbh.backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface FileRepository  extends JpaRepository<File, Long> {
    @Modifying
    @Query("UPDATE File f SET f.delDate = :delDate WHERE f.fileSeq = :fileSeq")
    void softDeleteById(@Param("fileSeq") Long fileSeq, @Param("delDate") LocalDateTime delDate);
}
