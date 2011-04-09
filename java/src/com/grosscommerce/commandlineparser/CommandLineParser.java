/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.commandlineparser;

import com.grosscommerce.ICEcatAPI.common.Constants;
import com.grosscommerce.ICEcatAPI.controller.ImportType;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Used for parsing command line's arguments
 * @author Anykey Skovorodkin
 */
public class CommandLineParser
{
    private JobTypeEnum jobType;
    public static final String CONFIG_FILE_ARGUMENT = "-configFile";
    public static final String FULL_IMPORT_TYPE     = "-full";
    public static final String UPDATE_IMPORT_TYPE   = "-update";

    private File configFilePath = null;
    private ImportType importType = ImportType.Full;

    public CommandLineParser()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public JobTypeEnum getJobType()
    {
        return jobType;
    }

    public File getConfigFilePath()
    {
        return configFilePath;
    }

    public ImportType getImportType()
    {
        return importType;
    }
    
    // </editor-fold>

    public void parseArguments(String[] cmdArgs) throws IllegalArgumentException
    {
        if(cmdArgs.length < 1)
        {
            // will be two arguments: job type and config file path
            throw new IllegalArgumentException("Should be more than one argument");
        }

        // parse job type
        this.jobType = JobTypeEnum.valueOfIgnoreCase(cmdArgs[0]);

        switch(this.jobType)
        {
            case CreateConfig:
                this.parseAgrumentsForConfigCreation(Arrays.asList(cmdArgs));
                break;
                
            case ImportCatalog:
                this.parseAgrumentsForImport(Arrays.asList(cmdArgs));
                break;
                
            case Help:
                break; // nothing to do

            default:
                throw new IllegalArgumentException("Job type: " + this.jobType + " is not implemented yet");
        }
    }

    private void parseAgrumentsForImport(List<String> cmdArgs)
    {
        if(cmdArgs.contains(FULL_IMPORT_TYPE))
        {
            this.importType = ImportType.Full;
        }
        else if(cmdArgs.contains(UPDATE_IMPORT_TYPE))
        {
            this.importType = ImportType.Update;
        }

        this.configFilePath = this.getConfigFileArg(cmdArgs);
    }

    private File getConfigFileArg(List<String> cmdArgs)
    {
        if(cmdArgs.contains(CONFIG_FILE_ARGUMENT))
        {
            int index = cmdArgs.indexOf(CONFIG_FILE_ARGUMENT);

            if(index == cmdArgs.size() - 1)
            {
                throw new IllegalArgumentException("Please, specify file path. For example: ... " + CONFIG_FILE_ARGUMENT + " \"/home/config.xml\"");
            }
            else
            {
                return new File(cmdArgs.get(index + 1));
            }
        }
        else
        {
            Logger.getLogger("Config file is not specified, so we will use user.dir.");
            
            return new File(System.getProperty("user.dir"),
                    Constants.DEFAULT_CONFIG_FILE_NAME);
        }
    }

    private void parseAgrumentsForConfigCreation(List<String> cmdArgs)
    {
        this.configFilePath = this.getConfigFileArg(cmdArgs);
    }

    public static void printArgumentsInfo()
    {
        System.out.println("Arguments: ");
        System.out.println("  Job types:");
        System.out.println("      " + JobTypeEnum.Help.toString() + "   shows help information");
        System.out.println();
        System.out.println("      " + JobTypeEnum.CreateConfig.toString() + "   starts configuration wizard");
        System.out.println("             " + CONFIG_FILE_ARGUMENT + "   config file path (for example: /home/admin/icecat/config.xml)");
        System.out.println();
        System.out.println("      " + JobTypeEnum.ImportCatalog.toString() + "   schedule icecat import");
        System.out.println("             " + FULL_IMPORT_TYPE + "   schedule full catalog importing. Will be drop all products and add again!");
        System.out.println("             " + UPDATE_IMPORT_TYPE + "   schedule for update existing products.");
        System.out.println("             " + CONFIG_FILE_ARGUMENT + "   config file path (for example: /home/admin/icecat/config.xml)");
        System.out.println();
        System.out.println();

        System.out.println("Command line examples:");
        System.out.println("   Create config file: java -jar ice.jar -createConfig -configFile \"/home/admin/ice/config.xml\"");
        System.out.println("   Import full catalog: java -jar ice.jar -import -full -configFile \"/home/admin/ice/config.xml\"");
        System.out.println("   Update catalog: java -jar ice.jar -import -update -configFile \"/home/admin/ice/config.xml\"");
    }
}
