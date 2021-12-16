package com.dlsc.jfxcentral.util;

import javafx.application.Application;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.converter.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CSSParsingApp extends Application {

    @Override
    public void start(Stage primaryStage) {
//        Scene scene = new Scene(root, 300, 250);
//        scene.getStylesheets().add(getClass().getResource("modena.css").toExternalForm());
//
//        primaryStage.setScene(scene);
//        primaryStage.show();

        System.out.println("------------------- Text Area -------------------");
        dumpStylingSupport(TextArea.class);
        System.out.println("------------------- Button -------------------");
        dumpStylingSupport(Button.class);
        System.out.println("------------------- Label -------------------");
        dumpStylingSupport(Label.class);
    }

    private void dumpStylingSupport(Class styleable) {
        List<Class> hierarchy = new ArrayList<>();
        hierarchy.add(styleable);
        styleable = styleable.getSuperclass();
        do {
            hierarchy.add(styleable);
            styleable = styleable.getSuperclass();
        } while (styleable != null);

        HashSet<CssMetaData> visited = new HashSet<>();

        for (int i = hierarchy.size() - 1; i >= 0; i--) {
            try {
                Class clazz = hierarchy.get(i);
                if (Node.class.isAssignableFrom(clazz)) {
                    dumpStylingSupport(clazz, visited);
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void dumpStylingSupport(Class clazz, Set<CssMetaData> visited) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        try {
            Method m = clazz.getDeclaredMethod("getClassCssMetaData");
            m.setAccessible(true); // if security settings allow this
            List<CssMetaData<? extends Styleable, ?>> cssMetaData = (List<CssMetaData<? extends Styleable, ?>>) m.invoke(null);

            for (CssMetaData<? extends Styleable, ?> s : cssMetaData) {
                if (!visited.contains(s)) {
                    visited.add(s);

                    String possibleValues = "";
                    StyleConverter<?, ?> converter = s.getConverter();
                    if (converter instanceof EnumConverter) {
                        EnumConverter enumConverter = (EnumConverter) converter;
                        Field field = EnumConverter.class.getDeclaredField("enumClass");
                        field.setAccessible(true);
                        Class<Enum> fieldEnumClass = (Class<Enum>) field.get(enumConverter);
                        possibleValues = Arrays.toString(getEnumValues(fieldEnumClass));
                    } else if (converter instanceof BooleanConverter) {
                        possibleValues = "[true | false]";
                    } else if (converter instanceof SizeConverter) {
                        possibleValues = "size in pixels or em";
                    } else if (converter instanceof StringConverter) {
                        possibleValues = "text";
                    } else if (converter instanceof ShapeConverter) {
                        possibleValues = "shape / svg";
                    } else if (converter instanceof InsetsConverter) {
                        possibleValues = "Insets, e.g. 5px 10px 5px 10px or 1em 2em 1em 2em";
                    } else {
                        possibleValues = converter.toString();
                    }

                    System.out.println("stylable: " + clazz.getSimpleName() + ", property name: " + s.getProperty() + ", values: " + possibleValues);
                }
            }
        } catch (NoSuchMethodException ex) {
        }
    }

    private <E extends Enum> E[] getEnumValues(Class<E> enumClass) throws NoSuchFieldException, IllegalAccessException {
        Field f = enumClass.getDeclaredField("$VALUES");
        f.setAccessible(true);
        Object o = f.get(null);
        return (E[]) o;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}