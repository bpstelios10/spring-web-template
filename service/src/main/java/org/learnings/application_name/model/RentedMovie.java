package org.learnings.application_name.model;

import java.util.Date;
import java.util.UUID;

public interface RentedMovie {
    UUID clientID();

    UUID movieID();

    int timesRented();

    Date dateRented();
}
