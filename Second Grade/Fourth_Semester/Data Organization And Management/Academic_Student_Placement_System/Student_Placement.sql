CREATE TYPE univercity_type AS ENUM ('state','private');
CREATE TYPE language_type AS ENUM ('English','Turkish');
CREATE TYPE education_type AS ENUM ('fe','ee');
CREATE TYPE highschool_type AS ENUM ('Top Ranked','Not');

CREATE TABLE tbl_universities (
    "universityID" SERIAL NOT NULL,
    "name" VARCHAR(100),
    "address" VARCHAR(255),
    "email" VARCHAR(100),
    "city" VARCHAR(50),
    "type" univercity_type, -- 'state' or 'private'
    "yearFoundation" INTEGER,
    CONSTRAINT "pk_universityID" PRIMARY KEY ("universityID")
);

CREATE TABLE tbl_faculties (
    "facultyID" SERIAL NOT NULL,
    "universityID" INTEGER NOT NULL,
    "name" VARCHAR(100),
    "email" VARCHAR(100),
    CONSTRAINT "pk_facultyID" PRIMARY KEY ("facultyID"),
    CONSTRAINT "fk_universityID" FOREIGN KEY ("universityID") REFERENCES tbl_universities ("universityID")
);

CREATE TABLE tbl_departments (
    "departmentID" SERIAL NOT NULL,
    "facultyID" INTEGER NOT NULL,
    "name" VARCHAR(100),
    "email" VARCHAR(100),
    "language" language_type, -- 'English' or 'Turkish'
    "educationType" education_type, -- 'formal' or 'evening'
    "quota" INTEGER,
    "quotaForTopStudents" INTEGER,
    "educationPeriod" INTEGER,
    "minimumScore2024" INTEGER,
    "minimumOrder2024" INTEGER,
    CONSTRAINT "pk_departmentID" PRIMARY KEY ("departmentID"),
    CONSTRAINT "fk_facultyID" FOREIGN KEY ("facultyID") REFERENCES tbl_faculties ("facultyID")
);

CREATE TABLE tbl_students (
    "studentID" SERIAL NOT NULL,
    "name" VARCHAR(100),
    "surname" VARCHAR(100),
    "examScore" INTEGER,
    "ranking" INTEGER,
    "highSchoolPosition" highschool_type, -- 'Top Ranked' or 'Not'
    "preference1" INTEGER,
    "preference2" INTEGER,
    "preference3" INTEGER,
    CONSTRAINT "pk_studentID" PRIMARY KEY ("studentID"),
    CONSTRAINT "fk_preference1" FOREIGN KEY ("preference1") REFERENCES tbl_departments ("departmentID"),
    CONSTRAINT "fk_preference2" FOREIGN KEY ("preference2") REFERENCES tbl_departments ("departmentID"),
    CONSTRAINT "fk_preference3" FOREIGN KEY ("preference3") REFERENCES tbl_departments ("departmentID")
);


INSERT INTO tbl_universities ("name", "address", "email", "city", "type", "yearFoundation") VALUES
('Istanbul Technical University', 'Maslak 34467 Istanbul', 'info@itu.edu.tr', 'Istanbul', 'state', 1773),
('Stanford University', '450 Serra Mall, Stanford, CA', 'contact@stanford.edu', 'Stanford', 'private', 1885),
('Acibadem University', 'Sariyer, Istanbul', 'info@acibadem.tr', 'Adiyaman', 'state', 1992),
('Dokuz Eylul University', 'Buca', 'inquiries@deu.edu.tr', 'Izmir', 'state', 1853),
('Izmir University', 'Izmir, 06800 Çeşme/Izmir', 'info@iu.edu.tr', 'Izmir', 'state', 1967),
('Sabancı University', 'Orta Mahalle, Üniversite Cd. No:27, 34956 Tuzla/İstanbul', 'info@sabanciuniv.edu', 'Istanbul', 'private', 1994),
('Koç University', 'Rumelifeneri Yolu, 34450 Sarıyer/İstanbul', 'info@ku.edu.tr', 'Istanbul', 'private', 1993),
('Ankara University', 'Tandoğan, 06100 Ankara', 'info@ankara.edu.tr', 'Ankara', 'state', 1946),
('Izmir Technical University', 'Karsiyaka, Izmir', 'info@iztu.tr', 'Izmir', 'state', 1096),
('Harvard University', 'Cambridge, MA, USA', 'info@harvard.edu', 'Cambridge', 'private', 1636),
('Antalya University', 'Muratpasa', 'hello@antalya.tr', 'Antalya', 'state', 1999);


INSERT INTO tbl_faculties ("universityID", "name", "email") VALUES
(1, 'Engineering', 'engineering@itu.edu.tr'),
(2, 'Medicine', 'medicine@stanford.edu'),
(2, 'Engineering', 'engineering@stanford.edu'),
(3, 'Engineering', 'engineering@acibadem.tr'),
(4, 'Engineering', 'economics@deu.edu.tr'),
(5, 'Physics', 'physics@iu.edu.tr'),
(6, 'Arts', 'arts@sab.edu.tr'),
(7, 'Law', 'law@koc.edu.tr'),
(8, 'Chemistry', 'chemistry@au.edu.tr'),
(9, 'Architecture', 'architecture@iyte.edu.tr'),
(10, 'Law', 'law@harward.edu'),
(11, 'History', 'history@antalya.tr');


