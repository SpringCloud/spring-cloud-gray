package cn.springcloud.gray.bean.domain;

import java.util.Objects;

/**
 * A standard set of data size units.
 *
 * @author Stephane Nicoll
 * @since 5.1
 */
public enum DataUnit {

    /**
     * Bytes.
     */
    BYTES("B", DataSize.ofBytes(1)),

    /**
     * Kilobytes.
     */
    KILOBYTES("KB", DataSize.ofKilobytes(1)),

    /**
     * Megabytes.
     */
    MEGABYTES("MB", DataSize.ofMegabytes(1)),

    /**
     * Gigabytes.
     */
    GIGABYTES("GB", DataSize.ofGigabytes(1)),

    /**
     * Terabytes.
     */
    TERABYTES("TB", DataSize.ofTerabytes(1));


    private final String suffix;

    private final DataSize size;


    DataUnit(String suffix, DataSize size) {
        this.suffix = suffix;
        this.size = size;
    }

    DataSize size() {
        return this.size;
    }

    /**
     * Return the {@link DataUnit} matching the specified {@code suffix}.
     *
     * @param suffix one of the standard suffix
     * @return the {@link DataUnit} matching the specified {@code suffix}
     * @throws IllegalArgumentException if the suffix does not match any
     *                                  of this enum's constants
     */
    public static DataUnit fromSuffix(String suffix) {
        for (DataUnit candidate : values()) {
            if (Objects.equals(candidate.suffix, suffix)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Unknown unit '" + suffix + "'");
    }

}