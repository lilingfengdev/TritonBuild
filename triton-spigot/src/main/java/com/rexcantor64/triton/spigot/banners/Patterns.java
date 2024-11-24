package com.rexcantor64.triton.spigot.banners;

import lombok.Getter;
import lombok.val;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.banner.PatternType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

@Getter
public enum Patterns {
    BASE('a', "BASE"),
    BL('b', "SQUARE_BOTTOM_LEFT"),
    BO('c', "BORDER"),
    BR('d', "SQUARE_BOTTOM_RIGHT"),
    BRI('e', "BRICKS"),
    BS('f', "STRIPE_BOTTOM"),
    BT('g', "TRIANGLE_BOTTOM"),
    BTS('h', "TRIANGLES_BOTTOM"),
    CBO('i', "CURLY_BORDER"),
    CR('j', "CROSS"),
    CRE('k', "CREEPER"),
    CS('l', "STRIPE_CENTER"),
    DLS('m', "STRIPE_DOWNLEFT"),
    DRS('n', "STRIPE_DOWNRIGHT"),
    FLO('o', "FLOWER"),
    GRA('p', "GRADIENT"),
    HH('q', "HALF_HORIZONTAL"),
    LD('r', "DIAGONAL_LEFT"),
    LS('s', "STRIPE_LEFT"),
    MC('t', "CIRCLE", "CIRCLE_MIDDLE"),
    MOJ('u', "MOJANG"),
    MR('v', "RHOMBUS", "RHOMBUS_MIDDLE"),
    MS('w', "STRIPE_MIDDLE"),
    RD('x', "DIAGONAL_RIGHT"),
    RS('y', "STRIPE_RIGHT"),
    SC('z', "STRAIGHT_CROSS"),
    SKU('A', "SKULL"),
    SS('B', "SMALL_STRIPES", "STRIPE_SMALL"),
    TL('C', "SQUARE_TOP_LEFT"),
    TR('D', "SQUARE_TOP_RIGHT"),
    TS('E', "STRIPE_TOP"),
    TT('F', "TRIANGLE_TOP"),
    TTS('G', "TRIANGLES_TOP"),
    VH('H', "HALF_VERTICAL"),
    LUD('I', "DIAGONAL_UP_LEFT", "DIAGONAL_LEFT_MIRROR"),
    RUD('J', "DIAGONAL_UP_RIGHT", "DIAGONAL_RIGHT_MIRROR"),
    GRU('K', "GRADIENT_UP"),
    HHB('L', "HALF_HORIZONTAL_BOTTOM", "HALF_HORIZONTAL_MIRROR"),
    VHR('M', "HALF_VERTICAL_RIGHT", "HALF_VERTICAL_MIRROR");

    private final char code;
    private final String[] typeAliases;

    private final static @Nullable Method valueOfMethod;

    static {
        @Nullable Method method;
        try {
            method = PatternType.class.getMethod("valueOf", String.class);
        } catch (NoSuchMethodException e) {
            method = null;
        }
        valueOfMethod = method;
    }

    Patterns(char code, String... typeAliases) {
        this.code = code;
        this.typeAliases = typeAliases;
    }

    public static Patterns getByCode(char code) {
        for (Patterns p : values())
            if (p.getCode() == code)
                return p;
        return null;
    }

    public PatternType toPatternType() {
        try {
            // For 1.21.1 and above
            return toPatternTypeFromRegistry();
        } catch (NoClassDefFoundError | NoSuchMethodError | NoSuchFieldError ignore) {
            // BANNER_PATTERN Registry does not exist in this Spigot version
        }
        // Fallback to enum's valueOf
        return toPatternTypeFromEnum();
    }

    /**
     * Gets the pattern type from Bukkit's registry.
     * This is the correct way to get pattern types since Spigot 1.21.1.
     *
     * @return The Bukkit PatternType that corresponds to this type.
     */
    private PatternType toPatternTypeFromRegistry() {
        for (String alias : this.typeAliases) {
            val type = Registry.BANNER_PATTERN.get(NamespacedKey.fromString(alias.toLowerCase(Locale.ROOT)));
            if (type != null) {
                return type;
            }
        }
        throw new IllegalArgumentException("Cannot find corresponding pattern type to " + Arrays.toString(this.typeAliases));
    }

    /**
     * Gets the pattern type from the PatternType enum itself.
     * This only works up to Spigot 1.21.0.
     *
     * @return The Bukkit PatternType that corresponds to this type.
     */
    private PatternType toPatternTypeFromEnum() {
        for (String alias : this.typeAliases) {
            try {
                return (PatternType) valueOfMethod.invoke(null, alias);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ignore) {
            }
        }
        throw new IllegalArgumentException("Cannot find corresponding pattern type to " + Arrays.toString(this.typeAliases));
    }

}
