CREATE SCHEMA IF NOT EXISTS petmanagement;
USE petmanagement;

DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS customers;
CREATE TABLE IF NOT EXISTS customers (
            CustomerID INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, 
            FirstName VARCHAR(50) NOT NULL, 
            LastName VARCHAR(50) NOT NULL, 
            EmailAddress VARCHAR(100) NOT NULL UNIQUE,
            PhoneNumber VARCHAR(15), 
            JoinDate DATE DEFAULT now(), 
            Gender ENUM('Unspecified', 'Male', 'Female', 'Other') DEFAULT 'Unspecified', 
            Address VARCHAR(255), 
            City VARCHAR(50), 
            State VARCHAR(50), 
            PostalCode VARCHAR(15)
            );
CREATE TABLE IF NOT EXISTS pets (
            PetID INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, 
            CustomerID INTEGER NOT NULL, 
            PetName VARCHAR(50) NOT NULL, 
            Gender ENUM('Unspecified', 'Male', 'Female', 'Other') DEFAULT 'Unspecified', 
            Breed VARCHAR(75), 
            BehaviourDescription VARCHAR(100),
            AddedDate Date DEFAULT now(), 
            FOREIGN KEY (CustomerID) references customers(CustomerID)
            );

INSERT INTO customers (FirstName, LastName, EmailAddress, PhoneNumber)
VALUES 
("Lane", "Dorscher", LOWER("dorscherl@gmail.com"), "712-254-2249"),
("Cody", "Wilson", LOWER("cwilson@gmail.com"), "712-621-6343"),
("Krysten", "Ring", LOWER("kring@live.com"), "712-265-4213"),
("Lois", "Amburn", LOWER("amburnLois@yahoo.com"), "465-531-2134"),
("Bob", "Monroe", LOWER("bobbym@hotmail.com"), ""),
("John", "Doe", LOWER("classified@fakemail.com"), "666-666-6666");

INSERT INTO pets (CustomerID, PetName, Gender, BehaviourDescription)
VALUES
(1, "Dozer", "Male", "Mildly trained, well mannered dog. Loves attention."),
(1, "Pippin", "Male", "Calm, nosy dog. Loves attention. Will play-bite hand if he thinks youre trying to play (Never hard)."),
(1, "Buddy", "Unspecified", "Loving bird, loves singing and flying around. Prefers high ledges away from animals."),
(3, "Sadie", "Female", "energetic pitbull, loves butt pets and being the center of attention. Hates conflict"),
(3, "Chuck", "Male", "always gets in your face to let you know to pet him");

select * from customers;

update customers
set PhoneNumber = '999-999-9999'
where CustomerID = 2;





select * from customers;
select * from pets;

select * from pets where CustomerID = (select CustomerID from customers where EmailAddress = "dorscherl@gmail.com");
select * from pets where CustomerID = (select CustomerID from customers where EmailAddress = "kring@live.com");

