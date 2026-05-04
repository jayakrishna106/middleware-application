package com.middleware.transformation_service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformationService {
    private static final Pattern XML_ONE_FIELD = Pattern.compile("<root>\\s*<([a-zA-Z0-9_\\-]+)>(.*?)</\\1>\\s*</root>");
    private static final Pattern JSON_ONE_FIELD = Pattern.compile("\\{\\s*\"([a-zA-Z0-9_\\-]+)\"\\s*:\\s*\"(.*?)\"\\s*}");

    public String xmlToJson(String xml) {
        Matcher m = XML_ONE_FIELD.matcher(xml);
        if (!m.matches()) {
            throw new IllegalArgumentException("Unsupported XML format. Expected <root><key>value</key></root>");
        }
        return "{\"" + escape(m.group(1)) + "\":\"" + escape(m.group(2)) + "\"}";
    }

    public String jsonToXml(String json) {
        Matcher m = JSON_ONE_FIELD.matcher(json);
        if (!m.matches()) {
            throw new IllegalArgumentException("Unsupported JSON format. Expected {\"key\":\"value\"}");
        }
        return "<root><" + m.group(1) + ">" + unescape(m.group(2)) + "</" + m.group(1) + "></root>";
    }

    public String applyMustache(String template, String key, String value) {
        return template.replace("{{" + key + "}}", value);
    }

    public String applyFtl(String template, String key, String value) {
        return template.replace("${" + key + "}", value);
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String unescape(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
