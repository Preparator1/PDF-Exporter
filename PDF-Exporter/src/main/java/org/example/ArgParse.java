package org.example;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ArgParse {
    static boolean command = false;
    static String commandValue = "";
    static boolean input = false;
    static String inputValue = "";
    static boolean output = false;
    static String outputValue = "";
    static boolean recursive = false;

    static boolean debug = false;

    public static void parser(String[] args) {
        int argCount = 0;

        while(argCount != args.length){
            if(args[argCount].startsWith("-")) {
                switch (args[argCount]) {
                    case "-c":
                    case "--command":
                        if (argHaveValue(args, argCount)) {
                            File bartyFile = new File(args[argCount + 1]);
                            if (bartyFile.exists() && !bartyFile.isDirectory()) {
                                command = true;
                                commandValue = args[argCount + 1];
                                argCount += 2;
                            } else {
                                System.out.println("Error: Command file does not exist! (File location: " + args[argCount + 1] + ")");
                                System.exit(1);
                            }
                        } else {
                            error();
                        }
                        break;

                    case "-i":
                    case "--input":
                        if (argHaveValue(args, argCount)) {
                            Path inputPath = Paths.get(args[argCount + 1]);
                            if (Files.isDirectory(inputPath)) {
                                input = true;
                                inputValue = args[argCount + 1];
                                argCount += 2;
                            } else {
                                System.out.println("Error: Input directory does not exist! (Directory: " + args[argCount + 1] + ")");
                                System.exit(1);
                            }
                        } else {
                            error();
                        }
                        break;

                    case "-o":
                    case "--output":
                        if (argHaveValue(args, argCount)) {
                            Path outputPath = Paths.get(args[argCount + 1]);
                            if (Files.isDirectory(outputPath)) {
                                output = true;
                                outputValue = args[argCount + 1];
                                argCount += 2;
                            } else {
                                System.out.println("Error: Output directory does not exist! (Directory: " + args[argCount + 1] + ")");
                                System.exit(1);
                            }
                        } else {
                            error();
                        }
                        break;

                    case "-r":
                    case "--recursive":
                        recursive = true;
                        argCount++;
                        break;

                    case "-d":
                    case "--debug":
                        debug = true;
                        argCount++;
                        break;

                    case "-h":
                    case "--help":
                        if (args.length == 1) {
                            helpPrint();
                            System.exit(0);
                        } else {
                            error();
                        }
                        break;

                    default:
                        error();
                        break;
                }
            } else {
                error();
            }
        }

        if(!command){
            commandValue = "-";
        }

        if(!input){
            inputValue = "-";
        }

        if(!output){
            outputValue = "-";
        }
    }

    public static void helpPrint() {
        System.out.println("Help Print!");
    }

    public static void error() {
        System.out.println("Error: Not valid arguments were given.");
        System.exit(1);
    }

    public static boolean argHaveValue(String[] args, int argCount) {
        return argCount + 1 < args.length && !args[argCount + 1].startsWith("-");
    }
}
