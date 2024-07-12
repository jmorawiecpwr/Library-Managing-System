# Library Database Application

This project is a JavaFX-based application for managing a library database. It provides a graphical user interface (GUI) for adding, editing, searching, sorting, and deleting book records in a SQLite database.

## Features

- **Add Books**: Users can add new books to the library database.
- **Edit Books**: Users can edit the details of existing books.
- **Delete Books**: Users can delete books from the library database.
- **Search Books**: Users can search for books based on title or author.
- **Sort Books**: Users can sort the list of books by ID, title, or author.

## Installation

1. **Clone the repository**:
    ```sh
    git clone https://github.com/yourusername/library-database.git
    cd library-database
    ```

2. **Set up the environment**:
    Ensure you have JDK and JavaFX SDK installed and properly set up in your environment.

3. **Build the project**:
    Use your preferred IDE (e.g., IntelliJ IDEA, Eclipse) to open the project and build it.

## Usage

1. **Run the application**:
    You can run the application from your IDE or using the command line:
    ```sh
    ./gradlew run
    ```

2. **Interact with the GUI**:
    - **Add a Book**: Enter the book title and author in the respective fields and click "Add".
    - **Edit a Book**: Select a book from the list, modify the title and/or author, and click "Edit". Then, confirm or cancel the changes.
    - **Delete a Book**: Select a book from the list and click "Delete".
    - **Search for Books**: Enter a search query in the search field.
    - **Sort Books**: Choose a sorting criterion from the dropdown menu.

## Code Overview

### Main Application (`GUI.java`)

- **`start` method**: Initializes the GUI components and sets up event handlers.
- **`refreshBooksList` method**: Fetches the list of books from the database, applies sorting, and updates the GUI list view.
- **`filterBooksList` method**: Filters the books based on the search query.
- **`showAlert` method**: Displays error messages.

### Database Connection (`DatabaseConnection.java`)

- **`connect` method**: Establishes a connection to the SQLite database.
- **`createTable` method**: Creates the books table if it does not exist.
- **`insertBook` method**: Inserts a new book record into the database.
- **`selectBooks` method**: Fetches all book records from the database.
- **`updateBook` method**: Updates an existing book record.
- **`deleteBook` method**: Deletes a book record from the database.

### Additional Components

- **Icons**: Icons for the buttons are located in the `icons` directory.
- **Styles**: The application's styles are defined in the `styles.css` file.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
