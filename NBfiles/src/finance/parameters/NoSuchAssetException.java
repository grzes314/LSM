
package finance.parameters;

/**
 *
 * @author Grzegorz Los
 */
public class NoSuchAssetException extends IllegalArgumentException
{
    public NoSuchAssetException() {}
    public NoSuchAssetException(String mssg) {
        super(mssg);
    }

}
