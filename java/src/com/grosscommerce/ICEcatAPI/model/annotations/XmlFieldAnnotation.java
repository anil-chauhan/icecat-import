/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package gc.ICEcatAPI.model.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * @author Anykey Skovorodkin
 */
@Retention(value=RetentionPolicy.RUNTIME)
public @interface XmlFieldAnnotation
{
    XmlNodeType nodeType() default XmlNodeType.XmlAttribute;
    ValueTypeEnum valueType();
    String propertyName();
    boolean nullable() default false;
}
