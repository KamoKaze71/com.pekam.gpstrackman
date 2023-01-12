package com.pekam.gpstrackman.events;

import pekam.entities.TblUser;

public class UserEvent {
    public TblUser mUser;

    public UserEvent(TblUser user) {
        mUser = user;
     }

    public TblUser getUser() {
        return mUser;
    }
}
