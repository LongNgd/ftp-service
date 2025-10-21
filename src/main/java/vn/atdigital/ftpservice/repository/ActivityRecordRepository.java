package vn.atdigital.ftpservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.atdigital.ftpservice.domain.model.ActivityRecord;

@Repository
public interface ActivityRecordRepository extends JpaRepository<ActivityRecord, Long> {
}
