package genesis;

public enum ResponseType {

    GREETING, FAREWELL, VALUE, LAUGH, QUESTION, QUERRY, QUERRY2, SAVEDSTRING, MOST_RELEVANT_DATA, WHO_RESPONSE, WHAT_RESPONSE;

    public String toString() {
        return name().toLowerCase().replaceFirst(name().substring(0, 1).toLowerCase(), name().substring(0, 1).toUpperCase());
    }

    public static ResponseType getResponseType(String name) {
        for (ResponseType r : values()) {
            if(r.name().equalsIgnoreCase(name))
                return r;
        }
        return null;
    }

}