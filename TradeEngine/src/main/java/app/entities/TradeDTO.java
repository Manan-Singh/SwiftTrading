package app.entities;

import javax.xml.bind.annotation.XmlElement;

public class TradeDTO extends Order {

    public TradeDTO() {}

    @XmlElement(name = "result")
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
