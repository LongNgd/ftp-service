package vn.atdigital.ftpservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.atdigital.ftpservice.domain.model.FileRecord;

import java.util.Optional;

@Repository
public interface FileRecordRepository extends JpaRepository<FileRecord,Long> {
    Optional<FileRecord> findByPath(String filePath);
}
