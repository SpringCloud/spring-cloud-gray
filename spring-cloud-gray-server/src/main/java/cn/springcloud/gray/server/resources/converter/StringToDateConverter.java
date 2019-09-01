package cn.springcloud.gray.server.resources.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by saleson on 2017/7/9.
 */
public class StringToDateConverter implements Converter<String, Date> {

    private static Logger log = LoggerFactory.getLogger(StringToDateConverter.class);


    private final static String[] dateFormats = {
            "EEE, d MMM yyyy HH:mm:ss z",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd HH:mm:ss.SSSZ",
            "yyyy-MM-dd HH:mm:ssZ",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd"};

    @Override
    public Date convert(String source) {
        source = StringUtils.strip(source);
        Assert.hasText(source, "Null or emtpy date string");
        Date date = null;
        try {
            date = DateUtils.parseDate(source, dateFormats);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        if (date == null) {
            String errMsg = String.format("Failed to convert [%s] to [%s] for value '%s'",
                    String.class.toString(), Date.class.toString(), source);
            log.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        return date;

    }
}
