/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Ultramilk
 */
public class loginAtribute {
    private static String username;
    private static char[] password;
    private static int role;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        loginAtribute.username = username;
    }

    public static char[] getPassword() {
        return password;
    }

    public static void setPassword(char[] password) {
        loginAtribute.password = password;
    }

    public static int getRole() {
        return role;
    }

    public static void setRole(int role) {
        loginAtribute.role = role;
    }
    
    

    
}
