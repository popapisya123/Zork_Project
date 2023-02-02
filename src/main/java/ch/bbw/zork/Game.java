package ch.bbw.zork;import ch.bbw.objects.Crowbar;import ch.bbw.objects.Key;import ch.bbw.objects.Pocket;/** * Class Game - the main class of the "Zork" game. * <p> * Author:  Michael Kolling, 1.1, March 2000 * refactoring: Rinaldo Lanza, September 2020 */public class Game {  private final Parser parser;  private Room currentRoom;  private Room lastRoom;  private Room laboratory, patientRoom, treatmentRoom, groupRoom, doctorsOffice, xRayRoom, outside;  private Pocket pocket = new Pocket();  public Game() {    parser = new Parser(System.in);    Key keyToPatientRoom = new Key(1, "key to open patient room");    Key keyToExit = new Key(2, "key to exit psychiatry");    Key keyToGroupRoom = new Key(3, "key open group room");    Crowbar crowbar = new Crowbar(1, "crowbar to open x-ray room");    laboratory = new Room("laboratory", keyToPatientRoom, null);    patientRoom = new Room("patient room", null, crowbar);    treatmentRoom = new Room("treatment room", null, null);    groupRoom = new Room("group room", null, null);    doctorsOffice = new Room("doctor's office", keyToExit, null);    xRayRoom = new Room("x-ray room", keyToGroupRoom, null);    outside = new Room("outside the clinic", null, null);    laboratory.setExits(null, treatmentRoom, null, null);    patientRoom.setExits(treatmentRoom, groupRoom, outside, xRayRoom);    treatmentRoom.setExits(null, null, patientRoom, laboratory);    groupRoom.setExits(doctorsOffice, null, null, patientRoom);    doctorsOffice.setExits(null, null, groupRoom, null);    xRayRoom.setExits(null, patientRoom, null, null);    outside.setExits(patientRoom, null, null, null);    currentRoom = treatmentRoom; // start game in treatment room  }  /**   * Main play routine.  Loops until end of play.   */  public void play() {    printWelcome();    // Enter the main command loop.  Here we repeatedly read commands and    // execute them until the game is over.    boolean finished = false;    while (!finished) {      Command command = parser.getCommand();      finished = processCommand(command);    }    System.out.println("Thank you for playing.  Good bye.");  }  private void printWelcome() {    System.out.println();    System.out.println("Welcome to Zork!");    System.out.println("Zork is a simple adventure game.");    System.out.println("Type 'help' if you need help.");    System.out.println();    System.out.println(currentRoom.longDescription());  }  private boolean processCommand(Command command) {    if (command.isUnknown()) {      System.out.println("I don't know what you mean...");      return false;    }    String commandWord = command.getCommandWord();    switch (commandWord) {      case "help":        printHelp();        break;      case "go":        lastRoom = currentRoom;        goRoom(command);        break;      case "back":        goRoom(command);        break;      case "quit":        if (command.hasSecondWord()) {          System.out.println("Quit what?");        } else {          return true; // signal that we want to quit        }        break;    }    return false;  }  private void printHelp() {    System.out.println("You are lost. You are alone. You wander");    System.out.println("around at Monash Uni, Peninsula Campus.");    System.out.println();    System.out.println("Your command words are:");    System.out.println(parser.showCommands());  }  private void goRoom(Command command) {    if (command.getCommandWord().equals("back")) {      currentRoom = lastRoom;      System.out.println(currentRoom.longDescription());      return;    }    if (!command.hasSecondWord()) {      System.out.println("Go where?");    } else {      String direction = command.getSecondWord();      // Try to leave current room.      Room nextRoom = currentRoom.nextRoom(direction);      if (nextRoom == null) {        System.out.println("There is no door!");      } else {        currentRoom = nextRoom;        System.out.println(currentRoom.longDescription());      }    }    if (command.getCommandWord().equals("search")) {      if (pocket.isAbleToAddItem()) {        if (currentRoom == laboratory) {          pocket.setCrowbar(laboratory.getCrowbar());          pocket.getKeys().add(laboratory.getKey());        } else if (currentRoom == patientRoom) {          pocket.setCrowbar(patientRoom.getCrowbar());          pocket.getKeys().add(patientRoom.getKey());        } else if (currentRoom == treatmentRoom) {          pocket.setCrowbar(treatmentRoom.getCrowbar());          pocket.getKeys().add(treatmentRoom.getKey());        } else if (currentRoom == groupRoom) {          pocket.setCrowbar(groupRoom.getCrowbar());          pocket.getKeys().add(groupRoom.getKey());        } else if (currentRoom == doctorsOffice) {          pocket.setCrowbar(doctorsOffice.getCrowbar());          pocket.getKeys().add(doctorsOffice.getKey());        } else if (currentRoom == xRayRoom) {          pocket.setCrowbar(xRayRoom.getCrowbar());          pocket.getKeys().add(xRayRoom.getKey());        } else if (currentRoom == outside) {          pocket.setCrowbar(outside.getCrowbar());          pocket.getKeys().add(outside.getKey());        }        pocket.showItems();      } else {        pocket.showItems();        System.out.println("You have too much weight on you. Type 'drop' to remove an item.");      }    }  }}