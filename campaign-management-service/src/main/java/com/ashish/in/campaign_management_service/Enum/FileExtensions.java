package com.ashish.in.campaign_management_service.Enum;


import java.lang.reflect.Array;
import java.util.Arrays;

public enum FileExtensions {
    CSV("csv"),
    XLSX("xlsx"),
    XL("xl");

    private final String extension;

    FileExtensions(String extension) {
        this.extension=extension;
    }

    String getExtension() {
        return extension;
    }

    public static boolean isAllowed(String ext) {
        return Arrays.stream(values()).anyMatch(e -> e.extension.equalsIgnoreCase(ext));

    }
}
