package com.dlsc.jfxcentral.util;

import com.dlsc.jfxcentral.data.model.ModelObject;

public class DeveloperTool extends ModelObject {

    private final Tool tool;

    public enum Tool {
        CSS_DOCS,
        IKONLI,
        CSS_SHOWCASE
    }

    public DeveloperTool(String name, Tool tool) {
        setName(name);
        this.tool = tool;
        setId(tool.name().toLowerCase().replace("_", "-"));
        setSummary(createDescription());
    }

    public Tool getTool() {
        return tool;
    }

    private String createDescription() {
        switch (getTool()) {
            case CSS_DOCS:
                return "The official JavaFX CSS reference documentation.";
            case IKONLI:
                return "Icon fonts gallery. Thousands of icons.";
            case CSS_SHOWCASE:
                return "Test suite for custom stylesheets.";
            default:
                return "";
        }
    }
}
