```mermaid
flowchart TD

%% Presentation Layer
subgraph "Presentation Layer"
    FAQView["FAQView"]
    MatchingView["MatchingView"]
    ProfileView["ProfileView"]
    SkolardApp["SkolardApp"]
end

%% Logic Layer
subgraph "Logic Layer"
    MatchingHandler["MatchingHandler"]
    PriorityList["PriorityList"]
    ProfileHandler["ProfileHandler"]
    DefaultProfileFormatter["DefaultProfileFormatter"]
    RatingList["RatingList"]
    SessionHandler["SessionHandler"]
    TimeList["TimeList"]
    TutorList["TutorList"]
end

%% Persistence Layer
subgraph "Persistence Layer"
    SessionDB["SessionDB"]
    StudentDB["StudentDB"]
    TutorDB["TutorDB"]
end

%% Objects Layer
subgraph "Objects Layer"
    User["User"]
    Student["Student"]
    Tutor["Tutor"]
    Session["Session"]
end

%% Inheritance
Student --|> User
Tutor --|> User

%% Presentation -> Logic
MatchingView --> MatchingHandler
ProfileView --> ProfileHandler
SkolardApp --> MatchingHandler
SkolardApp --> ProfileHandler

%% Logic -> Persistence
MatchingHandler --> SessionDB
MatchingHandler --> TimeList
ProfileHandler --> StudentDB
ProfileHandler --> TutorDB
SessionHandler --> SessionDB
DefaultProfileFormatter --> User

%% Persistence -> Objects
SessionDB --> Session
StudentDB --> Student
TutorDB --> Tutor

%% Logic Internal
RatingList --> PriorityList
TutorList --> PriorityList
TimeList --> PriorityList
