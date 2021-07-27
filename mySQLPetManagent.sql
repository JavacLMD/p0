drop table if exists pets;
drop table if exists customers;
create table IF NOT EXISTS customers ( 
            CustomerID INTEGER PRIMARY KEY NOT NULL auto_increment,
            FirstName VARCHAR(45),
            LastName VARCHAR(45),
            EmailAddress VARCHAR(100),
            PhoneNumber VARCHAR(15),
            UNIQUE (EmailAddress)
            );

create table IF NOT EXISTS pets ( 
			PetID INTEGER PRIMARY KEY NOT NULL,
            CustomerID int NOT NULL,
            PetName VARCHAR(45),
            BehaviourDescription VARCHAR(255),
            FOREIGN KEY (CustomerID) references customers(CustomerID));
            
INSERT INTO customers (FirstName, LastName, EmailAddress, PhoneNumber) 
VALUES 
("Lane", "Dorscher", "dorscherl@gmail.com", "7122542249"),
("Krysten", "Ring", "dl@gmail.com", "7122542249");


-- Delete from customers where CustomerID = 1;


select * from customers;

