package com.funny.framework.codegen.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class CodegenParam implements Serializable {
    private String groupId;
    private String artifactId;
    private String version;
    private String packageName;
    private String description;
    private String springbootVersion;
    private List<String> components;
    private String appType;
}
