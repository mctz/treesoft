package org.springframework.base.system.utils.security;

import java.beans.PropertyEditorSupport;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/security/StringEscapeEditor.class */
public class StringEscapeEditor extends PropertyEditorSupport {
    private boolean escapeHTML;
    private boolean escapeJavaScript;

    public StringEscapeEditor() {
    }

    public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript) {
        this.escapeHTML = escapeHTML;
        this.escapeJavaScript = escapeJavaScript;
    }

    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }

    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            setValue(null);
            return;
        }
        String value = text;
        if (this.escapeHTML) {
            value = HtmlUtils.htmlEscape(value);
        }
        if (this.escapeJavaScript) {
            value = JavaScriptUtils.javaScriptEscape(value);
        }
        setValue(value);
    }
}
