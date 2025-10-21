package vn.atdigital.ftpservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExtensionEnum {
    // Văn bản
    TXT, DOC, DOCX, PDF, RTF, ODT, MD,

    // Bảng tính
    XLS, XLSX, CSV, ODS,

    // Thuyết trình
    PPT, PPTX, ODP,

    // Hình ảnh
    JPG, JPEG, PNG, GIF, BMP, TIFF, SVG, WEBP,

    // Video
    MP4, AVI, MKV, MOV, WMV, FLV, WEBM,

    // Audio
    MP3, WAV, AAC, FLAC, OGG, M4A,

    // Lập trình / mã nguồn
    JAVA, PY, JS, TS, PHP, HTML, HTM, CSS, JSON, XML, YML, YAML, C, CPP, CS, GO, RS, KT,

    // Nén / đóng gói
    ZIP, RAR, TAR, GZ, BZ2, TGZ, JAR, WAR, EAR,

    // Hệ thống
    EXE, MSI, APK, DEB, RPM, SH, BAT, BIN, ISO;

    /**
     * Kiểm tra xem extension có nằm trong danh sách enum không
     * @param extension phần mở rộng file (ví dụ: "pdf" hoặc ".pdf")
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean checkExtension(String extension) {
        if (extension == null || extension.isBlank()) {
            return false;
        }
        // loại bỏ dấu chấm đầu tiên nếu có
        String cleanExt = extension.startsWith(".")
                ? extension.substring(1)
                : extension;

        try {
            ExtensionEnum.valueOf(cleanExt.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}