
package models;

/**
 *
 * @author Grzegorz Los
 */
public class WrongParamException extends RuntimeException
{
    public WrongParamException() {}
    public WrongParamException(String mssg) {
        super(mssg);
    }

}
