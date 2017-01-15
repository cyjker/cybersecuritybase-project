package sec.project.controller;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Message;
import sec.project.repository.MessageRepository;
import sec.project.repository.AccountRepository;


@Controller
public class SecretController {

    // Open connection to a database
    private final String databaseAddress = "jdbc:h2:file:./database";
    private Connection connection;
    
    @PostConstruct
    public void init() throws Exception {
        connection = DriverManager.getConnection(databaseAddress, "sa", "");
        
        try {
            // If database has not yet been created, insert content
            RunScript.execute(connection, new FileReader("sql/database-schema.sql"));
            RunScript.execute(connection, new FileReader("sql/database-import.sql"));
        } catch (Throwable t) {
            System.err.println(t.getMessage());
        }
    }
    
    @RequestMapping(value = "/secret", method = RequestMethod.GET)
    public String showsecrets(Authentication authentication, Model model) throws Exception {
        
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Secret");
        
        List<String> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        
        String id;
        String name;
        
        while (resultSet.next()) {
            id = resultSet.getString("id");
            ids.add(id);
            
            name = resultSet.getString("name");
            names.add(name);
        }
        
        model.addAttribute("ids", ids);
        model.addAttribute("names", names);
        
        return "secret";
    }
       
    @RequestMapping(value = "/secret", method = RequestMethod.POST)
    public String submitForm(Authentication authentication, @RequestParam String secret) throws Exception {
        String query = "INSERT INTO Secret (name) VALUES ('" + secret + "')";
        
        connection.createStatement().executeUpdate(query);
        
        return "redirect:/secret";
    }
}
