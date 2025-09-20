# Library Management System

A comprehensive Java-based Library Management System built with Object-Oriented Programming principles. This console application manages book inventory, patron records, book lending operations, and fee management with complete validation and reporting features.

## Features

### Book Management
- **Add Books** with title, author, and unique ISBN validation
- **Remove Books** from inventory (only if not borrowed)
- **Track Availability** with real-time status updates
- **Duplicate Prevention** using ISBN validation

### Patron Management
- **Register Patrons** with unique ID validation
- **Track Borrowing History** for each patron
- **Manage Fees** with outstanding balance tracking
- **Borrowing Limits** enforcement (max 5 books per patron)

### Lending Operations
- **Issue Books** with comprehensive validation:
  - Book availability check
  - Patron borrowing limit enforcement
  - Outstanding fee prevention
- **Return Books** with automatic late fee calculation
- **Real-time Status Updates** for all transactions

### Fee Management
- **Automatic Late Fees** on overdue returns
- **Flexible Payments** with partial payment support
- **Financial Tracking** with total collected fees
- **Outstanding Balance** management per patron

### Reporting System
- **Available Books Report** - All currently available books
- **Borrowed Books Report** - Books on loan with borrower information
- **Patrons Report** - Complete patron list with statistics
- **Library Statistics** - Comprehensive system metrics dashboard

##  Technology Stack

- **Language**: Java SE
- **IDE**: IntelliJ IDEA 
- **Data Structures**: ArrayList Collections
- **Programming Paradigm**: Object-Oriented Programming (OOP)
- **Key Concepts**: Encapsulation, Input Validation, Exception Handling


