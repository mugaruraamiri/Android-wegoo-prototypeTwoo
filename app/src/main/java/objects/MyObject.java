package objects;

import java.io.Serializable;

import driver.UsbSerialPort;

/**
 * Created by amiri on 4/6/16.
 */
public class MyObject implements Serializable {


    private static final long serialVersionUID = 1L;
    String name, pId, id;

    public MyObject() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }


    public String getpId() {
        return pId;
    }

    public String getId() {
        return id;
    }

}
