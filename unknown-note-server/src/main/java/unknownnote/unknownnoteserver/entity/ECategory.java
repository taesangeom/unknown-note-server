package unknownnote.unknownnoteserver.entity;

public enum ECategory {
    poem, whisper, novel;

    public static ECategory fromString(String value) {
        for (ECategory category : ECategory.values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
