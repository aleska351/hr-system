package com.codingdrama.hrsystem.service.email.context;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public abstract class AbstractEmailContext {

    private String from;
    private String to;
    private String subject;
    private String email;

    private String attachment;
    private String fromDisplayName;
    private String emailLanguage;
    private String displayName;
    private String templateLocation;
    private Map<String, Object> context;


    public AbstractEmailContext() {
        this.context = new HashMap<>();
    }

    public <T> void init(T context){
    }

    public Object put(String key, Object value) {
        return Objects.isNull(key) ? null : this.context.put(key.intern(),value);
    }
}
