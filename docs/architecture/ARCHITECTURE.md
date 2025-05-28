```mermaid

flowchart TD

%% Presentation Layer
subgraph Presentation Layer
    FAQView
    MatchingView
    ProfileView
    SkolardApp
end

%% Logic Layer
subgraph Logic Layer
    MatchingHandler
    PriorityList
    ProfileHandler
    DefaultProfileFormatter
    RatingList
    SessionHandler
    TimeList
    TutorList
end

%% Persistence Layer
subgraph Persistence Layer
    SessionDB
    StudentDB
    TutorDB
end

%% Objects Layer
subgraph Objects Layer
    User
    Student
    Tutor
    Session
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
PriorityList
RatingList --> PriorityList
TutorList --> PriorityList
TimeList --> PriorityList
``