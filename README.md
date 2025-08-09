# Custom ETL Reader

A simple web application for uploading and parsing CSV files (ETL process) using a React frontend and a Spring Boot backend.  
The backend parses uploaded CSV files and returns structured JSON data. The frontend allows users to upload CSV files and view parsed results.

---

## Features

- Upload CSV files from the browser  
- Backend CSV parsing with OpenCSV library  
- Returns JSON representation of CSV data  
- Handles simple CSV validation and error messages  
- Easy to extend for ETL transformations and data cleaning

---

## Tech Stack

- **Frontend:** React.js, Axios  
- **Backend:** Java 17+, Spring Boot, OpenCSV  
- **Build Tools:** Maven (backend), npm (frontend)

---

## Getting Started

### Prerequisites

- Java 17 or newer  
- Node.js (v16+) and npm  
- (Optional) Maven installed globally or use Maven Wrapper  

### Installation & Running

1. Clone the repo:

```bash
git clone https://github.com/yourusername/custom-etl-reader.git
cd custom-etl-reader
