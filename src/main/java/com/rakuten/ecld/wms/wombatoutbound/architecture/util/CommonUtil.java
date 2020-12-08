package com.rakuten.ecld.wms.wombatoutbound.architecture.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public final class CommonUtil {

    private CommonUtil() {
    }

    public static String concatString(final String delimeter, final String... inputStr) {
        if (ArrayUtils.isEmpty(inputStr)) {
            return "";
        }
        return Arrays.stream(inputStr).filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(delimeter));
    }

    /**
     * Returns an empty list if list is empty
     *
     * @param list
     * @return
     */
    public static <T> List<T> returnEmptyListIfNullOrEmpty(final List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    /**
     * check for empty collection return empty stream if agu is null or empty else return the same
     * object
     *
     * @param object
     * @return
     */
    public static <T> Stream<T> returnEmptyStreamIfNullOrEmpty(final Collection<T> object) {
        if (CollectionUtils.isEmpty(object)) {
            return Stream.empty();
        }
        return object.stream();
    }

    /**
     * Returns True if string is numeric
     *
     * @param
     * @return
     */
    public static boolean isNumeric(final String strNum) {
        if (StringUtils.isBlank(strNum)) {
            return false;
        }
        Pattern pattern = Pattern.compile("(^\\d+$)?"); // regex to match only whole numbers (String)
        return pattern.matcher(strNum).matches();
    }

    /**
     * Returns the current date object, as a java.util.Date object
     *
     * @return The current date as a java.util.Date object
     */
    public static Date getCurrentDate() {

        return Calendar.getInstance().getTime();
    }

    /**
     * Get current JST time
     *
     * @return
     */
    public static Timestamp getTodaysDateJST() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
        Instant nowUtc = Instant.now();
        return new Timestamp(nowUtc.toEpochMilli());
    }

    /**
     * Combine two file paths (usually, file base path and file name)
     *
     * @param frmFileRootPath : The root path (base path)
     * @param frmFilePath     : The file name
     * @return absolute path of the file by combining the two paths
     * @author @amitrajitbose
     */
    public static String combinePath(final String frmFileRootPath, final String frmFilePath) {
        if (StringUtils.isBlank(frmFileRootPath)) {
            return frmFilePath;
        }
        String rootName = frmFileRootPath;
        String fileName = frmFilePath;
        if (frmFileRootPath.endsWith("/")) {
            rootName = StringUtils.chop(frmFileRootPath);
        }
        if (frmFilePath.charAt(0) == '/') {
            fileName = frmFilePath.replaceFirst("/", "");
        }
        return rootName + "/" + fileName;
    }

    public static String formatLocCd(String locCd) {
        String str = StringUtils.defaultString(locCd);
        return str.replaceAll("^([0-9A-Z]*)-([0-9A-Z]{2}[0-9]{2})([A-Z])([0-9]{3})$", "$1-$2-$3-$4");
    }
}
