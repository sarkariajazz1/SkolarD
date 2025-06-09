```mermaid
flowchart TD

%% Layer: Presentation
subgraph Presentation
    FAQView
    MatchingView
    ProfileView
    SkolardApp
end

%% Layer: Logic
subgraph Logic
    MatchingHandler
    PriorityList
    ProfileHandler
    FAQHandler
    DefaultProfileFormatter
    RatingList
    SessionHandler
    TimeList
    TutorList
end

%% Layer: Persistence
subgraph Persistence
    SessionDB
    StudentDB
    TutorDB
end

%% Layer: Objects
subgraph Objects
    User
    Student
    Tutor
    Session
end

%% Inheritance
Student -->|extends| User
Tutor -->|extends| User

%% Presentation → Logic
MatchingView --> MatchingHandler
ProfileView --> ProfileHandler
FAQView --> FAQHandler
SkolardApp --> MatchingHandler
SkolardApp --> ProfileHandler
SkolardApp --> FAQHandler

%% Logic → Persistence
MatchingHandler --> SessionDB
MatchingHandler --> TimeList
ProfileHandler --> StudentDB
ProfileHandler --> TutorDB
SessionHandler --> SessionDB
DefaultProfileFormatter --> User

%% Persistence → Objects
SessionDB --> Session
StudentDB --> Student
TutorDB --> Tutor

%% Logic internal links
RatingList --> PriorityList
TutorList --> PriorityList
TimeList --> PriorityList
