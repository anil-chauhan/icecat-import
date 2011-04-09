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
 * Used for marks localized value fields.
 * @author Anykey Skovorodkin
 */
@Retention(value=RetentionPolicy.RUNTIME)
public @interface XmlLocalizedFieldAnnotation
{
    String nodeName();
    String valueAttrName() default "Value";
    String langIdAttrName() default "langid";
}