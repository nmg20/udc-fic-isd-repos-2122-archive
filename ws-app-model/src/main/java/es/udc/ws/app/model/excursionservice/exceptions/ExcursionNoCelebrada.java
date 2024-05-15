package es.udc.ws.app.model.excursionservice.exceptions;

public class ExcursionNoCelebrada extends Throwable{

    private Long excursionId;
    public ExcursionNoCelebrada(Long excursionId) {
        super("La excursion con id= \"" + excursionId + "\n aun no se ha celebrado");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId){
        this.excursionId = excursionId;
    }
}
