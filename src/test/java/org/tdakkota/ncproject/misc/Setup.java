package org.tdakkota.ncproject.misc;

import org.tdakkota.ncproject.entities.Area;
import org.tdakkota.ncproject.entities.Status;
import org.tdakkota.ncproject.entities.User;

public class Setup {
    private User user;
    private Area area;
    private Status status;

    public void create() {
        createUser();
        createArea();
        createStatus();
    }

    private void createUser() {
        User user = new User();
        user.username = "testuser";
        user.name = "testuser";
        user.setEncryptedPassword("testuser");
        user.persist();

        this.user = user;
    }

    private void createArea() {
        Area area = new Area();
        area.name = "testarea";
        area.persist();

        this.area = area;
    }

    private void createStatus() {
        Status status = new Status();
        status.name = "teststatus";
        status.persist();

        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public Area getArea() {
        return area;
    }

    public Status getStatus() {
        return status;
    }
}
