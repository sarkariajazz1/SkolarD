flowchart TD

%% Layer: Presentation
subgraph Presentation Layer
    SkolardApp
    ProfileView
    MatchingView
end

%% Layer: Logic
subgraph Logic Layer
    MatchingHandler
    ProfileHandler
    PriorityList
    TutorList
    RatingList
    TimeList
end

%% Layer: Persistence
subgraph Persistence Layer
    SessionPersistence
    StudentPersistence
    TutorPersistence
    PersistenceFactory
end

%% Layer: Objects
subgraph Objects Layer
    Student
    Session
    Tutor
end

%% Presentation -> Logic
SkolardApp --> ProfileView
SkolardApp --> MatchingView
ProfileView --> ProfileHandler
MatchingView --> MatchingHandler

%% Logic -> Persistence
MatchingHandler --> SessionPersistence
ProfileHandler --> StudentPersistence
ProfileHandler --> TutorPersistence

%% Persistence -> Objects
SessionPersistence --> Session
StudentPersistence --> Student
TutorPersistence --> Tutor

%% Logic internal dependencies
TutorList --> PriorityList
RatingList --> PriorityList
TimeList --> PriorityList
