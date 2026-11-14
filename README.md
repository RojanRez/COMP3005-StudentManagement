# Student Management Application - COMP 3005

## About the Project

For this assignment, I created a student management application using Java and PostgreSQL. The program uses JDBC to connect to the PostgreSQL database, where users can add, view, update, or delete student information.

I also added a menu to make it easier to use the program. Instead of writing SQL queries every time, the user can select an option from the menu and enter the required information.

## Tools Used

- Java 21
- PostgreSQL
- PostgreSQL JDBC Driver
- pgAdmin 4

## Database Setup

I used the COMP 3005 OpenStack VM to set up PostgreSQL and pgAdmin.

To set up the database:

1. Create a database named `student_db` in PostgreSQL.
2. Open the database in pgAdmin.
3. Run the `setup.sql` file. This creates the `students` table and inserts the three students provided in the assignment.
4. Create a PostgreSQL user named `student_app` with a password and give it access to the database.

To create the database user, run this command in PostgreSQL as an administrator:

```sql
CREATE USER student_app WITH PASSWORD 'YOUR_PASSWORD';
```

Replace `YOUR_PASSWORD` with a password of your choice. If the user already exists, you don't need to create it again.

Next, give `student_app` permission to access the database:

```sql
GRANT CONNECT ON DATABASE student_db TO student_app;

GRANT USAGE ON SCHEMA public TO student_app;

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE public.students TO student_app;

GRANT USAGE, SELECT
ON SEQUENCE public.students_student_id_seq TO student_app;
```

The `setup.sql` file should only be run once on a new database.

## Running the Application

Java 21 and the PostgreSQL JDBC driver are needed to run the program.

On the OpenStack VM, the JDBC driver is located at `/usr/share/java/postgresql.jar`.

First, open the terminal in the project folder and compile the Java file:

```bash
javac StudentApp.java
```

Next, enter the database password:

```bash
read -rs -p "Database password: " DB_PASSWORD
echo
export DB_PASSWORD
```

Then run the program:

```bash
java -cp ".:/usr/share/java/postgresql.jar" StudentApp
```

The program uses `localhost:5432` to connect to `student_db` with the `student_app` account. If the database is running on another server, the connection information in `StudentApp.java` needs to be updated.

## Program Functions

The application includes four functions:

- **getAllStudents():** Displays all students in the database, including their ID, name, email, and enrollment date.
- **addStudent():** Adds a new student using their first name, last name, email, and enrollment date.
- **updateStudentEmail():** Changes a student's email address using their student ID.
- **deleteStudent():** Removes a student from the database using their student ID.

When the program starts, it displays a menu with these options. After each operation, the program goes back to the main menu until the user decides to exit.

## Demonstration Video

Video link: To be added.
