package com.example.testrailPoc.models;

public record TestRailCase(int id, String title, String reference, String type, String priority) {
    public TestRailCase {
        title = title == null ? "" : title.trim();
        reference = reference == null ? "" : reference.trim();
        type = type == null ? "" : type.trim();
        priority = priority == null ? "" : priority.trim();
    }

    public String getIdAsString() {
        return Integer.toString(id);
    }

    public String getTitle() {
        return title;
    }

    public String getReference() {
        return reference;
    }

    private  String normalizeRef(String value) {
        if (value == null) return "";
        String normalized = java.text.Normalizer.normalize(value.trim(), java.text.Normalizer.Form.NFKC);
        normalized = normalized.replace('‑', '-')
                .replace('–', '-')
                .replace('—', '-')
                .replace('−', '-')
                .replaceAll("[\\u00A0\\u200B\\uFEFF]", "")
                .replace("\"", "")
                .trim();
        return normalized;
    }
}
