package org.learnings.application_name.model;

import java.util.Date;
import java.util.UUID;

public interface RentedMovie {
    public UUID clientID();

    public UUID movieID();

    public int timesRented();

    public Date dateRented();
}
