package cn.springcloud.gray.server.utils;

import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by admin on 2017/2/28.
 */
public class PaginationUtils {
    private static final Logger log = LoggerFactory.getLogger(PaginationUtils.class);

    public static final ObjectMapper PAGINATION_OBJECT_MAPPER = new ObjectMapper();


    public static <MODEL, T> Page<MODEL> convert(Pageable pageable, Page<T> p, ModelMapper<MODEL, T> modelMapper) {
        List<MODEL> list = modelMapper.dos2models(p.getContent());
        return new PageImpl<MODEL>(list, pageable, p.getTotalElements());
    }

    public static <MODEL, T> Page<MODEL> convert(Pageable pageable, Page<T> p, List<MODEL> models) {
        return new PageImpl<MODEL>(models, pageable, p.getTotalElements());
    }


    public static HttpHeaders generatePaginationHttpHeaders(Page<?> page) {
        return generatePaginationHttpHeaders(page, null);
    }

    public static HttpHeaders generatePaginationHttpHeaders(Page<?> page, String baseUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + page.getTotalElements());

        if (!StringUtils.isEmpty(baseUrl)) {
            Pagination pagination = new Pagination();
            int lastPage = 0;
            if (page.getTotalPages() > 0) {
                lastPage = page.getTotalPages() - 1;
            }
            if (page.getNumber() + 1 < page.getTotalPages()) {
                pagination.setNext(generateUri(baseUrl, page.getNumber() + 1, page.getSize()));
            }

            if (page.getNumber() > 0) {
                pagination.setPrev(generateUri(baseUrl, page.getNumber() - 1, page.getSize()));
            }
            pagination.setLast(generateUri(baseUrl, lastPage, page.getSize()));
            pagination.setFirst(generateUri(baseUrl, 0, page.getSize()));
            try {
                headers.add("X-Pagination", PAGINATION_OBJECT_MAPPER.writeValueAsString(pagination));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return headers;
    }

    public static String generateUri(String baseUrl, int page, int size) {
        return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", new Object[]{Integer.valueOf(page)}).queryParam("size", new Object[]{Integer.valueOf(size)}).toUriString();
    }

    static class Pagination {
        private String next;
        private String prev;
        private String last;
        private String first;

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getPrev() {
            return prev;
        }

        public void setPrev(String prev) {
            this.prev = prev;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }
    }
}
