package entity;

import java.io.Serializable;

public class SearchEntity implements Serializable {
    String name;
    String firstChar;

    @Override
    public String toString() {
        return "SearchEntity{" +
                "name='" + name + '\'' +
                ", firstChar='" + firstChar + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }
}
