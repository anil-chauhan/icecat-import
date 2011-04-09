/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce;

import com.grosscommerce.configfile.ConfigFile;
import com.grosscommerce.ICEcatAPI.controller.ImportContext;
import com.grosscommerce.ICEcatAPI.controller.ImportController;
import com.grosscommerce.emission.framework.importer.Token;
import com.grosscommerce.emission.framework.importer.TokenFilter;
import com.grosscommerce.emission.framework.importer.Tokens;
import com.grosscommerce.ICEcatAPI.model.Categories;
import com.grosscommerce.ICEcatAPI.model.Category;
import com.grosscommerce.ICEcatAPI.model.Language;
import com.grosscommerce.commandlineparser.CommandLineParser;
import com.grosscommerce.configfile.ConfigFileConsoleWizard;
import com.grosscommerce.utilities.LogUtility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * @author Anykey Skovorodkin
 */
class Program
{
    public static void main(String[] params)
    {
        //readXMl();

        LogUtility.initLogging();
        printProgramInfo();

        CommandLineParser cmdLineParser = new CommandLineParser();
        try
        {
            cmdLineParser.parseArguments(params);

            switch (cmdLineParser.getJobType())
            {
                case Help:
                    CommandLineParser.printArgumentsInfo();
                    break;

                case CreateConfig:
                    createConfigFile(cmdLineParser);
                    break;

                case ImportCatalog:
                    importCatalog(cmdLineParser);
                    break;
            }
        }
        catch (Throwable ex)
        {
            System.out.println("Error: " + ex.getMessage());

            CommandLineParser.printArgumentsInfo();
            return;
        }
    }

    private static void importCatalog(CommandLineParser cmdLineParser) throws IOException, IllegalAccessException, ParserConfigurationException, SAXException, Throwable
    {
        System.out.println(
                "****************** Import catalog ************************");

        ConfigFile configFile = new ConfigFile();
        configFile.loadFromFile(cmdLineParser.getConfigFilePath());

        Logger.getLogger(Process.class.getName()).info(
                "Validating config file...");

        ImportContext context = checkICECatSettings(configFile);
        context.setImportType(cmdLineParser.getImportType());
        checkImporterSettings(configFile, context);
        
        System.out.println("Start importing...");

        ImportController importController = new ImportController(context);

        for(Token token : configFile.getImporterSettings().getTokens().getAll())
        {
            importController.registerFilter(new TokenFilter(context, token,
                    configFile.getImporterSettings().getUserName(),
                    configFile.getImporterSettings().getPassword(),
                    configFile.getiCECatSettings().getParsingThreadsCount()));
        }

        importController.doImport();
    }

    private static void checkImporterSettings(ConfigFile configFile, ImportContext importContext) throws IllegalStateException, Throwable
    {
        Language language = importContext.getImportLanguage();
        
        Logger.getLogger(Process.class.getName()).info(
                "Check importer's settings...");
        Categories categoriesList = importContext.getCategories();

        Tokens tokensList = configFile.getImporterSettings().getTokens();
        Token[] tokens = tokensList.getAll();
        for (Token token : tokens)
        {
            Category category = categoriesList.getById(token.getCatId());
            if (category == null)
            {
                Logger.getLogger(Process.class.getName()).log(Level.INFO,
                                                              "Unknown category with id:{0}, this token will be skipped",
                                                              token.getCatId());
                tokensList.remove(token);
                continue;
            }
            if (token.getValue().isEmpty())
            {
                Logger.getLogger(Process.class.getName()).log(Level.INFO,
                                                              "Token for category:{0} is empty, will be skipped...",
                                                              category.getName(
                        language.getId()));
                tokensList.remove(token);
                continue;
            }
        }
        
        if (tokensList.getAll().length == 0)
        {
            Logger.getLogger(Process.class.getName()).info(
                    "There are no valid tokens...");
            throw new IllegalStateException("There are no valid tokens...");
        }
    }

    private static ImportContext checkICECatSettings(ConfigFile configFile) throws IllegalStateException, Throwable
    {
        Logger.getLogger(Process.class.getName()).info(
                "Check ICEcat parser's settings...");

        ImportContext importContext = new ImportContext();
        importContext.init(configFile.getiCECatSettings().getUserName(),
                configFile.getiCECatSettings().getPassword());

        Language language = importContext.getLanguages().getLanguageByShortCode(
                configFile.getLanguageShortCode());
        
        if (language == null)
        {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE,
                                                          "Unknown language: {0}",
                                                          configFile.getLanguageShortCode());
            throw new IllegalStateException("Bad config file");
        }

        importContext.setImportLanguage(language);

        return importContext;
    }

    private static void createConfigFile(CommandLineParser commandLineParser)
    {
        try
        {
            ConfigFileConsoleWizard consoleWizard = new ConfigFileConsoleWizard();
            ConfigFile configFile = consoleWizard.buildConfigFile();

            configFile.saveToFile(commandLineParser.getConfigFilePath());

            System.out.println(
                    "Config file was successfully created, path: " + commandLineParser.getConfigFilePath().getAbsolutePath());
        }
        catch (Throwable ex)
        {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void printProgramInfo()
    {
        System.out.println("IceCat catalog importer");
        System.out.println("Copyright: Gross Commerce 2011.");
    }
}
