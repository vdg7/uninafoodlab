package it.uninafoodlab.model.enums;

public enum Categoria {
    
    TRADIZIONE("Tradizione"),
    INNOVAZIONE("Innovazione"),
    PASTICCERIA("Pasticceria"),
    CUCINA_ASIATICA("Cucina asiatica"),
    PRIMI("Primi"),
    SECONDI("Secondi"),
    MARE("Mare"),
    CARNE("Carne"),
    VEGETARIANO("Vegetariano"),
    PANIFICAZIONE("Panificazione");
    
    private final String displayName;
    
    /**
     * @param displayName nome leggibile della categoria
     */
    Categoria(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * @return nome da visualizzare nell'interfaccia
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * @param dbValue valore stringa dal database
     * @return enum Categoria corrispondente
     * @throws IllegalArgumentException se il valore non corrisponde a nessuna categoria
     */
    public static Categoria fromString(String dbValue) {
        for (Categoria cat : Categoria.values()) {
            if (cat.displayName.equalsIgnoreCase(dbValue)) {
                return cat;
            }
        }
        throw new IllegalArgumentException("Categoria non valida: " + dbValue);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}