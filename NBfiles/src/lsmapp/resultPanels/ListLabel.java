
package lsmapp.resultPanels;

/**
 *
 * @author grzes
 */
public class ListLabel
{

    public ListLabel(int id, String label)
    {
        this.id = id;
        this.label = label;
    }

    @Override
    public String toString()
    {
        return label;
    }

    public int getId()
    {
        return id;
    }

    public String getLabel()
    {
        return label;
    }
 
    final int id;
    final String label;
}