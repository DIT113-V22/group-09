package apitest;

import org.opentest4j.AssertionFailedError;

public class TestResult {
    private boolean success;
    private AssertionFailedError e;

    public void setSuccess(boolean success){
        this.success = success;
    }

    public boolean isSuccess(){
        return success;
    }
    public void setException(AssertionFailedError e){
        this.e = e;
    }
    public AssertionFailedError getException(){
        return e;
    }
}
