package vn.atdigital.ftpservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.atdigital.ftpservice.common.enums.ActivityEnum;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "activity_record")
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    private String username;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private ActivityEnum activity;
}
