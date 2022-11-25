package model.components;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RoomBooker {
    private University university;
    // private Map<LocalDate, List<Booking>> bookings;

    public RoomBooker(University university) {
        this.university = university;
    }

    public void save(String saveName) {
        String savePath = getSavePath();
        try {
            FileOutputStream fos = new FileOutputStream(savePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(university);
            oos.close();
        } catch (IOException e) {
            // TODO
        }
    }

    public void load(String loadName) {
        // TODO
    }

    private String getSavePath() {
        // TODO
        // direct saved files into assets/data
        return null;
    }
}
