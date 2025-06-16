package karaoke.poprock.util;

import karaoke.poprock.model.Karyawan;

public class Session {
    private static Karyawan currentUser;

    public static void setCurrentUser(Karyawan user) {
        currentUser = user;
    }

    public static Karyawan getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
