/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package gc.commandlineparser;

/**
 *
 * @author Anykey Skovorodkin
 */
public enum JobTypeEnum
{
    CreateConfig("-createconfig"),
    ImportCatalog("-import"),
    Help("-help");

    private String value;

    private JobTypeEnum(String value)
    {
        this.value = value;
    }

    public static JobTypeEnum valueOfIgnoreCase(String strValue) throws IllegalArgumentException
    {
        JobTypeEnum[] values = JobTypeEnum.values();

        for(JobTypeEnum val : values)
        {
            if(val.value.equalsIgnoreCase(strValue))
            {
                return val;
            }
        }

        throw new IllegalArgumentException("Unknown job type: " + strValue);
    }

    @Override
    public String toString()
    {
        return this.value;
    }
}
