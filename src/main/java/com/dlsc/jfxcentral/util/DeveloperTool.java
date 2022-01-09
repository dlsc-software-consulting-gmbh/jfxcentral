package com.dlsc.jfxcentral.util;

import com.dlsc.jfxcentral.data.model.ModelObject;

public class DeveloperTool extends ModelObject {

    private final Tool tool;

    public enum Tool {
        CSS_DOCS,
        IKONLI
    }

    public DeveloperTool(String name, Tool tool) {
        setName(name);
        this.tool = tool;
    }

    public Tool getTool() {
        return tool;
    }
}
