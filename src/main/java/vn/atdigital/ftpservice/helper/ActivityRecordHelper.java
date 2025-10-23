package vn.atdigital.ftpservice.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.atdigital.ftpservice.common.enums.ActivityEnum;
import vn.atdigital.ftpservice.domain.model.ActivityRecord;
import vn.atdigital.ftpservice.repository.ActivityRecordRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ActivityRecordHelper {
    private final ActivityRecordRepository activityRecordRepository;

    public void createActivityRecord(String filePath, ActivityEnum activity, String username) {
        activityRecordRepository.save(
                ActivityRecord.builder()
                        .filePath(filePath)
                        .username(username)
                        .dateTime(LocalDateTime.now())
                        .activity(activity)
                        .build()
        );
    }
}
