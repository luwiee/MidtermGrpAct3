class Course implements Comparable<Course> {
    protected static final byte DEFAULT_BYTE = 0;
    protected static final double DEFAULT_DOUBLE = 0.0;
    protected static final String DEFAULT_STRING = "";

    private String courseNumber, descriptiveTitle;
    private byte term, year;
    private double units, grades;
    private boolean isElective;

    /**
     * Default Constructor
     * <p>
     * Constructs a course with the empty string as the course number,
     * empty string as the course title, 0 as the units,
     * 0 as the year level, 0 as the term, 0 as the grade
     */
    Course() {
        this(DEFAULT_STRING, DEFAULT_STRING,
                DEFAULT_BYTE, DEFAULT_BYTE, DEFAULT_DOUBLE, DEFAULT_DOUBLE);
    }

    /**
     * Constructor with parameters.
     * @param courseNumber the given course's number to be set
     * @param descriptiveTitle the given course's descriptive number to be set
     * @param term the given course's term to be set
     * @param year the given course's year to be set
     * @param units the given course's units to be set
     * @param grades the given course's grades to be set
     */
    Course(String courseNumber, String descriptiveTitle,
           byte term, byte year, double units, double grades) {
        this.courseNumber = courseNumber;
        this.descriptiveTitle = descriptiveTitle;
        this.term = term;
        this.year = year;
        this.units = units;
        this.grades = grades;
    }

    /**
     * This method gets the course's number/code.
     * @return the course's number/code
     */
    public String getCourseNumber() {
        return courseNumber;
    }

    /**
     * This method sets the course's number.
     * @param courseNumber the provided number/code to be set
     */
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    /**
     * This method gets the course's descriptive title.
     * @return the course's descriptive title
     */
    public String getDescriptiveTitle() {
        return descriptiveTitle;
    }

    /**
     * This method sets the course's descriptive title.
     * @param descriptiveTitle the provided descriptive title to be set
     */
    public void setDescriptiveTitle(String descriptiveTitle) {
        this.descriptiveTitle = descriptiveTitle;
    }

    /**
     * This method gets the course's term.
     * @return the course's term
     */
    public byte getTerm() {
        return term;
    }

    /**
     * This method sets the course's term.
     * @param term the provided term to be set
     */
    public void setTerm(byte term) {
        this.term = term;
    }

    /**
     * This method gets the course's year.
     * @return the course's year
     */
    public byte getYear() {
        return year;
    }

    /**
     * This method sets the course's year.
     * @param year the provided year to be set
     */
    public void setYear(byte year) {
        this.year = year;
    }

    /**
     * This method gets the course's number of units.
     * @return the course's number of units
     */
    public double getUnits() {
        return units;
    }

    /**
     * This method sets the course's number of units.
     * @param units the provided number of units to be set
     */
    public void setUnits(double units) {
        this.units = units;
    }

    /**
     * This method gets the course's grade.
     * @return the course's grade
     */
    public double getGrades() {
        return grades;
    }

    /**
     * This method sets the course's grade.
     * @param grades the provided grade to be set
     */
    public void setGrades(double grades) {
        this.grades = grades;
    }

    /**
     * This method gets the isElective value of a course.
     * @return the course's isElective value
     */
    public boolean getIsElective() {
        return isElective;
    }

    /**
     * This method determines if the course is an elective or not.
     * @param isElective the provided true/false value to be set
     */
    public void setIsElective(boolean isElective) {
        this.isElective = isElective;
    }

    /**
     * This method displays the course's details.
     * <p>
     * Details such as the course number, descriptive title, number of units, and grades.
     * If grade is not available - N/A
     * @return the formatted string of the course's details
     */
    @Override
    public String toString() {
        return String.format("%-15s %-110s %-8.1f %-6s", getCourseNumber(),
                getDescriptiveTitle(), getUnits(), (getGrades() == 0) ? "N\\A" : String.valueOf(getGrades()));
    }

    /**
     * This method compares courses, specifically comparing the grades attribute.
     * @return 0 if grades are equal, -1 if 1st grade is lesser than 2nd grade,
     * and 1 if 1st grade is greater than 2nd grade
     */
    @Override
    public int compareTo(Course other) {
        return Double.compare(this.getGrades(), other.getGrades());
    }
}