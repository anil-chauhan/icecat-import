/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.configfile;

import com.grosscommerce.ICEcat.controller.ImportContext;
import com.grosscommerce.emission.framework.importer.Token;
import com.grosscommerce.ICEcat.model.Category;
import com.grosscommerce.ICEcat.model.Language;
import com.grosscommerce.ICEcat.model.Languages;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anykey Skovorodkin
 */
public class ConfigFileConsoleWizard
{
    public ConfigFileConsoleWizard()
    {

    }

    public ConfigFile buildConfigFile() throws Throwable
    {
        System.out.println("******************* Config File Wizard ****************");

        ConfigFile configFile = new ConfigFile();

        System.out.println("******************* ICEcat settings ****************");

        this.fillAuthParams(configFile.getiCECatSettings());

        ImportContext importContext = new ImportContext();
        importContext.init(configFile.getiCECatSettings().getUserName(),
                configFile.getiCECatSettings().getPassword());

        this.specifyLanguages(configFile, importContext);

        System.out.println("******************* Importer settings ****************");

        this.fillAuthParams(configFile.getImporterSettings());

        this.specifyTokens(configFile, importContext);

        System.out.println("Building config file...");
        
        return configFile;
    }

    private void fillAuthParams(SettingsBase settingsBase)
    {
        System.out.print("User name: ");
        settingsBase.setUserName(this.readNotEmptyConsoleValue());
        System.out.print("Pasword: ");
        settingsBase.setPassword(this.readNotEmptyConsoleValue());
    }

    private String readConsoleValue()
    {
        Scanner scanner = new Scanner(System.in);
        String value = scanner.nextLine();
        return value;
    }

    private String readNotEmptyConsoleValue()
    {
        Scanner scanner = new Scanner(System.in);
        String value = null;
        int effortCount = 0;
        while(true)
        {
            value = scanner.nextLine();

            if(!value.isEmpty())
            {
                return value;
            }
            else if(effortCount < 3)
            {
                System.out.println("Value can't be empty!");
                System.out.print("Try again: ");
                effortCount++;
            }
            else
            {
                throw new IllegalArgumentException("This is not a toy... ass...");
            }
        }
    }

    private void specifyLanguages(ConfigFile configFile, ImportContext importContext)
    {
        Languages languageList = importContext.getLanguages();

        System.out.println("Possible languages:");

        for(Language language : languageList.getAll())
        {
            System.out.println(" " + language.getShortCode());
        }

        Language selectedlanguage = null;

        while(selectedlanguage == null)
        {
            System.out.print("Enter language: ");
            String shortCode = this.readNotEmptyConsoleValue();

            selectedlanguage = languageList.getLanguageByShortCode(shortCode);

            if(selectedlanguage == null)
            {
                System.out.println("Please, enter correct language short code!");
            }
        }

        configFile.setLanguageShortCode(selectedlanguage.getShortCode());
        importContext.setImportLanguage(selectedlanguage);
    }

    private void specifyTokens(ConfigFile configFile, ImportContext importContext)
    {
        List<Category> categories = importContext.getCategories().getCategories(0);
        
        Language lang = importContext.getImportLanguage();

        for(Category category : categories)
        {
            String categoryName = category.getName(lang.getId());
            if(categoryName == null || categoryName.isEmpty())
            {
                Logger.getLogger(ConfigFileConsoleWizard.class.getName()).log(
                        Level.INFO, "Empty category name, will be skipped. Id: {0}", category.getId());
                continue;
            }

            System.out.print("Enter token for category: " + categoryName + " : ");
            String tokenValue = this.readConsoleValue();

            Token token = new Token();
            token.setCatId(category.getId());
            token.setCatName(categoryName);
            token.setValue(tokenValue);

            configFile.getImporterSettings().getTokens().add(token);
        }
    }
}
