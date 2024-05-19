package com.project.foradhd.global.paging.argumentresolver;

import com.project.foradhd.global.paging.dto.request.PagingRequest;
import com.project.foradhd.global.paging.dto.request.PagingRequest.SortRequest;
import com.project.foradhd.global.paging.enums.Sort.Direction;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;

public class PagingRequestArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int DEFAULT_PAGE_VALUE = 0;
    private static final int DEFAULT_SIZE_VALUE = 10;
    private static final String PAGE_PARAM_NAME = "page";
    private static final String SIZE_PARAM_NAME = "size";
    private static final String SORT_PARAM_NAME = "sort";
    private static final String SORT_SEPARATOR = ",";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PagingRequest.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String pageParam = webRequest.getParameter(PAGE_PARAM_NAME);
        String sizeParam = webRequest.getParameter(SIZE_PARAM_NAME);
        String[] sortParams = webRequest.getParameterValues(SORT_PARAM_NAME);

        int page = pageParam == null ? DEFAULT_PAGE_VALUE : Integer.parseInt(pageParam);
        int size = sizeParam == null ? DEFAULT_SIZE_VALUE : Integer.parseInt(sizeParam);
        List<SortRequest> sort = sortParams == null ? List.of() : parseSort(sortParams);
        return new PagingRequest(page, size, sort);
    }

    private List<SortRequest> parseSort(String[] sortParams) throws ServletRequestBindingException {
        List<SortRequest> sort = new ArrayList<>();
        for (String sortParam : sortParams) {
            String[] splitSortParam = sortParam.split(SORT_SEPARATOR);
            if (splitSortParam.length != 2) {
                throw new MissingServletRequestParameterException(SORT_PARAM_NAME, String[].class.getName());
            }

            String property = splitSortParam[0].strip();
            Direction direction = Direction.from(splitSortParam[1].strip())
                    .orElseThrow(() ->
                            new MissingServletRequestParameterException(SORT_PARAM_NAME, Direction.class.getName()));
            sort.add(new SortRequest(property, direction));
        }
        return sort;
    }
}
