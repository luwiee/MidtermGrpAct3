import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class CourseAdministration {
    private static final Scanner keyboard = new Scanner(System.in);
    private static final byte BYTE_SENTINEL_VALUE = Byte.MIN_VALUE;
    private static final double DOUBLE_SENTINEL_VALUE = Double.MIN_VALUE;

    /**
     * This method returns an ArrayList of type Course that contains
     * data parsed from a CSV file
     * <p>
     * 1. An ArrayList-Course- is initialized <br>
     * 2. A buffered read reads a cvs file from the passed fileName parameter<br>
     * 3. A while loop iterated through all the Courses<br>
     * 3.1. The data of the course has the separator of a comma - line.split <br>
     * 3.2. It makes a Course object for each set of data <br>
     * 3.3. It then adds it to the array of data to the list.
     */
    protected static ArrayList<Course> parseCSV(String fileName) {
        ArrayList<Course> courseList = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            br.readLine();
            while ((line = br.readLine()) != null) {
                // Split on comma
                String[] csv = line.split(",");
                if (csv.length < 6) csv = buildArray(line.split(","));

                // Create a temporary Course Object to store the values
                Course course = new Course();

                // Parse Values from the CSV and store it to the Course object
                course.setYear(Byte.parseByte(csv[0]));
                course.setTerm(Byte.parseByte(csv[1]));
                course.setCourseNumber(csv[2]);
                course.setDescriptiveTitle(csv[3]);
                course.setUnits(Double.parseDouble(csv[4]));

                // Test case if Grade is not defined
                if (csv[5].equals("")) course.setGrades(0);
                else course.setGrades(Double.parseDouble(csv[5]));

                // Add the course to the course list
                courseList.add(course);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found.");
        } catch (IOException ioException) {
            System.out.println("I/O error: " + ioException);
        }
        return courseList;
    }

    /**
     * This method allows the construction of the array.
     *
     * @param line String read by the reader
     * @return String array containing data for the Course object
     */
    private static String[] buildArray(String[] line) {
        ArrayList<String> courseInfo = new ArrayList<>(Arrays.asList(line));
        int size = 6 - courseInfo.size();
        for (int i = 0; i < size; i++) {
            courseInfo.add("");
        }
        return courseInfo.toArray(new String[6]);
    }

    /**
     * Overwrites the current data of the file with new data
     * <p>
     * METHOD ALGORITHM: <br>
     * 1. The method warns the user that changes will be made to the file<br>
     * 2. If input Y <br>
     * 2.1. A for loop will go through the file and print it out<br>
     * 2.2. then the writer overwrites the file<br>
     * 3. If the user inputs otherwise the file does not get overwritten
     *
     * @param courseList the ArrayList of courses from the CurriculumData_Grades.csv file
     */
    private static void saveChangesToFile(ArrayList<Course> courseList) {
        System.out.print("The changes will be permanent. Are you sure? Y/N");
        String confirmation = keyboard.nextLine();

        if (!confirmation.equalsIgnoreCase("y")) {
            System.out.println("The changes were not saved.");
            return;
        }

        try {
            for (Course c : courseList) System.out.println(c);

            PrintWriter copyWriter =
                    new PrintWriter(new FileWriter("CurriculumData_Grades_Copy.csv"));
            for (Course course : courseList) {
                copyWriter.println(course.getYear() + "," + course.getTerm() + "," +
                        course.getCourseNumber() + "," + course.getDescriptiveTitle() +
                        "," + course.getUnits() + "," +
                        ((course.getGrades() == 0) ? "" : String.valueOf(course.getGrades())));
            }
            copyWriter.close();

            PrintWriter textCopyWriter = new PrintWriter(new FileWriter("CurriculumData_Grades_Copy.txt"));
            textCopyWriter.printf("%-16s %-120s %-10s %-7s\n", "COURSE NO.", "COURSE DESCRIPTION", "UNITS", "GRADE");
            for (Course course : courseList) textCopyWriter.println(course);
            textCopyWriter.close();
        } catch (IOException ioException) {
            System.out.println("I/O error: " + ioException);
        }

    }

    /**
     * Helper method that displays a header for the year and term
     * <p>
     * METHOD ALGORITHM: <br>
     * 1. Gets the year and term <br>
     * 2. Sets the year text and term text to each specified String through a switch expression <br>
     * 3. Concatenate that to the header text <br>
     * 4. Display the header
     *
     * @param year the year that a set of courses are in
     * @param term the term that a set of courses are in
     */
    private static void displayHeaderText(int year, int term) {
        String year_text = "";
        String term_text = "";

        switch (year) {
            case 1 -> year_text = "First";
            case 2 -> year_text = "Second";
            case 3 -> year_text = "Third";
            case 4 -> year_text = "Fourth";
            case 5 -> year_text = "Fifth";
        }
        switch (term) {
            case 1 -> term_text = "First";
            case 2 -> term_text = "Second";
            case 3 -> term_text = "Third";
        }
        for (int i = 0; i < 145; i++) System.out.print("-");
        System.out.println("\nYear = " + year_text + " Year\tTerm = " + term_text + " Semester ");
    }

    /**
     * This method displays the header for its specific year and term
     * <p>
     * METHOD ALGORITHM: <br>
     * 1. Accepts the specified year and term. <br>
     * 2. Calls the Helper Method displayHeaderText <br>
     * 3. Prints the header for course info and has a ternary operator for if it has Grades<br>
     * 4. Displays the header
     *
     * @param year      the year that a certain set of courses are in
     * @param term      the term that a certain set of courses are in
     * @param hasGrades if the Header includes grades
     */
    private static void displayHeader(int year, int term, boolean hasGrades) {
        displayHeaderText(year, term);
        System.out.printf("%-15s %-110s %-8s %-6s\n", "COURSE NO.", "COURSE DESCRIPTION", "UNITS", ((hasGrades) ? "GRADE" : ""));
        for (int i = 0; i < 145; i++) System.out.print("-");
        System.out.println();
    }


    /**
     * Buffer between operations
     */
    private static void termBuffer() {
        System.out.println();
        System.out.print("Press any key to see courses for the next term...");
        keyboard.nextLine();
        System.out.println();
    }


    /**
     * This method shows the list of courses and their respective units present in the term.
     * If hasGrades is True then it will also show their Grades
     * <p>
     * METHOD ALGORITHM <br>
     * 1. Gets the highest year in the curriculum data file. <br>
     * 2. Displays the courses for each term.
     *
     * @param courseList the ArrayList of courses from the CurriculumData_Grades.csv file
     * @param hasGrades  if the call pertains to a show Grades call
     */
    private static void showCourses(ArrayList<Course> courseList, boolean hasGrades) {
        int highestYear = 1;
        for (Course c : courseList) {
            if (c.getYear() > highestYear) highestYear = c.getYear();
        }
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t   COURSES");
        for (int y = 1; y <= highestYear; y++) {
            for (int t = 1; t <= 3; t++) {
                displayHeader(y, t, hasGrades);
                for (Course c : courseList) {
                    if (c.getYear() == y && c.getTerm() == t) {
                        if (hasGrades) {
                            System.out.println(c);
                        } else {
                            System.out.printf("%-15s %-110s %-8s\n", c.getCourseNumber(), c.getDescriptiveTitle(), c.getUnits());
                        }
                    }
                }
                termBuffer();
            }
        }
        if (!hasGrades) return;

        System.out.println();
        System.out.print("Press enter key to see grades in ascending order...");
        keyboard.nextLine();
        System.out.println();

        sortCoursesByGPA(courseList, 1);

        System.out.println();
        System.out.print("Press enter key to see grades in descending order...");
        keyboard.nextLine();
        System.out.println();

        sortCoursesByGPA(courseList, 2);
    }


    /**
     * Displays the failed courses
     * <p>
     * METHOD ALGORITHM: <br>
     * 1) Create an ArrayList wherein courses with failing grades are added. <br>
     * 2) Get the highest number of years and terms per year in the curriculum. <br>
     * 3) Display courses with failing grades for each term.
     *
     * @param courseList the ArrayList of courses from the CurriculumData_Grades.csv file
     */
    private static void showFailedCourses(ArrayList<Course> courseList) {
        ArrayList<Course> failedCourseList = new ArrayList<>();
        for (Course c : courseList) {
            if (c.getGrades() != 0 && isFailedCourse(c)) {
                failedCourseList.add(c);
            }
        }

        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t      FAILED COURSES");
        for (int i = 0; i < 145; i++) System.out.print("-");
        System.out.printf("\n%-15s %-110s %-8s %-6s\n", "COURSE NO.", "COURSE DESCRIPTION", "UNITS", "GRADE");
        for (Course c : failedCourseList) System.out.println(c);
        System.out.println();
    }

    /**
     * This method is in charge of dealing with course shifts; it checks the shifter's data to the
     * main curriculum data, then transfers the shifter's data and course number to their comparable courses.<br>
     * <p>
     * METHOD ALGORITHM: <br>
     * 1. Compares the Shifters and MainCurriculum arrayList <br>
     * 2. If the course descriptions are the same <br>
     * Changes the original course number into the shifter's course number <br>
     * Sets the original course grade to the shifter's course grade <br>
     * 3. Display courses that had equivalents <br>
     * 4. Display courses that did not have an equivalent
     *
     * @param courseList the ArrayList of courses from the CurriculumData_Grades.csv file
     */
    private static void shiftCourse(ArrayList<Course> courseList) {
        ArrayList<Course> shifterCourseList = parseCSV("Shifter.csv");
        String shiftChoice;
        System.out.println("Detected Shifter!");
        System.out.println("Shifting to: BSCS 1");
        System.out.print("Are you sure you want to shift courses?(y/n): ");
        shiftChoice = Character.toString(keyboard.next().charAt(0));

        if (shiftChoice.equalsIgnoreCase("y")) {
            for (Course sC : shifterCourseList) {
                for (Course c : courseList) {
                    if ((sC.getDescriptiveTitle()).compareToIgnoreCase(c.getDescriptiveTitle()) == 0) {
                        c.setGrades(sC.getGrades());
                        c.setCourseNumber(sC.getCourseNumber());
                        break;
                    }
                }
            }
            System.out.println();
            System.out.print("YOUR COURSES");
            showShifterCourses(shifterCourseList);

            uncarriedCourses(courseList, shifterCourseList);

            System.out.println();
            System.out.print("You have successfully shifted courses!");
        }
        System.out.println();
    }

    /**
     * This method is a very bare-bones way of displaying each course in the courseList
     * <p>
     * METHOD ALGORITHM <br>
     * 1. For each course in the course ArrayList <br>
     * Print said course into string
     *
     * @param shifterCourseList the ArrayList of courses from the Shifter.csv file
     */
    private static void showShifterCourses(ArrayList<Course> shifterCourseList) {
        System.out.printf("\n%-15s %-110s %-8s %-6s\n", "COURSE NO.", "COURSE DESCRIPTION", "UNITS", "GRADE");
        for (Course shifterCourse : shifterCourseList) {
            System.out.println(shifterCourse.toString());
        }
    }

    /**
     * This method removes courses with equivalents from the original shifter's ArrayList and sorts out the courses
     * with no counterparts.
     * This function is also in responsible for populating the main course ArrayList with the uncarried courses.
     * <p>
     * METHOD ALGORITHM <br>
     * 1. Compares each course in the Shifter's ArrayList and the original curriculum ArrayList <br>
     * 2. If the descriptive titles are the same <br>
     * Removes the shifter course from the shifter's ArrayList <br>
     * 3. Shows a list of the uncarried courses <br>
     * 4. Adds the uncarried course to the main curriculum ArrayList <br>
     * Adds a prefix to the course name <br>
     * Sets the units of the course to 0
     *
     * @param shifterCourseList the ArrayList of courses from the Shifter.csv file
     * @param courseList        the ArrayList of courses from the CurriculumData_Grades.csv file
     */
    private static void uncarriedCourses(ArrayList<Course> courseList, ArrayList<Course> shifterCourseList) {
        int prevSize;
        int curSize = 0;

        do {
            prevSize = curSize;
            for (int i = 0; i < shifterCourseList.size(); i++) {
                for (Course c : courseList) {
                    if ((shifterCourseList.get(i)).getDescriptiveTitle().compareToIgnoreCase(c.getDescriptiveTitle()) == 0) {
                        shifterCourseList.remove(i);
                        break;
                    }
                }
            }
            curSize = shifterCourseList.size();
        } while (prevSize != curSize);

        System.out.println();
        System.out.print("UNCARRIED COURSES");
        showShifterCourses(shifterCourseList);

        for (Course sC : shifterCourseList) {
            sC.setCourseNumber("*" + sC.getCourseNumber());
            sC.setUnits(0);
            courseList.add(sC);
        }
    }

    /**
     * Helper Method that Checks if a Course is failed or not
     *
     * @param course receives the Course instance
     * @return true if the grade is lesser than 75
     */
    private static boolean isFailedCourse(Course course) {
        return course.getGrades() < 75 && course.getGrades() != 0;
    }


    /**
     * This method sorts courses by GPA, either in ascending or descending order.
     * <p>
     * METHOD ALGORITHM: <br>
     * 1) Create a new ArrayList wherein courses with grades are added. <br>
     * 2) If choice is 1, return an ArrayList of courses with GPAs in ascending order. <br>
     * Else, return an ArrayList of courses with GPAs in descending order.
     *
     * @param courseList the ArrayList of courses from the CurriculumData_Grades.csv file
     * @param aOrD       the choice indicating if the ArrayList should be sorted in descending or ascending order
     */
    private static void sortCoursesByGPA(ArrayList<Course> courseList, int aOrD) {
        ArrayList<Course> courseListCopy = new ArrayList<>();
        for (Course c : courseList) {
            if (c.getGrades() != 0)
                courseListCopy.add(c);
        }
        Collections.sort(courseListCopy);
        switch (aOrD) {
            case 1 -> System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t   ASCENDING ORDER");
            case 2 -> { // Descending Order
                Collections.reverse(courseListCopy);
                System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t   DESCENDING ORDER");
            }
        }
        System.out.printf("\n%-15s %-110s %-8s %-6s\n", "COURSE NO.", "COURSE DESCRIPTION", "UNITS", "GRADE");
        for (int i = 0; i < 145; i++) System.out.print("-");
        System.out.println();
        for (Course c : courseListCopy) System.out.println(c);
    }

    /**
     * METHOD ALGORITHM: <br>
     * 1. Display the header and elective courses <br>
     * 2. Prompt user if they want to edit an elective <br>
     * if choice == 1, invoke the editCourse method,
     * then print the updated electives list <br>
     * else return to main menu <br>
     *
     * @param courseList the passed ArrayList contains the courses parsed from the CSV
     *                   This method displays elective courses and allows the user to edit them.
     */
    private static void manageElectiveCourses(ArrayList<Course> courseList) {
        int choice;
        do {
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t   ELECTIVE COURSES");
            for (int i = 0; i < 145; i++) System.out.print("-");
            System.out.printf("\n%-15s %-110s %-8s %-6s\n", "COURSE NO.", "COURSE DESCRIPTION", "UNITS", "GRADE");
            for (Course c : courseList)
                if (c.getIsElective())
                    System.out.println(c);

            System.out.println("\nWhat would you like to do?\n");
            System.out.println("1. Manage an elective course.");
            System.out.println("2. Return to the main menu.\n");
            choice = acceptByteInput();
            if (choice < 1 || choice > 2)
                System.out.println("The number must be from 1 to 2.");

            if (choice == 1) {
                editCourse(courseList);
            }
        } while (choice != 2);
    }

    /**
     * This method shows courses that have no GPA.
     * <p>
     * METHOD ALGORITHM: <br>
     * 1) Print out courses from courseList that do not have a GPA.
     *
     * @param courseList the ArrayList of courses from the CurriculumData_Grades.csv file
     */
    private static void showCoursesWithoutGPA(ArrayList<Course> courseList) {
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t   SHOWING COURSES WITHOUT GPA");
        for (int i = 0; i < 145; i++) System.out.print("-");
        System.out.printf("\n%-15s %-110s %-8s %-6s\n", "COURSE NO.", "COURSE DESCRIPTION", "UNITS", "GRADE");
        for (Course c : courseList)
            if (c.getGrades() == 0.0) System.out.println(c);
    }

    /**
     * METHOD ALGORITHM: <br>
     * 1. Use a for-each loop to iterate for every Course object in the loop <br>
     * 2. If a course object's course number is equal to the search key, return this <br>
     * course object and terminate this method <br>
     * 3. Else return an empty course
     *
     * @param courseList the passed ArrayList contains the courses parsed from the CSV
     * @param searchKey  a String parameter to be used for searching the ArrayList
     * @return return a matching course, or if no course is found, return an empty Course object
     */
    private static Course searchCourseList(ArrayList<Course> courseList,
                                           String searchKey) {
        for (Course course : courseList) {
            if (course.getCourseNumber().equalsIgnoreCase(searchKey))
                return course;
        }
        return new Course();
    }

    /**
     * This method sets the GPA for a specific course number.
     * <p>
     * METHOD ALGORITHM: <br>
     * 1) Displays the list of courses without GPAs <br>
     * 2) Asks the user to input a course number from the displayed list <br>
     * 3) If the inputted course number is not found or null, the user is asked to input a valid input <br>
     * 4) Asks the user to input GPA for the selected course number. <br>
     *
     * @param courseList the ArrayList of courses from the CurriculumData_Grades.csv file
     */
    private static void inputGrades(ArrayList<Course> courseList) {
        Course selectedCourse;
        String searchKey;
        do {
            showCoursesWithoutGPA(courseList);
            searchKey = acceptStringInput("\nEnter a course number: ");
            selectedCourse = searchCourseList(courseList, searchKey);
            if (selectedCourse.getCourseNumber().equals("")) {
                System.out.println("Invalid Course Number inputted. Try again.");
                System.out.print("Press enter to continue.");
                keyboard.nextLine();
            }
        } while (selectedCourse.getCourseNumber().equals(""));
        selectedCourse.setGrades(acceptDoubleInput());

    }

    /**
     * This method allows the user to edit a course's code and description.
     * <p>
     * METHOD ALGORITHM: <br>
     * 1) Display the courses from courseList. <br>
     * 2) Ask user for the course number to be changed. <br>
     * 3) If the course entered is null or not found in the list, the user has to input a valid course.
     * Else, the user is asked to enter a new course number and its new respective description
     * for the chosen course. <br>
     * 4) Display the new course number and its respective description.
     *
     * @param courseList the ArrayList of courses from the CurriculumData_Grades.csv file
     */
    private static void editCourse(ArrayList<Course> courseList) {
        Course courseToBeChanged;
        String searchKey;

        do {
            searchKey = acceptStringInput("Enter course number to be changed (ex. CS 122): ");
            courseToBeChanged = searchCourseList(courseList, searchKey);
            if (courseToBeChanged.getCourseNumber().equals(""))
                System.out.println("Invalid Course Number inputted. Try again.");
            else {
                for (Course c : courseList) {
                    if (c.getCourseNumber().compareToIgnoreCase(courseToBeChanged.getCourseNumber()) == 0) {
                        String newCN = acceptStringInput("Enter the new course number: ");
                        String newTitle = acceptStringInput("Enter the new descriptive title: ");
                        c.setCourseNumber(newCN);
                        c.setDescriptiveTitle(newTitle);
                        System.out.println("New course number: " + c.getCourseNumber());
                        System.out.println("New course description: " + c.getDescriptiveTitle());
                        break;
                    }
                }
            }
        } while (courseToBeChanged.getCourseNumber().equals(""));
    }

    /**
     * This method helps accept a variable with the byte data type.
     * <p>
     * METHOD DESCRIPTION: <br>
     * Accepts the input given by the user within the
     * range of the byte data type. The method is
     * enclosed in a loop and will continue till the
     * user either enters a valid input or hits an error.
     *
     * @return the valid byte input to be used
     */
    private static byte acceptByteInput() {
        byte input;
        while (true) {
            try {
                input = Byte.parseByte(acceptStringInput("Select an item: "));
                if (input != BYTE_SENTINEL_VALUE) return input;
            } catch (NumberFormatException exception) {
                System.out.println("You have entered an invalid integer.");
            } catch (Exception exception) {
                System.out.println("Fatal error: " + exception);
            }
        }
    }


    /**
     * This method helps accept a variable with the double data type.
     * <p>
     * METHOD DESCRIPTION: <br>
     * Accepts the input given by the user within the
     * range of the double data type. The method is
     * enclosed in a loop and will continue till the
     * user either enters a valid input or hits an error.
     *
     * @return a double input
     */
    private static double acceptDoubleInput() {
        double input;
        while (true) {
            try {
                input = Double.parseDouble(acceptStringInput("Input GPA: "));
                if (input != DOUBLE_SENTINEL_VALUE) return input;
            } catch (NumberFormatException exception) {
                System.out.println("You have entered an invalid Double value.");
            } catch (Exception exception) {
                System.out.println("Fatal error: " + exception);
            }
        }
    }

    /**
     * METHOD DESCRIPTION: Accepts input from the keyboard, loops until user enters
     * a valid String value
     *
     * @param message signifies the message to be displayed upon prompting the user
     * @return a String value accepted from the keyboard
     */
    private static String acceptStringInput(String message) {
        String userInput;
        while (true) {
            System.out.print(message);
            userInput = keyboard.nextLine();
            if (userInput != null) return userInput;
        }
    }

    /**
     * Serves as buffer in between operations.
     */
    private static void inputBuffer() {
        System.out.print("Press enter key to choose another item.");
        keyboard.nextLine();
        System.out.println();
    }


    /**
     * Outputs introductory messages to the console
     */
    private static void showIntroduction() {
        String smile = "\uD83D\uDE0A";
        String love = "\uD83D\uDC9C";
        String woman = "\uD83D\uDC67";
        String man = "\uD83D\uDC66";

        System.out.println("\n");
        System.out.println("College of Information and Computing Sciences");
        System.out.println(" Saint Louis University");
        System.out.println(" Baguio City ");
        System.out.println("---------------------------------------------");
        for (int index = 0; index < 10; index++) {
            System.out.print(smile + love + smile + "");
        }
        System.out.println("\n");
        System.out.println(" Group 4 " + smile);
        System.out.println(" Developers: \n Aliyah Javier" + woman + "\n Eduard Tadeo" + man
                + "\n Lawrence Miguel" + man + "\n Rhyen Natividad" + man);
        System.out.println("\n");
        System.out.print("Please press a key to start the program...");
        keyboard.nextLine();

        System.out.println();
        System.out.println();
        System.out.println("Welcome to the Curriculum Monitoring program!");
        System.out.println("See menu below for the possible options");
        System.out.println();
    }

    /**
     * Displays the current main menu of the program.
     * Displays what the program is able to do and with further added functions.
     */
    private static void showMenu() {
        System.out.println("""
               [------------------------------------------------]
                 Curriculum Monitoring program
                 1: Show courses for each term (w/o-Grades)
                 2: Show courses for each term (w-Grades)
                 3: Show elective courses
                 4: Show failed courses
                 5: Enter grades for finished courses
                 6: Edit a course
                 7: Shift from another program
                 8: Save changes
                 9: Quit
                [--------------------------------------------------]
                """);
    }

    private static void searchForElectives(ArrayList<Course> courseList) {
        for (Course course : courseList) {
            if (course.getCourseNumber().equalsIgnoreCase("CSE"))
                course.setIsElective(true);
        }
    }

    /**
     * Main Method.
     * <p>
     * METHOD ALGORITHM: <br>
     *
     * @param args command line argument
     */
    public static void main(String[] args) {
        ArrayList<Course> courseList = parseCSV("CurriculumData_Grades.csv");
        searchForElectives(courseList);
        showIntroduction();
        byte choice = 0;
        do {
            showMenu();
            try {
                choice = acceptByteInput();
                if (choice < 1 || choice > 10)
                    System.out.println("The number must be from 1 to 10.");
                switch (choice) {
                    case 1 -> showCourses(courseList, false);
                    case 2 -> showCourses(courseList, true);
                    case 3 -> manageElectiveCourses(courseList);
                    case 4 -> showFailedCourses(courseList);
                    case 5 -> inputGrades(courseList);
                    case 6 -> {
                        showCourses(courseList, false);
                        editCourse(courseList);
                    }
                    case 7 -> shiftCourse(courseList);
                    case 8 -> saveChangesToFile(courseList);

                }
                if (choice % 3 != 0) inputBuffer();
            } catch (NumberFormatException x) {
                System.out.println("You entered an invalid integer.");
            }
        } while (choice != 9);
    }
}
