package vn.atdigital.ftpservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.atdigital.ftpservice.common.enums.AttachmentOwnerTypeEnum;

@Data
@Entity
@Builder
@Table(name = "file_record")
@NoArgsConstructor
@AllArgsConstructor
public class FileRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String path;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "owner_type")
    @Enumerated(EnumType.STRING)
    private AttachmentOwnerTypeEnum ownerType;
}
