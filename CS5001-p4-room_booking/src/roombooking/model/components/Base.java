package model.components;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Base implements Serializable {
    public final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
}
