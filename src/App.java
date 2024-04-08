import business.UserManager;
import core.Database;
import core.Helper;
import view.AdminView;
import view.LoginView;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        Helper.setTheme();
        Connection con = Database.getInstance();

        LoginView loginView = new LoginView();
        UserManager userManager = new UserManager();
    }
}
