package com.dlsc.jfxcentral.views.ikonli;

import com.dlsc.gemsfx.SearchField;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import org.kordamp.ikonli.Ikon;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class IkonSearchField extends SearchField<Ikon> {

    public IkonSearchField() {
        iconSet.addListener(it -> updateField());
        setHidePopupWithSingleChoice(true);
    }

    private void updateField() {
        EnumSet<? extends Ikon> iconSet = getIconSet();
        setSuggestionProvider(request -> iconSet.stream().filter(ikon -> ikon.getDescription().toLowerCase().contains(request.getUserText().toLowerCase())).collect(Collectors.toList()));
        setMatcher((ikon, text) -> ikon.getDescription().toLowerCase().startsWith(text.toLowerCase()));
        setComparator(Comparator.comparing(Ikon::getDescription));
        setConverter(new StringConverter<>() {
            @Override
            public String toString(Ikon ikon) {
                if (ikon != null) {
                    return ikon.getDescription();
                }
                return "";
            }

            @Override
            public Ikon fromString(String s) {
                return null;
            }
        });
    }

    private final ObjectProperty<EnumSet<? extends Ikon>> iconSet = new SimpleObjectProperty<>(this, "iconSet");

    public EnumSet<? extends Ikon> getIconSet() {
        return iconSet.get();
    }

    public ObjectProperty<EnumSet<? extends Ikon>> iconSetProperty() {
        return iconSet;
    }

    public void setIconSet(EnumSet<? extends Ikon> iconSet) {
        this.iconSet.set(iconSet);
    }
}
