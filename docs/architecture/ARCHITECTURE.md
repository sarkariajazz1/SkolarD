```mermaid
flowchart TD

flowchart TD

%% === Layers ===
subgraph Objects Layer
    Card
    FAQ
    Feedback
    LoginCredentials
    Message
    RatingRequest
    Session
    Student
    Support
    SupportTicket
    Tutor
    User
end

subgraph Persistence Layer
    CardDB
    FAQDB
    LoginDB
    MessageDB
    RatingDB
    RatingRequestDB
    SessionDB
    StudentDB
    SupportDB
    TutorCoursesDB
    TutorDB
end

subgraph Logic Layer
    AuthLogic
    BookingLogic
    FAQLogic
    MessageLogic
    PaymentLogic
    ProfileLogic
    RatingLogic
    SessionLogic
    SupportLogic
end

subgraph Presentation Layer
    AuthUI
    BookingUI
    FAQUI
    MessageUI
    PaymentUI
    ProfileUI
    RatingUI
    SessionUI
    SupportUI
    DashboardUI
    DateTimeUI
    SkolardApp
end

%% === Logic Layer Access to Persistence ===
AuthLogic --> LoginDB
BookingLogic --> SessionDB
FAQLogic --> FAQDB
MessageLogic --> MessageDB
PaymentLogic --> CardDB
ProfileLogic --> StudentDB
ProfileLogic --> TutorDB
RatingLogic --> RatingDB
RatingLogic --> RatingRequestDB
SessionLogic --> SessionDB
SessionLogic --> RatingRequestDB
SupportLogic --> SupportDB

%% === Persistence Cross-Access ===
SessionDB --> StudentDB
SessionDB --> TutorDB
RatingRequestDB --> SessionDB
RatingRequestDB --> StudentDB
SupportDB --> StudentDB
SupportDB --> TutorDB

%% === Presentation Layer Access to Logic ===
AuthUI --> AuthLogic
AuthUI --> FAQLogic
AuthUI --> MessageLogic
AuthUI --> ProfileLogic
AuthUI --> SupportLogic
BookingUI --> BookingLogic
BookingUI --> PaymentLogic
FAQUI --> FAQLogic
FAQUI --> MessageUI
FAQUI --> SupportUI
MessageUI --> MessageLogic
PaymentUI --> PaymentLogic
ProfileUI --> ProfileLogic
RatingUI --> RatingLogic
SessionUI --> SessionLogic
SupportUI --> SupportLogic
SupportUI --> MessageUI
DashboardUI --> AuthLogic
DashboardUI --> MessageLogic
SkolardApp --> AuthLogic
SkolardApp --> BookingLogic
SkolardApp --> MessageLogic
SkolardApp --> PaymentLogic
SkolardApp --> ProfileLogic
SkolardApp --> RatingLogic
SkolardApp --> SessionLogic
SkolardApp --> SupportLogic
SkolardApp --> AuthUI
SkolardApp --> BookingUI
SkolardApp --> FAQUI
SkolardApp --> MessageUI
SkolardApp --> SupportUI
SkolardApp --> SessionUI
SkolardApp --> DashboardUI
