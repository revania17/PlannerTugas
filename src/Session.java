public class Session { //menyimpan id_user dari pengguna yang sedang login
    private static int currentUserId;

    public static void setCurrentUserId(int id) {
        currentUserId = id;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }
}
