package de.pasuki.colorful_redstone.datagen;

import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public final class ModLanguageEntries {
    private static final Map<DyeColor, String> DE_COLOR_NAMES = new EnumMap<>(DyeColor.class);

    static {
        DE_COLOR_NAMES.put(DyeColor.WHITE, "Weiss");
        DE_COLOR_NAMES.put(DyeColor.ORANGE, "Orange");
        DE_COLOR_NAMES.put(DyeColor.MAGENTA, "Magenta");
        DE_COLOR_NAMES.put(DyeColor.LIGHT_BLUE, "Hellblau");
        DE_COLOR_NAMES.put(DyeColor.YELLOW, "Gelb");
        DE_COLOR_NAMES.put(DyeColor.LIME, "Hellgruen");
        DE_COLOR_NAMES.put(DyeColor.PINK, "Rosa");
        DE_COLOR_NAMES.put(DyeColor.GRAY, "Grau");
        DE_COLOR_NAMES.put(DyeColor.LIGHT_GRAY, "Hellgrau");
        DE_COLOR_NAMES.put(DyeColor.CYAN, "Tuerkis");
        DE_COLOR_NAMES.put(DyeColor.PURPLE, "Lila");
        DE_COLOR_NAMES.put(DyeColor.BLUE, "Blau");
        DE_COLOR_NAMES.put(DyeColor.BROWN, "Braun");
        DE_COLOR_NAMES.put(DyeColor.GREEN, "Gruen");
        DE_COLOR_NAMES.put(DyeColor.RED, "Rot");
        DE_COLOR_NAMES.put(DyeColor.BLACK, "Schwarz");
    }

    private ModLanguageEntries() {
    }

    public static String englishDustName(DyeColor color) {
        return titleCase(color.getName()) + "stone Dust";
    }

    public static String germanDustName(DyeColor color) {
        return DE_COLOR_NAMES.getOrDefault(color, titleCase(color.getName())) + "stein-Staub";
    }

    public static String englishBlockName(DyeColor color) {
        return "Block of " + titleCase(color.getName()) + "stone";
    }

    public static String germanBlockName(DyeColor color) {
        return DE_COLOR_NAMES.getOrDefault(color, titleCase(color.getName())) + "stein-Block";
    }

    public static String englishTorchName(DyeColor color) {
        return titleCase(color.getName()) + "stone Torch";
    }

    public static String germanTorchName(DyeColor color) {
        return DE_COLOR_NAMES.getOrDefault(color, titleCase(color.getName())) + "stein-Fackel";
    }

    public static String englishRepeaterName(DyeColor color) {
        return titleCase(color.getName()) + "stone Repeater";
    }

    public static String germanRepeaterName(DyeColor color) {
        return DE_COLOR_NAMES.getOrDefault(color, titleCase(color.getName())) + "stein-Verstaerker";
    }

    public static String englishComparatorName(DyeColor color) {
        return titleCase(color.getName()) + "stone Comparator";
    }

    public static String germanComparatorName(DyeColor color) {
        return DE_COLOR_NAMES.getOrDefault(color, titleCase(color.getName())) + "stein-Komparator";
    }

    private static String titleCase(String text) {
        String[] parts = text.toLowerCase(Locale.ROOT).split("_");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) {
                continue;
            }
            if (i > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(parts[i].charAt(0)));
            if (parts[i].length() > 1) {
                builder.append(parts[i].substring(1));
            }
        }

        return builder.toString();
    }
}

