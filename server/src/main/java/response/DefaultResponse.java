package response;

/** The Default Response that will be displayed for many Services */
public class DefaultResponse {
    /** String used to contain the DefaultResponse Message that will be displayed for many Services */
    private String message = new String();

    public DefaultResponse(String message) {
        this.message = message;
    }
    public DefaultResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}