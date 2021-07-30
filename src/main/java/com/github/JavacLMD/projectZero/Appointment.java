package com.github.JavacLMD.projectZero;

import java.sql.Date;
import java.sql.Time;

public class Appointment {

    private Date scheduledDate;
    private Time scheduledTime;
    private Integer customerID; //scheduled customer id
    private Integer petID; //scheduled pet id associated with customer

    private String description; //description of what was done at the appointment

}
