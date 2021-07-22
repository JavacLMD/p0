 create table customers (
	customerID int primary key not null,
    firstName varchar(255),
    lastName varchar(255),
    address varchar(255),
    city varchar (255),
    state enum ('IA - Iowa', 'NE - Nebraska'),
    postalCode varchar(50),
    country varchar(255)
);

create table pets (
	petID int not null,
    customerID int,
    petName varchar(255),
    petType enum('dog', 'cat'),
    breed varchar(255),
    primary key (petID),
    foreign key (customerID) references customers(customerID)
);

Insert into customers
	(customerID, firstName, lastName, address, city, state, postalCode, country)
values 
	(0, "Lane", "Dorscher", "527 Main Street", "Griswold", "IA - Iowa", 51535, "United States");

insert into pets
	(petID, customerID, petName, petType, breed)
values
	(0, 0, "Dozer", 'dog', "American Pit Bull");


select * from customers;