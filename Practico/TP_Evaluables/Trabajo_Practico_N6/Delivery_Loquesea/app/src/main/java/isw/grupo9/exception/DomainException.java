package isw.grupo9.exception;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Hashtable;
import java.util.Map;

public class DomainException extends RuntimeException{

    private Map<String, String> causes;

    public DomainException(){
        super();
        causes = new Hashtable<>();
    }

    public void addCause(String key, String cause){
        causes.put(key, cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getCause(String key){
        return causes.getOrDefault(key, "");
    }

    public Map<String, String> getCauses(){
        return causes;
    }

}
