```mermaid
flowchart TD

%% Layer: Presentation
subgraph Presentation
    SkolardApp
    LoginView
    SignUpView
    BookingView
    SupportDashboard
    TutorView
    DateTimeLabel
    FAQView
    MessageView
    PaymentView
    StudentProfileView
    TutorProfileView
    RatingView
    SessionView
    SupportView
end

%% Layer: Logic
subgraph Logic
    LoginHandler
    BookingHandler
    FAQHandler
    MessageHandler
    PaymentHandler
    ProfileHandler
    ProfileCreator
    ProfileUpdater
    ProfileViewer
    DefaultProfileFormatter
    RatingHandler
    SessionHandler
    SessionAccess
    SessionBooking
    SessionManagement
    SupportHandler
    PriorityList
    GradeComparator
    TimeComparator
    TutorComparator
end

%% Layer: Persistence
subgraph Persistence
    CardPersistence
    FAQPersistence
    LoginPersistence
    MessagePersistence
    RatingPersistence
    RatingRequestPersistence
    SessionPersistence
    StudentPersistence
    SupportPersistence
    TutorPersistence
    PersistenceFactory
    PersistenceProvider
    ConnectionManager
end

%% Layer: DB Implementations
subgraph SQLite
    CardDB
    FAQDB
    LoginDB
    MessageDB
    RatingDB
    RatingRequestDB
    SessionDB
    StudentDB
    SupportDB
    TutorDB
    TutorCoursesDB
end

%% Layer: Objects
subgraph Objects
    User
    Student
    Tutor
    Session
    RatingRequest
    Support
    SupportTicket
    Card
    FAQ
    Feedback
    Message
    LoginCredentials
end

%% Utils Layer
subgraph Utils
    CourseUtil
    EmailUtil
    GradeUtil
    MessageUtil
    PasswordUtil
    ValidationUtil
end

%% Inheritance
Student -->|extends| User
Tutor -->|extends| User
Session --> Student
Session --> Tutor

%% Presentation → Logic
SkolardApp --> LoginHandler
SkolardApp --> BookingHandler
SkolardApp --> ProfileHandler
SkolardApp --> RatingHandler
SkolardApp --> SupportHandler
FAQView --> FAQHandler
MessageView --> MessageHandler
RatingView --> RatingHandler
SessionView --> SessionHandler
StudentProfileView --> ProfileViewer
TutorProfileView --> ProfileViewer

%% Logic → Persistence
LoginHandler --> LoginPersistence
BookingHandler --> SessionPersistence
ProfileHandler --> StudentPersistence
ProfileHandler --> TutorPersistence
RatingHandler --> RatingRequestPersistence
RatingHandler --> RatingPersistence
SessionHandler --> SessionPersistence
SupportHandler --> SupportPersistence
FAQHandler --> FAQPersistence

%% Persistence → DB
LoginPersistence --> LoginDB
SessionPersistence --> SessionDB
StudentPersistence --> StudentDB
TutorPersistence --> TutorDB
RatingPersistence --> RatingDB
RatingRequestPersistence --> RatingRequestDB
FAQPersistence --> FAQDB
MessagePersistence --> MessageDB
SupportPersistence --> SupportDB

%% Persistence Core
PersistenceFactory --> PersistenceProvider
PersistenceProvider --> ConnectionManager

%% Logic internal links
BookingHandler --> PriorityList
RatingHandler --> SessionHandler
ProfileHandler --> DefaultProfileFormatter

%% DB → Objects
SessionDB --> Session
StudentDB --> Student
TutorDB --> Tutor
RatingRequestDB --> RatingRequest
FAQDB --> FAQ
MessageDB --> Message
SupportDB --> Support

