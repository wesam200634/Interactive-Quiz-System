package com.quizapp.service;

import com.quizapp.model.Admin;
import com.quizapp.model.User;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages user persistence and authentication.
 */
public class UserService {
   
    private final JsonService jsonService;
    
    private final Path usersFile;
    
    private final ArrayList<User> users;

    /**
     * Constructs the user service.
     *
     * @param usersFile path to users.json
     * @throws IOException on load failure
     */
    public UserService(Path usersFile) throws IOException {
      
        this.jsonService = new JsonService();
        
        this.usersFile = usersFile;
        
        if (!java.nio.file.Files.exists(usersFile)) {
        
            seedDefaultUsers();
        
        }
        
        this.users = jsonService.loadUsers(usersFile);
    
    }

    private void seedDefaultUsers() throws IOException {
    
        List<User> defaults = new ArrayList<>();
        
        User user = new User(1, "student");
        
        user.setPassword("1234");
        
        defaults.add(user);
        
        Admin admin = new Admin(999, "admin");
        
        admin.setPassword("admin123");
        
        defaults.add(admin);
        
        jsonService.saveUsers(usersFile, defaults);
    
    }

    public ArrayList<User> getUsers() {
    
        return users;
    
    }

    
    public User authenticate(String name, String password, boolean adminRequested) {
    
        for (User user : users) {
        
            if (user.getName().equalsIgnoreCase(name)
            
                    && user.getPassword().equals(password)
                    
                    && user.isBanned() == false) {
                
                if (adminRequested && user instanceof Admin) {
                
                    return user;
                
                }
                
                if (!adminRequested && !(user instanceof Admin)) {
                
                    return user;
                
                }
            }
        }
        
        return null;
    
    }

    public void save() throws IOException {
    
        jsonService.saveUsers(usersFile, users);
    
    }

    public int nextId() {
    
        int max = 0;
        
        for (User user : users) {
        
            if (user.getId() > max) {
            
                max = user.getId();
            
            }
    
        }
        
        return max + 1;
    
    }

    public void addUser(User user) {
     
        users.add(user);
    
    }
}
