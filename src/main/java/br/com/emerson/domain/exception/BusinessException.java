package br.com.emerson.domain.exception;

public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }

    public static BusinessException error(){
        throw new BusinessException("Error during execution");
    }

    public static BusinessException notFound() {
        throw new BusinessException("Recurso ou funcionalidade nao encontrada!");
    }

    public static  BusinessException repeatData() {
        throw new BusinessException("Os dados que voce esta inserindo ja existem na base de conhecimento!");
    }

}
