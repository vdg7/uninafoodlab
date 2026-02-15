package it.uninafoodlab.model.enums;

public enum TipoModifica {
    
    CAMBIO_DATA("Cambio data"),
    CAMBIO_ORA("Cambio ora"),
    CANCELLAZIONE("Cancellazione"),
    ALTRO("Altro");
    
    private final String displayName;
    
    /**
     * @param displayName nome leggibile del tipo modifica
     */
    TipoModifica(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * @return nome da visualizzare nell'interfaccia
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * @param dbValue valore stringa dal database (es: "CAMBIO_DATA")
     * @return enum TipoModifica corrispondente
     * @throws IllegalArgumentException se il valore non corrisponde a nessun tipo
     */
    public static TipoModifica fromString(String dbValue) {
        try {
            return TipoModifica.valueOf(dbValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo modifica non valido: " + dbValue);
        }
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}