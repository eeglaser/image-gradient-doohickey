package main.java.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextUI {
  static Backend back = new Backend();

  private static void runLoop(Scanner scnr) {
    boolean shouldQuit = false;
    while (!shouldQuit) {
      printMenu();
      String input = scnr.nextLine();
      if (input != null) {
        input = input.trim();
        try {
          switch (Integer.parseInt(input)) {
            case 0:
              System.out.println("Goodbye!");
              shouldQuit = true;
              break;
            case 1:
              loadImage(scnr);
              break;
            case 2:
              getPath(scnr);
              break;
            case 3:
              lookupImage(scnr);
              break;
            case 4:
              listImages();
              break;
            case 5:
              removeImage(scnr);
              break;
            default:
              System.out.println("Unrecognized command.");
              break;
          }
        } catch (Exception e) {
          continue;
        }
      }
    }
  }

  private static void lookupImage(Scanner scnr) {
    String target = null;
    while(target == null || target.isBlank()) {
      System.out.println("Please enter a string to search by: ");
      target = scnr.nextLine();
    }
    
    List<PreprocessedImage> list = back.getAllImages();
    List<String> results = new ArrayList<String>();
    System.out.println("Results: ");
    for(int i = 0; i < list.size(); i++) {
      if(list.get(i).getImagePath().contains(target)) {
        String str = i + ": " + list.get(i).toString();
        results.add(str);
        System.out.println(str);
      }
    }
    if(results.isEmpty()) {
      System.out.println("No matches found.");
    }

  }

  private static void getPath(Scanner scnr) {
    System.out.println("Use list-all feature to see indices.");
    System.out.println("Enter -1 to quit to menu.");
    String firstInput = null;
    int firstIndex = -1;
    // Get first image
    while (firstInput == null) {
      System.out.println("Please enter the index of the first image in the path: ");
      firstInput = scnr.nextLine();

      if (firstInput != null) {
        firstInput = firstInput.trim();

        try {
          firstIndex = Integer.parseInt(firstInput);
        } catch (Exception e) {
          firstInput = null;
          continue;
        }

        if (firstIndex < 0) {
          return;
        }
      }
    }


    String secondInput = null;
    int secondIndex = -1;
    // Get second image
    while (secondInput == null) {
      System.out.println("Please enter the index of the second image in the path: ");
      secondInput = scnr.nextLine();

      if (secondInput != null) {
        secondInput = secondInput.trim();

        try {
          secondIndex = Integer.parseInt(secondInput);
        } catch (Exception e) {
          secondInput = null;
          continue;
        }

        if (secondIndex < 0) {
          return;
        }
      }
    }

    // Get path...
    List<PreprocessedImage> results = null;
    List<PreprocessedImage> images = back.getAllImages();
    System.out.println("Getting results...");
    try {
      results = back.getImagePath(images.get(firstIndex), images.get(secondIndex));
      System.out.println("Results List:");
      for (int i = 0; i < results.size(); i++) {
        System.out.println(i + ": " + results.get(i).toString());
      }
    } catch (Exception e) {
      System.out.println("Encountered error getting results: " + e.getMessage());
    }
  }

  private static void listImages() {
    List<PreprocessedImage> list = back.getAllImages();
    for (int i = 0; i < list.size(); i++) {
      System.out.println(i + ": " + list.get(i).toString());
    }

  }

  private static void removeImage(Scanner scnr) {
    // TODO Auto-generated method stub

  }

  private static void loadImage(Scanner scnr) {
    System.out
        .print("Please provide the path to the image or directory to load.\nAccepted file types: ");
    for (String s : Backend.SUPPORTED_FILE_EXTENSIONS) {
      System.out.print(s + ", ");
    }
    System.out.println();

    String input = scnr.nextLine();
    try {
      System.out.println("Started loading...");
      back.receiveFile(new File(input.trim()));
      System.out.println("Finished loading.");
    } catch (Exception e) {
      System.out.println("Encountered a problem while loading: " + e.getMessage());
    }
  }

  private static void printMenu() {
    System.out.println("---MENU---\n" + "  1: Load image file(s)\n" + "  2: Get image path\n"
        + "  3: Lookup image index by name\n" + "  4: List all loaded images\n"
        + "  5: Remove loaded images\n" + "  0: Quit\n" + "\n"
        + "Please type a number corresponding to your choice.");
  }

  public static void main(String[] args) {
    System.out.println("guh");
    Scanner scanner = new Scanner(System.in);
    runLoop(scanner);
  }
}
