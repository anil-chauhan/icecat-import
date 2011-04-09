/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce.ICEcat.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Element;

/**
 * Used for storing all categories.
 * @author Anykey Skovorodkin
 */
public class Categories extends XmlObjectsListBase<Category>
{

    public static final String ROOT_NODE_NAME = "CategoriesList";
    private HashMap<Integer, Category> categories = new HashMap<Integer, Category>();
    private int minLevel = Integer.MAX_VALUE;
    private int maxLevel = Integer.MIN_VALUE;

    public Categories()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getMaxLevel()
    {
        return maxLevel;
    }

    public int getMinLevel()
    {
        return minLevel;
    }
    // </editor-fold>

    /**
     * Used for getting visible category by id.
     * @param id
     * @param visible
     * @return
     */
    public Category getById(int id, boolean visible)
    {
        Category cat = this.categories.get(id);

        if (visible && cat != null && !cat.isVisible())
        {
            return this.getById(cat.getParentCategoryId(), visible);
        }

        return cat;
    }

    public Category getById(int id)
    {
        return this.getById(id, false);
    }

    public void checkLevels()
    {
        Category[] allCategories = this.categories.values().toArray(
                new Category[0]);

        for (Category c : allCategories)
        {
            if (c.getLevel() < 0)
            {
                c.setLevel(this.obtainCategoryLevel(c));
            }

            this.maxLevel = Math.max(maxLevel, c.getLevel());
            this.minLevel = Math.min(minLevel, c.getLevel());
        }
    }

    public List<Category> getCategories(int level)
    {
        ArrayList<Category> result = new ArrayList<Category>();

        Category[] allCategories = this.categories.values().toArray(
                new Category[0]);

        for (Category c : allCategories)
        {
            if (c.getLevel() == level)
            {
                result.add(c);
            }
        }

        return result;
    }

    private int obtainCategoryLevel(Category cat)
    {
        int level = 0;

        Category parent = cat;
        while ((parent = this.getById(parent.getParentCategoryId(), false)) != null && parent.getId() != parent.getParentCategoryId())
        {
            if (parent.getLevel() >= 0)
            {
                return parent.getLevel() + level + 1;
            }
            else
            {
                level++;
            }
        }

        return level;
    }

    // <editor-fold defaultstate="collapsed" desc="XmlObjectsListBase">
    @Override
    protected String getChildNodesName()
    {
        return Category.ROOT_NODE_NAME;
    }

    @Override
    protected Category loadFromElement(Element objElement) throws Throwable
    {
        Category category = new Category();
        if (category.parseFromElement(objElement))
        {
            return category;
        }

        return null;
    }

    @Override
    public Category[] getAll()
    {
        return this.categories.values().toArray(new Category[0]);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase implementation">
    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="overrides">
    @Override
    public void saveObject(Category object)
    {
        this.categories.put(object.getId(), object);
    }

    @Override
    protected boolean parseFromElementInternal(Element thisObjectElement)
    {
        if (super.parseFromElementInternal(thisObjectElement))
        {
            this.checkLevels();
            return true;
        }
        else
        {
            this.categories.clear();
            return false;
        }
    }

    // </editor-fold>
    public Category getParentCategory(Category category, int level)
    {
        if (category.getLevel() == level)
        {
            return category;
        }

        Category parentCategory = this.getById(category.getParentCategoryId());

        while (parentCategory != null)
        {
            if (parentCategory.getLevel() == level)
            {
                return parentCategory;
            }

            parentCategory = this.getById(parentCategory.getParentCategoryId());
        }

        return null;
    }
}
