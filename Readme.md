# Pet Management App
This app is for employees to register customers and all their pets into a database. This application will take in the customer's name and phone number along with information about their pet(s): name, breed, description of likes.

## Actors / Features

- User Can:
  - Insert Customers to database
      - <span style="color: #ad0606">Must Include:</span>
        - First Name
        - Last Name
        - Email Address (HAS TO BE UNIQUE)
        - Phone Number
      - <span style="color: #06ad25">Optional:</span>
        - Gender (will default to 'Unspecified')
        - Address
        - City
        - State
        - Postal Code
  - Remove Customers from database
  - Search Customers
    - By name
    - By email
    - By associated pet name ? 
    - Update customer info
  - Add Pets into database
     - Must have associated customer
     - <span style="color: #ad0606">Must Include:</span>
       - Pet Name
       - Breed
       - Gender
       - Behaviour Description
     - <span style="color: #06ad25">Optional:</span>
       - Birth Date
  - Remove Pets from database
- System will:
  - Poll all tables (Customers, Pets, Appointments)
  - Insert Customer
    - Automatically pass join date
  - Insert Pet
    - Automatically pass added date
    - 

## Requirements
- sqlite
