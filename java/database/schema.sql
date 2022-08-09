BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS responses;
Drop Table If EXISTS messageLog;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50),
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE responses (
        response_id SERIAL,
        category varChar(20),
        topic varChar(20),
        keyword varChar(30),
        subkeyword varChar(30),
        content_type varChar(20),
        display varChar(390),
        display_type varChar(10),
        Link varChar(100),
        date_last_updated varChar(10),
        description varChar(10)
);
CREATE TABLE messageLog (
        message_id SERIAL,
        user_id int,
        body varChar(390),
        sender varChar(10),
        type varChar(10),
        link varChar(100)
);

INSERT INTO responses (category,topic,keyword,subkeyword,content_type,display,display_type,Link,date_last_updated,description) VALUES
        ('Bootcamp OS','Description','Description','General','Informational','Bootcamp OS is a Learning Management System ("LMS") used by Tech Elevator//s students to access reading materials, take quizzes, and submit exercises. ','text','n/a','n/a','n/a'),
        ('Bootcamp OS','Terminology','Unit','General','Informational','An unit in Bootcamp OS is a grouping of learning content for a single topic. Often contains reading material, a tutorial, a quiz, and a set of exercises.','text','n/a','n/a','n/a'),
        ('Bootcamp OS','Terminology','Section','General','Informational','A section in Bootcamp OS is a grouping of related units.','text','n/a','n/a','n/a'),
        ('Bootcamp OS','Terminology','Course','General','Informational','A course in Bootcamp OS encompasses all learning content for an entire class.','text','n/a','n/a','n/a'),
        ('Bootcamp OS','Terminology','Unit','General','Informational','A checkpoint in Bootcamp OS is the graded portion of a unit, typically the unit exercise. Not all units have checkpoints.','text','n/a','n/a','n/a'),
        ('Bootcamp OS','Terminology','LMS','General','Informational','A Learning Management System ("LMS") is a software application used in educational courses and learning programs. It provides several functions such as delivery and grading of learning materials and reporting course progress.','text','n/a','n/a','n/a'),
        ('Bootcamp OS','Terminology','Lessons','General','Informational','In Bootcamp OS, each page of reading material is called a lesson. Each lesson explains how a particular topic fits into software development, what problem it solves in the real world, along with code samples to help you apply it.','text','n/a','n/a','n/a'),
        ('Pathway','Program','Phases','General','Informational','The Pathway Program is broken into 3 phases: Self-Discovery, Career Prep & Personal Branding, Connections & Careers.','text','n/a','n/a','n/a'),
        ('Pathway','Program','Phases','Details','Informational','Each phase will consist of various panels, live workshops, one-on-one sessions with your Pathway Program Director, and asynchronous videos in BootcampOS.','text','n/a','n/a','n/a'),
        ('Pathway','Feedback','Form','General','Internal Link','Pathway Event Feedback','Link','https://docs.google.com/forms/d/e/1FAIpQLSdb-YxpZluzW10l-lUncZy_4a_ZDoP0-HvSJIZXBBx4nOcO6A/viewform','n/a','n/a'),
        ('Pathway','Matchmaking','Eligibility','General','Informational','In order to attend Matchmaking, you must meet the following eligibility requirements: 1) Indicate during Week 1 of the cohort that you plan on finding a job in the tech field after graduation on the survey shared on Slack, 2) Complete all required lessons in BootcampOS by Week 10 of the Pathway Program, and 3) Receive a Satisfactory grade or higher on all final Pathway assignments','text','n/a','n/a','n/a'),
        ('Pathway','Assignments','Description','General','Informational','Pathway assignments include writing a resume, crafting an elevator pitch, and updating your LinkedIn profile. ','text','n/a','n/a','n/a'),
        ('Pathway','Assignments','Grading','General','Informational','Pathway assignments are graded on a six point scale.','text','n/a','n/a','n/a'),
        ('Pathway','Assignments','Grading','Not Attempted - 0 Points','Informational','Not Attempted (0 points) - No meaningful attempt was made to complete the assignment by the deadline.','text','n/a','n/a','n/a'),
        ('Pathway','Assignments','Grading','Incomplete - 1 point','Informational','Incomplete (1 point) - An attempt was made to complete the assignment by the deadline, but the assignment is incomplete and/or missing 90% of the requirements or more OR an attempt was made to complete the assignment, but it was turned in 24 hours after the deadline and is missing 50% of the requirements.','text','n/a','n/a','n/a'),
        ('Pathway','Assignments','Grading','Poor - 2 points','Informational','Poor (2 points) - An attempt was made to complete the assignment by the deadline, but the assignment is incomplete and/or missing 75% of the requirements or more OR an attempt was made to complete the assignment, but it was turned in 24 hours after the deadline and is missing 25% of the requirements.','text','n/a','n/a','n/a'),
        ('Pathway','Assignments','Grading','Unsatisfactory - 3 point','Informational','Unsatisfactory (3 point) - An attempt was made to complete the assignment by the deadline, but the assignment is incomplete and/or missing 50% of the requirements or more.','text','n/a','n/a','n/a'),
        ('Pathway','Assignments','Grading','Satisfactory - 4 points','Informational','Satisfactory (4 points) - Assignment was turned in by the deadline and includes 75% of the requirements laid out by the Pathway Director.','text','n/a','n/a','n/a'),
        ('Pathway','Assignments','Grading','Great - 5 points','Informational','Great (5 points) - Assignment was turned in by the deadline and includes 90% of the requirements laid out by the Pathway Director.','text','n/a','n/a','n/a'),
        ('Pathway','Assignments','Grading','Outstanding - 6 points','Informational','Outstanding (6 points) - Assignment was turned in by the deadline and includes all requirements laid out by the Pathway Director. No additional updates are required.','text','n/a','n/a','n/a'),
        ('General','Academic probation','Description','General','Informational','If you do not achieve Satisfactory grades on all final assignments or have not completed lessons from BootcampOS by the deadline, you will be placed on academic probation, which will affect your ability to graduate and you will not be allowed to attend Matchmaking.','text','n/a','n/a','n/a'),
        ('Pathway','CliftonStrengths','Description','General','Informational','CliftonStrengths is an online talent assessment to help you discover what you naturally do best, learn how to develop your greatest talents into strengths, and use your personalized results and reports to maximize your potential.','text','n/a','n/a','n/a'),
        ('Pathway','CliftonStrengths','Description','Details','Informational','The CliftonStrengths assessment measures your talents -- your natural patterns of thinking, feeling and behaving -- and categorizes them into the 34 CliftonStrengths themes.','text','n/a','n/a','n/a'),
        ('Pathway','CliftonStrengths','Presentation','General','Video','CliftonStrengths Video Presentation','link','https://drive.google.com/file/d/1kNKxzDGNdFewv20F-yx2w5ybOGt8VtJD/view','44608','n/a'),
        ('Pathway','CliftonStrengths','Presentation','General','Slides','CliftonStrengths Slides','link','https://drive.google.com/file/d/1UKUwAxdaDRK8V6TkDUMaXGPCc8wJLxXo/view','44608','n/a'),
        ('Pathway','CliftonStrengths','Website','General','External Link','Cliftonstrengths Website','link','https://www.gallup.com/cliftonstrengths','n/a','n/a'),
        ('Pathway','CliftonStrengths','Assessment','General','Informational','CliftonStrengths is a 30-minute assessment, you//ll see 177 paired statements and choose which one best describes you.','text','n/a','n/a','n/a'),
        ('Pathway','CliftonStrengths','Domains','General','Informational','The four CliftonStrengths domains are: Executing,  Influencing, Relationship Building, and Strategic Thinking.','text','n/a','n/a','n/a'),
        ('Pathway','CliftonStrengths','Domains','Details','Internal Link','The four CliftonStrengths domains are: Executing,  Influencing, Relationship Building, and Strategic Thinking.','link','https://drive.google.com/file/d/1dKvstw-XF-wAUX4d2nR7eaA-wAyo5jfQ/view','44635','n/a'),
        ('Pathway','CliftonStrengths','Strategic Thinking','Themes','Informational','The Strategic Thinking domain is comprised of the following themes: Analytical, Context, Futuristic, Ideation, Input, Intellection, Learner, and Strategic','text','n/a','n/a','n/a'),
        ('Pathway','CliftonStrengths','Relationship Building','Themes','Informational','The Relationship Building domain  is comprised of the following themes: Adaptability, Connectedness, Developer, Empathy, Harmony, Includer, Individualization, Positivity, and Relator','text','n/a','n/a','n/a'),
        ('Pathway','CliftonStrengths','Influencing','Themes','Informational','The Influencing domain is comprised of the following themes: Activator, Command, Communication, Competition, Maximizer, Self-Assurance, Significance, and Woo.','text','n/a','n/a','n/a'),
        ('Pathway','CliftonStrengths','Executing','Themes','Informational','The Executing domain is comprised of the following themes: Achiever, Arranger, Belief, Consistency, Deliberative, Discipline, Focus, Responsibility, and Restorative.','text','n/a','n/a','n/a'),
        ('Pathway','CliftonStrengths','Themes','General','Informational','When you take the CliftonStrengths assessment, you uncover your unique combination of 34 CliftonStrengths themes.','text','n/a','n/a','n/a'),
        ('Pathway','Elevator Pitch','Description','General','Informational','Your elevator pitch will serve as your first impression when meeting new people.','text','n/a','n/a','n/a'),
        ('Pathway','Elevator Pitch','Description','Details','Informational','An elevator pitch describes who you are, what you do, and why you//re awesome.','text','n/a','n/a','n/a'),
        ('Pathway','Elevator Pitch','Requirements','General','Informational','Your elevator pitch should be memorized, 30 to 40 seconds long, and answer the following four questions: what are your skills, what are your strengths, what is your motivation, and what is your goal.','text','n/a','n/a','n/a'),
        ('Pathway','Elevator Pitch','Presentation','General','Video','Elevator Pitch Video Presentation','link','https://drive.google.com/file/d/1XjHxRr_NyKxhHF6WUluna8PBHQWhH1Y_/view','44615','n/a'),
        ('Pathway','Elevator Pitch','Presentation','General','Slides','Elevator Pitch Slides','link','https://drive.google.com/file/d/1hzCGriWG65jSGtGtVArYmlv7TR8jmeAr/view','44609','n/a'),
        ('Pathway','Resume','Presentation','General','Video','Resume Video Presentation','link','https://drive.google.com/file/d/11BGJoFjACWaGyeKD2YW3D2F3lxOEyMeI/view','44615','n/a'),
        ('Pathway','Resume','Presentation','General','Slides','Resume Slides','link','https://drive.google.com/file/d/14QJxHbgz1fobZ8mIObZSRPZa2z3OTLZE/view','44615','n/a'),
        ('Pathway','Resume','Sections','General','Informational','Resumes should have the following sections: Name/Contact information, Professional Summary, Technical Skills, Technical Experience, Education, and Professional Experience.','text','n/a','n/a','n/a'),
        ('Pathway','Resume','Sections','Name/Contact information','Informational','The name/contact infromation section of your resume should contain your legal name, phone number, email address, link to your LinkedIn profile, and link to your Github.','text','n/a','n/a','n/a'),
        ('Pathway','Resume','Sections','Professional Summary','Informational','The professional summary resume section should be a 2 to 3 sentence paragraph, using fragments rather than full sentences, that answers: what do you want and what can you offer.','text','n/a','n/a','n/a'),
        ('Pathway','Resume','Sections','Technical Skills','Informational','The technical skills section of your resume should contain the languages and technologies know and can speak about.','text','n/a','n/a','n/a'),
        ('Pathway','Resume','Sections','Technical Experience','Informational','The technical experience section of your resume should contain Tech Elevator capstones, side projects, and competitions. You should describe what languages/technologies that you used for the project, your role, and what the project does.','text','n/a','n/a','n/a'),
        ('Pathway','Resume','Sections','Education','Informational','The education section of your resume should list Tech Elevator first and include all degree(s) and any relevant certification/licenses','text','n/a','n/a','n/a');


COMMIT TRANSACTION;
