package entity;

import java.io.Serializable;

public class SearchEntity implements Serializable {
    String name;

    String firstChar;

    String specification_status;

    public String getSpecification_status() {
        return specification_status;
    }

    public void setSpecification_status(String specification_status) {
        this.specification_status = specification_status;
    }

    @Override
    public String toString() {
        return "SearchEntity{" +
                "name='" + name + '\'' +
                ", firstChar='" + firstChar + '\'' +
                ", specification_status='" + specification_status + '\'' +
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