INSERT INTO tbl_departments ("facultyID", "name", "email", "language", "educationType", "quota", "quotaForTopStudents", "educationPeriod", "minimumScore2024", "minimumOrder2024") VALUES
(1, 'Computer Engineering', 'cs@itu.edu.tr', 'English', 'fe', 120, 30, 4, 85.5, 100),
(2, 'Neuroscience', 'neuro@stanford.edu', 'English', 'ee', 100, 20, 4, 90.0, 50),
(3, 'Machine Engineering', 'machine@stanford.edu', 'English', 'fe', 96, 31, 5, 85.0, 45),
(4, 'Electrical-Electronics Engineering', 'ieee@acibadem.tr', 'Turkish', 'fe', 80, 25, 5, 88.0, 75),
(5, 'Economy Engineering', 'economics@deu.edu.tr', 'English', 'ee', 110, 35, 3, 82.0, 120),
(6, 'Quantum Physics', 'quantum@iu.edu.tr', 'English', 'fe', 60, 15, 4, 91.5, 30),
(7, 'Modern Art', 'modart@sab.edu.tr', 'English', 'ee', 70, 20, 4, 78.0, 140),
(8, 'Justice', 'justice@koc.edu.tr', 'English', 'fe', 90, 30, 4, 87.0, 60),
(9, 'Chemistry Engineering', 'chemistry@au.edu.tr', 'English', 'ee', 50, 10, 6, 92.0, 20),
(10, 'Urban Planning', 'urbanplan@iyte.edu.tr', 'English', 'fe', 95, 25, 5, 85.0, 90),
(11, 'Criminal Law', 'law@harward.edu', 'Turkish', 'fe', 100, 30, 4, 85.0, 90),
(12, 'Prehistoric', 'history@antalya.tr', 'Turkish', 'ee', 75, 20, 4, 80.0, 110);


INSERT INTO tbl_students ("name", "surname", "examScore", "ranking", "highSchoolPosition", "preference1", "preference2", "preference3") VALUES
('Enes', 'Bilgiç', 680, 1900, 'Top Ranked', 1, 3, 2),
('Kadir', 'Gırıkcı', 630, 2100, 'Not', 2, 3, 1),
('Ezgi', 'Tan', 710, 1400, 'Top Ranked', 3, 1, 2),
('Kaan', 'Yılmaz', 600, 2600, 'Not', 7, 4, 2),
('İrem', 'Tekin', 650, 2300, 'Not', 1, 2, 3),
('Bora', 'Kaya', 720, 1200, 'Top Ranked', 1, 7, 4),
('Seda', 'Demir', 680, 1800, 'Top Ranked', 8, 1, 3),
('Mert', 'Can', 590, 2700, 'Not', 1, 9, 8),
('Yasemin', 'Güzel', 645, 2400, 'Not', 4, 7, 10),
('Ali', 'Çelik', 700, 1500, 'Top Ranked', 2, 8, 1);

--1
SELECT "name"
FROM tbl_universities
WHERE "city" LIKE 'A%' AND "yearFoundation" > 1990;

--2
SELECT u."name" AS University_Name
FROM tbl_universities u
JOIN tbl_faculties f1 ON u."universityID" = f1."universityID" AND f1."name" = 'Engineering'
JOIN tbl_faculties f2 ON u."universityID" = f2."universityID" AND f2."name" = 'Medicine'
GROUP BY u."name";

--3
SELECT u."type" AS University_Type, COUNT(f."facultyID") AS Faculty_Count
FROM tbl_universities u
JOIN tbl_faculties f ON u."universityID" = f."universityID"
GROUP BY u."type";

--4
SELECT "departmentID", "name"
FROM tbl_departments
WHERE "name" LIKE '%Engineering%' AND "educationType" = 'ee';

--5 
SELECT "departmentID", "name", "educationPeriod", "minimumScore2024"
FROM tbl_departments
ORDER BY "educationPeriod" DESC, "minimumScore2024" DESC
LIMIT 5;

--6  
SELECT D."departmentID", D."name", COUNT(*) AS Preference_Count
FROM tbl_departments D
JOIN (
    SELECT "preference1" AS Department_ID FROM tbl_students
    UNION ALL
    SELECT "preference2" FROM tbl_students
    UNION ALL
    SELECT "preference3" FROM tbl_students
) AS P ON D."departmentID" = P.Department_ID
WHERE D."educationPeriod" = 4
GROUP BY D."departmentID", D."name"
ORDER BY Preference_Count DESC;

--7 
SELECT S."studentID", S."name", S."surname", S."examScore"
FROM tbl_students S
JOIN tbl_departments D ON S."preference1" = D."departmentID"
WHERE D."name" LIKE '%Computer Engineering%'
ORDER BY S."examScore" DESC;

--8 
UPDATE tbl_faculties
SET "universityID" = (SELECT "universityID" FROM tbl_universities WHERE "name" = 'Izmir Technical University')
WHERE "universityID" = (SELECT "universityID" FROM tbl_universities WHERE "name" = 'Dokuz Eylul University')
AND "name" = 'Engineering';


--9
UPDATE tbl_departments
SET "educationPeriod" = "educationPeriod" + 1 
WHERE "facultyID" IN (SELECT "facultyID" FROM tbl_faculties WHERE "name" = 'Law');

--10
DELETE FROM tbl_departments
WHERE "facultyID" IN (SELECT "facultyID" FROM tbl_faculties WHERE "universityID" = (SELECT "universityID" FROM tbl_universities WHERE "name" = 'Izmir University'));
DELETE FROM tbl_faculties
WHERE "universityID" = (SELECT "universityID" FROM tbl_universities WHERE "name" = 'Izmir University');