
package finance.models;

/**
 *
 * @author Grzegorz Los
 */
public class WrongParamException extends IllegalArgumentException
{
    public WrongParamException() {}
    public WrongParamException(String mssg) {
        super(mssg);
    }

}
