package de.chaintracker.events;


import de.chaintracker.entity.User;

import java.time.OffsetDateTime;

public class UserCreated
{

    public String eventType = "UserCreate";

    public String userKey;

    public String email;

    public String firstName;

    public String lastName;

    public String userName;

    public String qrCode;
}
