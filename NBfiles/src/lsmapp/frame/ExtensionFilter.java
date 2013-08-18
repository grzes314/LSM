
package lsmapp.frame;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Grzegorz Los
 */
public class ExtensionFilter extends FileFilter
{
    public ExtensionFilter(String extention)
    {
        extentions = new String[1];
        extentions[0] = extention;
    }
    
    public ExtensionFilter(String[] extentions)
    {
        this.extentions = extentions;
    }
    
    @Override
    public boolean accept(File f) 
    {
        if (f.isDirectory())
            return true;
        return isIn(getExtension(f));
    }
        
    public static String getExtension(File f)
    {
        String ext = null;
        String name = f.getName();
        int i = name.lastIndexOf('.');
 
        if (i > 0 &&  i < name.length() - 1) {
            ext = name.substring(i+1).toLowerCase();
        }
        return ext;
    }
        
    private boolean isIn(String str)
    {
        if (str == null)
            return false;
        for (String ext: extentions)
            if (str.equals(ext))
                return true;
        return false;
    }
 
    @Override
    public String getDescription()
    {
        String res = extentions[0];
        for (int i = 1; i < extentions.length; ++i)
            res += ", " + extentions[i];
        return res;
    }
    
    String[] extentions;
}