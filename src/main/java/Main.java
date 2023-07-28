import com.weather.dao.ISessionDAO;
import com.weather.model.User;

public class Main {
    public static void main(String[] args) {
        User user = new User();
        user.setId(1L);
        ISessionDAO ISessionDAO = new ISessionDAO();
        boolean sessionExpired = ISessionDAO.isSessionExpiredForUser(user);
        System.out.println(sessionExpired);
    }
}
