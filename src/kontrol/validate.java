
package kontrol;


public class validate {
    String errorMsg = "";
    
    
    public boolean validatePasien(){
        
        
        return false;
    }
    
    public boolean validateInput(String a, String b, String c, String d, String e ,String f, String g){
        if("".equals(a) || "".equals(b) || "".equals(c) || "".equals(d) || "".equals(e) || "".equals(f) || "".equals(g)){
            errorMsg += "Harap isi form dengan benar !";
            return false;
        }else if(a.length() < 3){
            errorMsg += "No Rekam Medis Kurang dari 3 karakter";
            return false;
        }
        else{
            return true;
        }
    }
    
    public boolean validateCombo(){
        return false;
    }
    
    public boolean validateDate(){
        return false;
    }
    
    public String showMessage(){
        return errorMsg;
    }
}
