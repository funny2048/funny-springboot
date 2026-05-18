package com.funny.framework.web.filter.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Xss过滤包装请求
 *
 * @author funny2048
 * @version 1.0
 * @date 2019-10-28
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return XssHelper.stripXss(value);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> valuesMap = super.getParameterMap();
        if (valuesMap == null || valuesMap.isEmpty()) {
            return valuesMap;
        }
        Map<String, String[]> encodedValuesMap = new LinkedHashMap<>(valuesMap.size() * 2);
        for (Map.Entry<String, String[]> entry : valuesMap.entrySet()) {
            encodedValuesMap.put(entry.getKey(), XssHelper.stripXss(entry.getValue()));
        }
        return Collections.unmodifiableMap(encodedValuesMap);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        return XssHelper.stripXss(values);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return XssHelper.stripXss(value);
    }

}
