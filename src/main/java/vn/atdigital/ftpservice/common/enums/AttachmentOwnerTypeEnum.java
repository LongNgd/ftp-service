package vn.atdigital.ftpservice.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
@AllArgsConstructor
public enum AttachmentOwnerTypeEnum {
    ORGANISATION("Organisation"),
    SUBSTATION("Substation"),
    VOLTAGE_LEVEL("Voltage Level"),
    BAY("Bay"),
    TRANSFORMER("Transformer"),
    BUSHING("Bushing"),
    CIRCUIT_BREAKER("Circuit breaker"),
    CURRENT_TRANSFORMER("Current transformer"),
    VOLTAGE_TRANSFORMER("Voltage transformer"),
    SURGE_ARRESTER("Surge arrester"),
    POWER_CABLE("Power cable"),
    DISCONNECTOR("Disconnector");

    @JsonValue
    private final String value;

    public static AttachmentOwnerTypeEnum fromValue(String enumValue) {
        Assert.notNull(enumValue, "Owner type must not be null");
        String normalized = enumValue.trim()
                .replace('-', '_')
                .replace(' ', '_')
                .replace('/', '_')
                .replace('.', '_')
                .toUpperCase();
        for (AttachmentOwnerTypeEnum value : values()) {
            if (normalized.equals(value.toString())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Owner type '" + enumValue + "' is not valid");
    }
}
